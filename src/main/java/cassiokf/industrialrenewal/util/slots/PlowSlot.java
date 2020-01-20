package cassiokf.industrialrenewal.util.slots;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PlowSlot extends SlotItemHandler {

    public PlowSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        if (itemstack.isEmpty())
            return false;
        return itemstack.getItem() instanceof Item;
    }


    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
