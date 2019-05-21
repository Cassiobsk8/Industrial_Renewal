package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.Registry.ModBlocks;
import cassiokf.industrialrenewal.Registry.NetworkHandler;
import cassiokf.industrialrenewal.container.ContainerFluidLoader;
import cassiokf.industrialrenewal.network.PacketReturnFluidLoader;
import cassiokf.industrialrenewal.tileentity.railroad.fluidloader.TileEntityFluidLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GUIFluidLoader extends GuiContainer {

    private TileEntityFluidLoader entity;
    private IInventory playerInv;

    public GUIFluidLoader(IInventory playerInv, TileEntityFluidLoader entity) {
        super(new ContainerFluidLoader(playerInv, entity));

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

    private String getGUIButtonText() {
        String waitE;
        switch (this.entity.getWaitEnum()) {
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
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnFluidLoader(this.entity, true));
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/fluidloader.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.fluidLoader.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, 8, 4, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 92, 0x404040);

        String waitE = getGUIButtonText();
        fontRenderer.drawString(waitE, (xSize / 2 - fontRenderer.getStringWidth(waitE) / 2) - 50, 59, 0xffffff);

        int amount = entity.tank.getFluidAmount();
        // ProgressBar
        drawEnergyBar(entity.tank, amount);
        //
        int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

        if (isPointInRegion(79, 21, 18, 60, mouseX, mouseY)) {

            List<String> text = new ArrayList<String>();
            if (amount > 0) {
                text.add(TextFormatting.WHITE + "" + entity.tank.getFluid().getLocalizedName());
            }
            text.add(TextFormatting.GRAY + "" + amount + " / " + entity.tank.getCapacity() + " mB");
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }

        if (isPointInRegion(7, 53, 61, 18, mouseX, mouseY)) {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.button.cargoloaderbutton0") + " " + TextFormatting.DARK_GREEN + waitE);
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }

    private void drawEnergyBar(FluidTank tank, int amount) {
        int percentage = 58 * amount / tank.getCapacity();

        FluidStack fluid = tank.getFluid();

        if (fluid != null) {
            renderFluid(fluid.getFluid(), 80, 78, 16, percentage);
        }
    }

    protected void renderFluid(Fluid fluid, int x, int y, int mazX, int level) {

        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());

        if (fluidStillSprite == null)
            fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

        int color = fluid.getColor();

        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GL11.glColor4f(r, g, b, 1.0F);

        this.drawTexturedModalRect(x, y - level, fluidStillSprite, mazX, level);
    }
}
