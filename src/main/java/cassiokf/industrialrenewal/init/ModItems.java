package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.item.*;
import cassiokf.industrialrenewal.item.armor.ItemSafetyBelt;
import cassiokf.industrialrenewal.item.armor.ItemSafetyHelmet;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemOreDict ingotSteel = new ItemOreDict("ingot_steel", "ingotSteel", References.CREATIVE_IR_TAB);
    public static ItemOreDict stickIron = new ItemOreDict("stick_iron", "stickIron", References.CREATIVE_IR_TAB);
    public static ItemOreDict stickSteel = new ItemOreDict("stick_steel", "stickSteel", References.CREATIVE_IR_TAB);
    public static ItemOreDict spongeIron = new ItemOreDict("sponge_iron", "spongeIron", References.CREATIVE_IR_TAB);
    public static ItemOreDict smallSlab = new ItemOreDict("small_slab", "minislabStone", References.CREATIVE_IR_TAB);
    public static ItemOreDict sMotor = new ItemOreDict("motor", "motorSmall", References.CREATIVE_IR_TAB);

    public static ItemCartridge cartridge_plus = new ItemCartridge("cartridge_plus", References.CREATIVE_IR_TAB);
    public static ItemCartridge cartridge_minus = new ItemCartridge("cartridge_minus", References.CREATIVE_IR_TAB);
    public static ItemCartridge cartridge_half = new ItemCartridge("cartridge_half", References.CREATIVE_IR_TAB);
    public static ItemCartridge cartridge_double = new ItemCartridge("cartridge_double", References.CREATIVE_IR_TAB);
    public static ItemCartridge cartridge_inverter = new ItemCartridge("cartridge_inverter", References.CREATIVE_IR_TAB);

    public static ItemIronPlow locomotivePlowIron = new ItemIronPlow("plow_iron", References.CREAATIVE_IRWIP_TAB);

    public static ItemPowerScrewDrive screwDrive = new ItemPowerScrewDrive("screwdrive", References.CREATIVE_IR_TAB);
    public static ItemSteelSaw steelSaw = new ItemSteelSaw("steel_saw", "sawStone", References.CREATIVE_IR_TAB);

    public static ItemMineCartCargoContainer cargoContainer = new ItemMineCartCargoContainer("cargo_container", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static ItemHopperCart hopperCart = new ItemHopperCart("cart_hopper", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static ItemMineCartFluidContainer fluidContainer = new ItemMineCartFluidContainer("fluid_container", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static ItemSteamLocomotive steamLocomotive = new ItemSteamLocomotive("steam_locomotive", References.CREAATIVE_IRWIP_TAB);
    public static ItemLogCart logCart = new ItemLogCart("log_cart", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static ItemMineCartPassengerCar passengerCar = new ItemMineCartPassengerCar("passenger_car", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static ItemMineCartFlat mineCartFlat = new ItemMineCartFlat("minecart_flat", References.CREATIVE_IRLOCOMOTIVE_TAB);

    public static ItemMedKit medkit = new ItemMedKit("medkit", References.CREATIVE_IR_TAB);
    public static ItemFireExtinguisher fireExtinguisher = new ItemFireExtinguisher("item_fire_extinguisher", References.CREATIVE_IR_TAB);

    public static ItemSafetyHelmet safetyHelmet = new ItemSafetyHelmet("safety_helmet", References.CREATIVE_IR_TAB);
    public static ItemSafetyBelt safetyBelt = new ItemSafetyBelt("safety_belt", References.CREATIVE_IR_TAB);

    public static ItemDiscBase disc1 = new ItemDiscBase("record_royal_entrance", References.CREATIVE_IR_TAB, IRSoundRegister.DISC_1);

    public static ItemBookManual manual = new ItemBookManual("ir_manual", References.CREAATIVE_IRWIP_TAB);

    public static ItemBase pointer = new ItemBase("pointer", null);
    public static ItemBase pointerLong = new ItemBase("pointer_long", null);
    public static ItemBase fire = new ItemBase("fire", null);
    public static ItemBase barLevel = new ItemBase("bar_level", null);
    public static ItemBase fluidLoaderArm = new ItemBase("fluid_loader_arm", null);
    public static ItemBase tambor = new ItemBase("rotary_drum", null);

    public static ItemBattery battery = new ItemBattery("battery", References.CREATIVE_IR_TAB);

    public static ItemCoilHV coilHV = new ItemCoilHV("coil_hv", References.CREATIVE_IR_TAB);

    public static ItemBarrel barrel = new ItemBarrel("barrel_item", References.CREATIVE_IR_TAB);

    public static ItemFireBox fireBoxSolid = new ItemFireBox("firebox_solid", 1, References.CREATIVE_IR_TAB);
    public static ItemFireBox fireBoxFluid = new ItemFireBox("firebox_fluid", 2, References.CREATIVE_IR_TAB);

    public static ItemWindBlade windBlade = new ItemWindBlade("small_wind_blade", References.CREATIVE_IR_TAB);
    //public static ItemDrill drillSteel = new ItemDrill("drill_steel", References.CREAATIVE_IRWIP_TAB);
    //public static ItemDrill drillDiamond = new ItemDrill("drill_diamond", References.CREAATIVE_IRWIP_TAB);

    public static ItemOre hematiteChunk = new ItemOre("chunk_hematite", "oreIron", References.CREAATIVE_IRWIP_TAB);

    //public static ItemInstantNoodle instantNoodle = new ItemInstantNoodle("instant_noodle", References.CREAATIVE_IRWIP_TAB);

    public static void register(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                hematiteChunk,
                manual,
                ingotSteel,
                spongeIron,
                screwDrive,
                steelSaw,
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
        //drillSteel.registerItemModel();
        //drillDiamond.registerItemModel();
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
}
