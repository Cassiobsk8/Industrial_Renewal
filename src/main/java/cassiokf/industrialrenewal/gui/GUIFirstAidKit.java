package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.gui.container.ContainerFirstAidKit;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUIFirstAidKit extends GUIBase
{

    public GUIFirstAidKit(IInventory playerInv, TileEntityFirstAidKit te)
    {
        super(new ContainerFirstAidKit(playerInv, te), playerInv);
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/firstaidkit.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModBlocks.firstAidKit.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (isPointInRegion(53, 29, 70, 34, mouseX, mouseY))
        {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.blockfirstaidkit.bandages.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
