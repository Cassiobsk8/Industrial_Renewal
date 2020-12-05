package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.model.carts.ModelSteamLocomotive;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

//@OnlyIn(Dist.CLIENT)
public class RenderSteamLocomotive<T extends EntitySteamLocomotive> extends RenderRotatableBase<EntitySteamLocomotive>
{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/steamlocomotive.png");

    protected ModelSteamLocomotive modelMinecart = new ModelSteamLocomotive();

    public RenderSteamLocomotive(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySteamLocomotive entity)
    {
        return TEXTURES;
    }
}
