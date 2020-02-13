package cassiokf.industrialrenewal.config;

import cassiokf.industrialrenewal.References;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

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

    public static class Main
    {
        //MAIN
        public static ForgeConfigSpec.BooleanValue startWithManual;
        public static ForgeConfigSpec.IntValue electricFenceMode;
        public static ForgeConfigSpec.DoubleValue electricFenceDamageAmount;
        public static ForgeConfigSpec.DoubleValue electricFenceKnockBack;
        public static ForgeConfigSpec.BooleanValue pumpInfinityWater;
        public static ForgeConfigSpec.IntValue barrelCapacity;
        public static ForgeConfigSpec.IntValue fluidCartCapacity;
        public static ForgeConfigSpec.IntValue batteryBankCapacity;
        public static ForgeConfigSpec.IntValue batteryBankMaxInput;
        public static ForgeConfigSpec.IntValue batteryBankMaxOutput;
        public static ForgeConfigSpec.BooleanValue showMaster;
        public static ForgeConfigSpec.IntValue maxLVEnergyCableTransferAmount;
        public static ForgeConfigSpec.IntValue maxMVEnergyCableTransferAmount;
        public static ForgeConfigSpec.IntValue maxHVEnergyCableTransferAmount;
        public static ForgeConfigSpec.IntValue maxHVTransformerTransferAmount;
        public static ForgeConfigSpec.IntValue maxHVWireLength;
        public static ForgeConfigSpec.IntValue maxFluidPipeTransferAmount;
        public static ForgeConfigSpec.IntValue medKitEffectDuration;
        public static ForgeConfigSpec.IntValue temperatureScale;
        public static ForgeConfigSpec.IntValue steamBoilerWaterPerTick;
        public static ForgeConfigSpec.IntValue steamBoilerConversionFactor;
        public static ForgeConfigSpec.IntValue steamTurbineSteamPerTick;
        public static ForgeConfigSpec.IntValue steamTurbineEnergyPerTick;
        public static ForgeConfigSpec.BooleanValue fireExtinguisherOnNether;
        public static ForgeConfigSpec.DoubleValue razorWireDamage;
        //public final Map<String, Integer> fluidFuel = getFuelHash();
        public static ForgeConfigSpec.IntValue hoursBeforeChunkLoadingDeactivation; // 2 days by default.
        public static ForgeConfigSpec.IntValue chunkLoaderWidth;
        public static ForgeConfigSpec.BooleanValue emergencyMode;
        public static ForgeConfigSpec.IntValue ironBladeDurability;
        public static ForgeConfigSpec.IntValue baseSolarPanelMaxGeneration;
        public static ForgeConfigSpec.IntValue panelFrameMultiplier;
        public static ForgeConfigSpec.IntValue maxEnergySWindTurbine;
        public static ForgeConfigSpec.IntValue maxPumpRadius;
        public static ForgeConfigSpec.BooleanValue repleceLavaWithCobble;

        //SOUNDS
        public static ForgeConfigSpec.DoubleValue alarmVolume;
        public static ForgeConfigSpec.DoubleValue pumpVolume;
        public static ForgeConfigSpec.DoubleValue TurbineVolume;

        public Main(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Industrial Renewal Main Config").push("main");

            startWithManual = builder
                    .comment("Turn On/Off the manual item on first spawn (Default true) (WIP not working yet)")
                    .translation("gui.config.recipes.startwithmanual.name")
                    .define("startWithManual", true);
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
            steamBoilerConversionFactor = builder
                    .comment("The factor steam will be generated 1 Water : 5 Steam ex: 76 water/tick * 5 is 380 steam/tick per boiler (Default 5)")
                    .translation("gui.config.steam_factor.name")
                    .defineInRange("steamBoilerConversionFactor", 5, 1, Integer.MAX_VALUE);
            steamTurbineSteamPerTick = builder
                    .comment("How much steam the Steam Turbine need to rotate (Default 250)")
                    .defineInRange("steamTurbineSteamPerTick", 250, 1, Integer.MAX_VALUE);
            steamTurbineEnergyPerTick = builder
                    .comment("How much Energy the Steam Turbine will produce at full speed (Default 512)")
                    .defineInRange("steamTurbineEnergyPerTick", 512, 1, Integer.MAX_VALUE);
            fireExtinguisherOnNether = builder
                    .comment("If player can use fire extinguisher on nether lava (Default true)")
                    .translation("gui.config.nether_extinguisher.name")
                    .define("fireExtinguisherOnNether", true);
            razorWireDamage = builder
                    .comment("The amount of damage the razor wire would cause (Default 2.0 '1 heart')")
                    .defineInRange("razorWireDamage", 2.0D, 0, 999D);
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

            builder.pop();
            builder.comment("Sound").push("sound");
            alarmVolume = builder
                    .comment("The volume of the alarm (Default 4.0)")
                    .translation("gui.config.alarm_volume.name")
                    .defineInRange("alarmVolume", 4.0D, 0, 10D);
            pumpVolume = builder
                    .comment("Base volume of the Pump (default 0.3f)")
                    .defineInRange("pumpVolume", 0.3D, 0, 1D);
            TurbineVolume = builder
                    .comment("Base volume of the Steam Turbine (default 0.8f)")
                    .defineInRange("TurbineVolume", 0.8D, 0, 1D);
            builder.pop();
        }
    }
}