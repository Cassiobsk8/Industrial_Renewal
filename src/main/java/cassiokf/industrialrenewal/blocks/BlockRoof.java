package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockRoof extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    //public static final IUnlistedProperty<Boolean> SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("south"));
    //public static final IUnlistedProperty<Boolean> NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("north"));
    //public static final IUnlistedProperty<Boolean> EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("east"));
    //public static final IUnlistedProperty<Boolean> WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("west"));
    //public static final IUnlistedProperty<Boolean> DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("down"));

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockRoof()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return 255;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{SOUTH, NORTH, EAST, WEST, DOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
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

    private Boolean isEven(BlockPos pos, boolean baseOnX)
    {
        int number = pos.getZ();
        if (baseOnX) number = pos.getX();
        return (number % 2) == 0;
    }

    private boolean canConnectTo(final IBlockReader world, final BlockPos ownPos, final Direction ownFacing, final Direction neighborDirection)
    {
        final BlockPos neighborPos = ownPos.offset(neighborDirection);
        final BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborDirection == Direction.DOWN) return neighborState.isSolid();

        if (((neighborDirection == Direction.EAST || neighborDirection == Direction.WEST) && (ownFacing == Direction.SOUTH || ownFacing == Direction.NORTH))
                || ((neighborDirection == Direction.SOUTH || neighborDirection == Direction.NORTH) && (ownFacing == Direction.WEST || ownFacing == Direction.EAST)))
        {
            BlockState sState = world.getBlockState(ownPos.offset(Direction.SOUTH));
            BlockState nState = world.getBlockState(ownPos.offset(Direction.NORTH));
            Block sBlock = sState.getBlock();
            Block nBlock = nState.getBlock();
            if ((sBlock instanceof BlockRoof || sBlock instanceof BlockCatwalkLadder || sState.isSolid()) && (nBlock instanceof BlockRoof || nBlock instanceof BlockCatwalkLadder || nState.isSolid()))
            {
                // (block pos is Even) && (neighbor SW)
                boolean invert = ownFacing == Direction.WEST || ownFacing == Direction.EAST;
                Block nb = neighborState.getBlock();
                return isEven(ownPos, invert)
                        && (nb instanceof BlockRoof || neighborState.isSolid() || nb instanceof BlockPillar || nb instanceof BlockColumn);
            }
        }
        return false;
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    if (canConnectTo(world, pos, Direction.NORTH, Direction.DOWN)) return eState.with(SOUTH, false)
        //            .with(NORTH, false)
        //            .with(EAST, false)
        //            .with(WEST, false)
        //            .with(DOWN, true);

        //    Direction facing = state.get(FACING);
        //    return eState.with(SOUTH, canConnectTo(world, pos, facing, facing.getOpposite()))
        //            .with(NORTH, canConnectTo(world, pos, facing, facing))
        //            .with(EAST, canConnectTo(world, pos, facing, facing.rotateY()))
        //            .with(WEST, canConnectTo(world, pos, facing, facing.rotateYCCW()))
        //            .with(DOWN, false);
        //}
        return state;
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    /*
        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
        }

        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            return BASE_AABB;
        }
    */
    @Deprecated
    public boolean isTopSolid(BlockState state)
    {
        return true;
    }
}
