package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.proxy.CommonProxy;
import cassiokf.industrialrenewal.recipes.ModRecipes;
import cassiokf.industrialrenewal.tab.IndustrialRenewalTab;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid = IndustrialRenewal.MODID, name = IndustrialRenewal.NAME, version = IndustrialRenewal.VERSION)
public class IndustrialRenewal {

    public static final IndustrialRenewalTab creativeTab = new IndustrialRenewalTab();

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
	public static final String MODID = "industrialrenewal";
	public static final String NAME = "Industrial Renwal";
	public static final String VERSION = "1.0.0";

	@Mod.Instance(MODID)
	public static IndustrialRenewal instance;

	@SidedProxy(clientSide = "cassiokf.industrialrenewal.proxy.ClientProxy", serverSide = "cassiokf.industrialrenewal.proxy.CommonProxy", modId = MODID)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		System.out.println(NAME + " is loading preInit!");
		//test
        IRSoundHandler.init();
        //test
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event){
	    ModRecipes.init();
		System.out.println(NAME + " is loading init!");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		System.out.println(NAME + " is loading posInit!");

	}
}