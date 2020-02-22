package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockFloorLamp extends BlockIndustrialFloor
{
    public BlockFloorLamp()
    {
        super();
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
}