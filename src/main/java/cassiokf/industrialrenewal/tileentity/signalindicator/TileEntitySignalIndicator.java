package cassiokf.industrialrenewal.tileentity.signalindicator;

import net.minecraft.tileentity.TileEntity;

public class TileEntitySignalIndicator extends TileEntity {

    public TileEntitySignalIndicator() {
    }

    public boolean active() {
        if (this.world.isBlockPowered(this.pos)) {
            return true;
        }
        return false;
    }
}
