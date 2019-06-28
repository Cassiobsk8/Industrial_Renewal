package cassiokf.industrialrenewal.tesr;

import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRFirstAidKit extends TileEntitySpecialRenderer<TileEntityFirstAidKit> {

    private static World world = Minecraft.getMinecraft().world;

    private double xPos = 0D;
    private double zPos = 0D;

    @Override
    public void render(TileEntityFirstAidKit te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        doTheMath(te.getFaceDirection(), x, z);
        doRender(te, xPos, y + 0.5, zPos);
        doRender2(te, xPos, y + 0.25, zPos);
    }

    private void doTheMath(EnumFacing facing, double x, double z) {
        switch (facing) {
            /**TODO SOUTH and NORTH are backwards */
            case SOUTH:
                xPos = x + 0.68;
                zPos = z + 0.8;
                return;
            case NORTH:
                xPos = x + 0.32;
                zPos = z + 0.2;
                return;
            case EAST:
                xPos = x + 0.8;
                zPos = z + 0.69;
                return;
            case WEST:
                xPos = x + 0.2;
                zPos = z + 0.33;
                return;
        }
    }

    /**
     * x = side / y = up / z = front
     */
    private void doRender(TileEntityFirstAidKit te, double x, double y, double z) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (te.getFaceDirection()) {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getFaceDirection());
                break;
            case SOUTH:
                break;
            case NORTH:
                GlStateManager.rotate(180F, 0, 1, 0);
                break;
            case WEST:
                GlStateManager.rotate(90F, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(-90F, 0, 1, 0);
                break;
        }
        GlStateManager.scale(0.2F, 0.2F, 0.2F);
        for (int i = 0; i < 4; i++) {
            ItemStack stack = new ItemStack(te.inventory.getStackInSlot(i).getItem());

            if (!stack.isEmpty()) {
                EntityItem item = new EntityItem(world, 0, 0, 0, stack);
                item.hoverStart = 0F;
                Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0F, 0F, false);
            }
            GlStateManager.translate(-0.6, 0, 0);
        }
        GlStateManager.popMatrix();
    }

    private void doRender2(TileEntityFirstAidKit te, double x, double y, double z) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        switch (te.getFaceDirection()) {
            default:
                System.out.println("DEU BOSTA AKI TIO: " + te.getFaceDirection());
                break;
            case SOUTH:
                break;
            case NORTH:
                GlStateManager.rotate(180F, 0, 1, 0);
                break;
            case WEST:
                GlStateManager.rotate(90F, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(-90F, 0, 1, 0);
                break;
        }
        GlStateManager.scale(0.2F, 0.2F, 0.2F);
        for (int i = 4; i < 8; i++) {
            ItemStack stack = new ItemStack(te.inventory.getStackInSlot(i).getItem());

            if (!stack.isEmpty()) {
                EntityItem item = new EntityItem(world, 0, 0, 0, stack);
                item.hoverStart = 0F;
                Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0F, 0F, false);
            }
            GlStateManager.translate(-0.6, 0, 0);
        }
        GlStateManager.popMatrix();
    }
}
