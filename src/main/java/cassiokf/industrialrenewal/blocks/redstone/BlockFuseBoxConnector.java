package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityBoxConnector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class BlockFuseBoxConnector extends BlockHorizontalFacing
{
    public static final IntegerProperty UPCONDUIT = IntegerProperty.create("up", 0, 2);


    private static final VoxelShape WEST_EAST_BLOCK_AABB = Block.makeCuboidShape(6, 0, 0, 10, 2, 16);
    private static final VoxelShape NORTH_SOUTH_BLOCK_AABB = Block.makeCuboidShape(0, 0, 6, 16, 2, 10);

    public BlockFuseBoxConnector()
    {
        super(Block.Properties.create(Material.IRON));
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

/*
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, neighborPos);
        TileEntityBoxConnector te = (TileEntityBoxConnector) worldIn.getTileEntity(pos);
        if (te != null) {
            te.passRedstone();
        }
    }*/

    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
        Direction newFace = state.get(FACING).getOpposite();
        return state.with(FACING, newFace);
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

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        switch (state.get(FACING))
        {
            default:
            case NORTH:
            case SOUTH:
                return NORTH_SOUTH_BLOCK_AABB;
            case EAST:
            case WEST:
                return WEST_EAST_BLOCK_AABB;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityBoxConnector createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBoxConnector();
    }
}
