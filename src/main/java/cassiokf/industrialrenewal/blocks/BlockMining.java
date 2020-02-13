package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.item.ItemDrill;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityMining;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockMining extends Block3x3x3Base<TileEntityMining>
{
    public BlockMining(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntityMining te = (TileEntityMining) worldIn.getTileEntity(pos);
        if (te != null) te.dropAllItems();
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        TileEntityMining tile = getTileEntity(worldIn, pos);
        IItemHandler itemHandler = tile.getDrillHandler();
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemDrill || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemDrill && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isRemote)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty() && !tile.isRunning())
            {
                if (!worldIn.isRemote)
                {
                    player.addItemStackToInventory(itemHandler.extractItem(0, 64, false));
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER);
    }

    @Nullable
    @Override
    public TileEntityMining createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityMining();
    }
}
