package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockWindTurbinePillar;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.MultiBlockHelper;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityWindTurbinePillar extends TileEntityMultiBlocksTube<TileEntityWindTurbinePillar>
{
    private static final VoltsEnergyContainer dummyEnergyContainer = new VoltsEnergyContainer(0, 0, 0)
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

    public final VoltsEnergyContainer energyContainer = new VoltsEnergyContainer(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
    {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return TileEntityWindTurbinePillar.this.onEnergyReceived(maxReceive, simulate);
        }
    };

    public int averageEnergy;
    public int potentialEnergy;
    private int oldPotential = -1;
    private int oldEnergy;
    private int tick;

    private float amount;//For Lerp

    private EnumFacing facing;

    private static final EnumFacing[] faces = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
    private boolean isBase;


    @Override
    public void beforeInitialize()
    {
        getIsBase();
        sync();
    }

    public int onEnergyReceived(int maxReceive, boolean simulate)
    {
        if (world.isRemote) return 0;
        if (!isMaster()) return getMaster().onEnergyReceived(maxReceive, simulate);

        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;
        if (!simulate) potentialEnergy = maxReceive;
        if (maxReceive <= 0)
        {
            inUse = false;
            return 0;
        }
        List<Integer> out = MultiBlockHelper.outputEnergy(this, maxReceive, energyContainer.getMaxOutput(), simulate, world);
        if (!simulate) outPut += out.get(0);
        outPutCount = out.get(1);
        inUse = false;
        return out.get(0);
    }

    @Override
    public void tick()
    {
        if (!world.isRemote && isMaster())
        {
            if (tick >= 10)
            {
                tick = 0;
                averageEnergy = outPut / 10;
                outPut = 0;
                if (averageEnergy != oldEnergy || potentialEnergy != oldPotential)
                {
                    oldPotential = potentialEnergy;
                    oldEnergy = averageEnergy;
                    sync();
                }
            }
            tick++;
        }
        //if (world.isRemote && isMaster()) world.spawnParticle(EnumParticleTypes.BARRIER, pos.getX() +0.5D, pos.getY() +0.5D, pos.getZ() +0.5D, 0, 0, 0);
    }

    @Override
    public EnumFacing[] getFacesToCheck()
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
        outPut = 0;
        afterInit();
        //potentialEnergy = 0;
        if (!world.isRemote && isBase)
        {
            getMaster().getMachineContainers().clear();
            for (EnumFacing face : EnumFacing.HORIZONTALS)
            {
                BlockPos currentPos = pos.offset(face);

                TileEntity te = world.getTileEntity(currentPos);
                boolean hasMachine = !(te instanceof TileEntityWindTurbinePillar) && te != null;
                IEnergyStorage cap = null;
                if (hasMachine) cap = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
                if (hasMachine && cap != null && cap.canReceive()) getMaster().addMachine(te, face);
            }
        }
        this.sync();
    }

    @Override
    public void afterInit()
    {
        if (getIsBase() && getMaster() != this) setMaster(this);
    }

    public EnumFacing getBlockFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        facing = state.getBlock() instanceof BlockWindTurbinePillar ? state.getValue(BlockWindTurbinePillar.FACING) : EnumFacing.NORTH;
        return facing;
    }

    public void setFacing(EnumFacing facing)
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
        return Utils.formatEnergyString(getMaster().averageEnergy) + "/t";
    }

    public boolean isBase()
    {
        return isBase;
    }

    public boolean getIsBase()
    {
        IBlockState state = world.getBlockState(pos.down());
        isBase = !(state.getBlock() instanceof BlockWindTurbinePillar);
        return isBase;
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
            if (facing == EnumFacing.UP)
                return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
            else if (isBase)
                return CapabilityEnergy.ENERGY.cast(dummyEnergyContainer);
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.isBase = compound.getBoolean("base");
        averageEnergy = compound.getInteger("energy_average");
        if (hasWorld() && world.isRemote) this.potentialEnergy = compound.getInteger("potential");
        if (hasWorld() && world.isRemote) this.outPut = compound.getInteger("outPut");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("base", this.isBase);
        compound.setInteger("energy_average", averageEnergy);
        compound.setInteger("potential", potentialEnergy);
        compound.setInteger("outPut", outPut);
        return super.writeToNBT(compound);
    }
}
