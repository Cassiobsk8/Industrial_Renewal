package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.util.MultiBlockHelper;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public abstract class TileEntityEnergyCable extends TileEntityMultiBlocksTube<TileEntityEnergyCable>
{

    public final VoltsEnergyContainer energyContainer;

    public int averageEnergy;
    public int potentialEnergy;
    private int oldPotential = -1;
    private int oldEnergy;
    private int tick;

    public TileEntityEnergyCable()
    {
        this.energyContainer = new VoltsEnergyContainer(getMaxEnergyToTransport(), getMaxEnergyToTransport(), getMaxEnergyToTransport())
        {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate)
            {
                return TileEntityEnergyCable.this.onEnergyReceived(maxReceive, simulate);
            }
        };
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
                    Sync();
                }
            }
            tick++;
        }
    }

    public abstract int getMaxEnergyToTransport();

    public int onEnergyReceived(int maxReceive, boolean simulate)
    {
        if (!isMaster()) return getMaster().onEnergyReceived(maxReceive, simulate);

        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;
        if (!simulate) potentialEnergy = Math.min(maxReceive, this.energyContainer.getMaxOutput());
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
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = te != null
                    && !(te instanceof TileEntityEnergyCable)
                    && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite());
            IEnergyStorage eStorage = null;
            if (hasMachine) eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
            if (hasMachine && eStorage != null && eStorage.canReceive())
                addMachine(currentPos, face);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && getMaster() != null)
            return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        averageEnergy = compound.getInteger("energy_average");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy_average", averageEnergy);
        return super.writeToNBT(compound);
    }
}
