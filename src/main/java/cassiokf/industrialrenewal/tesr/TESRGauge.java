package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityGauge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRGauge extends TileEntitySpecialRenderer<TileEntityGauge>
{

    private static ItemStack pointer = new ItemStack(ModItems.pointer);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntityGauge te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        doTheMath(te.getGaugeFacing(), x, z);
        RenderFluidName(te, xPos, y + 0.2, zPos);
        RenderPointer(te, xPos, y + 0.5, zPos);
    }

    private void doTheMath(EnumFacing facing, double x, double z)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + 0.5;
                zPos = z + 0.4;
                return;
            case NORTH:
                xPos = x + 0.5;
                zPos = z + 0.6;
                return;
            case EAST:
                xPos = x + 0.4;
                zPos = z + 0.5;
                return;
            case WEST:
                xPos = x + 0.6;
                zPos = z + 0.5;
                return;
        }
    }

    /**
     * x = side / y = up / z = front
     */
    private void RenderFluidName(TileEntityGauge te, double x, double y, double z)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        switch (te.getGaugeFacing())
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getBaseFacing());
                break;
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
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.scale(0.01F, 0.01F, 1F);
        String st = te.GetText();
        int xh = -Minecraft.getMinecraft().fontRenderer.getStringWidth(st) / 2;
        Minecraft.getMinecraft().fontRenderer.drawString(st, xh, 0, 0xFFFFFFFF);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void RenderPointer(TileEntityGauge te, double x, double y, double z)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (te.getGaugeFacing())
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getBaseFacing());
                break;
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
        GlStateManager.scale(0.3F, 0.3F, 0.3F);
        GlStateManager.rotate(90, 0, 0, 1);
        float angle = te.GetTankFill();
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }
}
