package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockScaffold extends BlockBase
{
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(Direction.values())
                    .map(facing -> BooleanProperty.create(facing.getName()))
                    .collect(Collectors.toList())
    );

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB CBASE_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 1.0D, 0.9D);

    public BlockScaffold(Block.Properties properties)
    {
        super(properties);
    }


    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (player.inventory.getCurrentItem().getItem() == BlockItem.getItemFromBlock(ModBlocks.scaffold))
        {
            int n = 1;
            while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockScaffold)
            {
                n++;
            }
            if (worldIn.getBlockState(pos.up(n)).getBlock().isAir(worldIn.getBlockState(pos.up(n)), worldIn, pos.up(n)))
            {
                worldIn.setBlockState(pos.up(n), ModBlocks.scaffold.getDefaultState(), 3);
                if (!player.isCreative())
                {
                    player.inventory.getCurrentItem().shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    //TODO redo drop on base break maybe extend ScaffoldingBlock

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
            addCollisionBoxToList(pos, entityBox, collidingBoxes, CBASE_AABB);
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    protected boolean isValidConnection(final BlockState ownState, final BlockState neighbourState, final IBlockReader world, final BlockPos ownPos, final Direction neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        Block nbd = world.getBlockState(ownPos.offset(neighbourDirection).down()).getBlock();
        if (neighbourDirection == Direction.DOWN)
        {
            return neighbourState.isSolid();
        }
        if (neighbourDirection != Direction.UP)
        {
            return !isConnected(ownState, Direction.UP) && !(nb instanceof BlockScaffold) && !(nbd instanceof BlockScaffold);
        }
        return neighbourState.isSolid() || nb instanceof BlockScaffold;
    }

    private boolean canConnectTo(final BlockState ownState, final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);

        final boolean neighbourIsValidForThis = isValidConnection(ownState, neighbourState, worldIn, ownPos, neighbourDirection);

        return neighbourIsValidForThis;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        for (final Direction face : Direction.values())
        {
            stateIn = stateIn.with(CONNECTED_PROPERTIES.get(face.getIndex()), canConnectTo(stateIn, worldIn, currentPos, face));
        }
        return stateIn;
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        return state.get(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
