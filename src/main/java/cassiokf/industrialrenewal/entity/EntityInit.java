package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {

    public static void registerEntities() {
        RegisterEntity("cargo_container", EntityCargoContainer.class, References.ENTITY_CARGOCONTAINER_ID, 80);
        RegisterEntity("steam_locomotive", EntitySteamLocomotive.class, References.ENTITY_STEAMLOCOMOTIVE_ID, 80);
    }

    private static void RegisterEntity(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(References.MODID + ":" + name), entity, References.MODID + "." + name, id, IndustrialRenewal.instance, range, 1, true);
    }
}
