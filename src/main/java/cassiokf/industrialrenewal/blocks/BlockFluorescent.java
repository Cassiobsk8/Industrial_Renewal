package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
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

public class BlockFluorescent extends BlockBase
{

    //TODO converter para EnumFacing
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(Direction.values()).map(facing -> BooleanProperty.create(facing.getName())).collect(Collectors.toList()));
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
    protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockFluorescent(Block.Properties property)
    {
        super(property.lightValue(15));
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
        if (neighbourDirection != Direction.UP)
        {
            return nb instanceof BlockFluorescent;
        }
        return !(nb instanceof BlockRoof) || (ownPos.getZ() % 2) == 0;
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

/*
    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        BlockState actualState = getActualState(state, worldIn, pos);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
        if (isConnected(actualState, Direction.UP)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, TOP_AABB);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        return BASE_AABB;
    }
*/
}
