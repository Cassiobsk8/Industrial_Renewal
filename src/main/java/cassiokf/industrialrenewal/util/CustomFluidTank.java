package cassiokf.industrialrenewal.util;

import net.minecraftforge.fluids.FluidStack;
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
        return canFill() ? super.fill(resource, action) : 0;
    }

    public FluidStack drainInternal(int maxDrain, FluidAction action)
    {
        return super.drain(maxDrain, action);
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

    public boolean canFill()
    {
        return true;
    }

    public boolean canDrain()
    {
        return true;
    }
}
