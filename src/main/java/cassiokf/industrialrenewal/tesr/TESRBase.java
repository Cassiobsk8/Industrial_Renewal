package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class TESRBase<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
    public static final ItemStack cutter = new ItemStack(ModItems.cutter);
    public static final ItemStack energyBarLevel = new ItemStack(ModItems.barLevel);
    public static final ItemStack pointerLong = new ItemStack(ModItems.pointerLong);
    public static final ItemStack pointer = new ItemStack(ModItems.pointer);
    public static final ItemStack limiter = new ItemStack(ModItems.limiter);
    public static final ItemStack indicator_on = new ItemStack(ModItems.indicator_on);
    public static final ItemStack indicator_off = new ItemStack(ModItems.indicator_off);
    public static final ItemStack switch_on = new ItemStack(ModItems.switch_on);
    public static final ItemStack switch_off = new ItemStack(ModItems.switch_off);
    public static final ItemStack push_button = new ItemStack(ModItems.push_button);
    public static final ItemStack label_5 = new ItemStack(ModItems.label_5);

    public double xPos = 0D;
    public double zPos = 0D;

    public static void renderScreenTexts(EnumFacing facing, double x, double y, double z, String[] text, float spacing, float scale)
    {
        double lY = y;
        for (String line : text)
        {
            renderText(facing, x, lY, z, TextFormatting.GREEN + line, scale, false);
            lY -= spacing;
        }
    }

    /**
     * x = side / y = up / z = front
     */
    public static void renderText(EnumFacing facing, double x, double y, double z, String st, float scale)
    {
        renderText(facing, x, y, z, st, scale, true);
    }

    private static void renderText(EnumFacing facing, double x, double y, double z, String st, float scale, boolean centerText)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        rotateAccordingly(facing);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.scale(scale, scale, 1F);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int xh = centerText ? (-fontRenderer.getStringWidth(st) / 2) : 0;
        fontRenderer.drawString(st, xh, 0, 0xFFFFFFFF);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static ItemStack getIndicator(boolean value)
    {
        return value ? indicator_on : indicator_off;
    }

    public static ItemStack getSwitch(boolean value)
    {
        return value ? switch_on : switch_off;
    }

    public static void render3dItem(EnumFacing facing, World world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight)
    {
        render3dItem(facing, world, x, y, z, stack, scale, disableLight, false, 0, 0, 0, 0, false, false);
    }

    public static void render3dItem(EnumFacing facing, World world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, float rotation, float rX, float rY, float rZ)
    {
        render3dItem(facing, world, x, y, z, stack, scale, disableLight, true, rotation, rX, rY, rZ, false, false);
    }

    public static void render3dItem(EnumFacing facing, World world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, boolean applyRotation, float rotation, float rX, float rY, float rZ, boolean rotateHorizontal, boolean rotateVertical)
    {
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        if (disableLight) RenderHelper.disableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        rotateAccordingly(facing);
        GlStateManager.scale(scale, scale, scale);
        if (rotateHorizontal) GlStateManager.rotate(90, 1, 0, 0);
        if (rotateVertical) GlStateManager.rotate(90, 0, 1, 0);
        if (applyRotation) GlStateManager.rotate(rotation, rX, rY, rZ);

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        if (disableLight) RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }

    public static void render2dItem(EnumFacing facing, World world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight)
    {
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        if (disableLight) RenderHelper.disableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        rotateAccordingly(facing);
        GlStateManager.scale(scale, scale, scale);

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        if (disableLight) RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }

    public static void renderBarLevel(EnumFacing facing, double x, double y, double z, float fill, float scale)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        rotateAccordingly(facing);
        GlStateManager.scale(scale, fill * scale, 0.05F);
        Minecraft.getMinecraft().getRenderItem().renderItem(energyBarLevel, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }

    /**
     * x = side / y = up / z = front
     */
    public static void renderPointer(EnumFacing facing, double x, double y, double z, float angle, ItemStack pointer, float scale)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        rotateAccordingly(facing);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }

    public static void rotateAccordingly(EnumFacing facing)
    {
        switch (facing)
        {
            default:
            case SOUTH:
                GlStateManager.rotate(180F, 0, 1, 0);
                break;
            case NORTH:
                break;
            case WEST:
                GlStateManager.rotate(90F, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(-90F, 0, 1, 0);
                break;
        }
    }

    public static float smoothAnimation(float rotation, float oldRotation, float partialTick, boolean invert)
    {
        //shift = shiftOld + (shift - shiftOld) * partialTick
        float r = oldRotation + (rotation - oldRotation) * partialTick;
        return invert ? -r : r;
    }

    @Override
    public abstract void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);

    public void doTheMath(EnumFacing facing, double x, double z, double offset, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z + (1 - offset);
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + offset;
                return;
            case EAST:
                xPos = x + (1 - offset);
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + offset;
                zPos = z + (0.5 - sidePlus);
        }
    }
}
