package net.cassiokf.industrialrenewal.util.capability;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CustomFluidTank extends FluidTank
{
    public BlockEntitySyncable blockEntity;
    private boolean canFill = true;
    private boolean canDrain = true;
    
    public CustomFluidTank(int capacity)
    {
        super(capacity);
    }

    public CustomFluidTank(int capacity, Predicate<FluidStack> validator)
    {
        super(capacity, validator);
    }

    public void onFluidChange(){
        if(blockEntity!= null)
            blockEntity.sync();
    }
    
    public CustomFluidTank setBlockEntity(BlockEntitySyncable entity) {
        this.blockEntity = entity;
        return this;
    }

    public int fillInternal(FluidStack resource, FluidAction action)
    {
        onFluidChange();
        return super.fill(resource, action);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        onFluidChange();
        return canFill() ? super.fill(resource, action) : 0;
    }

    public FluidStack drainInternal(int maxDrain, FluidAction action)
    {
        onFluidChange();
        return super.drain(maxDrain, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        onFluidChange();
        return canDrain() ? super.drain(maxDrain, action) : super.drain(0, action);
    }
    
    public CustomFluidTank noDrain() {
        this.canDrain = false;
        return this;
    }
    
    public CustomFluidTank noFill() {
        this.canFill = false;
        return this;
    }
    
    public boolean canFill()
    {
        return canFill;
    }

    public boolean canDrain()
    {
        return canDrain;
    }
}
