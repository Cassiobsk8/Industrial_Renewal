package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.entity.EntityInit;
import cassiokf.industrialrenewal.entity.EntityLogCart;
import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.network.NetworkHandler;
import cassiokf.industrialrenewal.proxy.CommonProxy;
import cassiokf.industrialrenewal.recipes.ModRecipes;
import cassiokf.industrialrenewal.util.GUIHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import java.util.Random;


@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, guiFactory = References.GUI_FACTORY, updateJSON = References.VERSION_CHECKER_URL)
public class IndustrialRenewal {

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
        NetworkHandler.init();

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

        //private static final String NBT_KEY = "industrialrenewal.firstjoin";
        @SubscribeEvent
        public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            /*
            if (IRConfig.startWithManual) {
                EntityPlayer p = event.player;
                NBTTagCompound data = p.getEntityData();
                NBTTagCompound persistent;
                if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
                    data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));
                } else {
                    persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
                }

                if (!persistent.hasKey(NBT_KEY)) {
                    Utils.sendConsoleMessage("spawning");
                    persistent.setBoolean(NBT_KEY, true);
                    p.inventory.addItemStackToInventory(new ItemStack(ModItems.manual, 1));
                }
            }*/
        }

        @SubscribeEvent
        public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget() instanceof EntitySteamLocomotive) {
                if (!event.getWorld().isRemote) {
                    EntitySteamLocomotive entity = (EntitySteamLocomotive) event.getTarget();
                    int entityID = entity.getEntityId();
                    FMLNetworkHandler.openGui(event.getEntityPlayer(), IndustrialRenewal.instance, GUIHandler.STEAMLOCOMOTIVE, event.getWorld(), entityID, 0, 0);
                }
                event.setCanceled(true);
            } else if (event.getTarget() instanceof EntityLogCart) {
                if (!event.getWorld().isRemote) {
                    EntityLogCart entity = (EntityLogCart) event.getTarget();
                    int entityID = entity.getEntityId();
                    FMLNetworkHandler.openGui(event.getEntityPlayer(), IndustrialRenewal.instance, GUIHandler.LOGCART, event.getWorld(), entityID, 0, 0);
                }
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onEntityDrop(LivingDropsEvent event) {
            if (event.getEntity() instanceof EntityZombieVillager) {
                Random r = new Random();
                if (r.nextInt(100) < 25) {
                    //event.getEntity().dropItem(ModItems.instantNoodle, r.nextInt(2) + event.getLootingLevel());
                }
            }
        }
    }
}