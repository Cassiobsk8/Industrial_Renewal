package cassiokf.industrialrenewal.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>
{
    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        int energyReceived = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && energyReceived > 0) onEnergyChange();
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        int energyExtracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate && energyExtracted > 0) onEnergyChange();
        return energyExtracted;
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("energy", getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        setEnergy(nbt.getInt("energy"));
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }

    public void addEnergy(int energy)
    {
        this.energy += energy;
        if (this.energy > getMaxEnergyStored())
        {
            this.energy = getEnergyStored();
        }
    }

    public void setMaxCapacity(int maxCapacity)
    {
        this.capacity = maxCapacity;
    }

    public void onEnergyChange()
    {
    }
}
