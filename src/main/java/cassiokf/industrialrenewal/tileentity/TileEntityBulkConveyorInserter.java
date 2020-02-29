package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityBulkConveyorBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import static cassiokf.industrialrenewal.init.TileRegistration.CONVEYORVINSERTER_TILE;

public class TileEntityBulkConveyorInserter extends TileEntityBulkConveyorBase
{
    public TileEntityBulkConveyorInserter()
    {
        super(CONVEYORVINSERTER_TILE.get());
    }

    @Override
    public void tick()
    {
        if (!world.isRemote)
        {
            insertItem();
        }
        super.tick();
    }

    private void insertItem()
    {
        if (!inventory.orElse(null).getStackInSlot(2).isEmpty())
        {
            Direction facing = getBlockFacing();
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if (te != null)
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if (itemHandler != null)
                {
                    for (int j = 0; j < itemHandler.getSlots(); j++)
                    {
                        ItemStack stack = inventory.orElse(null).extractItem(2, 64, true);
                        if (!stack.isEmpty() && itemHandler.isItemValid(j, stack))
                        {
                            ItemStack left = itemHandler.insertItem(j, stack, false);
                            if (!ItemStack.areItemStacksEqual(stack, left))
                            {
                                int toExtract = stack.getCount() - left.getCount();
                                inventory.orElse(null).extractItem(2, toExtract, false);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean dropFrontItem(Direction facing, ItemStack frontPositionItem, BlockPos frontPos)
    {
        return false;
    }
}
