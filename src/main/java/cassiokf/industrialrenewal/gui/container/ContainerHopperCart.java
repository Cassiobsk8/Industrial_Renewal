package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.entity.EntityHopperCart;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerHopperCart extends ContainerBase
{

    public ContainerHopperCart(IInventory playerInv, EntityHopperCart cart)
    {
        IItemHandler inventory = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity
        int numRows = inventory.getSlots() / 9;

        for (int i = 0; i < numRows; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new SlotItemHandler(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }
        drawPlayerInv(playerInv, 1, 0);
    }
}
