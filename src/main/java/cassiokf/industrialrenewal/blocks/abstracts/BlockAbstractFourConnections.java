package cassiokf.industrialrenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public abstract class BlockAbstractFourConnections extends BlockAbstractNotFullCube
{
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public static double NORTHZ1 = 4;
    public static double SOUTHZ2 = 12;
    public static double WESTX1 = 4;
    public static double EASTX2 = 12;

    public float nodeWidth;
    public float nodeHeight;
    public float collisionY;

    public BlockAbstractFourConnections(Properties properties, float nodeWidth, float nodeHeight, float collisionY)
    {
        super(properties.notSolid());
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.collisionY = collisionY;
        setDefaultState(getDefaultBlockState());
    }

    public BlockState getDefaultBlockState()
    {
        return getDefaultState()
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(EAST, false)
                .with(WEST, false);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, false);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, true);
    }

    public VoxelShape getVoxelShape(BlockState state, boolean collision)
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
            SOUTHZ2 = nodeWidth / 2 + 8;
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
            EASTX2 = nodeWidth / 2 + 8;
        }
        return Block.makeCuboidShape(WESTX1, 8 - (nodeHeight / 2), NORTHZ1, EASTX2, (collision && fenceCollision()) ? 24 : (8 + (nodeHeight / 2)), SOUTHZ2);
    }

    public boolean fenceCollision()
    {
        return false;
    }

    public final boolean isConnected(final BlockState state, final BooleanProperty property)
    {
        return state.get(property);
    }

    public BooleanProperty getPropertyBasedOnDirection(Direction direction)
    {
        switch (direction)
        {
            default:
            case DOWN:
            case UP:
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

    public abstract boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection);

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = getDefaultState();
        for (Direction face : Direction.Plane.HORIZONTAL)
        {
            state = state.with(getPropertyBasedOnDirection(face), canConnectTo(context.getWorld(), context.getPos(), face));
        }
        return state;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing == Direction.UP || facing == Direction.DOWN) return stateIn;

        stateIn = stateIn.with(getPropertyBasedOnDirection(facing), canConnectTo(worldIn, currentPos, facing));
        return stateIn;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(NORTH, SOUTH, EAST, WEST);
    }
}
