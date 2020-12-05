package cassiokf.industrialrenewal.util.slots;

import cassiokf.industrialrenewal.init.ItemsRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FirstAidSlot extends SlotItemHandler {

    private ItemStack allowedOnSlot = new ItemStack(ItemsRegistration.MEDKIT.get());

    public FirstAidSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        if (itemstack.isEmpty())
            return false;
        return itemstack.isItemEqual(allowedOnSlot);
    }


    @Override
    public int getSlotStackLimit() {
        return 16;
    }
}
