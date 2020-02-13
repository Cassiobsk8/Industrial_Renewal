package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntitySignalIndicator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;

public class BlockSignalIndicator extends BlockTileEntity<TileEntitySignalIndicator>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.625D, 0.75D);
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.25F, 0.25F, 0.625F, 1F, 0.75D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.25F, 0.25F, 0.375F, 1F, 0.75D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.25F, 0.375F, 0.75D, 1F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.25F, 0.625F, 0.75D, 1F, 0);

    public BlockSignalIndicator(Block.Properties property)
    {
        super(property.lightValue(7));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing())
                .with(ONWALL, context.getFace() != Direction.UP)
                .with(ACTIVE, false);
    }

    private boolean getSignal(IBlockReader world, BlockPos pos)
    {
        TileEntitySignalIndicator te = (TileEntitySignalIndicator) world.getTileEntity(pos);
        return te.active();
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getMaterial().isReplaceable() && worldIn.getBlockState(pos.up()).getMaterial().isReplaceable();
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(ACTIVE, getSignal(worldIn, currentPos));
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            if (state.get(ONWALL)) {
                switch (state.get(FACING)) {
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

            } else {
                return AABB;
            }
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ONWALL, ACTIVE);
    }

    @Nullable
    @Override
    public TileEntitySignalIndicator createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySignalIndicator();
    }
}
