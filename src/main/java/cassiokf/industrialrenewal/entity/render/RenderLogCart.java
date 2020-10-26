package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityLogCart;
import cassiokf.industrialrenewal.model.carts.ModelLogCart;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLogCart<T extends EntityLogCart> extends RenderBase<EntityLogCart> {

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/log_cart.png");

    protected ModelLogCart modelMinecart = new ModelLogCart();

    public RenderLogCart(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLogCart entity) {
        return TEXTURES;
    }
}
