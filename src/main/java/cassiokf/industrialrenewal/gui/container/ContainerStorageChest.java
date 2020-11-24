package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.TEStorageChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerStorageChest extends ContainerBase {

    public ContainerStorageChest(IInventory playerInv, TEStorageChest te) {
        IItemHandler inventory = te.inventory; //Gets the inventory from our tile entity
        int startIndex = te.currentLine * 11;
        int numRows = inventory.getSlots() / 11;
        int limit = 0;
        for (int y = 0; y < numRows; ++y) {
            for (int x = 0; x < 11; ++x) {
                limit++;
                this.addSlotToContainer(new SlotItemHandler(inventory, startIndex + x + y * 11, 8 + x * 18, 7 + y * 18));
                if (limit >= 66) break;
            }
            if (limit >= 66) break;
        }
        drawPlayerInv(playerInv, 36, 18);
    }
}
