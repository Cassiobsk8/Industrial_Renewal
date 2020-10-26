package cassiokf.industrialrenewal.gui.container;

import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ContainerFluidLoader extends ContainerBase
{

    public ContainerFluidLoader(IInventory playerInv, TileEntityFluidLoader entity)
    {
        drawPlayerInv(playerInv);
    }
}
