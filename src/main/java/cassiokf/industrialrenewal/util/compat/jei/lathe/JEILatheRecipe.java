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

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        int time = LatheRecipe.LATHE_RECIPES.get(input.getItem()).getProcessTime();
        String string = JEICompat.translateToLocalFormated("gui.jei.category.energyneeded")
                + " "
                + time * IRConfig.MainConfig.Main.energyPerTickLatheMachine
                + " FE";
        FontRenderer renderer = minecraft.fontRenderer;
        renderer.drawString(string, 22, 64, Color.GRAY.getRGB());
    }
}
