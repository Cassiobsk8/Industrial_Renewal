package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityFlatCart;
import cassiokf.industrialrenewal.model.carts.ModelCartFlat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderFlatCart<T extends EntityFlatCart> extends RenderCartsBase<EntityFlatCart>
{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/base_cart.png");

    protected final ModelCartFlat modelMinecart = new ModelCartFlat();

    public RenderFlatCart(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFlatCart entity)
    {
        return TEXTURES;
    }
}
