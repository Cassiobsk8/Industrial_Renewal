package cassiokf.industrialrenewal.tileentity.firstaidkit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class TESRFirstAidKit extends TileEntitySpecialRenderer<TileEntityFirstAidKit> {


    private static World world = Minecraft.getMinecraft().world;

    @Override
    public void render(TileEntityFirstAidKit te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        doRender(te, x + 0.68, y + 0.5, z + 0.8);
        doRender2(te, x + 0.68, y + 0.25, z + 0.8);
    }

    /**
     * x = side / y = up / z = front
     */
    private void doRender(TileEntityFirstAidKit te, double x, double y, double z) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
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
