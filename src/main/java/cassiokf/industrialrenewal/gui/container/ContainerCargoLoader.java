package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCargoLoader extends ContainerBase {

    private final IItemHandler inventory;

    public ContainerCargoLoader(IInventory playerInv, TileEntityCargoLoader te) {
        this.inventory = te.getInternalCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity

        this.addSlotToContainer(new SlotItemHandler(inventory, 0, 62, 20) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new SlotItemHandler(inventory, 1, 80, 20) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new SlotItemHandler(inventory, 2, 98, 20) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new SlotItemHandler(inventory, 3, 71, 38) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        this.addSlotToContainer(new SlotItemHandler(inventory, 4, 89, 38) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        //Player Slots
        drawPlayerInv(playerInv);
    }
}
