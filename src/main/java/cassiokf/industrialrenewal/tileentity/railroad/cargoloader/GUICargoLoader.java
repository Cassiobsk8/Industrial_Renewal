package cassiokf.industrialrenewal.tileentity.railroad.cargoloader;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.ModBlocks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/cargoloader.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.cargoLoader.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }
}
