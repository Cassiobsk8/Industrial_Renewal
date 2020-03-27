package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.container.ContainerLatheMachine;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.machines.TELathe;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class GUILatheMachine extends GuiContainer
{

    private TELathe te;
    private IInventory playerInv;

    public GUILatheMachine(IInventory playerInv, TELathe te)
    {
        super(new ContainerLatheMachine(playerInv, te));

        this.xSize = 176;
        this.ySize = 166;

        this.te = te;
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
    public void initGui()
    {
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/lathe.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        IEnergyStorage energyStorage = te.getEnergyStorage();
        int energyFill = (int) (Utils.normalize(energyStorage.getEnergyStored(), 0, energyStorage.getMaxEnergyStored()) * 69);
        this.drawTexturedModalRect(this.guiLeft + 8, (this.guiTop + 78) - energyFill, 176, 0, 16, energyFill);

        int progress = (int) (te.getNormalizedProcess() * 43);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.MODID, "textures/gui/container/lathe.png"));
        this.drawTexturedModalRect((this.guiLeft + 81) + progress, this.guiTop + 50, 176, 70, 7, 13);

        ItemStack stack = te.getProcessingItem();
        if (stack != null && !stack.isEmpty())
        {
            itemRender.renderItemIntoGUI(stack, this.guiLeft + 96, this.guiTop + 33);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) //Draw Dynamic things
    {
        String name = I18n.format(ModBlocks.latheMachine.getTranslationKey() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 28, ySize - 94, 0x404040);

        int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        int actualMouseY = mouseY - ((this.height - this.ySize) / 2);

        if (isPointInRegion(7, 9, 17, 69, mouseX, mouseY))
        {
            List<String> text = new ArrayList<String>();
            IEnergyStorage energyStorage = te.getEnergyStorage();
            text.add(TextFormatting.GRAY + "" + energyStorage.getEnergyStored() + " / " + energyStorage.getMaxEnergyStored());
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }

}
