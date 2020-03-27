package cassiokf.industrialrenewal.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public class LatheRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    private final Item input;
    private final ItemStack output;
    private final int processTime;

    public LatheRecipe(Item input, ItemStack output, int processTime)
    {
        this.input = input;
        this.output = output;
        this.processTime = processTime;

        //this.setRegistryName(References.MODID,"lathe");

        // This output is not required, but it can be used to detect when a recipe has been
        // loaded into the game.
        System.out.println("Loaded " + this.toString());
    }

    public int getProcessTime()
    {
        return processTime;
    }

    public Item getInput()
    {
        return this.input;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        return this.input.equals(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
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

    public static class Factory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json)
        {
            final Item input = getOredictItemStack(context, json, "input").getItem();
            final ItemStack result = getOredictItemStack(context, json, "result");
            final int processTime = JsonUtils.getInt(json, "process_time");
            return new LatheRecipe(input, result, processTime);
        }

        private ItemStack getOredictItemStack(final JsonContext context, final JsonObject json, String memberName)
        {
            JsonObject jsonObject = JsonUtils.getJsonObject(json, memberName);
            if (jsonObject.has("ore"))
            {
                String ore = jsonObject.get("ore").getAsString();
                if (OreDictionary.doesOreNameExist(ore))
                {
                    List<ItemStack> stackList = OreDictionary.getOres(ore);
                    if (!stackList.isEmpty())
                    {
                        ItemStack stack = stackList.get(0);
                        if (jsonObject.has("count"))
                        {
                            int count = jsonObject.get("count").getAsInt();
                            stack.setCount(count);
                        }
                        return stack;
                    }
                }
                throw new JsonSyntaxException("Unknown oreDict item '" + ore + "'");
            }
            return CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, memberName), context);
        }
    }
}
