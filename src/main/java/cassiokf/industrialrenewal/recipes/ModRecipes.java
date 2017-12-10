package cassiokf.industrialrenewal.recipes;

import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModItems.spongeIron, new ItemStack(ModItems.ingotSteel), 0.7f);

        ModBlocks.oreCopper.initOreDict();
        ModItems.ingotSteel.initOreDict();
    }

}