package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.gui.container.ContainerStorageChest;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnTEStorageChest;
import cassiokf.industrialrenewal.tileentity.TEStorageChest;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.IInventory;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;
import java.util.Map;

public class GUIStorageChest extends GUIBase {
    private static Field campo;
    private static Map<String, KeyBinding> binds;
    private final TEStorageChest te;
    private GuiButton upB;
    private GuiButton downB;

    public GUIStorageChest(IInventory playerInv, TEStorageChest te) {
        super(new ContainerStorageChest(playerInv, te), playerInv);
        this.xSize = 220;
        this.ySize = 202;
        this.te = te;
        try {
            campo = KeyBinding.class.getDeclaredField("KEYBIND_ARRAY");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        campo.setAccessible(true);
        try {
            binds = (Map<String, KeyBinding>) campo.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        sendToServer(b.id);
    }

    private void sendToServer(int id) {
        NetworkHandler.INSTANCE.sendToServer(new PacketReturnTEStorageChest(this.te, Minecraft.getMinecraft().player.getEntityId(), id));
    }

    @Override
    public void initGui() {
        super.initGui();
        int posX1 = ((this.width - this.xSize) / 2);
        int posY1 = ((this.height - this.ySize) / 2);
        upB = new GuiButton(1, posX1 + 206, posY1 + 6, 10, 18, "Λ");
        downB = new GuiButton(2, posX1 + 206, posY1 + 96, 10, 18, "V");
        upB.enabled = false;
        downB.enabled = te.currentLine < te.additionalLines;
        this.buttonList.add(upB);
        this.buttonList.add(downB);
    }

    @Override
    public String getTexturePath() {
        return "textures/gui/container/storage_chest.png";
    }

    @Override
    public String getTranslationKey() {
        return ModBlocks.storageChest.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int progress = (int) ((te.additionalLines > 0 ? Utils.normalize(te.currentLine, 0, te.additionalLines) : 0) * 55);
        this.drawTexturedModalRect(this.guiLeft + 207, this.guiTop + 25 + progress, 221, 0, 8, 15);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        actualMouseY = mouseY - ((this.height - this.ySize) / 2);

        upB.enabled = te.currentLine > 0;
        downB.enabled = te.currentLine < te.additionalLines;

        if (isPointInRegion(205, 6, 10, 108, mouseX, mouseY)) {
            int scroll = Mouse.getDWheel();
            if (scroll > 0) scrollPressed(true);
            else if (scroll < 0) scrollPressed(false);
        }
    }

    public void scrollPressed(boolean up) {
        if (up && upB.enabled) sendToServer(upB.id);
        else if (!up && downB.enabled) sendToServer(downB.id);
    }
}
