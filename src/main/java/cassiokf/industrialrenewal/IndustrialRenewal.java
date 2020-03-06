package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.entity.EntitiesRegistration;
import cassiokf.industrialrenewal.init.*;
import cassiokf.industrialrenewal.model.ModelLoaderCustom;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("industrialrenewal")
public class IndustrialRenewal
{
    public static final String MODID = References.MODID;
    public static IndustrialRenewal instance;
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public IndustrialRenewal()
    {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);

        SoundsRegistration.init();
        ItemsRegistration.init();
        BlocksRegistration.init();
        TileRegistration.init();
        FluidsRegistration.init();
        EntitiesRegistration.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, IRConfig.COMMON_SPEC, References.MODID + ".toml");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info(References.NAME + " setup Start");

    }

    private void clientRegistries(final FMLClientSetupEvent event)
    {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(References.MODID, "smartmodel"), new ModelLoaderCustom());

        MinecraftForge.EVENT_BUS.register(IRSoundHandler.class);

        EntitiesRegistration.registerModels(event);

        RenderTypeLookup.setRenderLayer(BlocksRegistration.WINDOW.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.EFENCE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.LIGHT.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.ENERGYLEVEL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.FLAMEDETECTOR.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.SIGNALINDICATOR.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.STEAMTURBINE.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.FIRSTAIDKIT.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.RECORDPLAYER.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.BATTERYBANK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.BIGFENCECOLUMN.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.BIGFENCECORNER.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksRegistration.FLOORLAMP.get(), RenderType.getCutout());
    }
}