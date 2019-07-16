package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityBulkConveyor;
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
public class TESRBulkConveyor extends TileEntitySpecialRenderer<TileEntityBulkConveyor>
{

    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntityBulkConveyor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        ItemStack stack1 = te.getStackInSlot(0);
        ItemStack stack2 = te.getStackInSlot(1);
        ItemStack stack3 = te.getStackInSlot(2);

        if (!stack3.isEmpty())
        {
            doTheMath(te.getBlockFacing(), x, z, te.stack3Pos);
            RenderItem(te, stack3, xPos, y + te.stack3YPos, zPos);
        }
        if (!stack2.isEmpty())
        {
            doTheMath(te.getBlockFacing(), x, z, te.stack2Pos);
            RenderItem(te, stack2, xPos, y + te.stack2YPos, zPos);
        }
        if (!stack1.isEmpty())
        {
            doTheMath(te.getBlockFacing(), x, z, te.stack1Pos);
            RenderItem(te, stack1, xPos, y + te.stack1YPos, zPos);
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + 0.5;
                zPos = z - 0.02 - sidePlus;
                return;
            case NORTH:
                xPos = x + 0.5;
                zPos = z + 1.02 + sidePlus;
                return;
            case EAST:
                xPos = x - 0.02 - sidePlus;
                zPos = z + 0.5;
                return;
            case WEST:
                xPos = x + 1.02 + sidePlus;
                zPos = z + 0.5;
                return;
        }
    }

    /**
     * x = side / y = up / z = front
     */
    private void RenderItem(TileEntityBulkConveyor te, ItemStack item, double x, double y, double z)
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

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(item, te.getWorld(), null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(item, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        //RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }
}
