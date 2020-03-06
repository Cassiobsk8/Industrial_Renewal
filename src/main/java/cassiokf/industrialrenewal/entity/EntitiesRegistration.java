package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.entity.render.RenderCargoContainer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static cassiokf.industrialrenewal.References.MODID;

public class EntitiesRegistration
{

    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MODID);
    public static final RegistryObject<EntityType<EntityCargoContainer>> CARGO_CONTAINER = ENTITIES.register("cargo_container", () -> EntityType.Builder.<EntityCargoContainer>create(EntityCargoContainer::new, EntityClassification.MISC)
            .size(1, 1)
            .build("cargo_container"));

    public static void registerModels(FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitiesRegistration.CARGO_CONTAINER.get(), RenderCargoContainer::new);
    }

    public static void init()
    {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //public static void registerEntities() {
    //RegisterEntity("cargo_container", EntityCargoContainer.class, References.ENTITY_CARGOCONTAINER_ID, 80);
    //RegisterEntity("fluid_container", EntityFluidContainer.class, References.ENTITY_FLUIDCONTAINER_ID, 80);
    //RegisterEntity("steam_locomotive", EntitySteamLocomotive.class, References.ENTITY_STEAMLOCOMOTIVE_ID, 80);
    //RegisterEntity("log_cart", EntityLogCart.class, References.ENTITY_LOGCART_ID, 80);
    //RegisterEntity("passenger_car", EntityPassengerCar.class, References.ENTITY_PASSENGERCAR_ID, 80);
    //RegisterEntity("minecart_flat", EntityFlatCart.class, References.ENTITY_FLATCART_ID, 80);
    //RegisterEntity("cart_hopper", EntityHopperCart.class, References.ENTITY_HOPPERCART_ID, 80);
    //}
}
