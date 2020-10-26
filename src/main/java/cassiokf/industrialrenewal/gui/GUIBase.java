package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

public abstract class GUIBase extends GuiContainer
{

    public final IInventory playerInv;

    public int actualMouseX;
    public int actualMouseY;

    public GUIBase(Container container, IInventory playerInv)
    {
        super(container);

        this.xSize = 176;
        this.ySize = 166;

        this.playerInv = playerInv;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, getTexturePath()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    public abstract String getTexturePath();

    public abstract String getTranslationKey();

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        drawGuiContainerForegroundLayer(mouseX, mouseY, 0);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY, int invOffset)
    {
        drawGuiContainerForegroundLayer(mouseX, mouseY, invOffset, true);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY, int invOffset, boolean showName)
    {
        if (showName)
        {
            String name = I18n.format(getTranslationKey() + ".name");
            fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        }
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8 + invOffset, ySize - 94, 0x404040);

        actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        actualMouseY = mouseY - ((this.height - this.ySize) / 2);
    }

    public void drawFluidBar(FluidTank tank, int x, int y)
    {
        int percentage = 58 * tank.getFluidAmount() / tank.getCapacity();

        FluidStack fluid = tank.getFluid();

        if (fluid != null)
        {
            renderFluid(fluid.getFluid(), x, y, percentage);
        }
    }

    protected void renderFluid(Fluid fluid, int x, int y, int level)
    {

        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());

        if (fluidStillSprite == null)
            fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

        int color = fluid.getColor();

        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GL11.glColor4f(r, g, b, 1.0F);

        this.drawTexturedModalRect(x, y - level, fluidStillSprite, 16, level);
    }
}
