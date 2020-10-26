package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.gui.container.ContainerFuseBox;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnFuseBox;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFuseBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUIFuseBox extends GUIBase
{

    private final TileEntityFuseBox te;
    private final EntityPlayer player;

    public GUIFuseBox(EntityPlayer player, IInventory playerInv, TileEntityFuseBox te)
    {
        super(new ContainerFuseBox(playerInv, te), playerInv);
        this.te = te;
        this.player = player;
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        if (te.getActive() && slotIn.inventory != playerInv)
        {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnFuseBox(te, player.getEntityId()));
            return;
        }
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/fusebox.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModBlocks.fuseBox.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (isPointInRegion(16, 25, 144, 36, mouseX, mouseY))
        {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.fusebox.slots.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
        String output = I18n.format("gui.industrialrenewal.fusebox.output");
        String outputValue = String.valueOf(te.getPowerOut());
        String input = I18n.format("gui.industrialrenewal.fusebox.input");
        String inputValue = String.valueOf(te.getInPower());

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        fontRenderer.drawString(input, xSize / 2 - fontRenderer.getStringWidth(input) / 2 + 20, 140, 0x000000);
        fontRenderer.drawString(inputValue, xSize / 2 - fontRenderer.getStringWidth(inputValue) / 2 + 20, 150, 0x000000);

        fontRenderer.drawString(output, xSize / 2 - fontRenderer.getStringWidth(output) / 2 + 152, 140, 0x000000);
        fontRenderer.drawString(outputValue, xSize / 2 - fontRenderer.getStringWidth(outputValue) / 2 + 152, 150, 0x000000);
        GlStateManager.popMatrix();
    }
}
