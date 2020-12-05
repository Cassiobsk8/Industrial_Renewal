package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityBulkConveyorInserter extends TileEntityBulkConveyor
{
    public TileEntityBulkConveyorInserter(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
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
        if (!inventory.getStackInSlot(frontNumber).isEmpty())
        {
            Direction facing = getBlockFacing();
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if (te != null)
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if (itemHandler != null)
                {
                    Utils.moveItemToInventory(inventory, frontNumber, itemHandler);
                }
            }
        }
    }

    @Override
    public void dropFrontItem(Direction facing, ItemStack frontPositionItem, BlockPos frontPos)
    {
    }
}
