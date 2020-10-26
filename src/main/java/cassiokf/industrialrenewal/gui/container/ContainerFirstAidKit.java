package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import cassiokf.industrialrenewal.util.slots.FirstAidSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerFirstAidKit extends ContainerBase {

    private final IItemHandler inventory;

    public ContainerFirstAidKit(IInventory playerInv, TileEntityFirstAidKit te) {
        this.inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity

        this.addSlotToContainer(new FirstAidSlot(inventory, 0, 53, 29) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FirstAidSlot(inventory, 1, 71, 29) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FirstAidSlot(inventory, 2, 89, 29) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FirstAidSlot(inventory, 3, 107, 29) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        this.addSlotToContainer(new FirstAidSlot(inventory, 4, 53, 47) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FirstAidSlot(inventory, 5, 71, 47) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FirstAidSlot(inventory, 6, 89, 47) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FirstAidSlot(inventory, 7, 107, 47) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        //Player Slots
        drawPlayerInv(playerInv);
    }
}
