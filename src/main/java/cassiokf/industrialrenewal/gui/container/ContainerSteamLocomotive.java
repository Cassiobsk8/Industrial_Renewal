package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.util.slots.PlowSlot;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerSteamLocomotive extends ContainerBase {

    public ContainerSteamLocomotive(IInventory playerInv, EntitySteamLocomotive entity)
    {
        IItemHandler inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity

        this.addSlotToContainer(new PlowSlot(inventory, 0, 44, 52));

        drawPlayerInv(playerInv);
    }
}
