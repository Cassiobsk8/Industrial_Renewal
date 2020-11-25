package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityCargoContainer;
import cassiokf.industrialrenewal.model.carts.ModelCargoContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCargoContainer<T extends EntityCargoContainer> extends RenderCartsBase<EntityCargoContainer>
{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/cargocontainer.png");

    protected ModelCargoContainer modelMinecart = new ModelCargoContainer();

    public RenderCargoContainer(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCargoContainer entity) {
        return TEXTURES;
    }
}
