package cassiokf.industrialrenewal.util.compat.crafttweaker;

import cassiokf.industrialrenewal.recipes.LatheRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.industrialrenewal.lathe")
public class LatheMachineCT
{
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int time)
    {
        ItemStack inputStack = toStack(input);
        ItemStack outputStack = toStack(output);
        LatheRecipe latheRecipe = new LatheRecipe(inputStack.getItem(), outputStack, time);
        CraftTweakerAPI.apply(new Add(inputStack.getItem(), latheRecipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output)
    {
        ItemStack outputStack = toStack(output);
        LatheRecipe recipeToRemove = null;
        for (LatheRecipe recipe : LatheRecipe.LATHE_RECIPES.values())
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

    public static ItemStack toStack(IItemStack iStack)
    {
        if (iStack == null)
            return ItemStack.EMPTY;
        return (ItemStack) iStack.getInternal();
    }

    private static class Add implements IAction
    {
        private final LatheRecipe recipe;
        private final Item item;

        public Add(Item item, LatheRecipe recipe)
        {
            this.recipe = recipe;
            this.item = item;
        }

        @Override
        public void apply()
        {
            LatheRecipe.LATHE_RECIPES.put(item, recipe);
        }

        @Override
        public String describe()
        {
            return "Adding Lathe Machine Recipe for " + recipe.getRecipeOutput().getDisplayName();
        }
    }

    private static class Remove implements IAction
    {
        private final Item item;
        private final LatheRecipe recipe;

        public Remove(Item item, LatheRecipe recipe)
        {
            this.item = item;
            this.recipe = recipe;
        }

        @Override
        public void apply()
        {
            LatheRecipe.LATHE_RECIPES.remove(item);
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
        }

        @Override
        public String describe()
        {
            return "Removing all Lathe Machine Recipes";
        }
    }
}
