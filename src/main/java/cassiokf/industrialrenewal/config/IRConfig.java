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
    public static double electricFenceKnockBack;
    public static double alarmVolume;
    public static boolean pumpInfinityWater;
    public static int barrelCapacity;
    public static int fluidCartCapacity;
    public static int batteryBankCapacity;
    public static int batteryBankMaxInput;
    public static int batteryBankMaxOutput;
    public static int medKitEffectDuration;
    public static int temperatureScale;
    public static int steamBoilerConvertionFactor;
    public static boolean fireExtinguisheronNether;

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
        Property propertyStartWithManual = config.get(CATEGORY_NAME_OPTIONS, "StartWithManual", true);
        propertyStartWithManual.setLanguageKey("gui.config.recipes.startwithmanual.name");
        propertyStartWithManual.setComment("Turn On/Off the manual item on first spawn (Default true) (WIP not working yet)");

        Property propertyElectricFenceDamageType = config.get(CATEGORY_NAME_OPTIONS, "ElectricFenceDamageMode", 0);
        propertyElectricFenceDamageType.setLanguageKey("gui.config.electric_fence_damage_type.name");
        propertyElectricFenceDamageType.setComment("'0': Do damage only to monsters and only knockback players. '1': Do damage to monsters and player. '2': do only knockback to all living things. '3': do damage to all living things. '4': normal fence (Default 0)");

        Property propertyElectricFenceDamageAmount = config.get(CATEGORY_NAME_OPTIONS, "ElectricFenceDamageAmount", 2.0);
        propertyElectricFenceDamageAmount.setLanguageKey("gui.config.electric_fence_damage_amount.name");
        propertyElectricFenceDamageAmount.setComment("The amount of damage the fence would cause (Default 2.0 '1 heart')");

        Property propertyElectricFenceKnockBack = config.get(CATEGORY_NAME_OPTIONS, "ElectricFenceKnockBack", 0.3);
        propertyElectricFenceKnockBack.setLanguageKey("gui.config.electric_fence_knockback_amount.name");
        propertyElectricFenceKnockBack.setComment("The amount of knockback the fence would cause (Default 0.3)");

        Property propertyAlarmVolume = config.get(CATEGORY_NAME_OPTIONS, "AlarmVolume", 4.0);
        propertyAlarmVolume.setLanguageKey("gui.config.alarm_volume.name");
        propertyAlarmVolume.setComment("The volume of the alarm (Default 4.0)");

        Property propertyPumpInfinityWater = config.get(CATEGORY_NAME_OPTIONS, "pumpinfinitywater", true);
        propertyPumpInfinityWater.setLanguageKey("gui.config.pump_infinity_water.name");
        propertyPumpInfinityWater.setComment("If pumps will not consume the water");

        Property propertyBarrelCapacity = config.get(CATEGORY_NAME_OPTIONS, "BarrelCapacity", 64000);
        propertyBarrelCapacity.setLanguageKey("gui.config.barrel_capacity.name");
        propertyBarrelCapacity.setComment("The capacity of the barrel (Default 64000)");

        Property propertyFluidContainerCapacity = config.get(CATEGORY_NAME_OPTIONS, "FluidCartCapacity", 64000);
        propertyFluidContainerCapacity.setLanguageKey("gui.config.fluidcontainer_capacity.name");
        propertyFluidContainerCapacity.setComment("The capacity of the Fluid Container Cart (Default 64000)");

        Property propertyBatteryBankCapacity = config.get(CATEGORY_NAME_OPTIONS, "BatteryBankCapacity", 1000000);
        propertyBatteryBankCapacity.setLanguageKey("gui.config.batterybank_capacity.name");
        propertyBatteryBankCapacity.setComment("The capacity of the Battery Bank (Default 1000000)");

        Property propertyBatteryBankMaxInput = config.get(CATEGORY_NAME_OPTIONS, "BatteryBankMaxInput", 10240);
        propertyBatteryBankMaxInput.setLanguageKey("gui.config.batterybank_input.name");
        propertyBatteryBankMaxInput.setComment("The Max Input of the Battery Bank (Default 10240)");

        Property propertyBatteryBankMaxOutput = config.get(CATEGORY_NAME_OPTIONS, "BatteryBankMaxOutput", 10240);
        propertyBatteryBankMaxOutput.setLanguageKey("gui.config.batterybank_output.name");
        propertyBatteryBankMaxOutput.setComment("The Max Output of the Battery Bank (Default 10240)");

        Property propertyMedKitDuration = config.get(CATEGORY_NAME_OPTIONS, "MedikitDuration", 150);
        propertyMedKitDuration.setLanguageKey("gui.config.medkit_duration.name");
        propertyMedKitDuration.setComment("The time in miliseconds the medkit will regenerate players hearth (Default 150)");

        Property propertyTemperatureMode = config.get(CATEGORY_NAME_OPTIONS, "TemperatureMode", 0);
        propertyTemperatureMode.setLanguageKey("gui.config.temp_mode.name");
        propertyTemperatureMode.setComment("'0': Celsius '1': Fahrenheit '2': Kelvin (Default 0)");

        Property propertySteamBoilerConversion = config.get(CATEGORY_NAME_OPTIONS, "SteamConversionFactor", 10);
        propertySteamBoilerConversion.setLanguageKey("gui.config.steam_factor.name");
        propertySteamBoilerConversion.setComment(" The factor steam will be generated ez: 1 Water : 10 Steam (note: real life is 1700) (Default 10)");

        Property propertyFireExtinguisherNether = config.get(CATEGORY_NAME_OPTIONS, "NetherExtinguisher", true);
        propertyFireExtinguisherNether.setLanguageKey("gui.config.nether_extinguisher.name");
        propertyFireExtinguisherNether.setComment("If player can use fire extinguisher on nether lava");

        //Recipes
        Property propertyRecipeSpongeIron = config.get(CATEGORY_NAME_RECIPES, "spongeiron_recipe", true);
        propertyRecipeSpongeIron.setLanguageKey("gui.config.recipes.spongeiron_recipe.name");
        propertyRecipeSpongeIron.setComment("Turn On/Off the sponge iron recipe (Default true)");
        //End of properties

        List<String> propertyOrder = new ArrayList<String>();
        //Order
        propertyOrder.add(propertyStartWithManual.getName());
        propertyOrder.add(propertyElectricFenceDamageType.getName());
        propertyOrder.add(propertyElectricFenceDamageAmount.getName());
        propertyOrder.add(propertyElectricFenceKnockBack.getName());
        propertyOrder.add(propertyAlarmVolume.getName());
        propertyOrder.add(propertyPumpInfinityWater.getName());
        propertyOrder.add(propertyBarrelCapacity.getName());
        propertyOrder.add(propertyFluidContainerCapacity.getName());
        propertyOrder.add(propertyBatteryBankCapacity.getName());
        propertyOrder.add(propertyBatteryBankMaxInput.getName());
        propertyOrder.add(propertyBatteryBankMaxOutput.getName());
        propertyOrder.add(propertyMedKitDuration.getName());
        propertyOrder.add(propertyTemperatureMode.getName());
        propertyOrder.add(propertySteamBoilerConversion.getName());

        propertyOrder.add(propertyRecipeSpongeIron.getName());

        //End order
        config.setCategoryPropertyOrder(CATEGORY_NAME_OPTIONS, propertyOrder);
        config.setCategoryPropertyOrder(CATEGORY_NAME_RECIPES, propertyOrder);

        if (readFieldsFromConfig) {
            spongeIronRecipeActive = propertyRecipeSpongeIron.getBoolean();
            startWithManual = propertyStartWithManual.getBoolean();
            electricFenceMode = propertyElectricFenceDamageType.getInt();
            electricFenceKnockBack = propertyElectricFenceKnockBack.getDouble();
            alarmVolume = propertyAlarmVolume.getDouble();
            pumpInfinityWater = propertyPumpInfinityWater.getBoolean();
            electricFenceDamageAmount = propertyElectricFenceDamageAmount.getDouble();
            barrelCapacity = propertyBarrelCapacity.getInt();
            fluidCartCapacity = propertyFluidContainerCapacity.getInt();
            batteryBankCapacity = propertyBatteryBankCapacity.getInt();
            batteryBankMaxInput = propertyBatteryBankMaxInput.getInt();
            batteryBankMaxOutput = propertyBatteryBankMaxOutput.getInt();
            medKitEffectDuration = propertyMedKitDuration.getInt();
            temperatureScale = propertyTemperatureMode.getInt();
            steamBoilerConvertionFactor = propertySteamBoilerConversion.getInt();
        }

        propertyStartWithManual.set(startWithManual);
        propertyRecipeSpongeIron.set(spongeIronRecipeActive);
        propertyElectricFenceDamageType.set(electricFenceMode);
        propertyElectricFenceDamageAmount.set(electricFenceDamageAmount);
        propertyElectricFenceKnockBack.set(electricFenceKnockBack);
        propertyAlarmVolume.set(alarmVolume);
        propertyPumpInfinityWater.set(pumpInfinityWater);
        propertyBarrelCapacity.set(barrelCapacity);
        propertyFluidContainerCapacity.set(fluidCartCapacity);
        propertyBatteryBankCapacity.set(batteryBankCapacity);
        propertyBatteryBankMaxInput.set(batteryBankMaxInput);
        propertyBatteryBankMaxOutput.set(batteryBankMaxOutput);
        propertyMedKitDuration.set(medKitEffectDuration);
        propertyTemperatureMode.set(temperatureScale);
        propertySteamBoilerConversion.set(steamBoilerConvertionFactor);

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
