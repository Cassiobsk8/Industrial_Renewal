package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.redstone.TileEntityEntityDetector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerEntityDetector extends ContainerBase {

    public ContainerEntityDetector(IInventory playerInv, TileEntityEntityDetector te)
    {
        drawPlayerInv(playerInv);
    }
}
