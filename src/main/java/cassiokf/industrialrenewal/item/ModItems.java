package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.item.armor.ItemSafetyBelt;
import cassiokf.industrialrenewal.item.armor.ItemSafetyHelmet;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemOreDict ingotSteel = new ItemOreDict("ingot_steel", "ingotSteel", References.CREATIVE_IR_TAB);
    public static ItemOreDict stickIron = new ItemOreDict("stick_iron", "stickIron", References.CREATIVE_IR_TAB);
    public static ItemOreDict spongeIron = new ItemOreDict("sponge_iron", "spongeIron", References.CREATIVE_IR_TAB);
    public static ItemOreDict smallSlab = new ItemOreDict("small_slab", "minislabStone", References.CREATIVE_IR_TAB);

    public static ItemIronPlow locomotivePlowIron = new ItemIronPlow("plow_iron", References.CREAATIVE_IRWIP_TAB);

    public static ItemPowerScrewDrive screwDrive = new ItemPowerScrewDrive("screwdrive", References.CREATIVE_IR_TAB);
    public static ItemSteelSaw steelSaw = new ItemSteelSaw("steel_saw", "sawStone", References.CREATIVE_IR_TAB);

    public static ItemMineCartCargoContainer cargoContainer = new ItemMineCartCargoContainer("cargo_container", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static ItemMineCartFluidContainer fluidContainer = new ItemMineCartFluidContainer("fluid_container", References.CREAATIVE_IRWIP_TAB);
    public static ItemSteamLocomotive steamLocomotive = new ItemSteamLocomotive("steam_locomotive", References.CREAATIVE_IRWIP_TAB);

    public static ItemMedKit medkit = new ItemMedKit("medkit", References.CREATIVE_IR_TAB);
    public static ItemFireExtinguisher fireExtinguisher = new ItemFireExtinguisher("item_fire_extinguisher", References.CREATIVE_IR_TAB);

    public static ItemSafetyHelmet safetyHelmet = new ItemSafetyHelmet("safety_helmet", References.CREATIVE_IR_TAB);
    public static ItemSafetyBelt safetyBelt = new ItemSafetyBelt("safety_belt", References.CREATIVE_IR_TAB);

    public static ItemDiscBase disc1 = new ItemDiscBase("record_royal_entrance", References.CREATIVE_IR_TAB, IRSoundHandler.DISC_1);

    //public static ItemInstantNoodle instantNoodle = new ItemInstantNoodle("instant_noodle", References.CREAATIVE_IRWIP_TAB);

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                ingotSteel,
                spongeIron,
                screwDrive,
                steelSaw,
                cargoContainer,
                fluidContainer,
                steamLocomotive,
                smallSlab,
                stickIron,
                medkit,
                fireExtinguisher,
                safetyHelmet,
                safetyBelt,
                disc1,
                locomotivePlowIron
                //instantNoodle
        );
    }

    public static void registerModels() {
        ingotSteel.registerItemModel();
        spongeIron.registerItemModel();
        screwDrive.registerItemModel();
        steelSaw.registerItemModel();
        cargoContainer.registerItemModel();
        fluidContainer.registerItemModel();
        steamLocomotive.registerItemModel();
        smallSlab.registerItemModel();
        stickIron.registerItemModel();
        medkit.registerItemModel();
        fireExtinguisher.registerItemModel();
        safetyHelmet.registerItemModel();
        safetyBelt.registerItemModel();
        disc1.registerItemModel();
        locomotivePlowIron.registerItemModel();
        //instantNoodle.registerItemModel();
    }
}
