package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import net.minecraft.inventory.IInventory;

public class ContainerFluidLoader extends ContainerBase
{

    public ContainerFluidLoader(IInventory playerInv, TileEntityFluidLoader entity)
    {
        drawPlayerInv(playerInv);
    }
}
