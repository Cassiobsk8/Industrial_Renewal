package cassiokf.industrialrenewal.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntityConcrete extends TileEntity
{
    private boolean used = false;

    public boolean isUsed()
    {
        return used;
    }

    public void setUsed(boolean used)
    {
        this.used = used;
    }
}
