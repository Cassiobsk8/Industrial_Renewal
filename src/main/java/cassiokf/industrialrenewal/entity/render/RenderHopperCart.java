package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityHopperCart;
import cassiokf.industrialrenewal.model.carts.ModelCartHopper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderHopperCart<T extends EntityHopperCart> extends RenderCartsBase<EntityHopperCart>
{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/hopper_cart.png");

    protected ModelCartHopper modelMinecart = new ModelCartHopper();

    public RenderHopperCart(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHopperCart entity)
    {
        return TEXTURES;
    }
}
