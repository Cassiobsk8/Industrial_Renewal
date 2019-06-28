package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.container.ContainerRecordPlayer;
import cassiokf.industrialrenewal.network.PacketReturnRecordPlayer;
import cassiokf.industrialrenewal.tileentity.TileEntityRecordPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUIRecordPlayer extends GuiContainer {

    private TileEntityRecordPlayer te;
    private IInventory playerInv;

    public GUIRecordPlayer(IInventory playerInv, TileEntityRecordPlayer te) {
        super(new ContainerRecordPlayer(playerInv, te));

        this.xSize = 176;
        this.ySize = 166;

        this.te = te;
        this.playerInv = playerInv;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        NetworkHandler.INSTANCE.sendToServer(new PacketReturnRecordPlayer(this.te, b.id));
    }

    @Override
    public void initGui() {
        super.initGui();
        int posX1 = ((this.width - this.xSize) / 2);
        int posY1 = ((this.height - this.ySize) / 2);
        String play = I18n.format("gui." + References.MODID + ".button.play");
        String stop = I18n.format("gui." + References.MODID + ".button.stop");
        this.buttonList.add(new GuiButton(0, posX1 + 101, posY1 + 8, 32, 18, play));
        this.buttonList.add(new GuiButton(1, posX1 + 101, posY1 + 26, 32, 18, play));
        this.buttonList.add(new GuiButton(2, posX1 + 101, posY1 + 44, 32, 18, play));
        this.buttonList.add(new GuiButton(3, posX1 + 101, posY1 + 62, 32, 18, play));

        //this.buttonList.add(new GuiButton( 4, posX1 + 7, posY1 + 8, 68, 18, "Play All")); //TODO fix this
        this.buttonList.add(new GuiButton(5, posX1 + 7, posY1 + 26, 68, 18, stop));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/recordplayer.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //String name = I18n.format(ModBlocks.recordPlayer.getUnlocalizedName() + ".name");
        //fontRenderer.drawString(name, 4, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);

        int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

        if (isPointInRegion(80, 9, 16, 70, mouseX, mouseY)) {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.blockrecordplayer.discs.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
