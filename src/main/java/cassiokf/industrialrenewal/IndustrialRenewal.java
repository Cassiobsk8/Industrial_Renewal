package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.ChunkManagerCallback;
import cassiokf.industrialrenewal.handlers.EventHandler;
import cassiokf.industrialrenewal.init.*;
import cassiokf.industrialrenewal.proxy.CommonProxy;
import cassiokf.industrialrenewal.recipes.ModRecipes;
import cassiokf.industrialrenewal.world.generation.OreGeneration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

import static cassiokf.industrialrenewal.References.MODID;


@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, guiFactory = References.GUI_FACTORY, updateJSON = References.VERSION_CHECKER_URL)
public class IndustrialRenewal {

    @Mod.Instance(References.MODID)
    public static IndustrialRenewal instance;
    @SidedProxy(clientSide = "cassiokf.industrialrenewal.proxy.ClientProxy", serverSide = "cassiokf.industrialrenewal.proxy.CommonProxy", modId = MODID)
    public static CommonProxy proxy;

    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        System.out.println(References.NAME + " is loading preInit!");
        FluidInit.registerFluids();
        IRSoundRegister.registerSounds();
        EntityInit.registerEntities();
        proxy.preInit();
        NetworkHandler.init();
        GameRegistry.registerWorldGenerator(new OreGeneration(), 4);
        ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkManagerCallback());
        proxy.registerRenderers();
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        System.out.println("Done!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println(References.NAME + " is loading init!");
        ModRecipes.init();
        proxy.Init();
        System.out.println("Done!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        System.out.println(References.NAME + " is loading posInit!");

        final List<String> names = new ArrayList<>();
        for (String name : OreDictionary.getOreNames())
        {
            if (name.startsWith("ore")) names.add(name);
        }
        for (String name : names)
        {
            NonNullList<ItemStack> ores = OreDictionary.getOres(name);
            for (ItemStack ore : ores)
            {
                ModItems.allOres.add(ore.getItem());
            }
        }
        IRConfig.populateDeepVeinOres();

        System.out.println("Done!");
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event)
        {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
            ModItems.registerOreDict();
            ModBlocks.registerOreDict();
        }

        @SubscribeEvent
        public static void registerItems(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerItemModels();
        }

        @SubscribeEvent
        public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(MODID))
            {
                ConfigManager.sync(MODID, Config.Type.INSTANCE);
            }
        }
    }
}