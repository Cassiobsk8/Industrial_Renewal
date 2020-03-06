package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;


public class BlockFloorLamp extends BlockIndustrialFloor
{
    public BlockFloorLamp()
    {
        super();
    }

    @Override
    public BlockState getDefaultBlockState()
    {
        return super.getDefaultBlockState().with(UP, true);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        ItemStack itemst = new ItemStack(BlocksRegistration.FLUORESCENT_ITEM.get());
        if (!worldIn.isRemote)
        {
            spawnAsEntity(worldIn, pos, itemst);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction face : Direction.values())
        {
            state = state.with(getPropertyBasedOnDirection(face), canConnectTo(worldIn, pos, face));
        }
        worldIn.setBlockState(pos, state);
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        if (neighborDirection == Direction.UP) return true;
        return super.canConnectTo(worldIn, currentPos, neighborDirection);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        return ActionResultType.PASS;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(BlocksRegistration.INDFLOOR_ITEM.get());
    }
}