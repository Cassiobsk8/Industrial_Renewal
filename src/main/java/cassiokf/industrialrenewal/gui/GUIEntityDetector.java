package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.Registry.ModBlocks;
import cassiokf.industrialrenewal.Registry.NetworkHandler;
import cassiokf.industrialrenewal.container.ContainerEntityDetector;
import cassiokf.industrialrenewal.network.PacketReturnEntityDetector;
import cassiokf.industrialrenewal.tileentity.sensors.entitydetector.TileEntityEntityDetector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GUIEntityDetector extends GuiContainer {

    private TileEntityEntityDetector te;
    private IInventory playerInv;

    public GUIEntityDetector(IInventory playerInv, TileEntityEntityDetector te) {
        super(new ContainerEntityDetector(playerInv, te));

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
        NetworkHandler.INSTANCE.sendToServer(new PacketReturnEntityDetector(this.te, b.id));
    }

    private String getGUIButtonText() {
        switch (this.te.getEntityEnum()) {
            default:
            case ALL:
                return I18n.format("gui.industrialrenewal.button.all");
            case PLAYERS:
                return I18n.format("gui.industrialrenewal.button.players");
            case MOBHOSTIL:
                return I18n.format("gui.industrialrenewal.button.mobhostil");
            case MOBPASSIVE:
                return I18n.format("gui.industrialrenewal.button.mobpassive");
            case ITEMS:
                return I18n.format("gui.industrialrenewal.button.items");
            case CARTS:
                return I18n.format("gui.industrialrenewal.button.carts");
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        int posX1 = ((this.width - this.xSize) / 2);
        int posY1 = ((this.height - this.ySize) / 2);
        this.buttonList.add(new GuiButton(1, posX1 + 41, posY1 + 22, 72, 18, " "));
        this.buttonList.add(new GuiButton(2, posX1 + 61, posY1 + 64, 108, 18, " "));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/entitydetector.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.entityDetector.getUnlocalizedName() + ".name");

        String distance = I18n.format("gui." + References.MODID + ".button.distance") + " " + te.getDistance();
        String entity = I18n.format("gui." + References.MODID + ".button.entity") + " " + getGUIButtonText();

        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);

        fontRenderer.drawString(distance, xSize / 2 - fontRenderer.getStringWidth(distance) / 2 - 10, 28, 0xffffff);
        fontRenderer.drawString(entity, xSize / 2 - fontRenderer.getStringWidth(entity) / 2 + 27, 70, 0xffffff);

        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }
}
