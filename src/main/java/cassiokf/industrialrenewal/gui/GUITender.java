package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.entity.EntityTenderBase;
import cassiokf.industrialrenewal.gui.container.ContainerTender;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUITender extends GUIBase
{

    private final EntityTenderBase entity;

    public GUITender(IInventory playerInv, EntityTenderBase entity)
    {
        super(new ContainerTender(playerInv, entity), playerInv);
        this.entity = entity;
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/tender.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModItems.tender.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        drawFluidBar(entity.tank, 117, 66);

        if (isPointInRegion(117, 7, 18, 60, mouseX, mouseY))
        {
            int amount = entity.tank.getFluidAmount();
            List<String> text = new ArrayList<>();
            if (amount > 0)
            {
                text.add(TextFormatting.WHITE + "" + entity.tank.getFluid().getLocalizedName());
            }
            text.add(TextFormatting.GRAY + "" + amount + " / " + entity.tank.getCapacity() + " mB");
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
        if (isPointInRegion(62, 21, 52, 34, mouseX, mouseY))
        {
            List<String> text = new ArrayList<>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.fuel.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
