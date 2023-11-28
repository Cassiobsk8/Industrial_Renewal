package net.cassiokf.industrialrenewal.init;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.fluid.ModFluidTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, IndustrialRenewal.MODID);
    
    public static final RegistryObject<FlowingFluid> STEAM =
            FLUIDS.register("steam_still", ()-> new ForgeFlowingFluid.Source(ModFluids.STEAM_PROPERTIES));
    
    public static final RegistryObject<FlowingFluid> STEAM_FLOWING =
            FLUIDS.register("steam_flow", ()-> new ForgeFlowingFluid.Flowing(ModFluids.STEAM_PROPERTIES));
    
    public static final ForgeFlowingFluid.Properties STEAM_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.STEAM_FLUID_TYPE, STEAM, STEAM_FLOWING)
            .levelDecreasePerBlock(2).slopeFindDistance(4).explosionResistance(100f).tickRate(5)
            .block(ModFluids.STEAM_BLOCK);
    
    public static final RegistryObject<LiquidBlock> STEAM_BLOCK = ModBlocks.BLOCKS.register("steam",
            ()-> new LiquidBlock(ModFluids.STEAM, BlockBehaviour.Properties.copy(Blocks.WATER)
                    .strength(100f)){
            });
    
    public static void register(IEventBus e){
        FLUIDS.register(e);
    }
}
