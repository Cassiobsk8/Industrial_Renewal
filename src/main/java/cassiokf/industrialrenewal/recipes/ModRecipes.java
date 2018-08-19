package cassiokf.industrialrenewal.recipes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class ModRecipes {

    public static void init() {
        if (IRConfig.spongeIronRecipeActive) {
            GameRegistry.addSmelting(ModItems.spongeIron, new ItemStack(ModItems.ingotSteel), 0.7f);
        }
        GameRegistry.addShapelessRecipe(new ResourceLocation("industrialrenewal:small_slabs"), new ResourceLocation("small_slabs"), new ItemStack(ModItems.smallSlab, 4),
                Ingredient.fromStacks(new ItemStack(ModItems.steelSaw, 1, OreDictionary.WILDCARD_VALUE)), Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1)));
        GameRegistry.addShapelessRecipe(new ResourceLocation("industrialrenewal:record_royal_entrance"), new ResourceLocation("record_royal_entrance"), new ItemStack(ModItems.disc1, 1),
                Ingredient.fromStacks(new ItemStack(ModItems.steelSaw, 1, OreDictionary.WILDCARD_VALUE)), OreIngredient.fromItems(Items.RECORD_13,
                        Items.RECORD_11, Items.RECORD_CAT,Items.RECORD_BLOCKS, Items.RECORD_MALL, Items.RECORD_CHIRP, Items.RECORD_FAR,Items.RECORD_MELLOHI,Items.RECORD_STAL,Items.RECORD_STRAD,
                        Items.RECORD_WAIT, Items.RECORD_WARD));
        //ModBlocks.oreCopper.initOreDict();
        ModItems.ingotSteel.initOreDict();
        ModItems.stickIron.initOreDict();
        ModItems.disc1.initOreDict();
        ModItems.steelSaw.initOreDict();
        ModItems.spongeIron.initOreDict();
        ModItems.smallSlab.initOreDict();
    }

}