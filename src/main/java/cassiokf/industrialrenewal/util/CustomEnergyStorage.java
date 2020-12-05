package cassiokf.industrialrenewal.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class CustomEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundNBT> {
    protected int stored;
    protected int capacity;
    protected int input;
    protected int output;

    public CustomEnergyStorage() {
        this(1000, 1000, 1000);
    }

    public CustomEnergyStorage(int capacity, int input, int output) {
        this(0, capacity, input, output);
    }

    public CustomEnergyStorage(int power, int capacity, int input, int output) {
        this.stored = power;
        this.capacity = capacity;
        this.input = input;
        this.output = output;
    }

    public CustomEnergyStorage(CompoundNBT dataTag) {
        this.deserializeNBT(dataTag);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        final CompoundNBT dataTag = new CompoundNBT();
        serializeNBT(dataTag);
        return dataTag;
    }

    public void serializeNBT(CompoundNBT compound)
    {
        compound.putInt("IRStored", this.stored);
        compound.putInt("IRCapacity", this.capacity);
        compound.putInt("IRInput", this.input);
        compound.putInt("IROutput", this.output);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        if (nbt.contains("IRStored"))
            this.stored = nbt.getInt("IRStored");
        if (nbt.contains("IRCapacity"))
            this.capacity = nbt.getInt("IRCapacity");
        if (nbt.contains("IRInput"))
            this.input = nbt.getInt("IRInput");
        if (nbt.contains("IROutput"))
            this.output = nbt.getInt("IROutput");

        if (this.stored > this.getMaxEnergyStored())
            this.stored = this.getMaxEnergyStored();
    }

    public int receiveInternally(int maxReceive, boolean simulate)
    {
        final int acceptedPower = Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), Math.min(this.getMaxInput(), maxReceive));

        if (!simulate)
        {
            this.stored += acceptedPower;
            onEnergyChange();
        }

        return acceptedPower;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        final int acceptedPower = Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), Math.min(this.getMaxInput(), maxReceive));

        if (!simulate)
        {
            this.stored += acceptedPower;
            onEnergyChange();
        }

        return this.canReceive() ? acceptedPower : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        final int removedPower = Math.min(this.getEnergyStored(), Math.min(this.getMaxOutput(), maxExtract));
        if (!simulate)
        {
            this.stored -= removedPower;
            onEnergyChange();
        }
        return this.canExtract() ? removedPower : 0;
    }

    public int extractEnergyInternally(int maxExtract, boolean simulate)
    {
        final int removedPower = Math.min(this.getEnergyStored(), Math.min(this.getMaxOutput(), maxExtract));
        if (!simulate)
        {
            this.stored -= removedPower;
            onEnergyChange();
        }
        return removedPower;
    }

    @Override
    public int getEnergyStored()
    {
        return this.stored;
    }

    public void setEnergyStored(int stored)
    {
        this.stored = stored;
        onEnergyChange();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    public void setMaxEnergyStored(int capacity) {
        this.capacity = capacity;

        if (this.stored > capacity)
            this.stored = capacity;
    }

    public void onEnergyChange()
    {
    }

    public int getMaxInput() {
        return this.input;
    }

    public void setMaxInput(int input) {
        this.input = input;
    }

    public int getMaxOutput() {
        return this.output;
    }

    public void setMaxOutput(int output) {
        this.output = output;
    }

    @Override
    public boolean canExtract() {
        return this.getMaxOutput() > 0 && this.stored > 0;
    }

    @Override
    public boolean canReceive() {
        return this.getMaxInput() > 0;
    }
}
