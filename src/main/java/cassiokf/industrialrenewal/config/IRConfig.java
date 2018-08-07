package cassiokf.industrialrenewal.config;


import cassiokf.industrialrenewal.References;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IRConfig {

    public static final String CATEGORY_NAME_RECIPES = "Recipes";
    public static boolean spongeIronRecipeActive; //Refer to this to get the config boolean
    private static Configuration config = null;

    public static void preInit() {
        File configFile = new File(Loader.instance().getConfigDir(), "IndustrialRenewal.cfg");
        config = new Configuration(configFile);
        syncFromFiles();
    }

    public static Configuration getConfig() {
        return config;
    }

    public static void clientPreInit() {
        MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
    }

    public static void syncFromFiles() {
        syncConfig(true, true);
    }

    public static void syncFromGui() {
        syncConfig(false, true);
    }

    public static void syncFromFields() {
        syncConfig(false, false);
    }

    private static void syncConfig(boolean loadFromConfigFile, boolean readFieldsFromConfig) {
        if (loadFromConfigFile) {
            config.load();
        }

        //All the properties
        Property propertyRecipeSpongeIron = config.get(CATEGORY_NAME_RECIPES, "spongeiron_recipe", true);
        propertyRecipeSpongeIron.setLanguageKey("gui.config.recipes.spongeiron_recipe.name");
        propertyRecipeSpongeIron.setComment("gui.config.recipes.spongeiron_recipe.comment");
        //End of properties

        List<String> propertyOrderRecipes = new ArrayList<String>();
        //Order
        propertyOrderRecipes.add(propertyRecipeSpongeIron.getName());

        //End order
        config.setCategoryPropertyOrder(CATEGORY_NAME_RECIPES, propertyOrderRecipes);

        if (readFieldsFromConfig) {
            spongeIronRecipeActive = propertyRecipeSpongeIron.getBoolean();
        }

        propertyRecipeSpongeIron.set(spongeIronRecipeActive);

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static class ConfigEventHandler {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(References.MODID)) {
                syncFromGui();
            }
        }
    }

}
