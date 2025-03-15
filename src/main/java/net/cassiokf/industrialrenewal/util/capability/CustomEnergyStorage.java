package net.cassiokf.industrialrenewal.util.capability;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    
    private boolean canExtract = true;
    private boolean canReceive = true;
    
    private BlockEntitySyncable blockEntity;
    
    public CustomEnergyStorage(int capacity)
    {
        super(capacity, capacity, capacity);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int passEnergy)
    {
        super(capacity, maxReceive, maxExtract, passEnergy);
    }
    
    public CustomEnergyStorage setBlockEntity(BlockEntitySyncable entity) {
        this.blockEntity = entity;
        return this;
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

    public void onEnergyChange()
    {
        if(blockEntity!= null) {
            blockEntity.sync();
        }
    }
    
    @Override
    public boolean canExtract() {
        return canExtract && super.canExtract();
    }
    
    @Override
    public boolean canReceive() {
        return canReceive && super.canReceive();
    }
    
    public CustomEnergyStorage noExtraction() {
        this.canExtract = false;
        return this;
    }
    
    public CustomEnergyStorage noReceive() {
        this.canReceive = false;
        return this;
    }
    
    public void setMaxCapacity(int maxCapacity)
    {
        this.capacity = maxCapacity;
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }
    
    public int addEnergy(int maxReceive) {
        return addEnergy(maxReceive, false);
    }
    public int addEnergy(int maxReceive, boolean simulate)
    {
        int energyReceived = Math.min(capacity - energy, maxReceive);
        if (!simulate) {
            energy += energyReceived;
            onEnergyChange();
        }
        return energyReceived;
    }
    public int subtractEnergy(int maxExtract){
        return  subtractEnergy(maxExtract, false);
    }

    public int subtractEnergy(int maxExtract, boolean simulate){
        int energyExtracted = Math.min(energy,  maxExtract);
        if (!simulate) {
            energy -= energyExtracted;
            onEnergyChange();
        }
        return energyExtracted;
    }

//    @Override
//    public Tag serializeNBT() {
//        CompoundTag tag = new CompoundTag();
//        tag.putInt("energy", getEnergyStored());
//        return tag;
//    }


//    @Override
//    public void deserializeNBT(CompoundNBT nbt)
//    {
//        setEnergy(nbt.getInt("energy"));
//    }
}
