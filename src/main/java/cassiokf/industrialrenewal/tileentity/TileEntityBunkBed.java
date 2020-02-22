package cassiokf.industrialrenewal.tileentity;

import net.minecraft.tileentity.TileEntity;

import static cassiokf.industrialrenewal.init.TileRegistration.BUNKBED_TILE;

public class TileEntityBunkBed extends TileEntity
{
    public TileEntityBunkBed()
    {
        super(BUNKBED_TILE.get());
    }
}
