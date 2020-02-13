package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.fluids.FluidSteam;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(References.MODID)
public class FluidInit
{
    //public static final Fluid STEAM = new FluidSteam("steam", new ResourceLocation(References.MODID + ":blocks/steam_still"), new ResourceLocation(References.MODID + ":blocks/steam_flow"));
    public static final FluidSteam.Source STEAM = null;
    public static final FluidSteam.Flowing FLOWING_STEAM = null;

    public static void registerFluids(IForgeRegistry<Fluid> registry)
    {
        final Fluid[] fluids =
                {
                        new FluidSteam.Flowing().setRegistryName(References.MODID, "flowing_steam"),
                        new FluidSteam.Source().setRegistryName(References.MODID, "steam")
                };
        registry.registerAll(fluids);
    }

}
