package net.cassiokf.industrialrenewal.block.abstracts;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class BlockConnectedMultiblocks<TE extends BlockEntityMultiBlocksTube> extends BlockAbstractHorizontalFacing
{

    public BlockConnectedMultiblocks(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        BlockEntityMultiBlocksTube te = (BlockEntityMultiBlocksTube) worldIn.getBlockEntity(pos);
        if (te != null) te.checkForOutPuts(pos);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack) {
        BlockEntityMultiBlocksTube te = (BlockEntityMultiBlocksTube) world.getBlockEntity(pos);
        if (te != null)
            te.requestModelRefresh();

        super.setPlacedBy(world, pos, state, player, itemStack);
    }
}
