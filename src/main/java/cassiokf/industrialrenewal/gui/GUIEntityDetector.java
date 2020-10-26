package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.gui.container.ContainerEntityDetector;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnEntityDetector;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityEntityDetector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;

public class GUIEntityDetector extends GUIBase
{

    private final TileEntityEntityDetector te;

    public GUIEntityDetector(IInventory playerInv, TileEntityEntityDetector te)
    {
        super(new ContainerEntityDetector(playerInv, te), playerInv);
        this.te = te;
    }

    @Override
    protected void actionPerformed(GuiButton b)
    {
        NetworkHandler.INSTANCE.sendToServer(new PacketReturnEntityDetector(this.te, b.id));
    }

    private String getGUIButtonText()
    {
        switch (this.te.getEntityEnum())
        {
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
    public void initGui()
    {
        super.initGui();
        int posX1 = ((this.width - this.xSize) / 2);
        int posY1 = ((this.height - this.ySize) / 2);
        this.buttonList.add(new GuiButton(1, posX1 + 41, posY1 + 22, 72, 18, " "));
        this.buttonList.add(new GuiButton(2, posX1 + 61, posY1 + 64, 108, 18, " "));
    }


    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/entitydetector.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModBlocks.entityDetector.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String distance = I18n.format("gui." + References.MODID + ".button.distance") + " " + te.getDistance();
        String entity = I18n.format("gui." + References.MODID + ".button.entity") + " " + getGUIButtonText();

        fontRenderer.drawString(distance, xSize / 2 - fontRenderer.getStringWidth(distance) / 2 - 10, 28, 0xffffff);
        fontRenderer.drawString(entity, xSize / 2 - fontRenderer.getStringWidth(entity) / 2 + 27, 70, 0xffffff);
    }
}
