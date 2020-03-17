package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstractclass.TEBase;

public class TileEntityConcrete extends TEBase
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
