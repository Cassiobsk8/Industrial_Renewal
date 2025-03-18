package net.cassiokf.industrialrenewal.obj;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CapPercentage {
    private BlockEntity blockEntity;
    private IEnergyStorage energyStorage;
    private IFluidHandler fluidHandler;
    private float percentage;
    private int amount;
    
    public CapPercentage(IEnergyStorage energyStorage, BlockEntity blockEntity, float percentage, int amount) {
        this.energyStorage = energyStorage;
        this.blockEntity = blockEntity;
        this.percentage = percentage;
        this.amount = amount;
    }
    
    public CapPercentage(IFluidHandler fluidHandler, BlockEntity blockEntity, float percentage, int amount) {
        this.fluidHandler = fluidHandler;
        this.blockEntity = blockEntity;
        this.percentage = percentage;
        this.amount = amount;
    }
    
    public BlockEntity getBlockEntity() {
        return blockEntity;
    }
    
    public void setBlockEntity(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }
    
    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }
    
    public void setFluidHandler(IFluidHandler fluidHandler) {
        this.fluidHandler = fluidHandler;
    }
    
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }
    
    public void setEnergyStorage(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }
    
    public float getPercentage() {
        return percentage;
    }
    
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
