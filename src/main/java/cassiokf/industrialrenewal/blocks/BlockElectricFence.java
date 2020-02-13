package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
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

public class BlockElectricFence extends BlockBasicElectricFence
{
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(Direction.values()).map(facing -> BooleanProperty.create(facing.getName())).collect(Collectors.toList()));

    private static float NORTHZ1 = 0.4375f;
    private static float SOUTHZ2 = 0.5625f;
    private static float WESTX1 = 0.4375f;
    private static float EASTX2 = 0.5625f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.5f;
    private static float RUPY2 = 1.0f;

    public BlockElectricFence(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @Override
    @Deprecated
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        if (isConnected(state, Direction.UP))
        {
            return 7;
        } else
        {
            return 0;
        }
    }

    protected boolean isValidConnection(final IBlockReader world, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = world.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        if (neighbourDirection == Direction.DOWN)
        {
            return false;
        }
        if (neighbourDirection == Direction.UP)
        {
            int z = Math.abs(ownPos.getZ());
            int x = Math.abs(ownPos.getX());
            int w = x - z;
            return nb.isAir(neighbourState, world, neighbourPos) && (Math.abs(w) % 3 == 0);
        }
        return nb instanceof BlockElectricGate || neighbourState.isSolid() || nb instanceof BlockBasicElectricFence;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        for (final Direction face : Direction.values())
        {
            stateIn = stateIn.with(CONNECTED_PROPERTIES.get(face.getIndex()),
                    isValidConnection(worldIn, currentPos, face));
        }
        return stateIn;
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        return state.get(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

/*
    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        if (isConnected(state, Direction.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(state, Direction.NORTH)) {
            NORTHZ1 = 0.4375f;
        }
        if (isConnected(state, Direction.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(state, Direction.SOUTH)) {
            SOUTHZ2 = 0.5625f;
        }
        if (isConnected(state, Direction.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(state, Direction.WEST)) {
            WESTX1 = 0.4375f;
        }
        if (isConnected(state, Direction.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(state, Direction.EAST)) {
            EASTX2 = 0.5625f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        BlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, Direction.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(actualState, Direction.NORTH)) {
            NORTHZ1 = 0.4375f;
        }
        if (isConnected(actualState, Direction.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(actualState, Direction.SOUTH)) {
            SOUTHZ2 = 0.5625f;
        }
        if (isConnected(actualState, Direction.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(actualState, Direction.WEST)) {
            WESTX1 = 0.4375f;
        }
        if (isConnected(actualState, Direction.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(actualState, Direction.EAST)) {
            EASTX2 = 0.5625f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, RUPY2, SOUTHZ2);
    }*/
}
