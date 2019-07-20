package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityMining;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TESRMining extends TileEntitySpecialRenderer<TileEntityMining>
{
    private static ItemStack pointer = new ItemStack(ModItems.pointer);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntityMining te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            if (te.hasDrill()) RenderDrill(te, x + 0.5, y - 0.9f - te.getSlide(), z + 0.5);
            EnumFacing facing = te.getMasterFacing();
            doTheMath(facing, x, z, -1.14);
            RenderText(te, xPos, y + 0.18, zPos, te.getEnergyText(1));
            RenderText(te, xPos, y + 0.07, zPos, te.getEnergyText(2));
            RenderPointer(te, xPos, y + 0.43, zPos, te.getEnergyFill());

            doTheMath(facing, x, z, +1.16);
            RenderText(te, xPos, y + 0.18, zPos, te.getWaterText(1));
            RenderText(te, xPos, y + 0.07, zPos, te.getWaterText(2));
            RenderPointer(te, xPos, y + 0.43, zPos, te.getWaterFill());

            doTheMath(facing, x, z, 0);
            RenderText(te, xPos, y + 1.3, zPos, te.getHeatText());
            RenderPointer(te, xPos, y + 1.57, zPos, te.getHeatFill());
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z - 0.95;
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + 1.95;
                return;
            case EAST:
                xPos = x - 0.95;
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + 1.95;
                zPos = z + (0.5 - sidePlus);
                return;
        }
    }

    /*
     * x = side / y = up / z = front
     */
    private void RenderText(TileEntityMining te, double x, double y, double z, String st)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        switch (te.getMasterFacing())
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getMasterFacing());
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

    private void RenderPointer(TileEntityMining te, double x, double y, double z, float angle)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (te.getMasterFacing())
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getMasterFacing());
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
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }

    private void RenderDrill(TileEntityMining te, double x, double y, double z)
    {
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        //RenderHelper.disableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        switch (te.getMasterFacing())
        {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getMasterFacing());
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

        GlStateManager.scale(4.5f, 4.5f, 4.5f);

        GlStateManager.rotate(te.getRotation(), 0, 1, 0);

        ItemStack drill = te.getDrill();
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(drill, te.getWorld(), null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(drill, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        //RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }
}
