package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockBase;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockButtonRed extends BlockBase
{

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final BooleanProperty PRESS = BooleanProperty.create("press");
    private static final AxisAlignedBB DOWN_BLOCK_AABB = new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 0.4375F, 0.875F);
    private static final AxisAlignedBB UP_BLOCK_AABB = new AxisAlignedBB(0.125F, 1, 0.125F, 0.875F, 0.5625F, 0.875F);
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125F, 0.125F, 0.5625F, 0.875F, 0.875F);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.5625F, 0.875F, 0.875F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.4375F, 0.875F, 0.875F, 0);

    public BlockButtonRed(Block.Properties property)
    {
        super(property);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isRemote) return ActionResultType.SUCCESS;
        worldIn.setBlockState(pos, state.with(PRESS, !state.get(PRESS)));
        worldIn.playSound(null, pos, IRSoundRegister.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1f, 1f);
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

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
