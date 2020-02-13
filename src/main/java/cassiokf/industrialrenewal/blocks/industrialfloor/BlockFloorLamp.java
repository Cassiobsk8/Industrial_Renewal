package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockFloorLamp extends BlockIndustrialFloor
{
    public BlockFloorLamp(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        ItemStack itemst = new ItemStack(Item.getItemFromBlock(ModBlocks.fluorescent));
        if (!worldIn.isRemote)
        {
            spawnAsEntity(worldIn, pos, itemst);
        }
    }
}