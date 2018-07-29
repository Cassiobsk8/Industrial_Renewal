package cassiokf.industrialrenewal.recipes;

import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModItems.spongeIron, new ItemStack(ModItems.ingotSteel), 0.7f);
        GameRegistry.addShapelessRecipe(new ResourceLocation("industrialrenewal:small_slabs"), new ResourceLocation("small_slabs"), new ItemStack(ModItems.smallSlab, 4),
                Ingredient.fromStacks(new ItemStack(ModItems.steelSaw, 1, OreDictionary.WILDCARD_VALUE)), Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1)));

        //ModBlocks.oreCopper.initOreDict();
        ModItems.ingotSteel.initOreDict();
    }

}