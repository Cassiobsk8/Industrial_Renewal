package cassiokf.industrialrenewal.util.slots;

import cassiokf.industrialrenewal.Registry.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FirstAidSlot extends SlotItemHandler {

    private ItemStack allowedOnSlot = new ItemStack(ModItems.medkit);

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
