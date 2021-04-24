package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockWindTurbinePillar;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCable;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityWindTurbinePillar extends TileEntityEnergyCable
{
    private static final CustomEnergyStorage dummyEnergyContainer = new CustomEnergyStorage(0, 0, 0)
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
    };

    private float amount;//For Lerp

    private Direction facing;

    private static final Direction[] faces = new Direction[]{Direction.UP, Direction.DOWN};
    private boolean isBase;

    public TileEntityWindTurbinePillar(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public int getMaxEnergyToTransport()
    {
        return IRConfig.Main.maxEnergySWindTurbine.get();
    }

    @Override
    public void beforeInitialize()
    {
        getIsBase();
        sync();
    }

    @Override
    public Direction[] getFacesToCheck()
    {
        return faces;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityWindTurbinePillar;
    }

    @Override
    public void checkForOutPuts()
    {
        getIsBase();
        outPut = 0;
        potentialEnergy = 0;
        if (!world.isRemote && isBase)
        {
            for (Direction face : Direction.Plane.HORIZONTAL)
            {
                BlockPos currentPos = pos.offset(face);

                TileEntity te = world.getTileEntity(currentPos);
                boolean hasMachine = !(te instanceof TileEntityWindTurbinePillar) && te != null;
                IEnergyStorage cap = null;
                if (hasMachine) cap = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
                if (hasMachine && cap != null && cap.canReceive())
                    addMachine(te, face);
                else removeMachine(te);
            }
        }
        this.sync();
    }

    public Direction getBlockFacing()
    {
        if (facing != null) return facing;
        BlockState state = getBlockState();
        facing = state.getBlock() instanceof BlockWindTurbinePillar
                ? state.get(BlockWindTurbinePillar.FACING) : Direction.NORTH;
        return facing;
    }

    public void setFacing(Direction facing)
    {
        this.facing = facing;
    }

    public float getGenerationForGauge()
    {
        float currentAmount = Utils.normalizeClamped(getMaster().averageEnergy, 0, 128);
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1) * 90f;
    }

    public float getPotentialValue()
    {
        float currentAmount = Utils.normalizeClamped(getMaster().potentialEnergy, 0, 128);
        return currentAmount * 90f;
    }

    public String getText()
    {
        return getMaster().averageEnergy + " FE/t";
    }

    public boolean isBase()
    {
        return isBase;
    }

    public boolean getIsBase()
    {
        BlockState state = world.getBlockState(pos.down());
        isBase = !(state.getBlock() instanceof BlockWindTurbinePillar);
        return isBase;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
            if (facing == Direction.UP)
                return LazyOptional.of(() -> getMaster().energyContainer).cast();
            else if (isBase)
                return LazyOptional.of(() -> dummyEnergyContainer).cast();
        }
        return null;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        this.isBase = compound.getBoolean("base");
        if (hasWorld() && world.isRemote) this.potentialEnergy = compound.getInt("potential");
        if (hasWorld() && world.isRemote) this.outPut = compound.getInt("outPut");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("base", this.isBase);
        compound.putInt("potential", potentialEnergy);
        compound.putInt("outPut", outPut);
        return super.write(compound);
    }
}
