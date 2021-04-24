package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.item.*;
import cassiokf.industrialrenewal.item.armor.ItemSafetyBelt;
import cassiokf.industrialrenewal.item.armor.ItemSafetyHelmet;
import cassiokf.industrialrenewal.item.carts.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static cassiokf.industrialrenewal.References.MODID;

public class ItemsRegistration
{
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> INGOTSTEEL = ITEMS.register("ingot_steel", () -> new ItemBase(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> STICKIRON = ITEMS.register("stick_iron", () -> new ItemBase(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> STICKSTEEL = ITEMS.register("stick_steel", () -> new ItemBase(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> SPONGEIRON = ITEMS.register("sponge_iron", () -> new ItemBase(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> SMALLSLAB = ITEMS.register("small_slab", () -> new ItemBase(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> SMOTOR = ITEMS.register("motor", () -> new ItemBase(new Item.Properties().group(References.GROUP_INDR)));

    public static final RegistryObject<Item> CARTRIDGE_PLUS = ITEMS.register("cartridge_plus", () -> new ItemCartridge(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> CARTRIDGE_MINUS = ITEMS.register("cartridge_minus", () -> new ItemCartridge(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> CARTRIDGE_HALF = ITEMS.register("cartridge_half", () -> new ItemCartridge(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> CARTRIDGE_DOUBLE = ITEMS.register("cartridge_double", () -> new ItemCartridge(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> CARTRIDGE_INVERTER = ITEMS.register("cartridge_inverter", () -> new ItemCartridge(new Item.Properties().group(References.GROUP_INDR)));

    public static final RegistryObject<Item> LOCOMOTIVEPLOWIRON = ITEMS.register("plow_iron", () -> new ItemIronPlow(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> CARTLINKAGE = ITEMS.register("cart_linkable", () -> new ItemCartLinkable(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> SCREWDRIVE = ITEMS.register("screwdrive", () -> new ItemPowerScrewDrive(new Item.Properties().group(References.GROUP_INDR)));

    public static final RegistryObject<Item> CARGOCONTAINER = ITEMS.register("cargo_container", () -> new ItemMineCartCargoContainer(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> HOPPERCART = ITEMS.register("cart_hopper", () -> new ItemHopperCart(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> FLUIDCONTAINER = ITEMS.register("fluid_container", () -> new ItemMineCartFluidContainer(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> STEAMLOCOMOTIVE = ITEMS.register("steam_locomotive", () -> new ItemSteamLocomotive(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> LOGCART = ITEMS.register("log_cart", () -> new ItemLogCart(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> PASSENGERCAR = ITEMS.register("passenger_car", () -> new ItemMineCartPassengerCar(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> MINECARTFLAT = ITEMS.register("minecart_flat", () -> new ItemMineCartFlat(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));
    public static final RegistryObject<Item> TENDER = ITEMS.register("tender", () -> new ItemCartTender(new Item.Properties().group(References.GROUP_INDR_RAILROAD)));

    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit", () -> new ItemMedKit(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> FIREEXTINGUISHER = ITEMS.register("item_fire_extinguisher", () -> new ItemFireExtinguisher(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> SAFETYHELMET = ITEMS.register("safety_helmet", () -> new ItemSafetyHelmet(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> SAFETYBELT = ITEMS.register("safety_belt", () -> new ItemSafetyBelt(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> DISC1 = ITEMS.register("record_royal_entrance", () -> new ItemDiscBase(new Item.Properties().group(References.GROUP_INDR), SoundsRegistration.DISC_1));
    public static final RegistryObject<Item> MANUAL = ITEMS.register("ir_manual", () -> new ItemBookManual(new Item.Properties().group(References.GROUP_INDR)));

    public static final RegistryObject<Item> POINTER = ITEMS.register("pointer", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> LIMITER = ITEMS.register("limiter", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> POINTERLONG = ITEMS.register("pointer_long", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> FIRE = ITEMS.register("fire", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> BARLEVEL = ITEMS.register("bar_level", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> FLUIDLOADERARM = ITEMS.register("fluid_loader_arm", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> TAMBOR = ITEMS.register("rotary_drum", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> CUTTER = ITEMS.register("lathecutter", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> INDICATOR_ON = ITEMS.register("indicator_on", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> INDICATOR_OFF = ITEMS.register("indicator_off", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> SWITCH_ON = ITEMS.register("switch_on", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> SWITCH_OFF = ITEMS.register("indicator_off", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> PUSH_BUTTON = ITEMS.register("push_button", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> LABEL_5 = ITEMS.register("label_5", () -> new ItemBase(new Item.Properties().group(null)));
    public static final RegistryObject<Item> DISCR = ITEMS.register("disc_r", () -> new ItemBase(new Item.Properties().group(null)));

    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new ItemBattery(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> BATTERY_LITHIUM = ITEMS.register("battery_lithium", () -> new ItemBattery(new Item.Properties().group(References.GROUP_INDR)));

    public static final RegistryObject<Item> COILHV = ITEMS.register("coil_hv", () -> new ItemCoilHV(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> BARREL = ITEMS.register("barrel_item", () -> new ItemBarrel(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> FIREBOXSOLID = ITEMS.register("firebox_solid", () -> new ItemFireBox(new Item.Properties().group(References.GROUP_INDR), 1));
    public static final RegistryObject<Item> FIREBOXFLUID = ITEMS.register("firebox_fluid", () -> new ItemFireBox(new Item.Properties().group(References.GROUP_INDR), 2));
    public static final RegistryObject<Item> WINDBLADE = ITEMS.register("small_wind_blade", () -> new ItemWindBlade(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> DRILLSTEEL = ITEMS.register("drill_steel", () -> new ItemDrill(new Item.Properties().group(References.GROUP_INDR), 400));
    public static final RegistryObject<Item> DRILLDIAMOND = ITEMS.register("drill_diamond", () -> new ItemDrill(new Item.Properties().group(References.GROUP_INDR), 2000));
    public static final RegistryObject<Item> DRILLDEEP = ITEMS.register("drill_deep", () -> new ItemDrill(new Item.Properties().group(References.GROUP_INDR), 12000));

    public static final RegistryObject<Item> PROSPECTINGPAN = ITEMS.register("prospecting_pan", () -> new ItemProspectingPan(new Item.Properties().group(References.GROUP_INDR)));
    public static final RegistryObject<Item> PROSPECTINGWAND = ITEMS.register("regen_wand", () -> new ItemRegenerationWand(new Item.Properties().group(References.GROUP_INDR)));

    public static final RegistryObject<Item> HEMATITECHUNK = ITEMS.register("chunk_hematite", () -> new ItemOre(new Item.Properties().group(References.GROUP_INDR)));

    public static void init()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
