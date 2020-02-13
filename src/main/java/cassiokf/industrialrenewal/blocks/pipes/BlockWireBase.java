package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.TileEntityWireIsolator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWireBase extends BlockTileEntity<TileEntityWireIsolator>
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.5D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.5D, 0.6875D, 0.6875D, 1.0);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.5D, 0.3125D, 0.3125D, 1.0, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.5D, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.3125D, 0.5D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);

    public BlockWireBase(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntityWireIsolator connector = (TileEntityWireIsolator) worldIn.getTileEntity(pos);
        if (connector != null) connector.onBlockBreak();
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    /*
        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
        {
            BlockState actualState = getActualState(state, worldIn, pos);
            Direction dir = actualState.get(FACING);
            if (dir == Direction.NORTH)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            }
            if (dir == Direction.SOUTH)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            }
            if (dir == Direction.EAST)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }
            if (dir == Direction.WEST)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
            if (dir == Direction.DOWN)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
            }
            if (dir == Direction.UP)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, UP_AABB);
            }
        }

        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
        {
            BlockState actualState = getActualState(state, source, pos);
            Direction dir = actualState.get(FACING);
            switch (dir)
            {
                case NORTH:
                    return NORTH_AABB;
                case SOUTH:
                    return SOUTH_AABB;
                case EAST:
                    return EAST_AABB;
                case WEST:
                    return WEST_AABB;
                case DOWN:
                    return DOWN_AABB;
                default:
                    return UP_AABB;
            }
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getFace().getOpposite());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntityWireIsolator createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityWireIsolator();
    }
}
