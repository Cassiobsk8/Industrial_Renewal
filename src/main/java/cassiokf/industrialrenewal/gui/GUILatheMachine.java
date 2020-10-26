package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.gui.container.ContainerLatheMachine;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.machines.TELathe;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class GUILatheMachine extends GUIBase
{

    private final TELathe te;

    public GUILatheMachine(IInventory playerInv, TELathe te)
    {
        super(new ContainerLatheMachine(playerInv, te), playerInv);
        this.te = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        IEnergyStorage energyStorage = te.getEnergyStorage();
        int energyFill = (int) (Utils.normalize(energyStorage.getEnergyStored(), 0, energyStorage.getMaxEnergyStored()) * 69);
        this.drawTexturedModalRect(this.guiLeft + 8, (this.guiTop + 78) - energyFill, 176, 0, 16, energyFill);

        int progress = (int) (te.getNormalizedProcess() * 43);
        this.drawTexturedModalRect((this.guiLeft + 81) + progress, this.guiTop + 50, 176, 70, 7, 13);

        ItemStack stack = te.getProcessingItem();
        if (stack != null && !stack.isEmpty())
        {
            itemRender.renderItemIntoGUI(stack, this.guiLeft + 96, this.guiTop + 33);
        }
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/lathe.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModBlocks.latheMachine.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) //Draw Dynamic things
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY, 20);

        if (isPointInRegion(7, 9, 17, 69, mouseX, mouseY))
        {
            List<String> text = new ArrayList<String>();
            IEnergyStorage energyStorage = te.getEnergyStorage();
            text.add(TextFormatting.GRAY + "" + energyStorage.getEnergyStored() + " / " + energyStorage.getMaxEnergyStored());
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }

}
