package cassiokf.industrialrenewal.container;

import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.util.slots.FuelSlot;
import cassiokf.industrialrenewal.util.slots.PlowSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerSteamLocomotive extends Container {

    private EntitySteamLocomotive entity;
    private IItemHandler inventory;

    public ContainerSteamLocomotive(IInventory playerInv, EntitySteamLocomotive entity) {
        this.entity = entity;
        this.inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity

        this.addSlotToContainer(new PlowSlot(inventory, 6, 8, 52));

        this.addSlotToContainer(new FuelSlot(inventory, 0, 100, 21));
        this.addSlotToContainer(new FuelSlot(inventory, 1, 118, 21));
        this.addSlotToContainer(new FuelSlot(inventory, 2, 136, 21));
        this.addSlotToContainer(new FuelSlot(inventory, 3, 100, 39));
        this.addSlotToContainer(new FuelSlot(inventory, 4, 118, 39));
        this.addSlotToContainer(new FuelSlot(inventory, 5, 136, 39));

        //Player Slots
        int xPos = 8;
        int yPos = 84;

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x, xPos + x * 18, yPos + 58));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isSpectator();
    }
}
