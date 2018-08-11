package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemOreDict ingotSteel = new ItemOreDict("ingot_steel", "ingotSteel", References.CREATIVE_IR_TAB);
    public static ItemOreDict stickIron = new ItemOreDict("stick_iron", "stickIron", References.CREATIVE_IR_TAB);

    public static ItemPowerScrewDrive screwDrive = new ItemPowerScrewDrive("screwdrive", References.CREATIVE_IR_TAB);
    public static ItemSteelSaw steelSaw = new ItemSteelSaw("steel_saw", References.CREATIVE_IR_TAB);

    public static ItemMineCartCargoContainer cargoContainer = new ItemMineCartCargoContainer("cargo_container", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static ItemSteamLocomotive steamLocomotive = new ItemSteamLocomotive("steam_locomotive", References.CREATIVE_IRLOCOMOTIVE_TAB);

    public static ItemBase spongeIron = new ItemBase("sponge_iron", References.CREATIVE_IR_TAB);
    public static ItemBase smallSlab = new ItemBase("small_slab", References.CREATIVE_IR_TAB);
    public static ItemMedKit medkit = new ItemMedKit("medkit", References.CREATIVE_IR_TAB);

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                ingotSteel,
                spongeIron,
                screwDrive,
                steelSaw,
                cargoContainer,
                steamLocomotive,
                smallSlab,
                stickIron,
                medkit
        );
    }

    public static void registerModels() {
        ingotSteel.registerItemModel();
        spongeIron.registerItemModel();
        screwDrive.registerItemModel();
        steelSaw.registerItemModel();
        cargoContainer.registerItemModel();
        steamLocomotive.registerItemModel();
        smallSlab.registerItemModel();
        stickIron.registerItemModel();
        medkit.registerItemModel();
    }
}
