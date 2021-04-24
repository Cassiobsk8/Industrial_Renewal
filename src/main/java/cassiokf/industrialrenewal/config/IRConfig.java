package cassiokf.industrialrenewal.config;

import cassiokf.industrialrenewal.References;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IRConfig
{
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Main MAIN;

    static
    {
        final Pair<Main, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Main::new);
        COMMON_SPEC = specPair.getRight();
        MAIN = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event)
    {

    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading event)
    {

    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == COMMON_SPEC) {
           // bakeConfig();
        }
    }

    public static class Main
    {
        //MAIN
        public static ForgeConfigSpec.BooleanValue debugMessages;
        public static ForgeConfigSpec.BooleanValue startWithManual;
        public static ForgeConfigSpec.BooleanValue searchBarStartFocused;
        public static ForgeConfigSpec.IntValue electricFenceMode;
        public static ForgeConfigSpec.DoubleValue electricFenceDamageAmount;
        public static ForgeConfigSpec.DoubleValue electricFenceKnockBack;
        public static ForgeConfigSpec.BooleanValue pumpInfinityWater;
        public static ForgeConfigSpec.IntValue barrelCapacity;
        public static ForgeConfigSpec.IntValue fluidTankCapacity;
        public static ForgeConfigSpec.IntValue portableGeneratorCapacity;
        public static ForgeConfigSpec.IntValue portableGeneratorEnergyPerTick;
        public static ForgeConfigSpec.IntValue fluidCartCapacity;
        public static ForgeConfigSpec.IntValue batteryBankCapacity;
        public static ForgeConfigSpec.IntValue batteryBankMaxInput;
        public static ForgeConfigSpec.IntValue batteryBankMaxOutput;
        public static ForgeConfigSpec.IntValue lithiumBatteryCapacity;
        public static ForgeConfigSpec.IntValue industrialBatteryBankMaxTransfer;
        public static ForgeConfigSpec.BooleanValue showMaster;
        public static ForgeConfigSpec.IntValue maxLVEnergyCableTransferAmount;
        public static ForgeConfigSpec.IntValue maxMVEnergyCableTransferAmount;
        public static ForgeConfigSpec.IntValue maxHVEnergyCableTransferAmount;
        public static ForgeConfigSpec.IntValue maxHVTransformerTransferAmount;
        public static ForgeConfigSpec.IntValue maxHVWireLength;
        public static ForgeConfigSpec.IntValue maxFluidPipeTransferAmount;
        public static ForgeConfigSpec.IntValue medKitEffectDuration;
        public static ForgeConfigSpec.IntValue temperatureScale;
        public static ForgeConfigSpec.ConfigValue<List<ResourceLocation>> boilerWaterNames;
        public static ForgeConfigSpec.ConfigValue<ResourceLocation> waterFromSteam;
        public static ForgeConfigSpec.IntValue steamBoilerWaterPerTick;
        public static ForgeConfigSpec.IntValue steamBoilerConversionFactor;
        public static ForgeConfigSpec.IntValue solidFuelPerFireboxTick;
        public static ForgeConfigSpec.IntValue steamTurbineSteamPerTick;
        public static ForgeConfigSpec.IntValue steamTurbineEnergyPerTick;
        public static ForgeConfigSpec.IntValue damGeneratorEnergyPerTick;
        public static ForgeConfigSpec.BooleanValue fireExtinguisherOnNether;
        public static ForgeConfigSpec.DoubleValue razorWireDamage;
        public static ForgeConfigSpec.ConfigValue<Map<String, Integer>> fluidFuel;
        public static ForgeConfigSpec.BooleanValue needPlayerToActivateChunkLoading;
        public static ForgeConfigSpec.IntValue hoursBeforeChunkLoadingDeactivation; // 2 days by default.
        public static ForgeConfigSpec.IntValue chunkLoaderWidth;
        public static ForgeConfigSpec.BooleanValue emergencyMode;
        public static ForgeConfigSpec.IntValue ironBladeDurability;
        public static ForgeConfigSpec.IntValue baseSolarPanelMaxGeneration;
        public static ForgeConfigSpec.IntValue panelFrameMultiplier;
        public static ForgeConfigSpec.IntValue maxEnergySWindTurbine;
        public static ForgeConfigSpec.IntValue maxPumpRadius;
        public static ForgeConfigSpec.BooleanValue repleceLavaWithCobble;
        public static ForgeConfigSpec.IntValue energyPerTickLatheMachine;
        public static ForgeConfigSpec.IntValue miningWaterPerTick;
        public static ForgeConfigSpec.IntValue miningEnergyPerTick;
        public static ForgeConfigSpec.IntValue miningDeepEnergyPerTick;
        public static ForgeConfigSpec.IntValue miningCooldown;
        public static ForgeConfigSpec.IntValue steelDrillMaxDamage;
        public static ForgeConfigSpec.IntValue diamondDrillMaxDamage;
        public static ForgeConfigSpec.IntValue deepVeinDrillMaxDamage;

        //SOUNDS
        public static ForgeConfigSpec.DoubleValue masterVolumeMult;
        public static ForgeConfigSpec.DoubleValue alarmVolume;
        public static ForgeConfigSpec.DoubleValue pumpVolume;
        public static ForgeConfigSpec.DoubleValue genVolume;
        public static ForgeConfigSpec.DoubleValue turbineVolume;
        public static ForgeConfigSpec.DoubleValue miningVolume;

        //Render
        public static ForgeConfigSpec.DoubleValue windBladesRenderDistanceMult;
        public static ForgeConfigSpec.DoubleValue frameSolarPanelRenderMult;
        public static ForgeConfigSpec.DoubleValue conveyorsItemsRenderMult;

        //Railroad
        public static ForgeConfigSpec.IntValue maxLoaderItemPerTick;

        //OreGen
        public static ForgeConfigSpec.BooleanValue spawnDeepVein;
        public static ForgeConfigSpec.IntValue deepVeinSpawnRate;
        public static ForgeConfigSpec.IntValue deepVeinMinOre;
        public static ForgeConfigSpec.IntValue deepVeinMaxOre;
        public static ForgeConfigSpec.ConfigValue<String> deepVeinID;
        public static ForgeConfigSpec.ConfigValue<Map<String, Integer>> deepVeinOres;

        public Main(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Industrial Renewal Main Config").push("main");
            debugMessages = builder
                    .comment("Debug messages on log")
                    .define("debugMessages", false);
            startWithManual = builder
                    .comment("Turn On/Off the manual item on first spawn (Default true) (WIP not working yet)")
                    .translation("gui.config.recipes.startwithmanual.name")
                    .define("startWithManual", true);
            searchBarStartFocused = builder
                    .comment("If the search bar on the containers will be active by default (Default: false)")
                    .define("searchBarStartFocused", false);
            electricFenceMode = builder
                    .comment("'0': Do damage only to monsters and only knockback players. '1': Do damage to monsters and player. '2': do only knockback to all living things. '3': do damage to all living things. '4': normal fence (Default 0)")
                    .translation("gui.config.electric_fence_damage_type.name")
                    .defineInRange("electricFenceMode", 0, 0, 4);
            electricFenceDamageAmount = builder
                    .comment("The amount of damage the fence would cause (Default 2.0 '1 heart')")
                    .translation("gui.config.electric_fence_damage_amount.name")
                    .defineInRange("electricFenceDamageAmount", 2.0D, 0, 100D);
            electricFenceKnockBack = builder
                    .comment("The amount of knockback the fence would cause (Default 0.3)")
                    .translation("gui.config.electric_fence_knockback_amount.name")
                    .defineInRange("electricFenceKnockBack", 0.3D, 0, 10D);
            pumpInfinityWater = builder
                    .comment("If pumps will not consume the water (Default true)")
                    .translation("gui.config.pump_infinity_water.name")
                    .define("pumpInfinityWater", true);
            barrelCapacity = builder
                    .comment("The capacity of the barrel (Default 64000)")
                    .translation("gui.config.barrel_capacity.name")
                    .defineInRange("barrelCapacity", 64000, 1, Integer.MAX_VALUE);
            fluidTankCapacity = builder
                    .comment("The capacity of the Fluid Tank (Default 1000000)")
                    .defineInRange("fluidTankCapacity", 1000000, 1, Integer.MAX_VALUE);
            portableGeneratorCapacity = builder
                    .comment("The capacity of the Portable Generator (Default 16000)")
                    .defineInRange("portableGeneratorCapacity", 16000, 1, Integer.MAX_VALUE);
            portableGeneratorEnergyPerTick = builder
                    .comment("The amount of energy the Portable Generator generates per tick (Default 32)")
                    .defineInRange("portableGeneratorEnergyPerTick", 32, 1, Integer.MAX_VALUE);
            fluidCartCapacity = builder
                    .comment("The capacity of the Fluid Container Cart (Default 64000)")
                    .translation("gui.config.fluidcontainer_capacity.name")
                    .defineInRange("fluidCartCapacity", 64000, 1, Integer.MAX_VALUE);
            batteryBankCapacity = builder
                    .comment("The capacity of the Battery Bank (Default 1000000)")
                    .translation("gui.config.batterybank_capacity.name")
                    .defineInRange("batteryBankCapacity", 1000000, 1, Integer.MAX_VALUE);
            batteryBankMaxInput = builder
                    .comment("The Max Input of the Battery Bank (Default 10240)")
                    .translation("gui.config.batterybank_input.name")
                    .defineInRange("batteryBankMaxInput", 10240, 1, Integer.MAX_VALUE);
            batteryBankMaxOutput = builder
                    .comment("The Max Output of the Battery Bank (Default 10240)")
                    .translation("gui.config.batterybank_output.name")
                    .defineInRange("batteryBankMaxOutput", 10240, 1, Integer.MAX_VALUE);
            lithiumBatteryCapacity = builder
                    .comment("The capacity of the Lithium Battery (Default 5000000)")
                    .defineInRange("lithiumBatteryCapacity", 5000000, 1, Integer.MAX_VALUE);
            industrialBatteryBankMaxTransfer = builder
                    .comment("The Max Transfer amount of the Industrial Battery Bank (Default 102400)")
                    .defineInRange("industrialBatteryBankMaxTransfer", 102400, 1, Integer.MAX_VALUE);
            showMaster = builder
                    .comment("If renders a indicator to show wat pipe/cable is master 'the one who controls all other connected pipes' (Default: false)")
                    .define("showMaster", false);
            maxLVEnergyCableTransferAmount = builder
                    .comment("The Max Output and Input of the LV Energy cable per connector (Default 256)")
                    .defineInRange("maxLVEnergyCableTransferAmount", 256, 1, Integer.MAX_VALUE);
            maxMVEnergyCableTransferAmount = builder
                    .comment("The Max Output and Input of the MV Energy cable per connector (Default 1024)")
                    .defineInRange("maxMVEnergyCableTransferAmount", 1024, 1, Integer.MAX_VALUE);
            maxHVEnergyCableTransferAmount = builder
                    .comment("The Max Output and Input of the HV Energy cable per connector (Default 10240)")
                    .defineInRange("maxHVEnergyCableTransferAmount", 10240, 1, Integer.MAX_VALUE);
            maxHVTransformerTransferAmount = builder
                    .comment("The Max Output and Input of the HV Transformer (Default 10240)")
                    .defineInRange("maxHVTransformerTransferAmount", 10240, 1, Integer.MAX_VALUE);
            maxHVWireLength = builder
                    .comment("The Max Length of the HV Wire (Default 64)")
                    .defineInRange("maxHVWireLength", 64, 1, 256);
            maxFluidPipeTransferAmount = builder
                    .comment("The Max Output and Input of the Fluid Pipe per connector (Default 500)")
                    .defineInRange("maxFluidPipeTransferAmount", 500, 1, Integer.MAX_VALUE);
            medKitEffectDuration = builder
                    .comment("The time in miliseconds the medkit will regenerate players hearth (Default 150)")
                    .translation("gui.config.medkit_duration.name")
                    .defineInRange("medKitEffectDuration", 150, 1, Integer.MAX_VALUE);
            temperatureScale = builder
                    .comment("'0': Celsius '1': Fahrenheit '2': Kelvin (Default 0)")
                    .translation("gui.config.temp_mode.name")
                    .defineInRange("temperatureScale", 0, 0, 2);
            steamBoilerWaterPerTick = builder
                    .comment("How much water the Steam Boiler will consume to produce steam (Default 76)")
                    .defineInRange("steamBoilerWaterPerTick", 76, 1, Integer.MAX_VALUE);
            boilerWaterNames = builder
                    .comment("The type of water used in the boiler (Default 'water')")
                    .define("boilerWaterNames", waterTypesInit());
            waterFromSteam = builder
                    .comment("The type of water it will turn from steam (Default 'water')")
                    .define("waterFromSteam", Fluids.WATER.getRegistryName());
            steamBoilerConversionFactor = builder
                    .comment("The factor steam will be generated 1 Water : 5 Steam ex: 76 water/tick * 5 is 380 steam/tick per boiler (Default 5)")
                    .translation("gui.config.steam_factor.name")
                    .defineInRange("steamBoilerConversionFactor", 5, 1, Integer.MAX_VALUE);
            solidFuelPerFireboxTick = builder
                    .comment("How many solid fuel ticks should be used for one firebox tick, ex: coal with 1600 / 2 is 800 ticks in the boiler (Default 2)")
                    .defineInRange("solidFuelPerFireboxTick", 2, 1, Integer.MAX_VALUE);
            steamTurbineSteamPerTick = builder
                    .comment("How much steam the Steam Turbine need to rotate (Default 250)")
                    .defineInRange("steamTurbineSteamPerTick", 250, 1, Integer.MAX_VALUE);
            steamTurbineEnergyPerTick = builder
                    .comment("How much Energy the Steam Turbine will produce at full speed (Default 512)")
                    .defineInRange("steamTurbineEnergyPerTick", 512, 1, Integer.MAX_VALUE);
            damGeneratorEnergyPerTick = builder
                    .comment("How much Energy the Dam Generator will produce at full speed (Default 1024)")
                    .defineInRange("damGeneratorEnergyPerTick", 1024, 1, Integer.MAX_VALUE);
            fireExtinguisherOnNether = builder
                    .comment("If player can use fire extinguisher on nether lava (Default true)")
                    .translation("gui.config.nether_extinguisher.name")
                    .define("fireExtinguisherOnNether", true);
            razorWireDamage = builder
                    .comment("The amount of damage the razor wire would cause (Default 2.0 '1 heart')")
                    .defineInRange("razorWireDamage", 2.0D, 0, 999D);
            fluidFuel = builder
                    .comment("Fluid fuels and its combustion value per 1 Bucket")
                    .translation("gui.config.fluidFuel.name")
                    .define("fluidFuel", getFuelHash());
            needPlayerToActivateChunkLoading = builder
                    .comment("true if Chunk Load Needs the player to get online or it will be deactivate (Default true)")
                    .define("needPlayerToActivateChunkLoading", true);
            hoursBeforeChunkLoadingDeactivation = builder
                    .comment("The number of hours the player will be offline before Chunk Loading deactivating (default 2 days)")
                    .translation("gui.config.hours_before_deactivation.name")
                    .defineInRange("hoursBeforeChunkLoadingDeactivation", 2 * 24, 1, Integer.MAX_VALUE);
            chunkLoaderWidth = builder
                    .comment("Width/length of chunks to be loaded, it is recommend this is an odd number (Default 3)")
                    .translation("gui.config.chunk_loader_width.name")
                    .defineInRange("chunkLoaderWidth", 3, 1, 9);
            emergencyMode = builder
                    .comment("Disables Chunk loading, use if there is a crash happening in a chunk loaded area (Default false)")
                    .translation("gui.config.emergency_mode.name")
                    .define("emergencyMode", false);
            ironBladeDurability = builder
                    .comment("How many hours will a blade last (default 48 hours)")
                    .translation("gui.config.blade_durability.name")
                    .defineInRange("ironBladeDurability", 48 * 60, 1, Integer.MAX_VALUE);
            baseSolarPanelMaxGeneration = builder
                    .comment("The amount of FE/t a base solar panel can generate (Default 15)")
                    .defineInRange("baseSolarPanelMaxGeneration", 15, 1, Integer.MAX_VALUE);
            panelFrameMultiplier = builder
                    .comment("How much the frame multiplies the solar panel generation contained in it (Default 2)")
                    .defineInRange("panelFrameMultiplier", 2, 1, Integer.MAX_VALUE);
            maxEnergySWindTurbine = builder
                    .comment("Max energy generated by small wind turbines (default 128 fe/t)")
                    .defineInRange("maxEnergySWindTurbine", 128, 1, Integer.MAX_VALUE);
            maxPumpRadius = builder
                    .comment("Max radius of the fluid pump (Default: 8)")
                    .defineInRange("maxPumpRadius", 8, 1, 16);
            repleceLavaWithCobble = builder
                    .comment("Pump replace the lava with cobblestone for lag reasons (default: true)")
                    .define("repleceLavaWithCobble", true);
            energyPerTickLatheMachine = builder
                    .comment("How much Energy the Lathe Machine will require for each tick (Default 128)")
                    .defineInRange("energyPerTickLatheMachine", 128, 1, Integer.MAX_VALUE);
            miningWaterPerTick = builder
                    .comment("Mining drill water consumption (default 10)")
                    .defineInRange("miningWaterPerTick", 10, 1, Integer.MAX_VALUE);
            miningEnergyPerTick = builder
                    .comment("Mining drill Energy consumption on world ores (default 768)")
                    .defineInRange("miningEnergyPerTick", 768, 1, Integer.MAX_VALUE);
            miningDeepEnergyPerTick = builder
                    .comment("Mining drill Energy consumption on Deep veins (default 1024)")
                    .defineInRange("miningDeepEnergyPerTick", 1024, 1, Integer.MAX_VALUE);
            miningCooldown = builder
                    .comment("Mining drill cooldown time between mining (default 120)")
                    .defineInRange("miningCooldown", 120, 1, Integer.MAX_VALUE);
            steelDrillMaxDamage = builder
                    .comment("Steel Drill max damage (default 400)")
                    .defineInRange("steelDrillMaxDamage", 400, 0, Integer.MAX_VALUE);
            diamondDrillMaxDamage = builder
                    .comment("Diamond Drill max damage (default 2000)")
                    .defineInRange("diamondDrillMaxDamage", 2000, 0, Integer.MAX_VALUE);
            deepVeinDrillMaxDamage = builder
                    .comment("DeepVein Drill max damage (default 12000)")
                    .defineInRange("deepVeinDrillMaxDamage", 12000, 0, Integer.MAX_VALUE);

            builder.pop();
            builder.comment("Sound").push("sound");
            masterVolumeMult = builder
                    .comment("Master volume Multiplier for all the machine (default 1.0D)")
                    .defineInRange("masterVolumeMult", 1F, 0F, 10F);
            alarmVolume = builder
                    .comment("The volume of the alarm (Default 4.0D)")
                    .defineInRange("alarmVolume", 4D, 0D, 10D);
            pumpVolume = builder
                    .comment("Base volume of the Pump (default 0.4D)")
                    .defineInRange("pumpVolume", 0.4D, 0D, 10D);
            genVolume = builder
                    .comment("Base volume of the Portable Generator (default 0.6D)")
                    .defineInRange("genVolume", 0.6D, 0D, 10D);
            turbineVolume = builder
                    .comment("Base volume of the Steam Turbine (default 0.8D)")
                    .defineInRange("turbineVolume", 0.8D, 0D, 10D);
            miningVolume = builder
                    .comment("Base volume of the Mining (default 0.8D)")
                    .defineInRange("miningVolume", 0.8D, 0D, 10D);
            builder.pop();
            builder.comment("Industrial Renewal Render Config").push("Render");

            windBladesRenderDistanceMult = builder
                    .comment("The multiplier for the Wind Blades render distance (Default: 4D)")
                    .defineInRange("windBladesRenderDistanceMult", 4D, 0.1D, 200D);
            frameSolarPanelRenderMult = builder
                    .comment("The multiplier for the Solar pane render distance on frames (Default: 2D)")
                    .defineInRange("frameSolarPanelRenderMult", 2D, 0.1D, 200D);
            conveyorsItemsRenderMult = builder
                    .comment("The multiplier for the Conveyors items render distance (Default: 1D)")
                    .defineInRange("conveyorsItemsRenderMult", 1D, 0.1D, 200D);
            builder.pop();
            builder.comment("Industrial Renewal Railroad Config").push("Railroad");

            maxLoaderItemPerTick = builder
                    .comment("Cart Item Loader items per Tick (default: 4)")
                    .defineInRange("maxLoaderItemPerTick", 4, 1, 64);

            builder.pop();
            builder.comment("Ore Generation Config").push("OreGen");

            builder.pop();
            builder.comment("Ore Generation").push("oregen");
            spawnDeepVein = builder
                    .comment("Spawn Deep Vein (Default: true)")
                    .define("spawnDeepVein", true);
            deepVeinSpawnRate = builder
                    .comment("Deep Vein spawn rate in % (Default: 5)")
                    .defineInRange("deepVeinSpawnRate",5, 0, 100);
            deepVeinMinOre = builder
                    .comment("Deep Vein min ore quantity (Default: 1000)")
                    .defineInRange("deepVeinMinOre", 1000, 1, Integer.MAX_VALUE);
            deepVeinMaxOre = builder
                    .comment("Deep Vein max ore quantity (Default: 8000)")
                    .defineInRange("deepVeinMaxOre", 8000, 1, Integer.MAX_VALUE);
            deepVeinID = builder
                    .comment("CAUTION: change this will cause DeepOreVeins to ReGenerate (Default: 'dv')")
                    .define("deepVeinID", "dv");
            deepVeinOres = builder
                    .comment("Ores to generate in Deep Vein (Oredict/id name and spawn chance)")
                    .define("deepVeinOres", getDeepVeinDefaultOres());
            builder.pop();
        }
    }
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

    public static Map<String, Integer> getDeepVeinDefaultOres()
    {
        Map<String, Integer> list = new HashMap<>();
        list.put("oreCoal", 60);
        list.put("oreCopper", 40);
        list.put("oreTin", 40);
        list.put("oreIron", 30);
        list.put("oreOsmium", 20);
        list.put("oreSilver", 20);
        list.put("oreGold", 10);
        list.put("oreNickel", 10);
        list.put("oreAluminum", 10);
        list.put("oreRedstone", 6);
        list.put("oreLapis", 6);
        list.put("oreDiamond", 1);

        return list;
    }

    public static List<ResourceLocation> waterTypesInit()
    {
        List<ResourceLocation> list = new ArrayList<>();
        list.add(Fluids.WATER.getRegistryName());
        return list;
    }

    public static Fluid getWaterFromSteamFluid()
    {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(Main.waterFromSteam.get());
        return fluid != null ? fluid : Fluids.WATER;
    }

    public static boolean waterTypesContains(ResourceLocation name)
    {
        for (ResourceLocation c : Main.boilerWaterNames.get())
        {
            if (c.equals(name)) return true;
        }
        return false;
    }
}