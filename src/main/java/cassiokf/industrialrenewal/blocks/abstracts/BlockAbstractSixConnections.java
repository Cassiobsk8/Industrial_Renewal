package cassiokf.industrialrenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public abstract class BlockAbstractSixConnections extends BlockAbstractFourConnections
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    private static double UP2 = 16;
    private static double DOWN1 = 0;

    public BlockAbstractSixConnections(Properties properties, float nodeWidth, float nodeHeight)
    {
        super(properties, nodeWidth, nodeHeight, 16);
    }

    @Override
    public BlockState getDefaultBlockState()
    {
        BlockState defaultState = getDefaultState();
        for (Direction dir : Direction.values())
        {
            defaultState = defaultState.with(getPropertyBasedOnDirection(dir), false);
        }
        return defaultState;
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        if (isConnected(state, NORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, SOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = (nodeWidth / 2) + 8;
        }
        if (isConnected(state, WEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, EAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = (nodeWidth / 2) + 8;
        }
        if (isConnected(state, UP))
        {
            UP2 = 16;
        } else
        {
            UP2 = 8 + (nodeHeight / 2);
        }
        if (isConnected(state, DOWN))
        {
            DOWN1 = 0;
        } else
        {
            DOWN1 = 8 - (nodeHeight / 2);
        }
        return Block.makeCuboidShape(WESTX1, DOWN1, NORTHZ1, EASTX2, (collision && fenceCollision()) ? 24 : UP2, SOUTHZ2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = getDefaultState();
        for (Direction face : Direction.values())
        {
            state = state.with(getPropertyBasedOnDirection(face), canConnectTo(context.getWorld(), context.getPos(), face));
        }
        return state;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(getPropertyBasedOnDirection(facing), canConnectTo(worldIn, currentPos, facing));
    }

    @Override
    public BooleanProperty getPropertyBasedOnDirection(Direction direction)
    {
        switch (direction)
        {
            default:
            case DOWN:
                return DOWN;
            case UP:
                return UP;
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }
}
