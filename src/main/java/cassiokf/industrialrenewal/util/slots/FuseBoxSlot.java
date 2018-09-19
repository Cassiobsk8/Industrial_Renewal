package cassiokf.industrialrenewal.util.slots;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FuseBoxSlot extends SlotItemHandler {

    public FuseBoxSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        if (itemstack.isEmpty())
            return false;
        return itemstack.getItem() == Items.REDSTONE || itemstack.getItem() == Items.SNOWBALL;
    }


    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
