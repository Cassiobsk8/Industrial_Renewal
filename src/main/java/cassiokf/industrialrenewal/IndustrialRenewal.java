package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.entity.EntityInit;
import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.network.PacketRequestUpdateFirstAidKit;
import cassiokf.industrialrenewal.network.PacketUpdateFirstAidKit;
import cassiokf.industrialrenewal.proxy.CommonProxy;
import cassiokf.industrialrenewal.recipes.ModRecipes;
import cassiokf.industrialrenewal.util.GUIHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, guiFactory = References.GUI_FACTORY, updateJSON = References.VERSION_CHECKER_URL)
public class IndustrialRenewal {

    public static SimpleNetworkWrapper network;

    @Mod.Instance(References.MODID)
    public static IndustrialRenewal instance;
    @SidedProxy(clientSide = "cassiokf.industrialrenewal.proxy.ClientProxy", serverSide = "cassiokf.industrialrenewal.proxy.CommonProxy", modId = References.MODID)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(References.NAME + " is loading preInit!");
        IRSoundHandler.registerSounds();
        EntityInit.registerEntities();
        IRConfig.preInit();
        proxy.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(IndustrialRenewal.instance, new GUIHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID);
        network.registerMessage(new PacketUpdateFirstAidKit.Handler(), PacketUpdateFirstAidKit.class, 0, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdateFirstAidKit.Handler(), PacketRequestUpdateFirstAidKit.class, 1, Side.SERVER);

        proxy.registerRenderers();
        System.out.println("Done!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println(References.NAME + " is loading init!");
        ModRecipes.init();
        proxy.Init();
        proxy.registerBlockRenderers();
        System.out.println("Done!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println(References.NAME + " is loading posInit!");
        System.out.println("Done!");
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerItemModels();
        }
    }
}