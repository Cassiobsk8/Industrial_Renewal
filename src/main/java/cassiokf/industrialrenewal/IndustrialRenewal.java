package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.entity.EntityInit;
import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.proxy.CommonProxy;
import cassiokf.industrialrenewal.recipes.ModRecipes;
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


@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, guiFactory = References.GUI_FACTORY)
public class IndustrialRenewal {

    @Mod.Instance(References.MODID)
    public static IndustrialRenewal instance;
    @SidedProxy(clientSide = "cassiokf.industrialrenewal.proxy.ClientProxy", serverSide = "cassiokf.industrialrenewal.proxy.CommonProxy", modId = References.MODID)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(References.NAME + " is loading preInit!");
        EntityInit.registerEntities();
        IRConfig.preInit();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println(References.NAME + " is loading init!");
        ModRecipes.init();
        IRSoundHandler.registerSounds();
        proxy.registerBlockRenderers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println(References.NAME + " is loading posInit!");

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