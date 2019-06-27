package cassiokf.industrialrenewal.recipes;

import cassiokf.industrialrenewal.config.IRConfig;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class ConditionFactoryRecipeSpongeIron implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        boolean value = JsonUtils.getBoolean(json, "value", true);
        return () -> IRConfig.MainConfig.Recipes.spongeIronRecipeActive == value;
    }
}
