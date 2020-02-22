package cassiokf.industrialrenewal.tileentity;

import net.minecraft.tileentity.TileEntity;

import static cassiokf.industrialrenewal.init.TileRegistration.CONCRETE_TILE;

public class TileEntityConcrete extends TileEntity
{
    private boolean used = false;

    public TileEntityConcrete()
    {
        super(CONCRETE_TILE.get());
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
