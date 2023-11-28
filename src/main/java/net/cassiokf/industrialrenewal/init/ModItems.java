package net.cassiokf.industrialrenewal.init;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.item.*;
import net.cassiokf.industrialrenewal.item.decor.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> RENDER_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialRenewal.MODID);
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialRenewal.MODID);
    
    public static final RegistryObject<Item> SCREW_DRIVER = ITEMS.register("screwdriver", ItemScrewdriver::new);

    public static final RegistryObject<Item> INGOT_STEEL = registerItem("ingot_steel", IRBaseItem::new);
    public static final RegistryObject<Item> STICK_STEEL = registerItem("stick_steel", IRBaseItem::new);
    public static final RegistryObject<Item> STICK_IRON = registerItem("stick_iron", IRBaseItem::new);
    public static final RegistryObject<Item> STICK_COPPER = registerItem("stick_copper", IRBaseItem::new);
    public static final RegistryObject<Item> STICK_GOLD = registerItem("stick_gold", IRBaseItem::new);
    
    public static final RegistryObject<Item> SMALL_MOTOR = registerItem("motor", IRBaseItem::new);
    
    public static final RegistryObject<Item> WIND_BLADE = registerItem("small_wind_blade", ()->
            new ItemWindBlade(new Item.Properties().durability(48 * 60)));
    
    public static final RegistryObject<Item>FIREBOX_SOLID = registerItem("firebox_solid", ()->
            new ItemFireBox(new Item.Properties(), 1));
    
    public static final RegistryObject<Item>FIREBOX_FLUID = registerItem("firebox_fluid", ()->
            new ItemFireBox(new Item.Properties(), 2));
    
    public static final RegistryObject<Item>DRILL_STEEL = registerItem("drill_steel", ()->
            new ItemDrill(new Item.Properties().durability(600)));
    
    public static final RegistryObject<Item>DRILL_DIAMOND = registerItem("drill_diamond", ()->
            new ItemDrill(new Item.Properties().durability(1200)));
    
    public static final RegistryObject<Item>DRILL_DEEP = registerItem("drill_deep", ()->
            new ItemDrill(new Item.Properties().durability(2000)));
    
    public static final RegistryObject<Item>BATTERY = registerItem("battery", ()->
            new ItemBattery(new Item.Properties().stacksTo(1), 10000, 1000));
    
    public static final RegistryObject<Item>BATTERY_LITHIUM = registerItem("battery_lithium", ()->
            new ItemBattery(new Item.Properties().stacksTo(1), 100000, 10000));
    
    public static final RegistryObject<Item> WIRE_COIL = registerItem("coil_hv",
            ()-> new ItemWireCoil(new Item.Properties()));
    
    public static final RegistryObject<BlockItem> PILLAR = registerItem("catwalk_pillar",
            ()-> new ItemBlockPillar(ModBlocks.PILLAR.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> PILLAR_STEEL = registerItem("catwalk_steel_pillar",
            ()-> new ItemBlockPillar(ModBlocks.PILLAR_STEEL.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> CATWALK = registerItem("catwalk",
            ()-> new ItemBlockCatwalk(ModBlocks.CATWALK.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> CATWALK_STEEL = registerItem("catwalk_steel",
            ()-> new ItemBlockCatwalk(ModBlocks.CATWALK_STEEL.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> CATWALK_STAIR = registerItem("catwalk_stair",
            ()-> new ItemBlockCatwalkStair(ModBlocks.CATWALK_STAIR.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> CATWALK_STEEL_STAIR = registerItem("catwalk_stair_steel",
            ()-> new ItemBlockCatwalkStair(ModBlocks.CATWALK_STAIR_STEEL.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> CATWALK_LADDER = registerItem("catwalk_ladder",
            ()-> new ItemBlockCatwalkLadder(ModBlocks.CATWALK_LADDER.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> CATWALK_LADDER_STEEL = registerItem("catwalk_ladder_steel",
            ()-> new ItemBlockCatwalkLadder(ModBlocks.CATWALK_LADDER_STEEL.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> PLATFORM = registerItem("platform",
            ()-> new ItemBlockPlatform(ModBlocks.PLATFORM.get(), new Item.Properties()));
    
    public static final RegistryObject<BlockItem> SCAFFOLD = registerItem("scaffold",
            ()-> new ItemBlockScaffold(ModBlocks.SCAFFOLD.get(), new Item.Properties()));
    
    public static final RegistryObject<Item> pointer = registerRenderItem("pointer", IRBaseItem::new);
    public static final RegistryObject<Item> limiter = registerRenderItem("limiter", IRBaseItem::new);
    public static final RegistryObject<Item> pointerLong = registerRenderItem("pointer_long", IRBaseItem::new);
    public static final RegistryObject<Item> fire = registerRenderItem("fire", IRBaseItem::new);
    public static final RegistryObject<Item> barLevel = registerRenderItem("bar_level", IRBaseItem::new);
    public static final RegistryObject<Item> fluidLoaderArm = registerRenderItem("fluid_loader_arm", IRBaseItem::new);
    public static final RegistryObject<Item> tambor = registerRenderItem("rotary_drum", IRBaseItem::new);
    public static final RegistryObject<Item> cutter = registerRenderItem("lathecutter", IRBaseItem::new);
    public static final RegistryObject<Item> indicator_on = registerRenderItem("indicator_on", IRBaseItem::new);
    public static final RegistryObject<Item> indicator_off = registerRenderItem("indicator_off", IRBaseItem::new);
    public static final RegistryObject<Item> switch_on = registerRenderItem("switch_on", IRBaseItem::new);
    public static final RegistryObject<Item> switch_off = registerRenderItem("switch_off", IRBaseItem::new);
    public static final RegistryObject<Item> push_button = registerRenderItem("push_button", IRBaseItem::new);
    public static final RegistryObject<Item> label_5 = registerRenderItem("label_5", IRBaseItem::new);
    
    private static <T extends Item>RegistryObject<T> registerItem(String name, final Supplier<T> item) {
        RegistryObject<T> toReturn = ITEMS.register(name, item);
        return toReturn;
    }
    
    private static <T extends Item>RegistryObject<T> registerRenderItem(String name, final Supplier<T> item) {
        RegistryObject<T> toReturn = RENDER_ITEMS.register(name, item);
        return toReturn;
    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        RENDER_ITEMS.register(eventBus);
    }
}
