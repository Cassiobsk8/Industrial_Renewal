package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntityPassengerCar;
import cassiokf.industrialrenewal.model.carts.ModelPassengerCar;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

//@OnlyIn(Dist.CLIENT)
public class RenderPassengerCar<T extends EntityPassengerCar> extends RenderRotatableBase<EntityPassengerCar> {

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/passenger_car.png");

    protected ModelPassengerCar modelMinecart = new ModelPassengerCar();

    public RenderPassengerCar(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ModelBase getModel()
    {
        return modelMinecart;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPassengerCar entity) {
        return TEXTURES;
    }
}
