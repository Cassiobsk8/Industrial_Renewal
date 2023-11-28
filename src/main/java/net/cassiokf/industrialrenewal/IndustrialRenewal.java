package net.cassiokf.industrialrenewal;

import com.mojang.logging.LogUtils;
import net.cassiokf.industrialrenewal.ber.*;
import net.cassiokf.industrialrenewal.fluid.ModFluidTypes;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.init.ModFluids;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.tab.ModCreativeTabs;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(IndustrialRenewal.MODID)
public class IndustrialRenewal {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "industrialrenewal";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public IndustrialRenewal() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModBlockEntity.registerInit(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
    
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.STEAM.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.STEAM_FLOWING.get(), RenderType.translucent());
        }
        
        @SubscribeEvent
        public static void initClient(EntityRenderersEvent.RegisterRenderers event) {
            Utils.sendConsoleMessage("Industrial Renewal == Registering BlockEntityRenderer");
            event.registerBlockEntityRenderer(ModBlockEntity.BATTERY_BANK.get(), BERBatteryBank::new);
            event.registerBlockEntityRenderer(ModBlockEntity.SOLAR_PANEL_FRAME.get(), BERSolarPanelFrame::new);
            event.registerBlockEntityRenderer(ModBlockEntity.ENERGY_LEVEL.get(), BEREnergyLevel::new);
            event.registerBlockEntityRenderer(ModBlockEntity.FLUID_GAUGE.get(), BERFluidGauge::new);
            event.registerBlockEntityRenderer(ModBlockEntity.ISOLATOR_TILE.get(), BERWire::new);
            event.registerBlockEntityRenderer(ModBlockEntity.TRANSFORMER_TILE.get(), BERTransformerHV::new);
            event.registerBlockEntityRenderer(ModBlockEntity.INDUSTRIAL_BATTERY_TILE.get(), BERIndustrialBatteryBank::new);
            event.registerBlockEntityRenderer(ModBlockEntity.FLUID_TANK_TILE.get(), BERFluidTank::new);
            event.registerBlockEntityRenderer(ModBlockEntity.DAM_TURBINE_TILE.get(), BERDamTurbine::new);
            event.registerBlockEntityRenderer(ModBlockEntity.DAM_GENERATOR.get(), BERDamGenerator::new);
            event.registerBlockEntityRenderer(ModBlockEntity.STEAM_BOILER_TILE.get(), BERSteamBoiler::new);
            event.registerBlockEntityRenderer(ModBlockEntity.PORTABLE_GENERATOR_TILE.get(), BERPortableGenerator::new);
            event.registerBlockEntityRenderer(ModBlockEntity.STEAM_TURBINE_TILE.get(), BERSteamTurbine::new);
            event.registerBlockEntityRenderer(ModBlockEntity.MINER_TILE.get(), BERMining::new);
        }
    }
}
