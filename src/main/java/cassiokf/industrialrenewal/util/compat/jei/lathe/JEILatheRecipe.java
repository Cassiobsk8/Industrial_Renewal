package cassiokf.industrialrenewal.util.compat.jei.lathe;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.recipes.LatheRecipe;
import cassiokf.industrialrenewal.util.compat.jei.JEICompat;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JEILatheRecipe implements IRecipeWrapper
{
    private final java.util.List<ItemStack> input;
    private final ItemStack outPut;

    public JEILatheRecipe(java.util.List<ItemStack> input, ItemStack outPut)
    {
        this.input = input;
        this.outPut = outPut;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        List<java.util.List<ItemStack>> inceptionList = new CopyOnWriteArrayList<>();
        inceptionList.add(input);
        ingredients.setInputLists(ItemStack.class, inceptionList);
        ingredients.setOutput(ItemStack.class, outPut);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        int time = LatheRecipe.CACHED_RECIPES.get(input.get(0).getItem()).getProcessTime();
        String string = JEICompat.translateToLocalFormated("gui.jei.category.energyneeded")
                + " "
                + time * IRConfig.Main.energyPerTickLatheMachine.get()
                + " FE";
        FontRenderer renderer = minecraft.fontRenderer;
        renderer.drawString(string, 22, 64, Color.GRAY.getRGB());
    }
}
