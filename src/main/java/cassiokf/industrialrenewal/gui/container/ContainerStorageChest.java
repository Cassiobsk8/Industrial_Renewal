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
        int xS = 0;
        int yS = 0;
        for (int y = 0; y < numRows; ++y)
        {
            for (int x = 0; x < 11; ++x)
            {
                int index = x + y * 11;
                if (index >= startIndex && limit < 66)
                {
                    limit++;
                    xS = x;
                    this.addSlotToContainer(new SlotItemHandler(inventory, index, 8 + xS * 18, 16 + yS * 18));
                }
                else
                    this.addSlotToContainer(new SlotItemHandler(inventory, index, Integer.MIN_VALUE, Integer.MIN_VALUE));
            }
            if (xS > 0) yS++;
        }
        drawPlayerInv(playerInv, 45, 18);
    }
}
