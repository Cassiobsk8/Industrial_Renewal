package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractFacing;
import cassiokf.industrialrenewal.init.SoundsRegistration;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockButtonRed extends BlockAbstractFacing
{
    public static final BooleanProperty PRESS = BooleanProperty.create("press");

    private static final VoxelShape DOWN_BLOCK_AABB = Block.makeCuboidShape(2, 0, 2, 14, 7, 14);
    private static final VoxelShape UP_BLOCK_AABB = Block.makeCuboidShape(2, 16, 2, 14, 9, 14);
    private static final VoxelShape WEST_BLOCK_AABB = Block.makeCuboidShape(0, 2, 2, 7, 14, 14);
    private static final VoxelShape EAST_BLOCK_AABB = Block.makeCuboidShape(16, 2, 2, 9, 14, 14);
    private static final VoxelShape SOUTH_BLOCK_AABB = Block.makeCuboidShape(2, 2, 9, 14, 14, 16);
    private static final VoxelShape NORTH_BLOCK_AABB = Block.makeCuboidShape(2, 2, 7, 14, 14, 0);

    public BlockButtonRed()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isRemote) return ActionResultType.SUCCESS;
        worldIn.setBlockState(pos, state.with(PRESS, !state.get(PRESS)));
        worldIn.playSound(null, pos, SoundsRegistration.TILEENTITY_VALVE_CHANGE.get(), SoundCategory.BLOCKS, 1f, 1f);
        worldIn.notifyNeighborsOfStateChange(pos.offset(state.get(FACING)), this);
        return ActionResultType.SUCCESS;
    }

    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        boolean active = !state.get(PRESS);
        return active ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
    {
        return true;
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos, boolean collision)
    {
        Direction dir = state.get(FACING);
        switch (dir)
        {
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

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, PRESS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getFace().getOpposite()).with(PRESS, true);
    }
}
