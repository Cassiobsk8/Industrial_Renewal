package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit
{

    public static void registerEntities()
    {
        RegisterEntity("cargo_container", EntityCargoContainer.class, References.ENTITY_CARGOCONTAINER_ID, 80);
        RegisterEntity("fluid_container", EntityFluidContainer.class, References.ENTITY_FLUIDCONTAINER_ID, 80);
        RegisterEntity("steam_locomotive", EntitySteamLocomotive.class, References.ENTITY_STEAMLOCOMOTIVE_ID, 80);
        RegisterEntity("log_cart", EntityLogCart.class, References.ENTITY_LOGCART_ID, 80);
        RegisterEntity("passenger_car", EntityPassengerCar.class, References.ENTITY_PASSENGERCAR_ID, 80);
        RegisterEntity("minecart_flat", EntityFlatCart.class, References.ENTITY_FLATCART_ID, 80);
        RegisterEntity("cart_hopper", EntityHopperCart.class, References.ENTITY_HOPPERCART_ID, 80);
        RegisterEntity("tender", EntityTenderBase.class, References.ENTITY_TENDER_ID, 80);
        //RegisterEntity("container_ship", EntityContainerShip.class, References.ENTITY_CONTAINER_SHIP, 80);
    }

    private static void RegisterEntity(String name, Class<? extends Entity> entity, int id, int range)
    {
        EntityRegistry.registerModEntity(new ResourceLocation(References.MODID + ":" + name), entity, References.MODID + "." + name, id, IndustrialRenewal.instance, range, 1, true);
    }
}
