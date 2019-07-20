package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntitySolarPanelFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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
public class TESRSolarPanelFrame extends TileEntitySpecialRenderer<TileEntitySolarPanelFrame>
{

    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntitySolarPanelFrame te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.hasPanel())
        {
            doTheMath(te.getBlockFacing(), x, z);
            RenderBlade(te, xPos, y + 0.45f, zPos);
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + 0.5;
                zPos = z + 0.6;
                return;
            case NORTH:
                xPos = x + 0.5;
                zPos = z + 0.4;
                return;
            case EAST:
                xPos = x + 0.6;
                zPos = z + 0.5;
                return;
            case WEST:
                xPos = x + 0.4;
                zPos = z + 0.5;
                return;
        }
    }

    /*
     * x = side / y = up / z = front
     */
    private void RenderBlade(TileEntitySolarPanelFrame te, double x, double y, double z)
    {
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        //RenderHelper.disableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        switch (te.getBlockFacing())
        {
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
            default:
                System.out.println("WHY: " + te.getBlockFacing());
                break;
        }
        GlStateManager.rotate(22.5f, 1, 0, 0);
        GlStateManager.scale(4.0f, 4.0f, 4.0f);

        ItemStack panel = te.getPanel();
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(panel, te.getWorld(), null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(panel, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        //RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }
}
