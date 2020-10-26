package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.entity.EntityLogCart;
import cassiokf.industrialrenewal.util.slots.LogSlot;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerLogCart extends ContainerBase {

    public ContainerLogCart(IInventory playerInv, EntityLogCart cart)
    {
        IItemHandler inventory = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity
        int numRows = inventory.getSlots() / 9;

        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new LogSlot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        drawPlayerInv(playerInv);
    }
}
