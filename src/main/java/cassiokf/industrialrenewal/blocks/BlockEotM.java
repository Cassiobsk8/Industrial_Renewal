package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockEotM extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0D, 0.0625D, 0.1875D, 0.0625D, 0.9375D, 0.8125D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1D, 0.0625D, 0.1875D, 0.9375D, 0.9375D, 0.8125D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.1875D, 0.0625D, 0.9375D, 0.8125D, 0.9375D, 1D);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.1875D, 0.0625D, 0.0625D, 0.8125D, 0.9375D, 0D);

    public BlockEotM(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction face = null;
        for (Direction face1 : Direction.Plane.HORIZONTAL)
        {
            if (context.getFace().equals(face1))
            {
                face = context.getFace().getOpposite();
            }
        }
        if (face == null) face = context.getPlayer().getHorizontalFacing();

        return getDefaultState().with(FACING, face);
    }
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
    {
        switch (state.getActualState(source, pos).get(FACING))
        {
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

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
