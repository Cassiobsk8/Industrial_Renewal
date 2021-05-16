package cassiokf.industrialrenewal.util.compat.crafttweaker;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.recipes.LatheRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ZenClass("mods.industrialrenewal.lathe")
@ZenRegister
public class LatheMachineCT
{
    @ZenMethod
    public static void addRecipe(IIngredient output, IIngredient input, int time)
    {
        ItemStack outputStack = CraftTweakerMC.getItemStacks(output.getItems())[0];
        List<ItemStack> stackList = Arrays.asList(CraftTweakerMC.getItemStacks(input.getItems()));
        LatheRecipe latheRecipe = new LatheRecipe(stackList, outputStack, time);
        CraftTweakerAPI.apply(new Add(stackList, latheRecipe));
    }

    @ZenMethod
    public static void removeRecipe(IIngredient output)
    {
        ItemStack outputStack = CraftTweakerMC.getItemStacks(output.getItems())[0];
        LatheRecipe recipeToRemove = null;
        for (LatheRecipe recipe : LatheRecipe.LATHE_RECIPES)
        {
            if (recipe.getRecipeOutput().getItem().equals(outputStack.getItem()))
            {
                recipeToRemove = recipe;
                break;
            }
        }
        if (recipeToRemove != null)
            CraftTweakerAPI.apply(new Remove(recipeToRemove.getInput(), recipeToRemove));
    }

    @ZenMethod
    public static void removeAll()
    {
        CraftTweakerAPI.apply(new RemoveAll());
    }

    private static class Add implements IAction
    {
        private final LatheRecipe recipe;
        private final List<ItemStack> item;

        public Add(List<ItemStack> item, LatheRecipe recipe)
        {
            this.recipe = recipe;
            this.item = item;
        }

        @Override
        public void apply()
        {
            LatheRecipe.LATHE_RECIPES.add(recipe);
            for (ItemStack stack : item)
            {
                LatheRecipe.CACHED_RECIPES.put(stack.getItem(), recipe);
            }
            IndustrialRenewal.LOGGER.info("Added New Recipe via CraftTweaker: " + recipe.getInput().get(0).getDisplayName() + " to: " + recipe.getRecipeOutput().getDisplayName() + " from variants: " + item);
        }

        @Override
        public String describe()
        {
            return "Adding Lathe Machine Recipe for " + recipe.getRecipeOutput().getDisplayName();
        }
    }

    private static class Remove implements IAction
    {
        private final List<ItemStack> item;
        private final LatheRecipe recipe;

        public Remove(List<ItemStack> item, LatheRecipe recipe)
        {
            this.item = item;
            this.recipe = recipe;
        }

        @Override
        public void apply()
        {
            LatheRecipe.LATHE_RECIPES.remove(recipe);
            for (ItemStack stack : item)
            {
                LatheRecipe.CACHED_RECIPES.remove(stack.getItem());
            }
            System.out.println("Removed " + recipe);
        }

        @Override
        public String describe()
        {
            return "Removing Lathe Machine Recipe for " + recipe.getRecipeOutput().getDisplayName();
        }
    }

    private static class RemoveAll implements IAction
    {
        public RemoveAll()
        {
        }

        @Override
        public void apply()
        {
            LatheRecipe.LATHE_RECIPES.clear();
            LatheRecipe.CACHED_RECIPES.clear();
            System.out.println("All Recipes Removed");
        }

        @Override
        public String describe()
        {
            return "Removing all Lathe Machine Recipes";
        }
    }
}
