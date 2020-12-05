package cassiokf.industrialrenewal.util;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CustomFluidTank extends FluidTank
{
    public CustomFluidTank(int capacity)
    {
        super(capacity);
    }

    public CustomFluidTank(int capacity, Predicate<FluidStack> validator)
    {
        super(capacity, validator);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        return canFill(resource) ? super.fill(resource, action) : 0;
    }

    public FluidStack drainInternal(int maxDrain, FluidAction action)
    {
        return super.drain(maxDrain, action);
    }

    public boolean tryPassFluidInternal(int amount, IFluidHandler toTank)
    {
        FluidStack stack = this.drainInternal(toTank.fill(this.drainInternal(amount, FluidAction.SIMULATE), FluidAction.EXECUTE), FluidAction.EXECUTE);
        return stack.getAmount() > 0;
    }

    public boolean tryPassFluid(int amount, IFluidHandler toTank)
    {
        FluidStack stack = this.drain(toTank.fill(this.drain(amount, FluidAction.SIMULATE), FluidAction.EXECUTE), FluidAction.EXECUTE);
        return stack.getAmount() > 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        return canDrain() ? super.drain(maxDrain, action) : super.drain(0, action);
    }

    public int fillInternal(FluidStack resource, FluidAction action)
    {
        return super.fill(resource, action);
    }

    public boolean canFill(FluidStack resource)
    {
        return true;
    }

    public boolean canDrain()
    {
        return true;
    }
}
