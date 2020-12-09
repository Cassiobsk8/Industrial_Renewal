package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.redstone.TileEntityEntityDetector;
import net.minecraft.inventory.IInventory;

public class ContainerEntityDetector extends ContainerBase
{

    public ContainerEntityDetector(IInventory playerInv, TileEntityEntityDetector te)
    {
        drawPlayerInv(playerInv);
    }
}
