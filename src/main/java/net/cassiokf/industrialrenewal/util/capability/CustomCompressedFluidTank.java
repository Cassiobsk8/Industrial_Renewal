package net.cassiokf.industrialrenewal.util.capability;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CustomCompressedFluidTank extends CustomFluidTank
{
    public CustomCompressedFluidTank()
    {
        super(0);
    }
    
    public CustomCompressedFluidTank(int capacity)
    {
        super(capacity);
    }

    public CustomCompressedFluidTank(int capacity, Predicate<FluidStack> validator)
    {
        super(capacity, validator);
    }
    
    @Override
    public CustomCompressedFluidTank setBlockEntity(BlockEntitySyncable entity) {
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
    public int fill(FluidStack resource, FluidAction action)
    {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        return super.drain(0, action);
    }

    @Override
    public boolean canFill()
    {
        return false;
    }
    
    @Override
    public boolean canDrain()
    {
        return false;
    }
}
