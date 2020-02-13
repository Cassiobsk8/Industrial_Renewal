package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityAlarm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAlarm extends BlockTileEntity<TileEntityAlarm>
{

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    private static final AxisAlignedBB UP_BLOCK_AABB = new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 0.4375F, 0.875F);
    private static final AxisAlignedBB DOWN_BLOCK_AABB = new AxisAlignedBB(0.125F, 1, 0.125F, 0.875F, 0.5625F, 0.875F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F);
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125F, 0.125F, 0.5625F, 0.875F, 0.875F);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.5625F, 0.875F, 0.875F, 1);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.4375F, 0.875F, 0.875F, 0);


    public BlockAlarm(Block.Properties property)
    {
        super(property);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));

    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            Direction dir = state.get(FACING);
            switch (dir) {
                case NORTH:
                    return NORTH_BLOCK_AABB;
                case SOUTH:
                    return SOUTH_BLOCK_AABB;
                case EAST:
                    return EAST_BLOCK_AABB;
                case WEST:
                    return WEST_BLOCK_AABB;
                case DOWN:
                    return DOWN_BLOCK_AABB;
                default:
                    return UP_BLOCK_AABB;
            }
        }
    */
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        //todo make it change sound on right clicked with ScrewDrive
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getFace());
    }

    @Nullable
    @Override
    public TileEntityAlarm createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityAlarm();
    }
}