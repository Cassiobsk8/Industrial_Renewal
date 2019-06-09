package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.fluids.FluidSteam;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidInit
{
    public static final Fluid STEAM = new FluidSteam("steam", new ResourceLocation(References.MODID + ":blocks/steam_still"), new ResourceLocation(References.MODID + ":blocks/steam_flow"));

    public static void registerFluids()
    {
        registerFluids(STEAM);
    }

    public static void registerFluids(Fluid fluid)
    {
        FluidRegistry.registerFluid(fluid);
        FluidRegistry.addBucketForFluid(fluid);
    }
}
