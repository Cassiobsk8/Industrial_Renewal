package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cassiokf.industrialrenewal.References.MODID;


@Mod("industrialrenewal")
public class IndustrialRenewal
{

    public static IndustrialRenewal instance;
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    static
    {
        //FluidRegistry.enableUniversalBucket();
    }

    public IndustrialRenewal()
    {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info(References.NAME + " is loading preInit!");
        //FluidInit.registerFluids();
        IRSoundRegister.registerSounds();
        //EntityInit.registerEntities();
        //proxy.preInit();
        //NetworkHandler.init();
        //ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkManagerCallback());
        //proxy.registerRenderers();
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, IRConfig.COMMOM, "industrialrenewal.toml");
        //IRConfig.loadConfig(IRConfig.COMMOM, FMLPaths.CONFIGDIR.get().resolve("industrialrenewal.toml").toString());
        MinecraftForge.EVENT_BUS.register(IRSoundHandler.class);
        LOGGER.info("Done!");
    }

    private void init()
    {
        LOGGER.info(References.NAME + " is loading init!");
        //ModRecipes.init();
        //proxy.Init();

        //proxy.registerBlockRenderers();
        LOGGER.info("Done!");
    }

    private void clientRegistries(final FMLClientSetupEvent event)
    {
        //RenderHandler.registerEntitiesRender();
        //RenderHandler.registerCustomMeshesAndStates();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event)
        {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event)
        {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void onRegisterTileEntites(RegistryEvent.Register<TileEntityType<?>> event)
        {
            ModBlocks.registerTileEntity(event.getRegistry());
        }

        @SubscribeEvent
        public static void onEntityDrop(LivingDropsEvent event)
        {
            /*
            if (event.getEntity() instanceof EntityZombieVillager) {
                Random r = new Random();
                if (r.nextInt(100) < 25) {
                    //event.getEntity().dropItem(ModItems.instantNoodle, r.nextInt(2) + event.getLootingLevel());
                }
            }
             */
        }

        @SubscribeEvent
        public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(MODID))
            {
                //ConfigManager.sync(MODID, Config.Type.INSTANCE);
            }
        }
    }
}