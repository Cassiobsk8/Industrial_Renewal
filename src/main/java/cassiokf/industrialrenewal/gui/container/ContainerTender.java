package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.entity.EntityTenderBase;
import cassiokf.industrialrenewal.util.slots.FuelSlot;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerTender extends ContainerBase
{

    public ContainerTender(IInventory playerInv, EntityTenderBase entity)
    {
        IItemHandler inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity

        this.addSlotToContainer(new FuelSlot(inventory, 0, 62, 21));
        this.addSlotToContainer(new FuelSlot(inventory, 1, 80, 21));
        this.addSlotToContainer(new FuelSlot(inventory, 2, 98, 21));
        this.addSlotToContainer(new FuelSlot(inventory, 3, 62, 39));
        this.addSlotToContainer(new FuelSlot(inventory, 4, 80, 39));
        this.addSlotToContainer(new FuelSlot(inventory, 5, 98, 39));

        //Player Slots
        drawPlayerInv(playerInv);
    }
}
