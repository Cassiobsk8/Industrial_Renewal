package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFuseBox;
import cassiokf.industrialrenewal.util.slots.FuseBoxSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerFuseBox extends ContainerBase {

    private final IItemHandler inventory;

    public ContainerFuseBox(IInventory playerInv, TileEntityFuseBox te)
    {
        this.inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity

        this.addSlotToContainer(new FuseBoxSlot(inventory, 0, 17, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 1, 35, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 2, 53, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 3, 71, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        this.addSlotToContainer(new FuseBoxSlot(inventory, 4, 89, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 5, 107, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 6, 125, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 7, 143, 26) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 8, 17, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 9, 35, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 10, 53, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 11, 71, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        this.addSlotToContainer(new FuseBoxSlot(inventory, 12, 89, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 13, 107, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 14, 125, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new FuseBoxSlot(inventory, 15, 143, 44) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        drawPlayerInv(playerInv);
    }
}
