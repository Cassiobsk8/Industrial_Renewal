package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityWindTurbinePillar;
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
public class TESRWindTurbinePillar extends TileEntitySpecialRenderer<TileEntityWindTurbinePillar>
{

    private static ItemStack pointer = new ItemStack(ModItems.pointerLong);
    private static ItemStack limiter = new ItemStack(ModItems.limiter);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntityWindTurbinePillar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isBase())
        {
            doTheMath(te.getBlockFacing(), x, z, 0f);
            RenderString(te, xPos, y + 0.72, zPos);
            doTheMath(te.getBlockFacing(), x, z, 0.1f);
            RenderPointer(te, xPos, y + 0.845, zPos, pointer, te.getGenerationforGauge());
            RenderLimiter(te, xPos, y + 0.845, zPos, limiter, te.getPotentialValue());
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z + 0.22;
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + 0.78;
                return;
            case EAST:
                xPos = x + 0.22;
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + 0.78;
                zPos = z + (0.5 - sidePlus);
                return;
        }
    }

    /**
     * x = side / y = up / z = front
     */
    private void RenderString(TileEntityWindTurbinePillar te, double x, double y, double z)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        switch (te.getBlockFacing())
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getBlockFacing());
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
        GlStateManager.scale(0.006F, 0.006F, 1F);
        String st = te.getText();
        int xh = -Minecraft.getMinecraft().fontRenderer.getStringWidth(st) / 2;
        Minecraft.getMinecraft().fontRenderer.drawString(st, xh, 0, 0xFFFFFFFF);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void RenderPointer(TileEntityWindTurbinePillar te, double x, double y, double z, ItemStack item, float angle)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (te.getBlockFacing())
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
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }

    private void RenderLimiter(TileEntityWindTurbinePillar te, double x, double y, double z, ItemStack item, float angle)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (te.getBlockFacing())
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
        GlStateManager.scale(0.57F, 0.57F, 0.57F);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }
}
