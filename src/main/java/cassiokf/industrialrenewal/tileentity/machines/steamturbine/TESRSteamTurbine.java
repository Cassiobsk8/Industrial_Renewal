package cassiokf.industrialrenewal.tileentity.machines.steamturbine;

import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRSteamTurbine extends TileEntitySpecialRenderer<TileEntitySteamTurbine>
{

    private static ItemStack pointer = new ItemStack(ModItems.pointer);
    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntitySteamTurbine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.isMaster())
        {
            //STEAM
            doTheMath(te.getBlockFacing(), x, z, -1.1);
            RenderText(te, xPos, y + 1.25, zPos, te.getSteamText());
            RenderPointer(te, xPos, y + 1.5, zPos, te.GetSteamFill());
            //GENERATION
            doTheMath(te.getBlockFacing(), x, z, -1.1);
            RenderText(te, xPos, y + 0.5, zPos, te.getGenerationText());
            RenderPointer(te, xPos, y + 0.76, zPos, te.GetGenerationFill());
            //WATER
            doTheMath(te.getBlockFacing(), x, z, -1.1);
            RenderText(te, xPos, y - 0.25, zPos, te.getWaterText());
            RenderPointer(te, xPos, y + 0.01, zPos, te.GetWaterFill());
            //ROTATION
            doTheMath(te.getBlockFacing(), x, z, 0);
            RenderText(te, xPos, y + 1.25, zPos, te.getRotationText());
            RenderPointer(te, xPos, y + 1.5, zPos, te.getRotationFill());
            //ENERGY
            doTheMath(te.getBlockFacing(), x, z, +1.155);
            RenderText(te, xPos, y + 0.18, zPos, te.getEnergyText());
            RenderPointer(te, xPos, y + 0.45, zPos, te.GetEnergyFill());
        }
    }

    private void doTheMath(EnumFacing facing, double x, double z, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z - 0.96;
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + 1.96;
                return;
            case EAST:
                xPos = x - 0.96;
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + 1.96;
                zPos = z + (0.5 - sidePlus);
                return;
        }
    }

    /**
     * x = side / y = up / z = front
     */
    private void RenderText(TileEntitySteamTurbine te, double x, double y, double z, String st)
    {

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
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.scale(0.01F, 0.01F, 1F);
        int xh = -Minecraft.getMinecraft().fontRenderer.getStringWidth(st) / 2;
        Minecraft.getMinecraft().fontRenderer.drawString(st, xh, 0, 0xFFFFFFFF);
        GlStateManager.popMatrix();
    }

    private void RenderPointer(TileEntitySteamTurbine te, double x, double y, double z, float angle)
    {

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
        GlStateManager.scale(0.3F, 0.3F, 0.3F);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }
}
