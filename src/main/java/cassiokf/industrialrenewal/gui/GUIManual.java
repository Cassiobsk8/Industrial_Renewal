package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.util.buttons.ButtonBookOverIcon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GUIManual extends GuiScreen {

    private final int bookImageWidth = 256;
    private final int bookImageHeight = 171;
    private static ResourceLocation bookBackground = new ResourceLocation(References.MODID, "textures/gui/book_background.png");
    private static ResourceLocation logoIMG = new ResourceLocation(References.MODID, "textures/gui/book/logo.png");
    private static ResourceLocation railroadIMG = new ResourceLocation(References.MODID, "textures/gui/book/railroad.png");
    private World world;
    private EntityPlayer player;
    private int xOffset;
    private int yOffset;
    private ArrayList<ItemStack> items = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<ItemStack> railroadItems = new ArrayList<>();
    private int page = 0;
    private ButtonBookOverIcon button1;
    private ButtonBookOverIcon button2;
    private ButtonBookOverIcon button3;
    private ButtonBookOverIcon button4;
    private ButtonBookOverIcon button5;
    private ButtonBookOverIcon button6;
    private ButtonBookOverIcon buttonRailroad1;
    private ButtonBookOverIcon buttonRailroad2;
    private ButtonBookOverIcon buttonRailroad3;
    private ButtonBookOverIcon buttonBack;

    public GUIManual(World world, EntityPlayer player) {
        this.world = world;
        this.player = player;

        items.clear();
        texts.clear();
        railroadItems.clear();
        //FIRST PAGE
        items.add(new ItemStack(ModItems.steamLocomotive));
        texts.add(I18n.format("gui.industrialrenewal.railroad.category"));
        items.add(new ItemStack(ModBlocks.fuseBox));
        texts.add(I18n.format("gui.industrialrenewal.redstone.category"));
        items.add(new ItemStack(ModBlocks.catWalk));
        texts.add(I18n.format("gui.industrialrenewal.catwalk.category"));
        items.add(new ItemStack(ModBlocks.efence));
        texts.add(I18n.format("gui.industrialrenewal.fences.category"));
        items.add(new ItemStack(ModItems.fireExtinguisher));
        texts.add(I18n.format("gui.industrialrenewal.utilities.category"));
        items.add(new ItemStack(ModBlocks.pillar));
        texts.add(I18n.format("gui.industrialrenewal.construction.category"));
        //RAILROAD
        railroadItems.add(new ItemStack(ModItems.steamLocomotive));
        railroadItems.add(new ItemStack(ModItems.cargoContainer));
        railroadItems.add(new ItemStack(ModItems.fluidContainer));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        xOffset = (this.width - bookImageWidth) / 2;
        yOffset = (this.height - bookImageHeight) / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(bookBackground);
        drawTexturedModalRect(xOffset, yOffset, 0, 0, bookImageWidth, bookImageHeight);
        switch (page) {
            case 0:
            default:
                drawPictureOnTop(logoIMG, 56);
                drawIcons(items, texts, true);
                break;
            case 1:
                drawPictureOnTop(railroadIMG, 54);
                drawIcons(railroadItems, null, true);
                break;
            case 2:
                drawText("Aaaaa aaaaa a a a a aaaaaaaa aaaaaa aaaaaaa aaaaaaaa sssssssssa ssssssasas asdasdasd asdasdasd asdasd asd as dasdasdasd asdasdasdas dasdas d asdasd as dasdasdas dasd asda j a adhasd asdasdasd asdasdasd asdasdasd asdasdasdasaf");
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 11:
                drawText(I18n.format("gui.industrialrenewal.steam_locomotive.text0"));
                break;
            case 12:
                break;
            case 13:
                break;
        }
        drawPageName();
        drawPageNumber();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawText(String text) {
        int maxPixelsPerLine = 90;
        StringBuilder currentLine = new StringBuilder();
        int currentWidth = 0, yoffset = 52, xoffset = 18, maxLineFirstPage = 10;
        int currentLineN = 0;
        boolean changeLine = false;
        String[] words = text.split(" ");

        for (int i = 0; i < words.length; i++) {
            if (currentWidth + fontRenderer.getStringWidth(words[i]) <= maxPixelsPerLine && !words[i].equals("\n")) {
                currentLine.append(words[i]).append(" ");
                currentWidth += fontRenderer.getStringWidth(words[i]);
            } else {
                currentLineN++;
                if (currentLineN > maxLineFirstPage && !changeLine) {
                    changeLine = true;
                    yoffset = 18;
                    xoffset = xoffset + 110;
                }
                fontRenderer.drawString(I18n.format(currentLine.toString()), (this.width - bookImageWidth) / 2 + 5 + xoffset, (this.height - bookImageHeight) / 2 + yoffset, 4210752);
                currentLine = new StringBuilder();
                currentWidth = 0;
                yoffset += fontRenderer.FONT_HEIGHT;
                currentLine.append(words[i]).append(" ");
                currentWidth += fontRenderer.getStringWidth(words[i]);
            }
        }
        if (currentWidth != 0)
            fontRenderer.drawString(I18n.format(currentLine.toString()), (this.width - bookImageWidth) / 2 + 5 + xoffset, (this.height - bookImageHeight) / 2 + yoffset, 4210752);
    }

    private void drawIcons(ArrayList<ItemStack> array, ArrayList<String> texts, boolean pictureOnTop) {
        int x = xOffset + 22;
        int y = yOffset + 12;
        if (pictureOnTop) {
            y = y + 52;
        }
        boolean secondSide = false;
        for (ItemStack item : array) {
            String text;
            if (texts == null) {
                text = item.getDisplayName();
            } else {
                text = texts.get(items.indexOf(item));
            }
            drawIcon(text, item, x, y);
            y = y + 20;
            if (y > (yOffset + 150)) {
                if (!secondSide) {
                    y = yOffset + 12;
                    x = x + 108;
                    secondSide = true;
                }
            }
        }
    }

    private void drawIcon(String text, ItemStack item, int x, int y) {
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemIntoGUI(item, x, y);
        x = x + 20;
        fontRenderer.drawString(text, x, y + 5, 0);
        RenderHelper.enableStandardItemLighting();
    }

    private void drawPictureOnTop(ResourceLocation resourceLocation, int yImage) {
        int x = xOffset + 24;// + 132;
        int y = yOffset + 9;
        GlStateManager.pushMatrix();
        mc.getTextureManager().bindTexture(resourceLocation);
        //GlStateManager.scale(0.5f, 0.5f, 0.5f);
        drawTexturedModalRect(x, y, 0, 0, 100, yImage);
        GlStateManager.popMatrix();
    }

    private void drawPageName() {
        if (page < 7 && page > 0) {
            int x = xOffset + (bookImageWidth / 4) - 4;
            int y = yOffset + 1;
            String name = texts.get(page - 1);
            x = x - (name.length() / 2);
            RenderHelper.disableStandardItemLighting();
            RenderHelper.enableGUIStandardItemLighting();
            fontRenderer.drawString(name, x, y, 16711680);
            RenderHelper.enableStandardItemLighting();
        }
    }

    private void drawPageNumber() {
        if (page != 0) {
            int x = xOffset + 228;
            int y = yOffset + 3;
            fontRenderer.drawString(String.valueOf(page), x, y, 0);
        }
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        if (b.id < 20) {
            page = b.id;
        }
        if (b.id == 20) {
            page = 0;
        }
    }

    @Override
    public void updateScreen() {
        button1.visible = page == 0;
        button2.visible = page == 0;
        button3.visible = page == 0;
        button4.visible = page == 0;
        button5.visible = page == 0;
        button6.visible = page == 0;
        buttonRailroad1.visible = page == 1;
        buttonRailroad2.visible = page == 1;
        buttonRailroad3.visible = page == 1;
        buttonBack.visible = page != 0;
    }

    @Override
    public void initGui() {
        xOffset = (this.width - bookImageWidth) / 2;
        yOffset = (this.height - bookImageHeight) / 2;
        //Keyboard.enableRepeatEvents(true);
        int x = xOffset + 22;
        int y = yOffset + 62;
        //Page 0
        button1 = new ButtonBookOverIcon(1, x, y, 103, 20, " ");
        this.buttonList.add(button1);
        y = y + 20;
        button2 = new ButtonBookOverIcon(2, x, y, 103, 20, " ");
        this.buttonList.add(button2);
        y = y + 20;
        button3 = new ButtonBookOverIcon(3, x, y, 103, 20, " ");
        this.buttonList.add(button3);
        y = y + 20;
        button4 = new ButtonBookOverIcon(4, x, y, 103, 20, " ");
        this.buttonList.add(button4);
        y = y + 20;
        button5 = new ButtonBookOverIcon(5, x, y, 103, 20, " ");
        this.buttonList.add(button5);
        y = yOffset + 10;
        x = x + 108;
        button6 = new ButtonBookOverIcon(6, x, y, 103, 20, " ");
        this.buttonList.add(button6);

        //PAGE 1 RAILROAD
        x = xOffset + 22;
        y = yOffset + 62;
        buttonRailroad1 = new ButtonBookOverIcon(11, x, y, 103, 20, " ");
        this.buttonList.add(buttonRailroad1);
        y = y + 20;
        buttonRailroad2 = new ButtonBookOverIcon(12, x, y, 103, 20, " ");
        this.buttonList.add(buttonRailroad2);
        y = y + 20;
        buttonRailroad3 = new ButtonBookOverIcon(13, x, y, 103, 20, " ");
        this.buttonList.add(buttonRailroad3);


        buttonBack = new ButtonBookOverIcon(20, xOffset + 20, yOffset + 144, 20, 20, "<<");
        this.buttonList.add(buttonBack);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void onGuiClosed() {
        this.world.playSound(this.player, this.player.getPosition(), IRSoundHandler.BOOK_FLIP, SoundCategory.BLOCKS, 1f, 0.8f);
    }
}

