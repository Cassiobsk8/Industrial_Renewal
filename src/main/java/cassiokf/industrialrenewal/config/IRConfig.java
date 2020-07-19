package cassiokf.industrialrenewal.config;

import cassiokf.industrialrenewal.References;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.HashMap;
import java.util.Map;

public class IRConfig {
    public static Map<String, Integer> getFuelHash()
    {
        Map<String, Integer> fluidFuel = new HashMap<String, Integer>();
        fluidFuel.put("lava", 200);
        fluidFuel.put("rocket_fuel", 3200);
        fluidFuel.put("pyrotheum", 3200);
        fluidFuel.put("refined_fuel", 1500);
        fluidFuel.put("ic2biogas", 300);
        fluidFuel.put("crude_oil", 500);
        fluidFuel.put("refined_oil", 1000);
        fluidFuel.put("oil", 500);
        fluidFuel.put("oil_heavy", 600);
        fluidFuel.put("oil_dense", 800);
        fluidFuel.put("oil_distilled", 1200);
        fluidFuel.put("coal", 400);
        fluidFuel.put("refined_biofuel", 900);
        fluidFuel.put("bio_diesel", 600);
        fluidFuel.put("biodiesel", 600);
        fluidFuel.put("diesel", 900);
        fluidFuel.put("fuel", 1500);
        fluidFuel.put("fluiddiesel", 900);
        fluidFuel.put("fluidnitrodiesel", 1600);
        fluidFuel.put("empoweredoil", 700);
        fluidFuel.put("kerosene", 1500);
        fluidFuel.put("lpg", 1800);
        fluidFuel.put("gasoline", 1200);
        fluidFuel.put("fire_water", 1200);
        fluidFuel.put("ethanol", 900);
        fluidFuel.put("bio.ethanol", 900);
        return fluidFuel;
    }

    public static String[] waterTypesInit()
    {
        return new String[]{FluidRegistry.WATER.getName()};
    }

    public static Fluid getWaterFromSteamFluid()
    {
        Fluid fluid = FluidRegistry.getFluid(MainConfig.Main.waterFromSteam);
        return fluid != null ? fluid : FluidRegistry.WATER;
    }

    public static boolean waterTypesContains(String name)
    {
        for (String c : MainConfig.Main.boilerWaterNames)
        {
            if (c.equals(name)) return true;
        }
        return false;
    }

    /////////////////////NEWWWWW
    @Config(modid = References.MODID, type = Config.Type.INSTANCE, name = References.MODID)
    @Config.LangKey("gui.config.main_title")
    public static class MainConfig
    {
        @Config.Comment("Main")
        @Config.LangKey("gui.config.main_title")
        public static final SubCategoryMain Main = new SubCategoryMain();

        @Config.Comment("Sounds")
        public static final SubCategorySound Sounds = new SubCategorySound();

        @Config.Comment("Render Configurations")
        public static final SubCategoryRender Render = new SubCategoryRender();

        @Config.Comment("Recipes Configurations")
        @Config.LangKey("gui.config.category.recipes")
        @Config.RequiresMcRestart
        public static final SubCategoryRecipes Recipes = new SubCategoryRecipes();

        public static class SubCategoryMain
        {
            @Config.Comment("Turn On/Off the manual item on first spawn (Default true) (WIP not working yet)")
            @Config.LangKey("gui.config.recipes.startwithmanual.name")
            public boolean startWithManual = true;

            @Config.Comment("'0': Do damage only to monsters and only knockback players. '1': Do damage to monsters and player. '2': do only knockback to all living things. '3': do damage to all living things. '4': normal fence (Default 0)")
            @Config.LangKey("gui.config.electric_fence_damage_type.name")
            public int electricFenceMode = 0;

            @Config.Comment("The amount of damage the fence would cause (Default 2.0 '1 heart')")
            @Config.LangKey("gui.config.electric_fence_damage_amount.name")
            public double electricFenceDamageAmount = 2.0;

            @Config.Comment("The amount of knockback the fence would cause (Default 0.3)")
            @Config.LangKey("gui.config.electric_fence_knockback_amount.name")
            public double electricFenceKnockBack = 0.3;

            @Config.Comment("If pumps will not consume the water (Default true)")
            @Config.LangKey("gui.config.pump_infinity_water.name")
            public boolean pumpInfinityWater = true;

            @Config.Comment("How much will pump consume per tick (Default 10 FE/t)")
            public int pumpEnergyPerTick = 10;

            @Config.Comment("The capacity of the barrel (Default 64000)")
            @Config.LangKey("gui.config.barrel_capacity.name")
            public int barrelCapacity = 64000;

            @Config.Comment("The capacity of the Portable Generator (Default 16000)")
            public int portableGeneratorCapacity = 16000;

            @Config.Comment("The amount of energy the Portable Generator generates per tick (Default 32)")
            public int portableGeneratorEnergyPerTick = 32;

            @Config.Comment("The capacity of the Fluid Container Cart (Default 64000)")
            @Config.LangKey("gui.config.fluidcontainer_capacity.name")
            public int fluidCartCapacity = 64000;

            @Config.Comment("The capacity of the Battery Bank (Default 1000000)")
            @Config.LangKey("gui.config.batterybank_capacity.name")
            public int batteryBankCapacity = 1000000;

            @Config.Comment("The Max Input of the Battery Bank (Default 10240)")
            @Config.LangKey("gui.config.batterybank_input.name")
            public int batteryBankMaxInput = 10240;

            @Config.Comment("The Max Output of the Battery Bank (Default 10240)")
            @Config.LangKey("gui.config.batterybank_output.name")
            public int batteryBankMaxOutput = 10240;

            @Config.Comment("If renders a indicator to show wat pipe/cable is master 'the one who controls all other connected pipes' (Default: false)")
            public boolean showMaster = false;

            @Config.Comment("The Max Output and Input of the LV Energy cable per connector (Default 256)")
            public int maxLVEnergyCableTransferAmount = 256;

            @Config.Comment("The Max Output and Input of the MV Energy cable per connector (Default 1024)")
            public int maxMVEnergyCableTransferAmount = 1024;

            @Config.Comment("The Max Output and Input of the HV Energy cable per connector (Default 10240)")
            public int maxHVEnergyCableTransferAmount = 10240;

            @Config.Comment("The Max Output and Input of the HV Transformer (Default 10240)")
            public int maxHVTransformerTransferAmount = 10240;

            @Config.Comment("The Max Length of the HV Wire (Default 64)")
            public int maxHVWireLength = 64;

            @Config.Comment("The Max Output and Input of the Fluid Pipe per connector (Default 500)")
            public int maxFluidPipeTransferAmount = 500;

            @Config.Comment("The time in miliseconds the medkit will regenerate players hearth (Default 150)")
            @Config.LangKey("gui.config.medkit_duration.name")
            public int medKitEffectDuration = 150;

            @Config.Comment("'0': Celsius '1': Fahrenheit '2': Kelvin (Default 0)")
            @Config.LangKey("gui.config.temp_mode.name")
            public int temperatureScale = 0;

            @Config.Comment("The type of water used in the boiler (Default 'water')")
            public String[] boilerWaterNames = waterTypesInit();

            @Config.Comment("The type of water it will turn from steam (Default 'water')")
            public String waterFromSteam = FluidRegistry.WATER.getName();

            @Config.Comment("How much water the Steam Boiler will consume to produce steam (Default 76)")
            public int steamBoilerWaterPerTick = 76;

            @Config.Comment("The factor steam will be generated 1 Water : 5 Steam ex: 76 water/tick * 5 is 380 steam/tick per boiler (Default 5)")
            @Config.LangKey("gui.config.steam_factor.name")
            public int steamBoilerConversionFactor = 5;

            @Config.Comment("How much steam the Steam Turbine need to rotate (Default 250)")
            public int steamTurbineSteamPerTick = 250;

            @Config.Comment("How much Energy the Steam Turbine will produce at full speed (Default 512)")
            public int steamTurbineEnergyPerTick = 512;

            @Config.Comment("If player can use fire extinguisher on nether lava (Default true)")
            @Config.LangKey("gui.config.nether_extinguisher.name")
            public boolean fireExtinguisherOnNether = true;

            @Config.Comment("The amount of damage the razor wire would cause (Default 2.0 '1 heart')")
            public float razorWireDamage = 2.0f;

            @Config.Comment("Fluid fuels and its combustion value per 1 Bucket (Default: {lava=200, rocket_fuel=1120, pyrotheum=3200, refined_fuel=1500, ic2biogas=300, crude_oil=500, refined_oil=1000, coal=400, refined_biofuel=900, bio_diesel=600, biodiesel=600, diesel=900, fuel=1500, fluiddiesel=900, fluidnitrodiesel=1600, empoweredoil=700, kerosene=1500, lpg=1800, gasoline=1200, fire_water=1200, ethanol=900, bio.ethanol=900})")
            @Config.LangKey("gui.config.fluidFuel.name")
            public Map<String, Integer> fluidFuel = getFuelHash();

            @Config.Comment("true if Chunk Load Needs the player to get online or it will be deactivate (Default true)")
            public boolean needPlayerToActivateChunkLoading = true;

            @Config.LangKey("gui.config.hours_before_deactivation.name")
            @Config.Comment("The number of hours the player will be offline before Chunk Loading deactivating (default 2 days)")
            public int hoursBeforeChunkLoadingDeactivation = 2 * 24; // 2 days by default.

            @Config.LangKey("gui.config.chunk_loader_width.name")
            @Config.Comment("Width/length of chunks to be loaded, it is recommend this is an odd number (Default 3)")
            public int chunkLoaderWidth = 3;

            @Config.LangKey("gui.config.emergency_mode.name")
            @Config.Comment("Disables Chunk loading, use if there is a crash happening in a chunk loaded area (Default false)")
            public boolean emergencyMode = false;

            @Config.LangKey("gui.config.blade_durability.name")
            @Config.Comment("How many hours will a blade last (default 48 hours)")
            public int ironBladeDurability = 48 * 60;

            @Config.Comment("The amount of FE/t a base solar panel can generate (Default 15)")
            public int baseSolarPanelMaxGeneration = 15;

            @Config.Comment("How much the frame multiplies the solar panel generation contained in it (Default 2)")
            public int panelFrameMultiplier = 2;

            @Config.Comment("Max energy generated by small wind turbines (default 128 fe/t)")
            public int maxEnergySWindTurbine = 128;

            @Config.Comment("Max radius of the fluid pump (Default: 8)")
            public int maxPumpRadius = 8;

            @Config.Comment("Pump replace the lava with cobblestone for lag reasons (default: true)")
            public boolean repleceLavaWithCobble = true;

            @Config.Comment("How much Energy the Lathe Machine will require for each tick (Default 128)")
            public int energyPerTickLatheMachine = 128;
        }

        public static class SubCategorySound
        {
            @Config.Comment("Master volume Multiplier for all the machine (default 1.0f)")
            public float masterVolumeMult = 1.0f;

            @Config.Comment("The volume of the alarm (Default 4.0)")
            @Config.LangKey("gui.config.alarm_volume.name")
            public double alarmVolume = 4.0;

            @Config.Comment("Base volume of the Pump (default 0.4f)")
            public float pumpVolume = 0.4f;

            @Config.Comment("Base volume of the Portable Generator (default 0.6f)")
            public float genVolume = 0.6f;

            @Config.Comment("Base volume of the Steam Turbine (default 0.8f)")
            public float TurbineVolume = 0.8f;
        }

        public static class SubCategoryRender
        {
            @Config.Comment("The multiplier for the Wind Blades render distance (Default: 4D)")
            public double windBladesRenderDistanceMult = 4.0D;

            @Config.Comment("The multiplier for the Solar pane render distance on frames (Default: 2D)")
            public double frameSolarPanelRenderMult = 2.0D;
        }

        public static class SubCategoryRecipes
        {
            @Config.LangKey("gui.config.recipes.spongeiron_recipe.name")
            public boolean spongeIronRecipeActive = true;

            @Config.Comment("Sponge iron smelt into IndR Steel ingot? (default false)")
            public boolean spongeDefaultIngotSteel = false;
        }
    }
}
