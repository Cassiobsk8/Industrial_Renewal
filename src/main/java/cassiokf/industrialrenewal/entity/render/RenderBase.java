package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public abstract class RenderBase<T extends Entity> extends Render<T>
{
    private static final ItemStack pointer = new ItemStack(ModItems.pointer);

    public RenderBase(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public abstract ModelBase getModel();

    public void renderText(String text, double x, double y, double z)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.1F, 0.1F, 0.1F);
        GlStateManager.scale(0.07F, 0.07F, 1F);
        //RenderHelper.disableStandardItemLighting();
        int xh = -Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2;
        GlStateManager.translate(x, y, z);
        Minecraft.getMinecraft().fontRenderer.drawString(text, xh, 0, 0xFFFFFFFF);
        //RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void renderPointer(float angle, double x, double y, double z)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(0.15F, 0.15F, 1.0F);
        GlStateManager.rotate(-90, 0, 0, 1);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.rotate(-angle, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();
    }

    public void renderExtra(T entity, double x, double y, double z, float entityYaw, float partialTicks, float f, float f1, float f2)
    {}

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
