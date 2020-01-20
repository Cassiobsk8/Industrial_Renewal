package cassiokf.industrialrenewal.util;

import net.minecraftforge.energy.IEnergyStorage;

public class VoltsEnergyContainer implements IEnergyStorage
{
    protected int stored;
    protected int capacity;
    protected int input;
    protected int output;

    public VoltsEnergyContainer()
    {
        this(1000, 1000, 1000);
    }

    public VoltsEnergyContainer(int capacity, int input, int output)
    {
        this(0, capacity, input, output);
    }

    public VoltsEnergyContainer(int power, int capacity, int input, int output) {
        this.stored = power;
        this.capacity = capacity;
        this.input = input;
        this.output = output;
    }

    /*
        public VoltsEnergyContainer(NBTTagCompound dataTag) {
            this.deserializeNBT(dataTag);
        }

        @Override
        public NBTTagCompound serializeNBT() {
            final NBTTagCompound dataTag = new NBTTagCompound();

            dataTag.setInteger("IRStored", this.stored);
            dataTag.setInteger("IRCapacity", this.capacity);
            dataTag.setInteger("IRInput", this.input);
            dataTag.setInteger("IROutput", this.output);

            return dataTag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if (nbt.hasKey("IRStored"))
                this.stored = nbt.getInteger("IRStored");
            if (nbt.hasKey("IRCapacity"))
                this.capacity = nbt.getInteger("IRCapacity");
            if (nbt.hasKey("IRInput"))
                this.input = nbt.getInteger("IRInput");
            if (nbt.hasKey("IROutput"))
                this.output = nbt.getInteger("IROutput");

            if (this.stored > this.getMaxEnergyStored())
                this.stored = this.getMaxEnergyStored();
        }
    */
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        final int acceptedPower = Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), Math.min(this.getMaxInput(), maxReceive));

        if (!simulate)
        {
            this.stored += acceptedPower;
            onEnergyChange();
        }

        return this.canReceive() ? acceptedPower : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        final int removedPower = Math.min(this.getEnergyStored(), Math.min(this.getMaxOutput(), maxExtract));
        if (!simulate)
        {
            this.stored -= removedPower;
            onEnergyChange();
        }
        return this.canExtract() ? removedPower : 0;
    }

    @Override
    public int getEnergyStored() {
        return this.stored;
    }

    public void setEnergyStored(int stored) {
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
