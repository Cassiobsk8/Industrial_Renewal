package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntitySteamBoiler;
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
public class TESRSteamBoiler extends TileEntitySpecialRenderer<TileEntitySteamBoiler>
{

    private static ItemStack pointer = new ItemStack(ModItems.pointer);
    private static ItemStack fire = new ItemStack(ModItems.fire);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntitySteamBoiler te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            EnumFacing facing = te.getMasterFacing();
            //WATER
            doTheMath(facing, x, z, -0.69);
            RenderText(facing, xPos, y + 0.25, zPos, te.getWaterText());
            RenderPointer(facing, xPos, y + 0.51, zPos, te.GetWaterFill());
            //STEAM
            doTheMath(facing, x, z, 0.69);
            RenderText(facing, xPos, y + 0.25, zPos, te.getSteamText());
            RenderPointer(facing, xPos, y + 0.51, zPos, te.GetSteamFill());
            //ENERGY
            doTheMath(facing, x, z, 0);
            RenderText(facing, xPos, y + 0.18, zPos, te.getFuelText());
            RenderPointer(facing, xPos, y + 0.44, zPos, te.getFuelFill());
            //HEAT
            doTheMath(facing, x, z, 0);
            RenderText(facing, xPos, y + 0.93, zPos, te.getHeatText());
            RenderPointer(facing, xPos, y + 1.19, zPos, te.getHeatFill());
            //Fire
            if (te.getType() > 0 && te.getFuelTime() > 0)
            {
                doTheMath(facing, x, z, 0);
                RenderFire(te, xPos, y - 0.7, zPos);
            }
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z - 0.9;
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + 1.9;
                return;
            case EAST:
                xPos = x - 0.9;
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + 1.9;
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
        GlStateManager.scale(0.01F, 0.01F, 1F);
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
        GlStateManager.scale(0.3F, 0.3F, 0.3F);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }

    private void RenderFire(TileEntitySteamBoiler te, double x, double y, double z)
    {
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
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

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(fire, te.getWorld(), null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(fire, model);

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }
}
