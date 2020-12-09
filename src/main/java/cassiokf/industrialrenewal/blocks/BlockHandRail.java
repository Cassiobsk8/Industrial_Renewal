package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class BlockHandRail extends BlockHorizontalFacing
{
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    protected static final VoxelShape RNORTH_AABB = makeCuboidShape(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape RSOUTH_AABB = makeCuboidShape(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = makeCuboidShape(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape REAST_AABB = makeCuboidShape(15, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = makeCuboidShape(0, 0, 0, 16, 24, 1);
    protected static final VoxelShape SOUTH_AABB = makeCuboidShape(0, 0, 15, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = makeCuboidShape(0, 0, 0, 1, 24, 16);
    protected static final VoxelShape EAST_AABB = makeCuboidShape(15, 0, 0, 16, 24, 16);

    //TODO Make HandRail connected like window
    public BlockHandRail()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, DOWN);
    }

    private boolean downConnection(IBlockReader world, BlockPos pos)
    {
        BlockState downState = world.getBlockState(pos.down());
        return !downState.isSolidSide(world, pos.down(), Direction.UP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState()
                .with(FACING, context.getPlayer().getHorizontalFacing().getOpposite())
                .with(DOWN, downConnection(context.getWorld(), context.getPos()));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing.equals(Direction.DOWN)) return stateIn.with(DOWN, downConnection(worldIn, currentPos));
        return stateIn;
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        Direction face = state.get(FACING);
        if (face == Direction.NORTH)
        {
            return collision ? NORTH_AABB : RNORTH_AABB;
        }
        if (face == Direction.SOUTH)
        {
            return collision ? SOUTH_AABB : RSOUTH_AABB;
        }
        if (face == Direction.WEST)
        {
            return collision ? WEST_AABB : RWEST_AABB;
        } else
        {
            return collision ? EAST_AABB : REAST_AABB;
        }
    }
}
