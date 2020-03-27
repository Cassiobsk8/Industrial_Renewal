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

public class ContainerFuseBox extends Container {

    private TileEntityFuseBox te;
    private IItemHandler inventory;

    public ContainerFuseBox(IInventory playerInv, TileEntityFuseBox te) {
        this.te = te;
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
