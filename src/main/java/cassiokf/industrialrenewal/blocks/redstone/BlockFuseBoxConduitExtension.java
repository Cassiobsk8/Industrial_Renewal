package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class BlockFuseBoxConduitExtension extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0F, 0.375F, 0.125F, 1F, 0.625D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0F, 0.375F, 0.875F, 1F, 0.625D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.375F, 0F, 0.875F, 0.625D, 1F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.375F, 0F, 0.125F, 0.625D, 1F, 0);

    public BlockFuseBoxConduitExtension(Block.Properties property)
    {
        super(property);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        switch (state.get(FACING)) {
            default:
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
        }
    }
*/
}
