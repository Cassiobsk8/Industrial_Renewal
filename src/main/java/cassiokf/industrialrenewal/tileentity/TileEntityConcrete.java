package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.TileEntityRegister;
import net.minecraft.tileentity.TileEntity;

public class TileEntityConcrete extends TileEntity
{
    private boolean used = false;

    public TileEntityConcrete()
    {
        super(TileEntityRegister.CONCRETE);
    }

    public boolean isUsed()
    {
        return used;
    }

    public void setUsed(boolean value)
    {
        used = value;
    }
}
