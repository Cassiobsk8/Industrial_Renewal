package cassiokf.industrialrenewal.compat.jei.lathe;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.recipes.LatheRecipe;
import com.google.common.collect.Lists;
import mezz.jei.api.IJeiHelpers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LatheRecipeMaker
{
    public static List<JEILatheRecipe> getRecipes(IJeiHelpers helpers)
    {
        Set<Map.Entry<Item, LatheRecipe>> recipes = References.LATHE_RECIPES.entrySet();
        List<JEILatheRecipe> jeiRecipes = Lists.newArrayList();
        for (Map.Entry<Item, LatheRecipe> entry : recipes)
        {
            ItemStack input = new ItemStack(entry.getKey());
            ItemStack result = entry.getValue().getRecipeOutput();
            JEILatheRecipe recipe = new JEILatheRecipe(input, result);
            jeiRecipes.add(recipe);
        }
        return jeiRecipes;
    }
}
