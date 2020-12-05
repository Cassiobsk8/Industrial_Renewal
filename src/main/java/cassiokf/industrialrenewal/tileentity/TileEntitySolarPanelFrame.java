package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSolarPanelFrame;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.MultiBlockHelper;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TileEntitySolarPanelFrame extends TileEntityMultiBlocksTube<TileEntitySolarPanelFrame>
{
    public final CustomEnergyStorage energyContainer = new CustomEnergyStorage(10240, 0, 10240)
    {
        @Override
        public boolean canReceive()
        {
            return false;
        }

        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public int receiveInternally(int maxReceive, boolean simulate)
        {
            return TileEntitySolarPanelFrame.this.outputEnergy(maxReceive, simulate);
        }
    };
    private final Set<TileEntitySolarPanelFrame> panelReady = new HashSet<>();
    private final ItemStack panelStack = new ItemStack(BlocksRegistration.SPANEL_ITEM.get());
    private int tick;
    private Direction blockFacing;
    public boolean panelInv;
    private int energyCanOutput = 0;
    private final int random = new Random().nextInt(10);

    public TileEntitySolarPanelFrame(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onFirstTick()
    {
        if (!world.isRemote)
        {
            checkIfIsReady();
            if (isMaster()) getEnergyFromSun();
        }
    }

    @Override
    public void onTick()
    {
        if (!this.world.isRemote)
        {
            if (isMaster() && panelReady.size() > 0 && energyCanOutput > 0)
                energyContainer.receiveInternally(energyCanOutput, false);

            if (tick >= (20 + random))
            {
                tick = 0;
                checkIfIsReady();
                if (isMaster()) getEnergyFromSun();
            }
            tick++;
        }
    }

    private int outputEnergy(int maxReceive, boolean simulate)
    {
        if (!isMaster()) return getMaster().outputEnergy(maxReceive, simulate);
        if (inUse) return 0;
        if (maxReceive <= 0) return 0;
        inUse = true;
        int out = MultiBlockHelper.outputEnergy(this, maxReceive, energyContainer.getMaxOutput(), simulate, world).get(0);
        inUse = false;
        return out;
    }

    @Override
    public double getMaxRenderDistanceSquared()
    {
        return super.getMaxRenderDistanceSquared() * IRConfig.Render.frameSolarPanelRenderMult.get();
    }

    public void setPanelInv(boolean panelInv)
    {
        this.panelInv = panelInv;
        sync();
    }

    @Override
    public Direction[] getFacesToCheck()
    {
        return new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntitySolarPanelFrame;
    }

    public Set<TileEntitySolarPanelFrame> getPanelReadySet()
    {
        return panelReady;
    }

    public void checkIfIsReady()
    {
        if (hasPanel() && world.provider.hasSkyLight() && world.canBlockSeeSky(pos.offset(Direction.UP))
                && world.getSkylightSubtracted() == 0)
        {
            getMaster().getPanelReadySet().add(this);
        } else getMaster().getPanelReadySet().remove(this);
    }

    @Override
    public void checkForOutPuts()
    {
        if (world.isRemote) return;

        Direction facing = getBlockFacing();
        BlockPos currentPos = pos.offset(facing);
        BlockState state = world.getBlockState(currentPos);
        TileEntity te = world.getTileEntity(currentPos);
        if (te == null) return;
        if (!(state.getBlock() instanceof BlockSolarPanelFrame) && te.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null) != null)
            addMachine(te, facing);
    }

    public void getEnergyFromSun()
    {
        if (world.provider.hasSkyLight()
                && world.canBlockSeeSky(pos.offset(Direction.UP))
                && world.getSkylightSubtracted() == 0)
        {
            energyCanOutput = ((TileEntitySolarPanelBase.getGeneration(world, pos) * panelReady.size()) * getMultiplier());
        }
    }

    private int getMultiplier()
    {
        return IRConfig.Main.panelFrameMultiplier.get();
    }

    @Override
    public void onBlockBreak()
    {
        if (panelInv) Utils.spawnItemStack(world, pos, new ItemStack(BlocksRegistration.SPANEL_ITEM.get()));
        super.onBlockBreak();
    }

    public boolean hasPanel()
    {
        return panelInv;
    }

    public ItemStack getPanel()
    {
        return panelStack;
    }

    public Direction getBlockFacing()
    {
        if (blockFacing == null)
        {
            BlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() instanceof BlockSolarPanelFrame)
                blockFacing = state.get(BlockSolarPanelFrame.FACING);
            else blockFacing = Direction.NORTH;
        }
        return blockFacing;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        panelInv = compound.getBoolean("panel");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("panel", panelInv);
        return super.write(compound);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == getBlockFacing())
            return LazyOptional.of(() -> energyContainer).cast();
        return super.getCapability(capability, facing);
    }
}
