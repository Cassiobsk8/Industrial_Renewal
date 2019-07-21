package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.container.ContainerCargoLoader;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnCargoLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUICargoLoader extends GuiContainer {

    private TileEntityCargoLoader te;
    private IInventory playerInv;

    public GUICargoLoader(IInventory playerInv, TileEntityCargoLoader te) {
        super(new ContainerCargoLoader(playerInv, te));

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

    private String getGUIButtonText() {
        String waitE;
        switch (this.te.getWaitEnum()) {
            case WAIT_FULL:
                waitE = I18n.format("gui.industrialrenewal.button.waitfull");
                break;
            case WAIT_EMPTY:
                waitE = I18n.format("gui.industrialrenewal.button.waitempty");
                break;
            case NO_ACTIVITY:
                waitE = I18n.format("gui.industrialrenewal.button.noactivity");
                break;
            default:
            case NEVER:
                waitE = I18n.format("gui.industrialrenewal.button.never");
                break;
        }
        return waitE;
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        if (b.id == 0) {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnCargoLoader(this.te));
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        int posX1 = ((this.width - this.xSize) / 2);
        int posY1 = ((this.height - this.ySize) / 2);
        this.buttonList.add(new GuiButton(0, posX1 + 7, posY1 + 53, 61, 18, ""));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/cargoloader.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.cargoLoader.getTranslationKey() + ".name");
        String waitE = getGUIButtonText();
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(waitE, (xSize / 2 - fontRenderer.getStringWidth(waitE) / 2) - 50, 59, 0xffffff);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);

        int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

        if (isPointInRegion(7, 53, 61, 18, mouseX, mouseY)) {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.button.cargoloaderbutton0") + " " + TextFormatting.DARK_GREEN + waitE);
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
