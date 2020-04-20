package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSolarPanelFrame;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.MultiBlockHelper;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileEntitySolarPanelFrame extends TileEntityMultiBlocksTube<TileEntitySolarPanelFrame>
{
    public final VoltsEnergyContainer energyContainer;
    private final Set<TileEntitySolarPanelFrame> panelReady = new HashSet<>();
    private final ItemStack panelStack = new ItemStack(ModBlocks.spanel);
    private int tick;
    private EnumFacing blockFacing;
    public boolean panelInv;
    private int energyCanOutput = 0;
    private int random = 0;

    public TileEntitySolarPanelFrame()
    {
        this.energyContainer = new VoltsEnergyContainer(10240, 0, 10240)
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
    }

    @Override
    public void onFirstTick()
    {
        random = world.rand.nextInt(10);
        if (!world.isRemote)
        {
            checkIfIsReady();
            if (isMaster()) getEnergyFromSun();
        }
    }

    @Override
    public void tick()
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
        return super.getMaxRenderDistanceSquared() * IRConfig.MainConfig.Render.frameSolarPanelRenderMult;
    }

    public void setPanelInv(boolean panelInv)
    {
        this.panelInv = panelInv;
        Sync();
    }

    @Override
    public EnumFacing[] getFacesToCheck()
    {
        return EnumFacing.HORIZONTALS;
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
        if (hasPanel() && world.provider.hasSkyLight() && world.canBlockSeeSky(pos.offset(EnumFacing.UP))
                && world.getSkylightSubtracted() == 0)
        {
            getMaster().getPanelReadySet().add(this);
        } else getMaster().getPanelReadySet().remove(this);
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;

        EnumFacing facing = getBlockFacing();
        BlockPos currentPos = pos.offset(facing);
        IBlockState state = world.getBlockState(currentPos);
        TileEntity te = world.getTileEntity(currentPos);
        if (!(state.getBlock() instanceof BlockSolarPanelFrame)
                && te != null
                && te.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
        {
            addMachine(te, facing);
        } else removeMachine(te);
    }

    public void getEnergyFromSun()
    {
        if (world.provider.hasSkyLight()
                && world.canBlockSeeSky(pos.offset(EnumFacing.UP))
                && world.getSkylightSubtracted() == 0)
        {
            energyCanOutput = ((TileEntitySolarPanelBase.getGeneration(world, pos) * panelReady.size()) * getMultiplier());
        }
    }

    private int getMultiplier()
    {
        return IRConfig.MainConfig.Main.panelFrameMultiplier;
    }

    @Override
    public void onBlockBreak()
    {
        if (panelInv) Utils.spawnItemStack(world, pos, new ItemStack(ModBlocks.spanel));
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

    public EnumFacing getBlockFacing()
    {
        if (blockFacing == null)
        {
            IBlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() instanceof BlockSolarPanelFrame)
                blockFacing = state.getValue(BlockSolarPanelFrame.FACING);
            else blockFacing = EnumFacing.NORTH;
        }
        return blockFacing;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        panelInv = compound.getBoolean("panel");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("panel", panelInv);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && facing == getBlockFacing())
                || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == getBlockFacing())
            return CapabilityEnergy.ENERGY.cast(this.energyContainer);
        return super.getCapability(capability, facing);
    }
}
