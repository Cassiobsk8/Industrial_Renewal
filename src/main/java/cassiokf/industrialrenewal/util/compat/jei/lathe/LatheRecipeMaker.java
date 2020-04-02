package cassiokf.industrialrenewal.util.compat.jei.lathe;

import cassiokf.industrialrenewal.recipes.LatheRecipe;
import com.google.common.collect.Lists;
import mezz.jei.api.IJeiHelpers;
import net.minecraft.item.ItemStack;

import java.util.List;

public class LatheRecipeMaker
{
    public static List<JEILatheRecipe> getRecipes(IJeiHelpers helpers)
    {
        List<LatheRecipe> recipes = LatheRecipe.LATHE_RECIPES;
        List<JEILatheRecipe> jeiRecipes = Lists.newArrayList();
        for (LatheRecipe entry : recipes)
        {
            List<ItemStack> input = entry.getInput();
            ItemStack result = entry.getRecipeOutput();
            JEILatheRecipe recipe = new JEILatheRecipe(input, result);
            jeiRecipes.add(recipe);
        }
        return jeiRecipes;
    }
}
