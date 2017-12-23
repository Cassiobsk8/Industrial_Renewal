package cassiokf.industrialrenewal.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemOreDict ingotSteel = new ItemOreDict("ingot_steel", "ingotSteel");
    public static ItemOreDict spongeIron = new ItemOreDict("sponge_iron", null);
    public static ItemBase screwDrive = new ItemBase("screwdrive");
    public static ItemBase steelSaw = new ItemBase("steel_saw");
    public static ItemBase lamp = new ItemBase("lamp");

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                ingotSteel,
                spongeIron,
                screwDrive,
                steelSaw,
                lamp
        );
    }

    public static void registerModels() {
        ingotSteel.registerItemModel();
        spongeIron.registerItemModel();
        screwDrive.registerItemModel();
        steelSaw.registerItemModel();
        lamp.registerItemModel();
    }
}
