package cassiokf.industrialrenewal.compat.jei.lathe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class JEILatheRecipe implements IRecipeWrapper
{
    private final ItemStack input;
    private final ItemStack outPut;

    public JEILatheRecipe(ItemStack input, ItemStack outPut)
    {
        this.input = input;
        this.outPut = outPut;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, outPut);
    }
}
