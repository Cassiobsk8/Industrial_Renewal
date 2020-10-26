package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.machines.TELathe;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLatheMachine extends ContainerBase
{
    public ContainerLatheMachine(IInventory playerInv, TELathe te)
    {
        this.addSlotToContainer(new SlotItemHandler(te.getInputInv(), 0, 44, 30)
        {
            @Override
            public void onSlotChanged()
            {
                te.markDirty();
            }
        });
        this.addSlotToContainer(new SlotItemHandler(te.getOutputInv(), 0, 134, 30)
        {
            @Override
            public void onSlotChanged()
            {
                te.markDirty();
            }

        });

        drawPlayerInv(playerInv);
    }
}
