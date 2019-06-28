package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntitySmallWindTurbine;
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
public class TESRSmallWindTurbine extends TileEntitySpecialRenderer<TileEntitySmallWindTurbine>
{

    private static ItemStack blade = new ItemStack(ModItems.windBlade);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntitySmallWindTurbine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.hasBlade())
        {
            doTheMath(te.getBlockFacing(), x, z);
            RenderBlade(te, xPos, y + 0.5f, zPos);
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + 0.5;
                zPos = z + 0.7;
                return;
            case NORTH:
                xPos = x + 0.5;
                zPos = z + 0.3;
                return;
            case EAST:
                xPos = x + 0.7;
                zPos = z + 0.5;
                return;
            case WEST:
                xPos = x + 0.3;
                zPos = z + 0.5;
                return;
        }
    }

    /*
     * x = side / y = up / z = front
     */
    private void RenderBlade(TileEntitySmallWindTurbine te, double x, double y, double z)
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
        GlStateManager.rotate(90, 1, 0, 0);
        GlStateManager.scale(6.0f, 6.0f, 6.0f);

        GlStateManager.rotate(te.getRotation(), 0, 1, 0);

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(blade, te.getWorld(), null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(blade, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        //RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }
}
