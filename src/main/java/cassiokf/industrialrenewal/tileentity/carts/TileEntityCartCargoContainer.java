package cassiokf.industrialrenewal.tileentity.carts;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCartCargoContainer extends TileEntityCartBase {


    public TileEntityCartCargoContainer(World world, BlockPos pos, int color) {
        super(world, (double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), 5, color);
    }

    public TileEntityCartCargoContainer(World world) {
        super(world);
    }


    @Override
    protected void entityInit() {
        super.entityInit();
    }

}
