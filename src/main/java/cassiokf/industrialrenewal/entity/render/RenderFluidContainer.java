package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityFluidContainer;
import cassiokf.industrialrenewal.model.carts.ModelCartFluidTank;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderFluidContainer<T extends EntityFluidContainer> extends RenderCartsBase<EntityFluidContainer>
{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/fluid_container.png");

    protected ModelCartFluidTank modelMinecart = new ModelCartFluidTank();

    public RenderFluidContainer(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    public void renderExtra(EntityFluidContainer entity, double x, double y, double z, float entityYaw, float partialTicks, float f, float f1, float f2)
    {
        GlStateManager.pushMatrix();

        renderText(entity.GetText(), f, f1 - 50, f2 - 5.4);
        renderPointer(entity.GetTankFill(), f, f1 - 0.55, f2 - 0.5);
        GlStateManager.rotate(180, 0, 1, 0);
        renderText(entity.GetText(), f, f1 - 50, f2 - 5.4);
        renderPointer(entity.GetTankFill(), f, f1 - 0.55, f2 - 0.5);

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFluidContainer entity) {
        return TEXTURES;
    }
}
