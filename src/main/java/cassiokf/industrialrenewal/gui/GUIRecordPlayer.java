package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.gui.container.ContainerRecordPlayer;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnRecordPlayer;
import cassiokf.industrialrenewal.tileentity.TileEntityRecordPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GUIRecordPlayer extends GUIBase
{

    private final TileEntityRecordPlayer te;

    public GUIRecordPlayer(IInventory playerInv, TileEntityRecordPlayer te)
    {
        super(new ContainerRecordPlayer(playerInv, te), playerInv);
        this.te = te;
    }

    @Override
    protected void actionPerformed(GuiButton b)
    {
        NetworkHandler.INSTANCE.sendToServer(new PacketReturnRecordPlayer(this.te, b.id));
    }

    @Override
    public void initGui()
    {
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
    public String getTexturePath()
    {
        return "textures/gui/container/recordplayer.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModBlocks.recordPlayer.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY, 0, false);

        if (isPointInRegion(80, 9, 16, 70, mouseX, mouseY))
        {
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.GRAY + I18n.format("gui.industrialrenewal.blockrecordplayer.discs.tooltip"));
            this.drawHoveringText(text, actualMouseX, actualMouseY);
        }
    }
}
