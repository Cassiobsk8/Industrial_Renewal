package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityContainerShip;
import cassiokf.industrialrenewal.model.ships.ModelContainerShip;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderContainerShip extends RenderBoatBase<EntityContainerShip>
{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/cargo_ship.png");

    protected static final ModelContainerShip modelMinecart = new ModelContainerShip();

    public RenderContainerShip(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityContainerShip entity)
    {
        return TEXTURES;
    }
}
