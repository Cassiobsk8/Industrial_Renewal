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

    public static final String CATEGORY_NAME_OPTIONS = "Options";
    public static final String CATEGORY_NAME_RECIPES = "Recipes";
    public static boolean spongeIronRecipeActive; //Refer to this to get the config boolean
    public static boolean startWithManual;
    public static int electricFenceMode;
    public static double electricFenceDamageAmount;
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
        Property propertyStartWithManual = config.get(CATEGORY_NAME_OPTIONS, "start with manual", true);
        propertyStartWithManual.setLanguageKey("gui.config.recipes.startwithmanual.name");
        propertyStartWithManual.setComment("Turn On/Off the manual item on first spawn (Default true) (WIP not working yet)");

        Property propertyElectricFenceDamageType = config.get(CATEGORY_NAME_OPTIONS, "Electric Fence Damage Mode", 0);
        propertyElectricFenceDamageType.setLanguageKey("gui.config.electric_fence_damage_type.name");
        propertyElectricFenceDamageType.setComment("'0': Do damage only to monsters and only knockback players. '1': Do damage to monsters and player. '2': do only knockback to all living things. '3': do damage to all living things (Default 0)");

        Property propertyElectricFenceDamageAmount = config.get(CATEGORY_NAME_OPTIONS, "Electric Fence Damage Amount", 2.0);
        propertyElectricFenceDamageAmount.setLanguageKey("gui.config.electric_fence_damage_amount.name");
        propertyElectricFenceDamageAmount.setComment("The amount of damage the fence would cause (Default 2.0 '1 heart')");

        Property propertyRecipeSpongeIron = config.get(CATEGORY_NAME_RECIPES, "spongeiron_recipe", true);
        propertyRecipeSpongeIron.setLanguageKey("gui.config.recipes.spongeiron_recipe.name");
        propertyRecipeSpongeIron.setComment("Turn On/Off the sponge iron recipe (Default true)"); //TODO See whats going wrong
        //End of properties

        List<String> propertyOrderRecipes = new ArrayList<String>();
        //Order
        propertyOrderRecipes.add(propertyStartWithManual.getName());
        propertyOrderRecipes.add(propertyElectricFenceDamageType.getName());
        propertyOrderRecipes.add(propertyElectricFenceDamageAmount.getName());
        propertyOrderRecipes.add(propertyRecipeSpongeIron.getName());

        //End order
        config.setCategoryPropertyOrder(CATEGORY_NAME_OPTIONS, propertyOrderRecipes);
        config.setCategoryPropertyOrder(CATEGORY_NAME_RECIPES, propertyOrderRecipes);

        if (readFieldsFromConfig) {
            spongeIronRecipeActive = propertyRecipeSpongeIron.getBoolean();
            startWithManual = propertyStartWithManual.getBoolean();
            electricFenceMode = propertyElectricFenceDamageType.getInt();
            electricFenceDamageAmount = propertyElectricFenceDamageAmount.getDouble();
        }

        propertyStartWithManual.set(startWithManual);
        propertyRecipeSpongeIron.set(spongeIronRecipeActive);
        propertyElectricFenceDamageType.set(electricFenceMode);
        propertyElectricFenceDamageAmount.set(electricFenceDamageAmount);

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
