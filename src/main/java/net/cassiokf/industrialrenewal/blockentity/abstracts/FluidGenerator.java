package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.config.Config;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidGenerator {
    public static int energyPerTick = 32;
    private final BlockEntitySyncable attachedTE;
    public final CustomEnergyStorage energyStorage = new CustomEnergyStorage(128, 128, energyPerTick) {
        @Override
        public void onEnergyChange() {
            FluidGenerator.this.sync();
        }
        
        @Override
        public boolean canReceive() {
            return false;
        }
    };
    public final LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);
    private final int TANK_CAPACITY = 8000;
    public final FluidTank tank = new FluidTank(TANK_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            FluidGenerator.this.sync();
        }
        
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return Config.getFuelHash().get(stack.getFluid().getFluidType().getDescription(stack).toString()) != null;
        }
    };
    public final LazyOptional<FluidTank> tankHandler = LazyOptional.of(() -> tank);
    private boolean canGenerate = false;
    private boolean isGenerating = false;
    private boolean oldRunning = false;
    private int fuelTime = 0;
    public FluidGenerator(BlockEntitySyncable te) {
        attachedTE = te;
    }

    public void invalidate() {
        tankHandler.invalidate();
        energyHandler.invalidate();
    }
    
    public void onTick() {
        if (canGenerate) {
            updateLiquidFuel();
            if (fuelTime > 0) {
                isGenerating = true;
                //energyStorage.receiveEnergy(energyPerTick, false);
                energyStorage.addEnergy(energyPerTick);
                fuelTime--;
            } else isGenerating = false;
        } else isGenerating = false;
        
        if (isGenerating != oldRunning) {
            oldRunning = isGenerating;
            sync();
        }
    }
    
    private void updateLiquidFuel() {
        if (fuelTime > 0) return;
        FluidStack resource = tank.getFluid();
        if (resource.getAmount() <= 0) return;
        
        //int fuel = IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getName()) != null ? IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getName()) : 0;
        //        int fuel = Config.getFuelHash().get(resource.getFluid().getRegistryName().toString()) !=null? Config.getFuelHash().get(resource.getFluid().getRegistryName().toString()) : 0;
        int fuel = resource.getAmount();
        //        industrialrenewal.LOGGER.info("fuel: "+fuel);
        if (fuel > 0) {
            int amount = Math.min(1000, resource.getAmount());
            float norm = Utils.normalizeClamped(amount, 0, 1000);
            
            //            fuelTime = (int) (fuel * norm) * 4;
            fuelTime = Config.getFuelHash().get(resource.getTranslationKey()) != null ? Config.getFuelHash().get(resource.getDisplayName().toString()) : 0;
            tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            //maxFuelTime = fuelTime;
            //fuelName = resource.getLocalizedName();
        }
    }
    
    public boolean isGenerating() {
        return isGenerating;
    }
    
    public String getTankContent() {
        return tank.getFluid().getTranslationKey();
    }
    
    public int getTankAmount() {
        return tank.getFluidAmount();
    }
    
    public int getGenerateAmount() {
        return energyPerTick;
    }
    
    
    public CompoundTag saveGenerator(CompoundTag compound) {
        compound.putInt("FGenerator", fuelTime);
        compound.putBoolean("running", isGenerating);
        compound.putBoolean("generate", canGenerate);
        compound.putInt("energy", energyStorage.getEnergyStored());
        compound.put("tank", tank.writeToNBT(new CompoundTag()));
        return compound;
    }
    
    public void loadGenerator(CompoundTag compound) {
        fuelTime = compound.getInt("FGenerator");
        isGenerating = compound.getBoolean("running");
        canGenerate = compound.getBoolean("generate");
        energyStorage.setEnergy(compound.getInt("energy"));
        tank.readFromNBT(compound.getCompound("tank"));
    }
    
    public void setCanGenerate(boolean value) {
        canGenerate = value;
    }
    
    public void changeCanGenerate() {
        canGenerate = !canGenerate;
    }
    
    public void sync() {
        attachedTE.sync();
    }
    
    public void setChanged() {
        attachedTE.setChanged();
    }
}
