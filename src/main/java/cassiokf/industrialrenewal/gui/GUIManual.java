package cassiokf.industrialrenewal.gui;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
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

public class GUIManual extends GuiScreen
{

    private static final ResourceLocation bookBackground = new ResourceLocation(References.MODID, "textures/gui/book_background.png");
    private static final ResourceLocation logoIMG = new ResourceLocation(References.MODID, "textures/gui/book/logo.png");
    private static final ResourceLocation railroadIMG = new ResourceLocation(References.MODID, "textures/gui/book/railroad.png");
    private static final ResourceLocation redstoneIMG = new ResourceLocation(References.MODID, "textures/gui/book/redstone.png");
    private static final ResourceLocation utilsIMG = new ResourceLocation(References.MODID, "textures/gui/book/utils.png");
    private static final ResourceLocation warning = new ResourceLocation(References.MODID, "textures/gui/book/warning.png");
    private final int bookImageWidth = 256;
    private final int bookImageHeight = 171;
    private final World world;
    private final EntityPlayer player;
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private final ArrayList<String> texts = new ArrayList<>();
    private final ArrayList<ItemStack> railroadItems = new ArrayList<>();
    private final ArrayList<ItemStack> redstoneItems = new ArrayList<>();
    private final ArrayList<ItemStack> utilsItems = new ArrayList<>();
    private int xOffset;
    private int yOffset;
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
    private ButtonBookOverIcon buttonRailroad4;
    private ButtonBookOverIcon buttonRedstone1;
    private ButtonBookOverIcon buttonRedstone2;
    private ButtonBookOverIcon buttonRedstone3;
    private ButtonBookOverIcon buttonUtils1;
    private ButtonBookOverIcon buttonUtils2;
    private ButtonBookOverIcon buttonUtils3;
    private ButtonBookOverIcon buttonBack;

    public GUIManual(World world, EntityPlayer player)
    {
        this.world = world;
        this.player = player;

        items.clear();
        texts.clear();
        railroadItems.clear();
        utilsItems.clear();
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
        railroadItems.add(new ItemStack(ModBlocks.normalRail));
        //Redstone
        redstoneItems.add(new ItemStack(ModBlocks.fuseBox));
        redstoneItems.add(new ItemStack(ModBlocks.flameDetector));
        redstoneItems.add(new ItemStack(ModBlocks.entityDetector));
        //Utils
        utilsItems.add(new ItemStack(ModItems.fireExtinguisher));
        utilsItems.add(new ItemStack(ModBlocks.gutter));
        utilsItems.add(new ItemStack(ModBlocks.firstAidKit));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        xOffset = (this.width - bookImageWidth) / 2;
        yOffset = (this.height - bookImageHeight) / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(bookBackground);
        drawTexturedModalRect(xOffset, yOffset, 0, 0, bookImageWidth, bookImageHeight);
        switch (page)
        {
            default:
                drawPictureOnTop(warning, 48, 0, 0);
                drawText("Please, report this error code: " + "\n" + " \n " + "PAGEERROR" + page);
                break;
            case 0:
                drawPictureOnTop(logoIMG, 56, 0, 0);
                drawIcons(items, texts, true);
                break;
            case 1:
                drawPictureOnTop(railroadIMG, 54, 0, 0);
                drawIcons(railroadItems, null, true);
                break;
            case 2:
                drawPictureOnTop(redstoneIMG, 54, 0, 0);
                drawIcons(redstoneItems, null, true);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                drawPictureOnTop(utilsIMG, 54, 0, 0);
                drawIcons(utilsItems, null, true);
                break;
            case 6:
                break;
            case 11:
                drawPictureOnTop(railroadIMG, 54, 0, 55);
                drawText(I18n.format("gui.industrialrenewal.steam_locomotive.text0"));
                break;
            case 12:
                drawPictureOnTop(railroadIMG, 54, 0, 109);
                drawText(I18n.format("gui.industrialrenewal.cargo_container.text0"));
                break;
            case 13:
                drawPictureOnTop(railroadIMG, 54, 0, 163);
                drawText(I18n.format("gui.industrialrenewal.fluid_container.text0"));
                break;
            case 14:
                drawPictureOnTop(railroadIMG, 54, 101, 0);
                drawText(I18n.format("gui.industrialrenewal.rails.text0"));
                break;
            case 21:
                drawText(I18n.format("gui.industrialrenewal.fuse_box.text0"));
                break;
            case 22:
                drawText(I18n.format("gui.industrialrenewal.flame_detector.text0"));
                break;
            case 23:
                drawText(I18n.format("gui.industrialrenewal.entity_detector.text0"));
                break;
            case 51:
                drawText(I18n.format("gui.industrialrenewal.fire_extinguisher.text0"));
                break;
            case 52:
                drawText(I18n.format("gui.industrialrenewal.gutter.text0"));
                break;
            case 53:
                drawText(I18n.format("gui.industrialrenewal.first-aid_box.text0"));
                break;
        }
        drawPageName();
        drawPageNumber();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawText(String text)
    {
        int maxPixelsPerLine = 93;
        StringBuilder currentLine = new StringBuilder();
        int currentWidth = 0, yoffset = 66, xoffset = 18, maxLineFirstPage = 9;
        int currentLineN = 0;
        boolean changeLine = false;
        String[] words = text.split("\\s+");

        for (String word : words)
        {
            if (currentWidth + fontRenderer.getStringWidth(word) <= maxPixelsPerLine && !word.equals("$n"))
            {
                currentLine.append(word).append(" ");
                currentWidth += fontRenderer.getStringWidth(word);
            }
            else
            {
                currentLineN++;
                if (currentLineN > maxLineFirstPage && !changeLine)
                {
                    changeLine = true;
                    yoffset = 18;
                    xoffset = xoffset + 110;
                }
                fontRenderer.drawString(I18n.format(currentLine.toString()), (this.width - bookImageWidth) / 2 + 5 + xoffset, (this.height - bookImageHeight) / 2 + yoffset, 4210752);
                currentLine = new StringBuilder();
                currentWidth = 0;
                yoffset += fontRenderer.FONT_HEIGHT;
                if (!word.equals("$n"))
                {
                    currentLine.append(word).append(" ");
                    currentWidth += fontRenderer.getStringWidth(word);
                }
            }
        }
        if (currentWidth != 0)
            fontRenderer.drawString(I18n.format(currentLine.toString()), (this.width - bookImageWidth) / 2 + 5 + xoffset, (this.height - bookImageHeight) / 2 + yoffset, 4210752);
    }

    private void drawIcons(ArrayList<ItemStack> array, ArrayList<String> texts, boolean pictureOnTop)
    {
        int x = xOffset + 22;
        int y = yOffset + 12;
        if (pictureOnTop)
        {
            y = y + 52;
        }
        boolean secondSide = false;
        for (ItemStack item : array)
        {
            String text;
            if (texts == null || texts.get(items.indexOf(item)) == null)
            {
                text = item.getDisplayName();
            }
            else
            {
                text = texts.get(items.indexOf(item));
            }
            drawIcon(text, item, x, y);
            y = y + 20;
            if (y > (yOffset + 150))
            {
                if (!secondSide)
                {
                    y = yOffset + 12;
                    x = x + 108;
                    secondSide = true;
                }
            }
        }
    }

    private void drawIcon(String text, ItemStack item, int x, int y)
    {
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemIntoGUI(item, x, y);
        x = x + 20;
        fontRenderer.drawString(text, x, y + 5, 0);
        RenderHelper.enableStandardItemLighting();
    }

    private void drawPictureOnTop(ResourceLocation resourceLocation, int yImage, int startX, int startY)
    {
        int x = xOffset + 24;// + 132;
        int y = yOffset + 9;
        GlStateManager.pushMatrix();
        mc.getTextureManager().bindTexture(resourceLocation);
        //GlStateManager.scale(0.5f, 0.5f, 0.5f);
        drawTexturedModalRect(x, y, startX, startY, 100, yImage);
        GlStateManager.popMatrix();
    }

    private void drawPageName()
    {
        if (page < 7 && page > 0)
        {
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

    private void drawPageNumber()
    {
        if (page != 0)
        {
            int x = xOffset + 222;
            int y = yOffset + 3;
            fontRenderer.drawString(String.valueOf(page), x, y, 0);
        }
    }

    @Override
    protected void actionPerformed(GuiButton b)
    {
        if (b.id < 200)
        {
            page = b.id;
        }
        if (b.id == 200)
        {
            if (page <= 10)
            {
                page = 0;
            }
            else
            {
                int x = Math.abs(page);
                page = (int) Math.floor(x / Math.pow(10, Math.floor(Math.log10(x))));
            }
        }
    }

    @Override
    public void updateScreen()
    {
        button1.visible = page == 0;
        button2.visible = page == 0;
        button3.visible = page == 0;
        button4.visible = page == 0;
        button5.visible = page == 0;
        button6.visible = page == 0;

        buttonRailroad1.visible = page == 1;
        buttonRailroad2.visible = page == 1;
        buttonRailroad3.visible = page == 1;
        buttonRailroad4.visible = page == 1;

        buttonRedstone1.visible = page == 2;
        buttonRedstone2.visible = page == 2;
        buttonRedstone3.visible = page == 2;

        buttonUtils1.visible = page == 5;
        buttonUtils2.visible = page == 5;
        buttonUtils3.visible = page == 5;

        buttonBack.visible = page != 0;
    }

    @Override
    public void initGui()
    {
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
        y = y + 20;
        buttonRailroad4 = new ButtonBookOverIcon(14, x, y, 103, 20, " ");
        this.buttonList.add(buttonRailroad4);

        //PAGE 2 REDSTONE
        x = xOffset + 22;
        y = yOffset + 62;

        buttonRedstone1 = new ButtonBookOverIcon(21, x, y, 103, 20, " ");
        this.buttonList.add(buttonRedstone1);
        y = y + 20;
        buttonRedstone2 = new ButtonBookOverIcon(22, x, y, 103, 20, " ");
        this.buttonList.add(buttonRedstone2);
        y = y + 20;
        buttonRedstone3 = new ButtonBookOverIcon(23, x, y, 103, 20, " ");
        this.buttonList.add(buttonRedstone3);

        //PAGE 5 UTILS
        x = xOffset + 22;
        y = yOffset + 62;

        buttonUtils1 = new ButtonBookOverIcon(51, x, y, 103, 20, " ");
        this.buttonList.add(buttonUtils1);
        y = y + 20;
        buttonUtils2 = new ButtonBookOverIcon(52, x, y, 103, 20, " ");
        this.buttonList.add(buttonUtils2);
        y = y + 20;
        buttonUtils3 = new ButtonBookOverIcon(53, x, y, 103, 20, " ");
        this.buttonList.add(buttonUtils3);

        buttonBack = new ButtonBookOverIcon(200, xOffset + 20, yOffset + 144, 20, 20, "<<");
        this.buttonList.add(buttonBack);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    @Override
    public void onGuiClosed()
    {
        this.world.playSound(this.player, this.player.getPosition(), IRSoundRegister.BOOK_FLIP, SoundCategory.BLOCKS, 1f, 0.8f);
    }
}

