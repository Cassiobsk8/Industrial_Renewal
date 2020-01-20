package cassiokf.industrialrenewal.util.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class LogSlot extends SlotItemHandler {

    public LogSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        if (itemstack.isEmpty())
            return false;
        return false;//Utils.isWood(itemstack);
    }


    @Override
    public int getSlotStackLimit() {
        return 64;
    }
}
