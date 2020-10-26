package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.TileEntityRecordPlayer;
import cassiokf.industrialrenewal.util.slots.RecordSlot;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerRecordPlayer extends ContainerBase {

    private final IItemHandler inventory;

    public ContainerRecordPlayer(IInventory playerInv, TileEntityRecordPlayer te)
    {
        this.inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); //Gets the inventory from our tile entity

        this.addSlotToContainer(new RecordSlot(inventory, 0, 80, 9) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new RecordSlot(inventory, 1, 80, 27) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new RecordSlot(inventory, 2, 80, 45) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new RecordSlot(inventory, 3, 80, 63) {
            @Override
            public void onSlotChanged() {
                te.markDirty();
            }
        });

        drawPlayerInv(playerInv);
    }
}
