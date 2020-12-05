package cassiokf.industrialrenewal.recipes;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class LatheRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    public static final List<LatheRecipe> LATHE_RECIPES = new CopyOnWriteArrayList<>();
    public static final Map<Item, LatheRecipe> CACHED_RECIPES = new HashMap<>();

    private final List<ItemStack> input;
    private final ItemStack output;
    private final int processTime;

    public LatheRecipe(List<ItemStack> input, ItemStack output, int processTime)
    {
        this.input = input;
        this.output = output;
        this.processTime = processTime;

        //this.setRegistryName(References.MODID,"lathe");

        // This output is not required, but it can be used to detect when a recipe has been
        // loaded into the game.
        IndustrialRenewal.LOGGER.info("Loaded " + this.output.getDisplayName() + " Lathe Recipe");
    }

    public int getProcessTime()
    {
        return processTime;
    }

    public static void populateLatheRecipes()
    {
        int recipesAmount = 0;
        for (IRecipe recipe : ForgeRegistries.RECIPES)
        {
            if (recipe instanceof LatheRecipe)
            {
                //List<Item> list = LatheRecipe.inputToItemList(((LatheRecipe) recipe).input);
                LATHE_RECIPES.add((LatheRecipe) recipe);
                recipesAmount++;
                for (ItemStack item : ((LatheRecipe) recipe).input)
                {
                    CACHED_RECIPES.put(item.getItem(), (LatheRecipe) recipe);
                }
            }
        }

        IndustrialRenewal.LOGGER.info(TextFormatting.GREEN + References.NAME + " Registered " + recipesAmount + " Recipes for Lathe Machine");
    }

    @Override
    public boolean matches(IInventory inv, World worldIn)
    {
        return this.input.equals(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv)
    {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return this.output.copy();
    }

    public List<ItemStack> getInput()
    {
        return this.input;
    }

    public static class Factory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json)
        {
            final List<ItemStack> input = getOredictItemStack(context, json, "input");
            final ItemStack result = getOredictItemStack(context, json, "result").get(0);
            final int processTime = JsonUtils.getInt(json, "process_time");
            return new LatheRecipe(input, result, processTime);
        }

        private List<ItemStack> getOredictItemStack(final JsonContext context, final JsonObject json, String memberName)
        {
            JsonObject jsonObject = JsonUtils.getJsonObject(json, memberName);
            List<ItemStack> list = new ArrayList<>();
            if (jsonObject.has("ore"))
            {
                String ore = jsonObject.get("ore").getAsString();
                if (OreDictionary.doesOreNameExist(ore))
                {
                    List<ItemStack> list2 = OreDictionary.getOres(ore);
                    if (!list2.isEmpty())
                    {
                        for (ItemStack stack : list2)
                        {
                            list.add(stack.copy());
                        }

                        if (jsonObject.has("count"))
                        {
                            int count = jsonObject.get("count").getAsInt();
                            for (ItemStack stack : list)
                            {
                                stack.setCount(count);
                            }
                        }
                        return list;
                    }
                }
                throw new JsonSyntaxException("Unknown oreDict item '" + ore + "'");
            }

            list.add(CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, memberName), context));
            return list;
        }
    }
}
