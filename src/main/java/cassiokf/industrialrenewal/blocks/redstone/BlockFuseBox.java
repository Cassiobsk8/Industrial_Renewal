package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFuseBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFuseBox extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty DOWNCONDUIT = BooleanProperty.create("down");
    public static final BooleanProperty UPCONDUIT = BooleanProperty.create("up");


    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 2, 4, 5, 14, 12);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(16, 2, 4, 11, 14, 12);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(4, 2, 11, 12, 14, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(4, 2, 5, 12, 14, 0);

    public BlockFuseBox()
    {
        super(Block.Properties.create(Material.IRON));
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
        return stateIn
                .with(DOWNCONDUIT, canConnectConduit(1, worldIn, currentPos))
                .with(UPCONDUIT, canConnectConduit(0, worldIn, currentPos));
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing())
                .with(ACTIVE, false).with(DOWNCONDUIT, false).with(UPCONDUIT, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, DOWNCONDUIT, UPCONDUIT);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
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
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityFuseBox createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFuseBox();
    }
}
