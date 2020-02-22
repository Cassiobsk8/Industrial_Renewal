package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockAbstractHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSignalIndicator extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected static final VoxelShape AABB = Block.makeCuboidShape(4, 0, 4, 12, 26, 12);
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 4, 4, 10, 16, 12);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(6, 4, 4, 16, 16, 12);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(4, 4, 6, 12, 16, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(4, 4, 0, 12, 16, 10);

    public BlockSignalIndicator()
    {
        super(Block.Properties.create(Material.IRON).lightValue(7));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction facing = context.getPlacementHorizontalFacing();
        if (Direction.Plane.HORIZONTAL.test(context.getFace()))
            facing = context.getFace().getOpposite();
        BlockState state = getDefaultState()
                .with(FACING, facing)
                .with(ONWALL, context.getFace() != Direction.UP);
        return state.with(ACTIVE, getSignal(context.getWorld(), context.getPos(), state));
    }

    private boolean getSignal(IWorld world, BlockPos pos, BlockState state)
    {
        World worldIn = world.getWorld();
        BlockPos offsetPos = pos.offset(state.get(FACING));
        boolean onWall = state.get(ONWALL);
        if (!onWall)
        {
            return worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.down());
        } else
        {
            return worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(offsetPos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        worldIn.setBlockState(pos, state.with(ACTIVE, getSignal(worldIn, pos, state)));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(ACTIVE, getSignal(worldIn, currentPos, stateIn));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (state.get(ONWALL))
        {
            switch (state.get(FACING))
            {
                default:
                case NORTH:
                    return NORTH_BLOCK_AABB;
                case SOUTH:
                    return SOUTH_BLOCK_AABB;
                case EAST:
                    return EAST_BLOCK_AABB;
                case WEST:
                    return WEST_BLOCK_AABB;
            }
        } else
        {
            return AABB;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ONWALL, ACTIVE);
    }
}
