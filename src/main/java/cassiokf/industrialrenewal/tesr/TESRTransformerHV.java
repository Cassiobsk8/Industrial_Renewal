package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
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
public class TESRTransformerHV extends TileEntitySpecialRenderer<TileEntityTransformerHV>
{
    private static ItemStack pointerLong = new ItemStack(ModItems.pointerLong);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntityTransformerHV te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            //CABLE RENDER
            if (((IConnectorHV) te).getLeftOrCentralConnection() != null)
            {
                TESRWire.renderWire(te, ((IConnectorHV) te).getLeftOrCentralConnection(), x, y + 1.4D, z);
            }

            EnumFacing facing = te.getMasterFacing();
            //GENERATION
            doTheMath(facing, x, z, 0);
            RenderText(facing, xPos, y + 0.16, zPos, te.getGenerationText());
            doTheMathLong(facing, x, z, 0.13);
            RenderLongPointer(facing, xPos, y + 0.36, zPos, te.getGenerationFill());
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z - 0.86;
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + 1.86;
                return;
            case EAST:
                xPos = x - 0.86;
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + 1.86;
                zPos = z + (0.5 - sidePlus);
                return;
        }
    }

    private void doTheMathLong(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z - 0.84;
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + 1.84;
                return;
            case EAST:
                xPos = x - 0.84;
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + 1.84;
                zPos = z + (0.5 - sidePlus);
                return;
        }
    }

    /**
     * x = side / y = up / z = front
     */
    private void RenderText(EnumFacing facing, double x, double y, double z, String st)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        switch (facing)
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + facing);
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
        GlStateManager.scale(0.008F, 0.008F, 1F);
        int xh = -Minecraft.getMinecraft().fontRenderer.getStringWidth(st) / 2;
        Minecraft.getMinecraft().fontRenderer.drawString(st, xh, 0, 0xFFFFFFFF);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void RenderLongPointer(EnumFacing facing, double x, double y, double z, float angle)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (facing)
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + facing);
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
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointerLong, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }
}
