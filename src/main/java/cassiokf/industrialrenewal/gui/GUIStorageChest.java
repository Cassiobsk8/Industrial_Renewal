package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.gui.container.ContainerStorageChest;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnTEStorageChest;
import cassiokf.industrialrenewal.tileentity.TEStorageChest;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Locale;

public class GUIStorageChest extends GUIBase
{
    private final TEStorageChest te;
    private GuiButton upB;
    private GuiButton downB;
    private GuiTextField searchField;
    private boolean skip;

    public GUIStorageChest(IInventory playerInv, TEStorageChest te)
    {
        super(new ContainerStorageChest(playerInv, te), playerInv);
        this.xSize = 220;
        this.ySize = 211;
        this.te = te;
    }

    @Override
    protected void actionPerformed(GuiButton b)
    {
        sendToServer(b.id);
    }

    private void sendToServer(int id)
    {
        te.guiButtonClick(id, null);
        NetworkHandler.INSTANCE.sendToServer(new PacketReturnTEStorageChest(this.te, id, mc.player.getEntityId()));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int posX1 = ((this.width - this.xSize) / 2);
        int posY1 = ((this.height - this.ySize) / 2);
        upB = new GuiButton(1, posX1 + 206, posY1 + 15, 10, 18, I18n.format("gui.industrialrenewal.arrowup"));
        downB = new GuiButton(2, posX1 + 206, posY1 + 105, 10, 18, I18n.format("gui.industrialrenewal.arrowdown"));
        upB.enabled = false;
        downB.enabled = te.currentLine < te.additionalLines;
        this.buttonList.add(upB);
        this.buttonList.add(downB);

        Keyboard.enableRepeatEvents(true);
        FontRenderer fontRenderer = mc.fontRenderer;
        searchField = new GuiTextField(0, fontRenderer, this.guiLeft + 138, this.guiTop + 5, 80, fontRenderer.FONT_HEIGHT);
        searchField.setText(te.search);
        searchField.setMaxStringLength(50);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setTextColor(16777215);
        searchField.setCanLoseFocus(true);
        searchField.setVisible(true);
        searchField.setFocused(te.searchActive);
        skip = false;
    }

    public void onKeyboardEvent(GuiScreenEvent.KeyboardInputEvent event)
    {
        if (Keyboard.getEventKeyState())
        {
            int keyCode = Keyboard.getEventKey();
            char charCode = Keyboard.getEventCharacter();
            if (searchField.isFocused())
            {
                if (skip)
                {
                    skip = false;
                }
                else
                {
                    skip = true;
                    if (mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
                    { // Inventory key
                        event.setCanceled(true);
                        skip = false;
                    }
                    else if (keyCode == 28 || keyCode == 1)
                    {
                        searchField.setFocused(false);
                        event.setCanceled(true);
                        skip = true;
                    }
                    else
                    {
                        for (int i = 0; i < 9; ++i)
                        { // Hotbar keys
                            if (mc.gameSettings.keyBindsHotbar[i].isActiveAndMatches(keyCode))
                            {
                                event.setCanceled(true);
                                skip = false;
                                searchField.textboxKeyTyped(charCode, keyCode);
                                return;
                            }
                        }
                    }
                    searchField.textboxKeyTyped(charCode, keyCode);
                }
            }
            else if (mc.gameSettings.keyBindChat.getKeyCode() == Keyboard.getEventKey())
            { // Chat key
                searchField.setFocused(true);
                skip = true;
            }
        }
    }

    @Override
    public String getTexturePath()
    {
        return "textures/gui/container/storage_chest.png";
    }

    @Override
    public String getTranslationKey()
    {
        return ModBlocks.storageChest.getTranslationKey();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int progress = (int) ((te.additionalLines > 0 ? Utils.normalizeClamped(te.currentLine, 0, te.additionalLines) : 0) * 55);
        this.drawTexturedModalRect(this.guiLeft + 207, this.guiTop + 34 + progress, 221, 0, 8, 15);

        searchField.setEnabled(true);
        searchField.drawTextBox();
        te.search = searchField.getText();
        te.searchActive = searchField.isFocused();

        for (Slot s : inventorySlots.inventorySlots)
        {
            if (!(s.inventory instanceof InventoryPlayer))
            {
                ItemStack stack = s.getStack();
                if (!stackMatches(searchField.getText(), stack))
                {
                    int x = guiLeft + s.xPos;
                    int y = guiTop + s.yPos;
                    GlStateManager.disableDepth();
                    Gui.drawRect(x, y, x + 16, y + 16, 0x80FF0000);
                    GlStateManager.enableDepth();
                }
            }
        }
    }

    private boolean stackMatches(String text, ItemStack stack)
    {
        if (stack.getItem().equals(Items.AIR))
        {
            return true;
        }
        ArrayList<String> keys = new ArrayList<String>();
        for (String line : stack.getTooltip(mc.player,
                mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL))
        {
            keys.add(line);
        }
        for (String key : keys)
        {
            if (key.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        //super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        actualMouseY = mouseY - ((this.height - this.ySize) / 2);

        upB.enabled = te.currentLine > 0;
        downB.enabled = te.currentLine < te.additionalLines;

        if (isPointInRegion(205, 6, 10, 108, mouseX, mouseY))
        {
            int scroll = Mouse.getDWheel();
            if (scroll > 0) scrollPressed(true);
            else if (scroll < 0) scrollPressed(false);
        }
        else if (isPointInRegion(136, 4, 82, 11, mouseX, mouseY))
        {
            if (Mouse.getEventButtonState()) searchField.setFocused(true);
        }
        else if (isPointInRegion(0, 14, width, 200, mouseX, mouseY))
        {
            if (Mouse.getEventButtonState()) searchField.setFocused(false);
        }
    }

    public void scrollPressed(boolean up)
    {
        if (up && upB.enabled) sendToServer(upB.id);
        else if (!up && downB.enabled) sendToServer(downB.id);
    }

}
