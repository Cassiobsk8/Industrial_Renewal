package net.cassiokf.industrialrenewal.util.capability;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CustomPressureFluidTank extends CustomFluidTank {
    public CustomPressureFluidTank() {
        super(0);
    }
    
    public CustomPressureFluidTank(int capacity) {
        super(capacity);
    }
    
    public CustomPressureFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }
    
    @Override
    public CustomPressureFluidTank setBlockEntity(BlockEntitySyncable entity) {
        blockEntity = entity;
        return this;
    }
    
    public int receiveCompressedFluid(int amount, int y, FluidAction action) {
        return 0;
    }
    
    public boolean canPassCompressedFluid() {
        return true;
    }
    
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }
    
    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return super.drain(0, action);
    }
    
    @Override
    public boolean canFill() {
        return false;
    }
    
    @Override
    public boolean canDrain() {
        return false;
    }
}
