package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.entity.EntityHopperCart;
import cassiokf.industrialrenewal.gui.container.ContainerHopperCart;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GUIHopperCart extends GUIBase
{
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    private final int inventoryRows;

    public GUIHopperCart(IInventory playerInv, EntityHopperCart entity)
    {
        super(new ContainerHopperCart(playerInv, entity), playerInv);

        this.inventoryRows = entity.inventory.getSlots() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String name = I18n.format(getTranslationKey() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/generic_54.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModItems.hopperCart.getTranslationKey();
    }
}
