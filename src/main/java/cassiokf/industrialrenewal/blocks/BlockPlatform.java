package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockPlatform extends BlockBase
{
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(Direction.values()).map(facing -> BooleanProperty.create(facing.getName())).collect(Collectors.toList()));
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 2.0D, 0.03125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 1.0D, 0.96875D, 1.0D, 2.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 0.03125D, 2.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.96875D, 1.0D, 0.0D, 1.0D, 2.0D, 1.0D);

    public BlockPlatform(Block.Properties property)
    {
        super(property);
    }

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

    protected boolean isValidConnection(final BlockState ownState, final BlockState neighbourState, final IBlockReader world, final BlockPos ownPos, final Direction neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        Block ub = world.getBlockState(ownPos.up()).getBlock();
        Block nub = world.getBlockState(ownPos.offset(neighbourDirection).up()).getBlock();
        if (neighbourDirection != Direction.UP && neighbourDirection != Direction.DOWN)
        {
            return nb instanceof BlockPlatform || neighbourState.isSolid()
                    || nb instanceof RailBlock
                    || (nb instanceof BlockCatwalkStair && neighbourState.get(BlockCatwalkStair.FACING) == neighbourDirection.getOpposite())
                    || (ub instanceof BlockCatwalkGate && neighbourDirection == world.getBlockState(ownPos.up()).get(BlockCatwalkGate.FACING))
                    || (nub instanceof BlockCatwalkStair && world.getBlockState(ownPos.offset(neighbourDirection).up()).get(BlockCatwalkStair.FACING) == neighbourDirection);
        }
        if (neighbourDirection == Direction.DOWN)
        {
            return neighbourState.isSolid()
                    || nb instanceof BlockBrace
                    || nb instanceof BlockPlatform
                    || nb instanceof BlockPillar
                    || nb instanceof BlockColumn;
        }
        return neighbourState.isSolid() || nb instanceof BlockPlatform || nb instanceof BlockPillar;
    }


    private boolean canConnectTo(final BlockState ownState, final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
        final Block neighbourBlock = neighbourState.getBlock();

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
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        return BASE_AABB;
    }


    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);

        if (!isConnected(state, Direction.UP)) {
            if (!isConnected(state, Direction.NORTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            }
            if (!isConnected(state, Direction.SOUTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            }
            if (!isConnected(state, Direction.WEST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
            if (!isConnected(state, Direction.EAST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }
        }
    }
*/
}
