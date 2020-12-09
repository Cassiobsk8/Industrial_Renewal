package cassiokf.industrialrenewal.util.compat.jei;

import cassiokf.industrialrenewal.gui.GUILatheMachine;
import cassiokf.industrialrenewal.gui.container.ContainerLatheMachine;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.util.compat.jei.lathe.LatheRecipeCategory;
import cassiokf.industrialrenewal.util.compat.jei.lathe.LatheRecipeMaker;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@JEIPlugin
public class JEICompat implements IModPlugin
{
    private static final List<ItemStack> blackListedItems = new CopyOnWriteArrayList<ItemStack>();

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

    private void blackListItems(IJeiHelpers helpers)
    {
        blackListedItems.clear();
        blackListedItems.add(new ItemStack(ModItems.pointer));
        blackListedItems.add(new ItemStack(ModItems.limiter));
        blackListedItems.add(new ItemStack(ModItems.pointerLong));
        blackListedItems.add(new ItemStack(ModItems.fire));
        blackListedItems.add(new ItemStack(ModItems.barLevel));
        blackListedItems.add(new ItemStack(ModItems.fluidLoaderArm));
        blackListedItems.add(new ItemStack(ModItems.tambor));
        blackListedItems.add(new ItemStack(ModItems.cutter));
        blackListedItems.add(new ItemStack(ModItems.indicator_on));
        blackListedItems.add(new ItemStack(ModItems.indicator_off));
        blackListedItems.add(new ItemStack(ModItems.switch_on));
        blackListedItems.add(new ItemStack(ModItems.switch_off));
        blackListedItems.add(new ItemStack(ModItems.push_button));
        blackListedItems.add(new ItemStack(ModItems.label_5));
        blackListedItems.add(new ItemStack(ModItems.discR));

        for (ItemStack item : blackListedItems)
        {
            helpers.getItemBlacklist().addItemToBlacklist(item);
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

        blackListItems(jeiHelpers);

        registry.addRecipes(LatheRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.LATHE);
        registry.addRecipeClickArea(GUILatheMachine.class, 62, 35, 70, 26, RecipeCategories.LATHE);
        transferRegistry.addRecipeTransferHandler(ContainerLatheMachine.class, RecipeCategories.LATHE, 0, 1, 1, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.latheMachine), RecipeCategories.LATHE);
    }
}
