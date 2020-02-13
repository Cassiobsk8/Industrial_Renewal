package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFuseBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFuseBox extends BlockTileEntity<TileEntityFuseBox>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty DOWNCONDUIT = BooleanProperty.create("down");
    public static final BooleanProperty UPCONDUIT = BooleanProperty.create("up");


    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.25F, 0.3125F, 0.875F, 0.75D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125, 0.25F, 0.6875F, 0.875F, 0.75D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125, 0.6875F, 0.75D, 0.875F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125, 0.3125F, 0.75D, 0.875F, 0);

    public BlockFuseBox(Block.Properties property)
    {
        super(property);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        }
        TileEntityFuseBox te = (TileEntityFuseBox) worldIn.getTileEntity(pos);
        if (player.isCrouching() && te != null)
        {
            te.changeActivate();
            return ActionResultType.SUCCESS;
        }
        OpenGUI(worldIn, pos, player);
        return ActionResultType.SUCCESS;
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        //player.openGui(IndustrialRenewal.instance, GUIHandler.FUSEBOX, world, pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean canConnectConduit(int side, IBlockReader world, BlockPos pos)
    {
        BlockPos posoff;
        if (side == 0)
        { //up
            posoff = pos.offset(Direction.UP);
        } else
        {
            posoff = pos.offset(Direction.DOWN);
        }
        return world.getBlockState(posoff).getBlock() instanceof BlockFuseBoxConduitExtension || world.getBlockState(posoff).getBlock() instanceof BlockFuseBoxConnector;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        stateIn = stateIn.with(DOWNCONDUIT, canConnectConduit(1, worldIn, currentPos)).with(UPCONDUIT, canConnectConduit(0, worldIn, currentPos));
        return stateIn;
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing())
                .with(ACTIVE, false).with(DOWNCONDUIT, false).with(UPCONDUIT, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, DOWNCONDUIT, UPCONDUIT);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
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
        }
    */
    @Nullable
    @Override
    public TileEntityFuseBox createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFuseBox();
    }
}
