package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.gui.container.ContainerSteamLocomotive;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUISteamLocomotive extends GUIBase
{

    public GUISteamLocomotive(IInventory playerInv, EntitySteamLocomotive entity)
    {
        super(new ContainerSteamLocomotive(playerInv, entity), playerInv);
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/steam_locomotive.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModItems.steamLocomotive.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (isPointInRegion(44, 52, 16, 16, mouseX, mouseY))
        {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.entity_steamlocomotive.plow.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
