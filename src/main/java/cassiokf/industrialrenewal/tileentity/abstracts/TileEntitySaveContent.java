package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileEntitySaveContent extends TileEntitySync
{
    public TileEntitySaveContent(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public abstract CustomFluidTank getTank();
}
