package cassiokf.industrialrenewal.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidTankUtils extends FluidTank
{

    public FluidTankUtils(final TileEntity tileEntity, final int capacity)
    {
        super(capacity);
    }
}