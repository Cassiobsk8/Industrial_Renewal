package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.gui.container.ContainerFluidLoader;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnFluidLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUIFluidLoader extends GUIBase
{

    private final TileEntityFluidLoader entity;

    public GUIFluidLoader(IInventory playerInv, TileEntityFluidLoader entity)
    {
        super(new ContainerFluidLoader(playerInv, entity), playerInv);
        this.entity = entity;
    }

    private String getGUIButtonText()
    {
        String waitE;
        switch (this.entity.getWaitEnum())
        {
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

    private String getGUIModeText()
    {
        if (entity.isUnload()) return I18n.format("gui.industrialrenewal.button.unloader_mode");
        return I18n.format("gui.industrialrenewal.button.loader_mode");
    }

    @Override
    protected void actionPerformed(GuiButton b)
    {
        if (b.id == 0)
        {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnFluidLoader(this.entity, false));
        }
        if (b.id == 1)
        {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnFluidLoader(this.entity, true));
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int posX1 = ((this.width - this.xSize) / 2);
        int posY1 = ((this.height - this.ySize) / 2);
        this.buttonList.add(new GuiButton(0, posX1 + 7, posY1 + 53, 61, 18, ""));
        this.buttonList.add(new GuiButton(1, posX1 + 7, posY1 + 18, 52, 18, ""));
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/fluidloader.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModBlocks.fluidLoader.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String waitE = getGUIButtonText();
        String mode = getGUIModeText();
        fontRenderer.drawString(waitE, (xSize / 2 - fontRenderer.getStringWidth(waitE) / 2) - 50, 59, 0xffffff);
        fontRenderer.drawString(mode, (xSize / 2 - fontRenderer.getStringWidth(mode) / 2) - 55, 24, 0xffffff);

        // ProgressBar
        drawFluidBar(entity.tank, 80, 79);

        if (isPointInRegion(79, 21, 18, 60, mouseX, mouseY))
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

        if (isPointInRegion(7, 53, 61, 18, mouseX, mouseY))
        {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.button.cargoloaderbutton0") + " " + TextFormatting.DARK_GREEN + waitE);
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
