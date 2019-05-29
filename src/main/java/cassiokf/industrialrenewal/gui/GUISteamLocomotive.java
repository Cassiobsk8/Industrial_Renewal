package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.container.ContainerSteamLocomotive;
import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUISteamLocomotive extends GuiContainer {

    private EntitySteamLocomotive entity;
    private IInventory playerInv;

    public GUISteamLocomotive(IInventory playerInv, EntitySteamLocomotive entity) {
        super(new ContainerSteamLocomotive(playerInv, entity));

        this.xSize = 176;
        this.ySize = 166;

        this.entity = entity;
        this.playerInv = playerInv;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/steam_locomotive.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModItems.steamLocomotive.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);

        int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

        if (isPointInRegion(8, 52, 16, 16, mouseX, mouseY)) {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.entity_steamlocomotive.plow.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
        if (isPointInRegion(100, 21, 52, 34, mouseX, mouseY)) {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.fuel.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
