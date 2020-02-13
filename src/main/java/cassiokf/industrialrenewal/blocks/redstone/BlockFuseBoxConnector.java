package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityBoxConnector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
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

public class BlockFuseBoxConnector extends BlockTileEntity<TileEntityBoxConnector>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty UPCONDUIT = IntegerProperty.create("up", 0, 2);


    private static final AxisAlignedBB WEST_EAST_BLOCK_AABB = new AxisAlignedBB(0.375D, 0D, 0D, 0.625D, 0.125D, 1D);
    private static final AxisAlignedBB NORTH_SOUTH_BLOCK_AABB = new AxisAlignedBB(0D, 0D, 0.375D, 1D, 0.125D, 0.625D);

    public BlockFuseBoxConnector(Block.Properties property)
    {
        super(property);
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
    {
        return side == state.get(FACING).rotateY() || side == state.get(FACING).rotateYCCW();
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        TileEntityBoxConnector te = (TileEntityBoxConnector) world.getTileEntity(pos);
        int value = te.passRedstone();
        return state.get(FACING).rotateYCCW() == side ? value : 0;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (player.inventory.getCurrentItem().getItem() == ModItems.screwDrive)
        {
            rotateBlock(worldIn, pos, state);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
/*
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, neighborPos);
        TileEntityBoxConnector te = (TileEntityBoxConnector) worldIn.getTileEntity(pos);
        if (te != null) {
            te.passRedstone();
        }
    }*/

    private void rotateBlock(World world, BlockPos pos, BlockState state)
    {
        Direction newFace = state.get(FACING).getOpposite();
        world.setBlockState(pos, state.with(FACING, newFace));
    }

    private int canConnectConduit(BlockState state, IBlockReader world, BlockPos pos)
    {
        BlockState upState = world.getBlockState(pos.up());
        if (upState.getBlock() instanceof BlockFuseBox || upState.getBlock() instanceof BlockFuseBoxConduitExtension)
        {
            if (state.get(FACING) == upState.get(FACING))
            {
                return 1;
            }
            if (state.get(FACING) == upState.get(FACING).getOpposite())
            {
                return 2;
            }
        }
        return 0;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        stateIn = stateIn.with(UPCONDUIT, canConnectConduit(stateIn, worldIn, currentPos));
        return stateIn;
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(UPCONDUIT, 0);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, UPCONDUIT);
    }
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        switch (state.getActualState(source, pos).get(FACING)) {
            default:
            case NORTH:
            case SOUTH:
                return NORTH_SOUTH_BLOCK_AABB;
            case EAST:
            case WEST:
                return WEST_EAST_BLOCK_AABB;
        }
    }
*/

    @Nullable
    @Override
    public TileEntityBoxConnector createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBoxConnector();
    }
}
