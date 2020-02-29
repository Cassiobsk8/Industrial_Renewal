package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityBulkConveyorBase;

import static cassiokf.industrialrenewal.init.TileRegistration.CONVEYORV_TILE;

public class TileEntityBulkConveyor extends TileEntityBulkConveyorBase
{
    public TileEntityBulkConveyor()
    {
        super(CONVEYORV_TILE.get());
    }
}
