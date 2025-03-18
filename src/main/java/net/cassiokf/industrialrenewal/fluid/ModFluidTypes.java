package net.cassiokf.industrialrenewal.fluid;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

import java.awt.*;

public class ModFluidTypes {
    public static final ResourceLocation STEAM_STILL_RL = new ResourceLocation(IndustrialRenewal.MODID, "block/steam_still");
    public static final ResourceLocation STEAM_FLOWING_RL = new ResourceLocation(IndustrialRenewal.MODID, "block/steam_flow");
    public static final ResourceLocation SOAP_OVERLAY_RL = new ResourceLocation(IndustrialRenewal.MODID, "misc/in_soap_water");
    
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, IndustrialRenewal.MODID);
    
    public static final RegistryObject<FluidType> STEAM_FLUID_TYPE = register("steam", FluidType.Properties.create().density(-1000).temperature(380).viscosity(500));
    
    
    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new SteamFluidType(STEAM_STILL_RL, STEAM_FLOWING_RL, SOAP_OVERLAY_RL, Color.WHITE.getRGB(), new Vector3f(224f / 255f, 224f / 255f, 224f / 255f), properties));
    }
    
    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
