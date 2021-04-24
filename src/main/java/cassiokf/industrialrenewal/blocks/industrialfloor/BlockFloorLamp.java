package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;


public class BlockFloorLamp extends BlockIndustrialFloor
{
    public BlockFloorLamp()
    {
        super(Block.Properties.create(Material.IRON).lightValue(1));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        return ActionResultType.PASS;
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
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(BlocksRegistration.INDFLOOR_ITEM.get());
    }
}