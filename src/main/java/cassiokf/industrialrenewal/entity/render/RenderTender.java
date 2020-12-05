package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityTenderBase;
import cassiokf.industrialrenewal.model.carts.ModelTender;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderTender extends RenderRotatableBase<EntityTenderBase>
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/tender.png");

    protected final ModelTender modelMinecart = new ModelTender();

    public RenderTender(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTenderBase entity)
    {
        return TEXTURES;
    }
}
