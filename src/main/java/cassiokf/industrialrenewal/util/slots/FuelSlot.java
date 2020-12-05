package cassiokf.industrialrenewal.util.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FuelSlot extends SlotItemHandler {

    public FuelSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        if (itemstack.isEmpty())
            return false;
        return FurnaceTileEntity.isFuel(itemstack);
    }

    @Override
    public int getSlotStackLimit() {
        return 64;
    }
}
