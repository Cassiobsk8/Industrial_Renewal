package cassiokf.industrialrenewal.tileentity.filter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class FilterTESRender extends TileEntitySpecialRenderer<TileEntityFilter> {

    public static void translateAgainstPlayer(BlockPos pos, boolean offset) {
        final float x = (float) (pos.getX() - TileEntityRendererDispatcher.staticPlayerX);
        final float y = (float) (pos.getY() - TileEntityRendererDispatcher.staticPlayerY);
        final float z = (float) (pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);

        //if (offset) GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        if (offset) GlStateManager.translate(x + 0, y - 1, z + 0);
        else GlStateManager.translate(x, y, z);
    }

    public static void renderFluid(TileEntityFilter te, FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2) {
        final int color = fluid.getFluid().getColor(fluid);
        renderFluid(te, fluid, pos, x, y, z, x1, y1, z1, x2, y2, z2, color);
    }

    public static void renderFluid(TileEntityFilter te, FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2, int color) {

        final Minecraft mc = Minecraft.getMinecraft();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        final int brightness = mc.world.getCombinedLight(pos, fluid.getFluid().getLuminosity());

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        setupRenderState(x, y, z);

        final TextureAtlasSprite still = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
        final TextureAtlasSprite flowing = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());

        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, brightness);

        tessellator.draw();
        cleanupRenderState();
    }

    public static void addTexturedQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double z, double width, double height, double length, EnumFacing face, int color, int brightness) {

        if (sprite == null) {
            return;
        }

        final int firstLightValue = brightness >> 0x10 & 0xFFFF;
        final int secondLightValue = brightness & 0xFFFF;
        final int alpha = color >> 24 & 0xFF;
        final int red = color >> 16 & 0xFF;
        final int green = color >> 8 & 0xFF;
        final int blue = color & 0xFF;

        addTextureQuad(buffer, sprite, x, y, z, width, height, length, face, red, green, blue, alpha, firstLightValue, secondLightValue);
    }

    public static void addTextureQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double z, double width, double height, double length, EnumFacing face, int red, int green, int blue, int alpha, int light1, int light2) {

        double minU;
        double maxU;
        double minV;
        double maxV;

        final double size = 16f;

        final double x2 = x + width;
        final double y2 = y + height;
        final double z2 = z + length;

        final double u = x % 1d;
        double u1 = u + width;

        while (u1 > 1f) u1 -= 1f;

        final double vy = y % 1d;
        double vy1 = vy + height;

        while (vy1 > 1f) vy1 -= 1f;

        final double vz = z % 1d;
        double vz1 = vz + length;

        while (vz1 > 1f) vz1 -= 1f;

        switch (face) {

            case DOWN:

            case UP:
                minU = sprite.getInterpolatedU(u * size);
                maxU = sprite.getInterpolatedU(u1 * size);
                minV = sprite.getInterpolatedV(vz * size);
                maxV = sprite.getInterpolatedV(vz1 * size);
                break;

            case NORTH:

            case SOUTH:
                minU = sprite.getInterpolatedU(u1 * size);
                maxU = sprite.getInterpolatedU(u * size);
                minV = sprite.getInterpolatedV(vy * size);
                maxV = sprite.getInterpolatedV(vy1 * size);
                break;

            case WEST:

            case EAST:
                minU = sprite.getInterpolatedU(vz1 * size);
                maxU = sprite.getInterpolatedU(vz * size);
                minV = sprite.getInterpolatedV(vy * size);
                maxV = sprite.getInterpolatedV(vy1 * size);
                break;

            default:
                minU = sprite.getMinU();
                maxU = sprite.getMaxU();
                minV = sprite.getMinV();
                maxV = sprite.getMaxV();
        }

        switch (face) {

            case DOWN:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                break;

            case UP:
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;

            case NORTH:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;

            case SOUTH:
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;

            case WEST:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;

            case EAST:
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
        }
    }

    public static void setupRenderState(double x, double y, double z) {

        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled()) GL11.glShadeModel(GL11.GL_SMOOTH);
        else GL11.glShadeModel(GL11.GL_FLAT);

        GlStateManager.translate(x, y, z);
    }

    public static void cleanupRenderState() {
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void render(TileEntityFilter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te != null) {
            FluidStack fluid = te.getFluidName();
            if (fluid != null) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                translateAgainstPlayer(te.getPos(), true);
                //GlStateManager.translate(x, y-1, z);
                renderFluid(te, fluid, te.getPos(), 0.00d, 0.00d, 0.00d, 0.1880D, 0.255D, 0.1880D, 0.8120D, 1.0D, 0.8120D);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
}
