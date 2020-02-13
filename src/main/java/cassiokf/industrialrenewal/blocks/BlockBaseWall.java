package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.redstone.BlockSignalIndicator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockBaseWall extends BlockBase
{
    public static final BooleanProperty CORE = BooleanProperty.create("core");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    private static float NORTHZ1 = 0.25f;
    private static float SOUTHZ2 = 0.75f;
    private static float WESTX1 = 0.25f;
    private static float EASTX2 = 0.75f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

    public BlockBaseWall(Block.Properties properties)
    {
        super(properties);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
        {
            BlockState actualState = state.getActualState(source, pos);

            if (isConnected(actualState, NORTH))
            {
                NORTHZ1 = 0.0f;
            } else
            {
                NORTHZ1 = 0.25f;
            }
            if (isConnected(actualState, SOUTH))
            {
                SOUTHZ2 = 1.0f;
            } else
            {
                SOUTHZ2 = 0.75f;
            }
            if (isConnected(actualState, WEST))
            {
                WESTX1 = 0.0f;
            } else
            {
                WESTX1 = 0.25f;
            }
            if (isConnected(actualState, EAST))
            {
                EASTX2 = 1.0f;
            } else
            {
                EASTX2 = 0.75f;
            }
            return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        }


        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
        {
            if (!isActualState)
            {
                state = state.getActualState(worldIn, pos);
            }
            if (isConnected(state, NORTH))
            {
                NORTHZ1 = 0.0f;
            } else
            {
                NORTHZ1 = 0.25f;
            }
            if (isConnected(state, SOUTH))
            {
                SOUTHZ2 = 1.0f;
            } else
            {
                SOUTHZ2 = 0.75f;
            }
            if (isConnected(state, WEST))
            {
                WESTX1 = 0.0f;
            } else
            {
                WESTX1 = 0.25f;
            }
            if (isConnected(state, EAST))
            {
                EASTX2 = 1.0f;
            } else
            {
                EASTX2 = 0.75f;
            }
            final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CORE, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    private boolean shouldRenderCenter(IBlockReader world, BlockPos ownPos)
    {
        return !((canCenterConnectTo(world, ownPos, Direction.NORTH) && canCenterConnectTo(world, ownPos, Direction.SOUTH) && !canCenterConnectTo(world, ownPos, Direction.EAST) && !canCenterConnectTo(world, ownPos, Direction.WEST))
                || (!canCenterConnectTo(world, ownPos, Direction.NORTH) && !canCenterConnectTo(world, ownPos, Direction.SOUTH) && canCenterConnectTo(world, ownPos, Direction.EAST) && canCenterConnectTo(world, ownPos, Direction.WEST)));
    }

    private boolean canCenterConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockBaseWall || nb.isNormalCube(neighbourState, worldIn, neighbourPos) || nb instanceof BlockWindow;
    }

    private boolean canConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockBaseWall || nb.isNormalCube(neighbourState, worldIn, neighbourPos)
                || nb instanceof BlockElectricGate
                || nb instanceof BlockLight
                || nb instanceof BlockSignalIndicator
                || nb instanceof BlockWindow;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing == Direction.UP || facing == Direction.DOWN) return stateIn;

        stateIn = stateIn.with(getPropertyBasedOnDirection(facing), canConnectTo(worldIn, currentPos, facing))
                .with(CORE, shouldRenderCenter(worldIn, currentPos));
        return stateIn;
    }

    private BooleanProperty getPropertyBasedOnDirection(Direction direction)
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

    public final boolean isConnected(final BlockState state, final BooleanProperty property)
    {
        return state.get(property);
    }
}
