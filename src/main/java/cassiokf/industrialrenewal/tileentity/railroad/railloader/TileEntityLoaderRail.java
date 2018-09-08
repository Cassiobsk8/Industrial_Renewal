package cassiokf.industrialrenewal.tileentity.railroad.railloader;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLoaderRail extends TileEntity {

    public void letItGo() {
        IBlockState state = this.world.getBlockState(this.pos);
        if (!state.getValue(BlockLoaderRail.PASS)) {
            this.world.setBlockState(this.pos, state.withProperty(BlockLoaderRail.PASS, true));
        }
    }

}
