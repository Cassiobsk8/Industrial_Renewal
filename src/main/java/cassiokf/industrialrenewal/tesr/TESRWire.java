package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityWireBase;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class TESRWire extends TileEntitySpecialRenderer<TileEntityWireBase>
{
    public static void renderWire(BlockPos startPos, BlockPos endTE, double x, double y, double z)
    {
        y = y - 0.9D;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        Color c = new Color(56, 56, 56, 255);
        Color c2 = new Color(43, 43, 43, 255);

        double d6 = endTE.getX() + 0.4D;
        double d7 = endTE.getY();
        double d8 = endTE.getZ() + 0.4D;
        double d9 = (Math.PI / 2D);
        double d2 = Math.cos(d9) * 0.4D;
        double d3 = Math.sin(d9) * 0.4D;
        double d10 = startPos.getX() + d2;
        double d11 = startPos.getY();
        double d12 = startPos.getZ() + d3;
        x = x + 0.5D;
        z = z + 0.5D;
        double d13 = d6 - d10;
        double d14 = d7 - (d11 - 1.3D);
        double d15 = d8 - d12;
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();

        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

        int i = 24;

        for (int j = 0; j <= i; ++j)
        {
            if (j < (i / 2) + 1)
            {
                float f = Utils.normalize(c.getRed(), 0, 255);
                float f1 = Utils.normalize(c.getGreen(), 0, 255);
                float f2 = Utils.normalize(c.getBlue(), 0, 255);

                if (j % 2 == 0)
                {
                    f = Utils.normalize(c2.getRed(), 0, 255);
                    f1 = Utils.normalize(c2.getGreen(), 0, 255);
                    f2 = Utils.normalize(c2.getBlue(), 0, 255);
                }

                float f3 = (float) j / (float) i;
                double v = d14 * (f3 * f3 + f3) * 0.5D;
                double b = (((float) i - j) / 18.0F + 0.125F);

                bufferbuilder.pos(x + d13 * f3, y + v + b, z + d15 * f3)
                        .color(f, f1, f2, c.getTransparency()).endVertex();
                bufferbuilder.pos(x + d13 * f3 + 0.025D, y + v + b + 0.025D, z + d15 * f3)
                        .color(f, f1, f2, c.getTransparency()).endVertex();
            }
        }

        tessellator.draw();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

        for (int k = 0; k <= i; ++k)
        {
            if (k < (i / 2) + 1)
            {
                float f4 = Utils.normalize(c.getRed(), 0, 255);
                float f5 = Utils.normalize(c.getGreen(), 0, 255);
                float f6 = Utils.normalize(c.getBlue(), 0, 255);

                if (k % 2 == 0)
                {
                    f4 = Utils.normalize(c2.getRed(), 0, 255);
                    f5 = Utils.normalize(c2.getGreen(), 0, 255);
                    f6 = Utils.normalize(c2.getBlue(), 0, 255);
                }

                float f7 = (float) k / (float) i;
                double v = d14 * (f7 * f7 + f7) * 0.5D;
                double b = (((float) i - k) / 18.0F + 0.125F);

                bufferbuilder.pos(x + d13 * f7, y + v + b + 0.025D, z + d15 * f7)
                        .color(f4, f5, f6, c.getTransparency()).endVertex();
                bufferbuilder.pos(x + d13 * f7 + 0.025D, y + v + b, z + d15 * f7 + 0.025D)
                        .color(f4, f5, f6, c.getTransparency()).endVertex();
            }
        }

        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
    }

    @Override
    public void render(TileEntityWireBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isRightConnected())
        {
            renderWire(te.getPos(), te.rightConnectionPos, x, y, z);
        }
        if (te.isLeftConnected())
        {
            renderWire(te.getPos(), te.leftConnectionPos, x, y, z);
        }
    }

    /**
     * Gets the value between start and end according to pct
     */
    private double interpolateValue(double start, double end, double pct)
    {
        return start + (end - start) * pct;
    }

}
