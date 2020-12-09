package cassiokf.industrialrenewal.util.slots;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FuelSlot extends SlotItemHandler
{

    public FuelSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public static boolean isBucket(ItemStack stack)
    {
        return stack.getItem() == Items.BUCKET;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        if (itemstack.isEmpty())
            return false;
        return TileEntityFurnace.isItemFuel(itemstack) || isBucket(itemstack);
    }

    @Override
    public int getSlotStackLimit()
    {
        return 64;
    }
}
