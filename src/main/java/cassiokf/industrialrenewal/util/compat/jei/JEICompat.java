package cassiokf.industrialrenewal.util.compat.jei;

import cassiokf.industrialrenewal.gui.GUILatheMachine;
import cassiokf.industrialrenewal.gui.container.ContainerLatheMachine;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.util.compat.jei.lathe.LatheRecipeCategory;
import cassiokf.industrialrenewal.util.compat.jei.lathe.LatheRecipeMaker;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.IllegalFormatException;

@JEIPlugin
public class JEICompat implements IModPlugin
{
    public static String translateToLocal(String key)
    {
        return I18n.format(key);
    }

    public static String translateToLocalFormated(String key, Object... format)
    {
        String s = translateToLocal(key);
        try
        {
            return String.format(s, format);
        } catch (IllegalFormatException e)
        {
            return "Format error: " + s;
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper gui = helpers.getGuiHelper();
        registry.addRecipeCategories(new LatheRecipeCategory(gui));
    }

    @Override
    public void register(IModRegistry registry)
    {
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

        registry.addRecipes(LatheRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.LATHE);
        registry.addRecipeClickArea(GUILatheMachine.class, 62, 35, 70, 26, RecipeCategories.LATHE);
        transferRegistry.addRecipeTransferHandler(ContainerLatheMachine.class, RecipeCategories.LATHE, 0, 1, 1, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.latheMachine), RecipeCategories.LATHE);
    }
}
