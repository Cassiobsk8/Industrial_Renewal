package cassiokf.industrialrenewal.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemOreDict ingotSteel = new ItemOreDict("ingot_steel", "ingotSteel");
    public static ItemOreDict stickIron = new ItemOreDict("stick_iron", "stickIron");

    public static ItemPowerScrewDrive screwDrive = new ItemPowerScrewDrive("screwdrive");
    public static ItemSteelSaw steelSaw = new ItemSteelSaw("steel_saw");

    public static ItemMineCartCargoContainer cargoContainer = new ItemMineCartCargoContainer("cargo_container");
    public static ItemSteamLocomotive steamLocomotive = new ItemSteamLocomotive("steam_locomotive");

    public static ItemBase spongeIron = new ItemBase("sponge_iron");
    public static ItemBase smallSlab = new ItemBase("small_slab");

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                ingotSteel,
                spongeIron,
                screwDrive,
                steelSaw,
                cargoContainer,
                steamLocomotive,
                smallSlab,
                stickIron
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
    }
}
