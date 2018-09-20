package cassiokf.industrialrenewal.container;

import cassiokf.industrialrenewal.tileentity.sensors.entitydetector.TileEntityEntityDetector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerEntityDetector extends Container {

    private TileEntityEntityDetector te;

    public ContainerEntityDetector(IInventory playerInv, TileEntityEntityDetector te) {
        this.te = te;

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
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isSpectator();
    }
}
