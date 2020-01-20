package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.item.ItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{

    //public static Item ingotSteel = new ItemOreDict("ingot_steel", "ingotSteel", References.CREATIVE_IR_GROUP);
    //public static Item stickIron = new ItemOreDict("stick_iron", "stickIron", References.CREATIVE_IR_GROUP);
    //public static Item stickSteel = new ItemOreDict("stick_steel", "stickSteel", References.CREATIVE_IR_GROUP);
    //public static Item spongeIron = new ItemOreDict("sponge_iron", "spongeIron", References.CREATIVE_IR_GROUP);
    //public static Item smallSlab = new ItemOreDict("small_slab", "minislabStone", References.CREATIVE_IR_GROUP);
    //public static Item sMotor = new ItemOreDict("motor", "motorSmall", References.CREATIVE_IR_GROUP);
//
    //public static Item cartridge_plus = new ItemCartridge("cartridge_plus", References.CREATIVE_IR_GROUP);
    //public static Item cartridge_minus = new ItemCartridge("cartridge_minus", References.CREATIVE_IR_GROUP);
    //public static Item cartridge_half = new ItemCartridge("cartridge_half", References.CREATIVE_IR_GROUP);
    //public static Item cartridge_double = new ItemCartridge("cartridge_double", References.CREATIVE_IR_GROUP);
    //public static Item cartridge_inverter = new ItemCartridge("cartridge_inverter", References.CREATIVE_IR_GROUP);
//
    //public static Item locomotivePlowIron = new ItemIronPlow("plow_iron", References.CREAATIVE_IRWIP_GROUP);
//
    //public static Item screwDrive = new ItemPowerScrewDrive("screwdrive", References.CREATIVE_IR_GROUP);
    //public static Item steelSaw = new ItemSteelSaw("steel_saw", "sawStone", References.CREATIVE_IR_GROUP);
//
    //public static Item cargoContainer = new ItemMineCartCargoContainer("cargo_container", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static Item hopperCart = new ItemHopperCart("cart_hopper", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static Item fluidContainer = new ItemMineCartFluidContainer("fluid_container", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static Item steamLocomotive = new ItemSteamLocomotive("steam_locomotive", References.CREAATIVE_IRWIP_GROUP);
    //public static Item logCart = new ItemLogCart("log_cart", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static Item passengerCar = new ItemMineCartPassengerCar("passenger_car", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static Item mineCartFlat = new ItemMineCartFlat("minecart_flat", References.CREATIVE_IRLOCOMOTIVE_GROUP);
//
    //public static Item medkit = new ItemMedKit("medkit", References.CREATIVE_IR_GROUP);
    //public static Item fireExtinguisher = new ItemFireExtinguisher("item_fire_extinguisher", References.CREATIVE_IR_GROUP);
//
    //public static Item safetyHelmet = new ItemSafetyHelmet("safety_helmet", References.CREATIVE_IR_GROUP);
    //public static Item safetyBelt = new ItemSafetyBelt("safety_belt", References.CREATIVE_IR_GROUP);
//
    //public static Item disc1 = new ItemDiscBase("record_royal_entrance", References.CREATIVE_IR_GROUP, IRSoundRegister.DISC_1);
//
    //public static Item manual = new ItemBookManual("ir_manual", References.CREAATIVE_IRWIP_GROUP);

    public static Item pointer = new ItemBase("pointer", null);
    public static Item pointerLong = new ItemBase("pointer_long", null);
    public static Item fire = new ItemBase("fire", null);
    public static Item barLevel = new ItemBase("bar_level", null);
    public static Item fluidLoaderArm = new ItemBase("fluid_loader_arm", null);
    public static Item tambor = new ItemBase("rotary_drum", null);

    //public static Item battery = new ItemBattery("battery", References.CREATIVE_IR_GROUP);
//
    //public static Item coilHV = new ItemCoilHV("coil_hv", References.CREATIVE_IR_GROUP);
//
    //public static Item barrel = new ItemBarrel("barrel_item", References.CREATIVE_IR_GROUP);
//
    //public static Item fireBoxSolid = new ItemFireBox("firebox_solid", 1, References.CREATIVE_IR_GROUP);
    //public static Item fireBoxFluid = new ItemFireBox("firebox_fluid", 2, References.CREATIVE_IR_GROUP);
//
    //public static Item windBlade = new ItemWindBlade("small_wind_blade", References.CREATIVE_IR_GROUP);
    //public static Item drillSteel = new ItemDrill("drill_steel", References.CREAATIVE_IRWIP_GROUP);
    //public static Item drillDiamond = new ItemDrill("drill_diamond", References.CREAATIVE_IRWIP_GROUP);

    //public static Item hematiteChunk = new ItemOre("chunk_hematite", "oreIron", References.CREAATIVE_IRWIP_GROUP);

    //public static ItemInstantNoodle instantNoodle = new ItemInstantNoodle("instant_noodle", References.CREAATIVE_IRWIP_TAB);

    public static void register(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                //hematiteChunk,
                //manual,
                //ingotSteel,
                //spongeIron,
                //screwDrive,
                //steelSaw,
                //cargoContainer,
                //hopperCart,
                //fluidContainer,
                //steamLocomotive,
                //logCart,
                //passengerCar,
                //mineCartFlat,
                //smallSlab,
                //stickIron,
                //stickSteel,
                //medkit,
                //fireExtinguisher,
                //safetyHelmet,
                //safetyBelt,
                //disc1,
                //locomotivePlowIron,
                //instantNoodle
                //cartridge_plus,
                //cartridge_minus,
                //cartridge_half,
                //cartridge_double,
                //cartridge_inverter,
//
                //coilHV,

                pointer,
                pointerLong,
                barLevel,
                fire,
                //barrel,
                //fireBoxSolid,
                //fireBoxFluid,
                //battery,
                //sMotor,
                //windBlade,
                //drillSteel,
                //drillDiamond,
                fluidLoaderArm,
                tambor
        );
    }

    /*public static void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(References.MODID + ":" + id, "inventory"));
    }*/
/*
    public static void registerModels() {
        hematiteChunk.registerItemModel();
        ingotSteel.registerItemModel();
        spongeIron.registerItemModel();
        screwDrive.registerItemModel();
        steelSaw.registerItemModel();
        cargoContainer.registerItemModel();
        hopperCart.registerItemModel();
        fluidContainer.registerItemModel();
        steamLocomotive.registerItemModel();
        logCart.registerItemModel();
        passengerCar.registerItemModel();
        mineCartFlat.registerItemModel();
        smallSlab.registerItemModel();
        stickIron.registerItemModel();
        medkit.registerItemModel();
        fireExtinguisher.registerItemModel();
        safetyHelmet.registerItemModel();
        safetyBelt.registerItemModel();
        disc1.registerItemModel();
        locomotivePlowIron.registerItemModel();
        //instantNoodle.registerItemModel();
        cartridge_plus.registerItemModel();
        cartridge_minus.registerItemModel();
        cartridge_half.registerItemModel();
        cartridge_double.registerItemModel();
        cartridge_inverter.registerItemModel();
        manual.registerItemModel();
        coilHV.registerItemModel();
        stickSteel.registerItemModel();
        pointer.registerItemModel();
        pointerLong.registerItemModel();
        barLevel.registerItemModel();
        fluidLoaderArm.registerItemModel();
        tambor.registerItemModel();
        fire.registerItemModel();
        barrel.registerItemModel();
        fireBoxSolid.registerItemModel();
        fireBoxFluid.registerItemModel();
        battery.registerItemModel();
        sMotor.registerItemModel();
        windBlade.registerItemModel();
        drillSteel.registerItemModel();
        drillDiamond.registerItemModel();
    }

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
