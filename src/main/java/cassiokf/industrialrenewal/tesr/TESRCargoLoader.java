package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
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
public class TESRCargoLoader extends TileEntitySpecialRenderer<TileEntityCargoLoader>
{
    private static ItemStack pointer = new ItemStack(ModItems.pointer);
    private static ItemStack arm = new ItemStack(ModItems.tambor);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntityCargoLoader te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getBlockFacing();

            double armX = x + 0.5;
            double armZ = z + 0.5;
            if (facing == EnumFacing.SOUTH) armZ += 1;
            if (facing == EnumFacing.NORTH) armZ -= 1;
            if (facing == EnumFacing.EAST) armX += 1;
            if (facing == EnumFacing.WEST) armX -= 1;
            //doTheMath(facing, x, z, 0);
            RenderArm(te, armX, y + 0.2f, armZ);

            doTheMath(facing, x, z, 0);
            RenderText(facing, xPos, y + 0.93, zPos, te.getModeText());

            doTheMath(facing, x, z, 0);
            RenderText(facing, xPos, y + 0.05, zPos, te.getTankText());
            RenderPointer(facing, xPos, y + 0.26, zPos, te.getCartFluidAngle());
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z - 0.03;
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + 1.03;
                return;
            case EAST:
                xPos = x - 0.03;
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + 1.03;
                zPos = z + (0.5 - sidePlus);
                return;
        }
    }

    /*
     * x = side / y = up / z = front
     */
    private void RenderText(EnumFacing facing, double x, double y, double z, String st)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        switch (facing)
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
        }
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.scale(0.006F, 0.006F, 1F);
        int xh = -Minecraft.getMinecraft().fontRenderer.getStringWidth(st) / 2;
        Minecraft.getMinecraft().fontRenderer.drawString(st, xh, 0, 0xFFFFFFFF);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void RenderPointer(EnumFacing facing, double x, double y, double z, float angle)
    {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (facing)
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
        }
        GlStateManager.scale(0.2F, 0.2F, 0.2F);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }

    private void RenderArm(TileEntityCargoLoader te, double x, double y, double z)
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
        }

        GlStateManager.scale(2.08f, 2.08f, 2.08f);

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(arm, te.getWorld(), null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(arm, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        //RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }
}
