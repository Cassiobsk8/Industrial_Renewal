package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.item.*;
import cassiokf.industrialrenewal.item.armor.ItemSafetyBelt;
import cassiokf.industrialrenewal.item.armor.ItemSafetyHelmet;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{

    public static final Item ingotSteel = new ItemBase(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "ingot_steel");
    public static final Item stickIron = new ItemBase(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "stick_iron");
    public static final Item stickSteel = new ItemBase(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "stick_steel");
    public static final Item spongeIron = new ItemBase(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "sponge_iron");
    public static final Item smallSlab = new ItemBase(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "small_slab");
    public static final Item sMotor = new ItemBase(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "motor");
    //
    public static final Item cartridge_plus = new ItemCartridge(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "cartridge_plus");
    public static final Item cartridge_minus = new ItemCartridge(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "cartridge_minus");
    public static final Item cartridge_half = new ItemCartridge(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "cartridge_half");
    public static final Item cartridge_double = new ItemCartridge(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "cartridge_double");
    public static final Item cartridge_inverter = new ItemCartridge(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "cartridge_inverter");
    //
    public static final Item locomotivePlowIron = new ItemIronPlow(new Item.Properties().group(References.CREAATIVE_IRWIP_GROUP)).setRegistryName(References.MODID, "plow_iron");
    //
    public static final Item screwDrive = new ItemPowerScrewDrive(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "screwdrive");
    //public static final Item steelSaw = new ItemSteelSaw("steel_saw", "sawStone", References.CREATIVE_IR_GROUP);
//
    public static final Item cargoContainer = new ItemMineCartCargoContainer(new Item.Properties().group(References.CREATIVE_IRLOCOMOTIVE_GROUP)).setRegistryName(References.MODID, "cargo_container");
    public static final Item hopperCart = new ItemHopperCart(new Item.Properties().group(References.CREATIVE_IRLOCOMOTIVE_GROUP)).setRegistryName(References.MODID, "cart_hopper");
    public static final Item fluidContainer = new ItemMineCartFluidContainer(new Item.Properties().group(References.CREATIVE_IRLOCOMOTIVE_GROUP)).setRegistryName(References.MODID, "fluid_container");
    public static final Item steamLocomotive = new ItemSteamLocomotive(new Item.Properties().group(References.CREATIVE_IRLOCOMOTIVE_GROUP)).setRegistryName(References.MODID, "steam_locomotive");
    public static final Item logCart = new ItemLogCart(new Item.Properties().group(References.CREATIVE_IRLOCOMOTIVE_GROUP)).setRegistryName(References.MODID, "log_cart");
    public static final Item passengerCar = new ItemMineCartPassengerCar(new Item.Properties().group(References.CREATIVE_IRLOCOMOTIVE_GROUP)).setRegistryName(References.MODID, "passenger_car");
    public static final Item mineCartFlat = new ItemMineCartFlat(new Item.Properties().group(References.CREATIVE_IRLOCOMOTIVE_GROUP)).setRegistryName(References.MODID, "minecart_flat");
    //
    public static final Item medkit = new ItemMedKit(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "medkit");
    public static final Item fireExtinguisher = new ItemFireExtinguisher(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "item_fire_extinguisher");
    //
    public static final Item safetyHelmet = new ItemSafetyHelmet(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "safety_helmet");
    public static final Item safetyBelt = new ItemSafetyBelt(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "safety_belt");
    //
    public static final Item disc1 = new ItemDiscBase(new Item.Properties().group(References.CREATIVE_IR_GROUP), IRSoundRegister.DISC_1).setRegistryName(References.MODID, "record_royal_entrance");
    //
    public static final Item manual = new ItemBookManual(new Item.Properties().group(References.CREAATIVE_IRWIP_GROUP)).setRegistryName(References.MODID, "ir_manual");

    public static final Item pointer = new ItemBase(new Item.Properties().group(null)).setRegistryName(References.MODID, "pointer");
    public static final Item pointerLong = new ItemBase(new Item.Properties().group(null)).setRegistryName(References.MODID, "pointer_long");
    public static final Item fire = new ItemBase(new Item.Properties().group(null)).setRegistryName(References.MODID, "fire");
    public static final Item barLevel = new ItemBase(new Item.Properties().group(null)).setRegistryName(References.MODID, "bar_level");
    public static final Item fluidLoaderArm = new ItemBase(new Item.Properties().group(null)).setRegistryName(References.MODID, "fluid_loader_arm");
    public static final Item tambor = new ItemBase(new Item.Properties().group(null)).setRegistryName(References.MODID, "rotary_drum");

    public static final Item battery = new ItemBattery(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "battery");
    //
    public static final Item coilHV = new ItemCoilHV(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "coil_hv");
    //
    public static final Item barrel = new ItemBarrel(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "barrel_item");
    //
    public static final Item fireBoxSolid = new ItemFireBox(new Item.Properties().group(References.CREATIVE_IR_GROUP), 1).setRegistryName(References.MODID, "firebox_solid");
    public static final Item fireBoxFluid = new ItemFireBox(new Item.Properties().group(References.CREATIVE_IR_GROUP), 2).setRegistryName(References.MODID, "firebox_fluid");
    //
    public static final Item windBlade = new ItemWindBlade(new Item.Properties().group(References.CREATIVE_IR_GROUP)).setRegistryName(References.MODID, "small_wind_blade");

    //public static final Item drillSteel = new ItemDrill(new Item.Properties().group(References.CREAATIVE_IRWIP_GROUP)).setRegistryName(References.MODID,"drill_steel");
    //public static final Item drillDiamond = new ItemDrill(new Item.Properties().group(References.CREAATIVE_IRWIP_GROUP)).setRegistryName(References.MODID,"drill_diamond");

    public static final Item hematiteChunk = new ItemOre(new Item.Properties().group(References.CREAATIVE_IRWIP_GROUP)).setRegistryName(References.MODID, "chunk_hematite");

    //public static final ItemInstantNoodle instantNoodle = new ItemInstantNoodle("instant_noodle", References.CREAATIVE_IRWIP_TAB);

    public static void register(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                hematiteChunk,
                manual,
                ingotSteel,
                spongeIron,
                screwDrive,
                //steelSaw,
                cargoContainer,
                hopperCart,
                fluidContainer,
                steamLocomotive,
                logCart,
                passengerCar,
                mineCartFlat,
                smallSlab,
                stickIron,
                stickSteel,
                medkit,
                fireExtinguisher,
                safetyHelmet,
                safetyBelt,
                disc1,
                locomotivePlowIron,
                //instantNoodle
                cartridge_plus,
                cartridge_minus,
                cartridge_half,
                cartridge_double,
                cartridge_inverter,
//
                coilHV,

                pointer,
                pointerLong,
                barLevel,
                fire,
                barrel,
                fireBoxSolid,
                fireBoxFluid,
                battery,
                sMotor,
                windBlade,
                //drillSteel,
                //drillDiamond,
                fluidLoaderArm,
                tambor
        );
    }
/*
    public static void registerOreDict() {
        //oreCopper.initOreDict();
        hematiteChunk.initOreDict();
        ingotSteel.initOreDict();
        stickIron.initOreDict();
        stickSteel.initOreDict();
        disc1.initOreDict();
        steelSaw.initOreDict();
        spongeIron.initOreDict();
        smallSlab.initOreDict();
        sMotor.initOreDict();
    }
 */
}
