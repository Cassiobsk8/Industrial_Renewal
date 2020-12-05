package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.MultiBlockHelper;
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
import java.util.List;

public abstract class TileEntityEnergyCable extends TileEntityMultiBlocksTube<TileEntityEnergyCable>
{

    public final CustomEnergyStorage energyContainer = new CustomEnergyStorage(getMaxEnergyToTransport(), getMaxEnergyToTransport(), getMaxEnergyToTransport())
    {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return TileEntityEnergyCable.this.onEnergyReceived(maxReceive, simulate);
        }
    };

    public int averageEnergy;
    public int potentialEnergy;
    private int oldPotential = -1;
    private int oldEnergy;
    private int tick;

    public TileEntityEnergyCable(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }


    @Override
    public void onTick()
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
    }

    public abstract int getMaxEnergyToTransport();

    public int onEnergyReceived(int maxReceive, boolean simulate)
    {
        if (world.isRemote) return 0;
        if (!isMaster()) return getMaster().onEnergyReceived(maxReceive, simulate);

        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;
        int maxTransfer = Math.min(maxReceive, this.energyContainer.getMaxOutput());
        if (!simulate) potentialEnergy = maxTransfer;
        if (maxReceive <= 0)
        {
            inUse = false;
            return 0;
        }
        List<Integer> out = MultiBlockHelper.outputEnergy(this, maxTransfer, energyContainer.getMaxOutput(), simulate, world);
        if (!simulate) outPut += out.get(0);
        outPutCount = out.get(1);
        inUse = false;
        return out.get(0);
    }

    @Override
    public void checkForOutPuts()
    {
        if (world.isRemote) return;
        for (Direction face : Direction.values())
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = te != null && !(te instanceof TileEntityEnergyCable);
            IEnergyStorage eStorage = null;
            if (hasMachine) eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
            if (hasMachine && eStorage != null && eStorage.canReceive())
                addMachine(te, face);
        }
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> getMaster().energyContainer).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound) {
        averageEnergy = compound.getInt("energy_average");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("energy_average", averageEnergy);
        return super.write(compound);
    }
}
