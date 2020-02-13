package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockFrame extends BlockBase
{

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockFrame(Block.Properties property)
    {
        super(property);
    }
    /*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        return BASE_AABB;
    }
*/
}
