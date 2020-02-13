package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockWindow extends BlockBase
{
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(Direction.values()).map(facing -> BooleanProperty.create(facing.getName())).collect(Collectors.toList()));
    private static float NORTHZ1 = 0.4375f;
    private static float SOUTHZ2 = 0.5625f;
    private static float WESTX1 = 0.4375f;
    private static float EASTX2 = 0.5625f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

    public BlockWindow(Block.Properties property)
    {
        super(property);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            BlockState actualState = state.getActualState(source, pos);

            if (isConnected(actualState, Direction.NORTH)) {
                NORTHZ1 = 0.0f;
            } else
            {
                NORTHZ1 = 0.4375f;
            }
            if (isConnected(actualState, Direction.SOUTH)) {
                SOUTHZ2 = 1.0f;
            } else
            {
                SOUTHZ2 = 0.5625f;
            }
            if (isConnected(actualState, Direction.WEST)) {
                WESTX1 = 0.0f;
            } else
            {
                WESTX1 = 0.4375f;
            }
            if (isConnected(actualState, Direction.EAST)) {
                EASTX2 = 1.0f;
            } else
            {
                EASTX2 = 0.5625f;
            }
            return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        }


        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            if (!isActualState) {
                state = state.getActualState(worldIn, pos);
            }
            if (isConnected(state, Direction.NORTH)) {
                NORTHZ1 = 0.0f;
            } else
            {
                NORTHZ1 = 0.4375f;
            }
            if (isConnected(state, Direction.SOUTH)) {
                SOUTHZ2 = 1.0f;
            } else
            {
                SOUTHZ2 = 0.5625f;
            }
            if (isConnected(state, Direction.WEST)) {
                WESTX1 = 0.0f;
            } else
            {
                WESTX1 = 0.4375f;
            }
            if (isConnected(state, Direction.EAST)) {
                EASTX2 = 1.0f;
            } else
            {
                EASTX2 = 0.5625f;
            }
            final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
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
        return (!isThisConnected(world, ownPos, Direction.SOUTH) && !isThisConnected(world, ownPos, Direction.NORTH) && !isThisConnected(world, ownPos, Direction.WEST) && !isThisConnected(world, ownPos, Direction.EAST))
                || ((isThisConnected(world, ownPos, Direction.SOUTH) && isThisConnected(world, ownPos, Direction.WEST))
                || (isThisConnected(world, ownPos, Direction.SOUTH) && isThisConnected(world, ownPos, Direction.EAST))
                || (isThisConnected(world, ownPos, Direction.NORTH) && isThisConnected(world, ownPos, Direction.WEST))
                || (isThisConnected(world, ownPos, Direction.NORTH) && isThisConnected(world, ownPos, Direction.EAST)))
                || (sidesConnected(world, ownPos) == 1);
    }

    private int sidesConnected(IBlockReader world, BlockPos pos)
    {
        int sides = 0;
        for (Direction faces : Direction.Plane.HORIZONTAL)
        {
            BlockState neighbourState = world.getBlockState(pos.offset(faces));
            Block nb = neighbourState.getBlock();
            if (nb instanceof BlockWindow || neighbourState.isSolid() || nb instanceof BlockBaseWall)
            {
                sides++;
            }
        }
        return sides;
    }

    protected boolean isValidConnection(final BlockState ownState, final BlockState neighbourState, final IBlockReader world, final BlockPos ownPos, final Direction neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        if (neighbourDirection == Direction.DOWN)
        {
            return false;
        }
        if (neighbourDirection == Direction.UP)
        {
            return shouldRenderCenter(world, ownPos);
        }
        return nb instanceof BlockWindow || neighbourState.isSolid() || nb instanceof BlockBaseWall;
    }

    private boolean isThisConnected(IBlockReader world, BlockPos pos, Direction neighbourFacing)
    {
        BlockState neighbourState = world.getBlockState(pos.offset(neighbourFacing));
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockWindow || neighbourState.isSolid() || nb instanceof BlockBaseWall;
    }

    private boolean canConnectTo(final BlockState ownState, final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
        return isValidConnection(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        for (final Direction face : Direction.values())
        {
            stateIn = stateIn.with(CONNECTED_PROPERTIES.get(face.getIndex()),
                    canConnectTo(stateIn, worldIn, currentPos, face));
        }
        return stateIn;
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        return state.get(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }
}
