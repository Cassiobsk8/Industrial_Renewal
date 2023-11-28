package net.cassiokf.industrialrenewal.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Config {
//    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
//    public static final ForgeConfigSpec SPEC;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> BATTERY_BANK_ENERGY_CAPACITY;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> PUMP_ENERGY_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> PUMP_RF_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Integer> PUMP_RADIUS;
//    public static final ForgeConfigSpec.ConfigValue<Boolean> PUMP_INFINITE_WATER;
//    public static final ForgeConfigSpec.ConfigValue<Boolean> PUMP_REPLACE_COBBLE;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> INDUSTRIAL_BATTERY_BANK_ENERGY_PER_BATTERY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> INDUSTRIAL_BATTERY_BANK_TRANSFER_RATE;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> FLUID_TANK_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> FLUID_TANK_TRANSFER_RATE;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> LATHE_ENERGY_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> LATHE_ENERGY_PER_TICK;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_WATER_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_WATER_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_MAX_HEAT;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_ENERGY_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_ENERGY_RECEIVE;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_ENERGY_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_MINING_SPEED;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_HEAT_DAMAGE_THRESHOLD;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MINER_RADIUS;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_BOILER_WATER_TANK_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_BOILER_STEAM_TANK_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_BOILER_FUEL_TANK_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_BOILER_MAX_HEAT;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_BOILER_WATER_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_BOILER_SOLID_FUEL_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_BOILER_LIQUID_FUEL_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Float> STEAM_BOILER_WATER_STEAM_CONVERSION;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_TURBINE_WATER_TANK_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_TURBINE_STEAM_TANK_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_TURBINE_ENERGY_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_TURBINE_ENERGY_EXTRACT;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_TURBINE_MAX_ROTATION;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_TURBINE_ENERGY_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Integer> STEAM_TURBINE_STEAM_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Float> STEAM_TURBINE_STEAM_WATER_CONVERSION;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> LV_CABLE_TRANSFER_RATE;
//    public static final ForgeConfigSpec.ConfigValue<Integer> MV_CABLE_TRANSFER_RATE;
//    public static final ForgeConfigSpec.ConfigValue<Integer> HV_CABLE_TRANSFER_RATE;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> SOLAR_FORCE_GENERATION;
//    public static final ForgeConfigSpec.ConfigValue<Boolean> SOLAR_DECORATIVE;
//
//    public static final ForgeConfigSpec.ConfigValue<Boolean> SOLAR_FRAME_DECORATIVE;
//    public static final ForgeConfigSpec.ConfigValue<Float> SOLAR_FRAME_MULTIPLIER;
//    public static final ForgeConfigSpec.ConfigValue<Integer> SOLAR_FRAME_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> SOLAR_FRAME_TRANSFER_RATE;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> PORTABLE_GENERATOR_ENERGY_PER_TICK;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> WIND_TURBINE_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> WIND_TURBINE_TRANSFER_RATE;
//    public static final ForgeConfigSpec.ConfigValue<Integer> WIND_TURBINE_ENERGY_PER_TICK;
//
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> DAM_INTAKE_WATER_PRODUCTION;
//    public static final ForgeConfigSpec.ConfigValue<Integer> DAM_OUTLET_WATER_CONSUMPTION;
//    public static final ForgeConfigSpec.ConfigValue<Integer> DAM_TURBINE_WATER_TANK_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> DAM_TURBINE_MAX_EFFICIENCY;
//
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> DAM_GENERATOR_ENERGY_CAPACITY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> DAM_GENERATOR_RF_PER_TICK;
//    public static final ForgeConfigSpec.ConfigValue<Integer> DAM_GENERATOR_TRANSFER_RATE;
//    public static final ForgeConfigSpec.ConfigValue<Integer> HIGH_PRESSURE_PIPE_TRANSFER_RATE;
//
//    public static final ForgeConfigSpec.ConfigValue<Integer> TRANSFORMER_TRANSFER_RATE;
//
//    public static final ForgeConfigSpec.ConfigValue<Float> CATWALK_SPEED;
//    public static final ForgeConfigSpec.ConfigValue<Boolean> INDUSTRIAL_FLOOR_COLLISION;
//
//    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MINER_ORE_BLACKLIST;
//    public static final ForgeConfigSpec.ConfigValue<Boolean> MINER_BLACKLIST_AS_WHITE;
//
//    static {
//        BUILDER.push("Battery Bank");
//        BATTERY_BANK_ENERGY_CAPACITY = BUILDER.comment("Battery Bank Energy Capacity (default 1000000)").define("battery_capacity", 1000000);
//        BUILDER.pop();
//
//        BUILDER.push("Pump");
//        PUMP_ENERGY_CAPACITY = BUILDER.comment("Pump Energy Capacity (default 10)").define("pump_energy_capacity", 200);
//        PUMP_RF_PER_TICK = BUILDER.comment("Pump Energy Consume Per Tick (default 10)").define("pump_rf_per_tick", 10);
//        PUMP_RADIUS = BUILDER.comment("Pump Radius (default 16)").define("pump_radius", 16);
//        PUMP_INFINITE_WATER = BUILDER.comment("Pump Infinite Water (default true)").define("pump_infinite_water", true);
//        PUMP_REPLACE_COBBLE = BUILDER.comment("Pump Replace Lava With Cobble (default true)").define("pump_replace_cobble", true);
//        BUILDER.pop();
//
//        BUILDER.push("Industrial Battery Bank");
//        INDUSTRIAL_BATTERY_BANK_ENERGY_PER_BATTERY = BUILDER.comment("Industrial Battery Bank Energy Per Battery (default 6480000)").define("industrial_battery_bank_energy_per_battery", 6480000);
//        INDUSTRIAL_BATTERY_BANK_TRANSFER_RATE = BUILDER.comment("Industrial Battery Bank Energy Transfer Rate (default 102400)").define("industrial_battery_bank_transfer_rate", 102400);
//        BUILDER.pop();
//
//        BUILDER.push("Fluid Tank");
//        FLUID_TANK_CAPACITY = BUILDER.comment("Fluid tank capacity in mb (default 640000)").define("fluid_tank_capacity", 640000);
//        FLUID_TANK_TRANSFER_RATE = BUILDER.comment("Fluid tank transfer rate in mb (default 128000)").define("fluid_tank_transfer_rate", 128000);
//        BUILDER.pop();
//
//        BUILDER.push("Lathe");
//        LATHE_ENERGY_CAPACITY = BUILDER.comment("Lathe Energy Capacity (default 10240)").define("lathe_energy_capacity", 10240);
//        LATHE_ENERGY_PER_TICK = BUILDER.comment("Lathe Energy Per Tick (default 128)").define("lathe_energy_per_tick", 128);
//        BUILDER.pop();
//
//        BUILDER.push("Miner");
//        MINER_WATER_CAPACITY = BUILDER.comment("Miner Water Capacity (default 32000)").define("miner_water_capacity", 32000);
//        MINER_MAX_HEAT = BUILDER.comment("Miner Max Heat (default 18000)").define("miner_max_heat", 18000);
//        MINER_WATER_PER_TICK = BUILDER.comment("Miner Water Per Tick (default 10)").define("miner_water_per_tick", 10);
//        MINER_ENERGY_CAPACITY= BUILDER.comment("Miner Energy Capacity (default 100000)").define("miner_energy_capacity", 100000);
//        MINER_ENERGY_RECEIVE= BUILDER.comment("Miner Energy Receive Rate (default 10240)").define("miner_energy_receive", 10240);
//        MINER_ENERGY_PER_TICK = BUILDER.comment("Miner Energy Per Tick (default 500)").define("miner_energy_per_tick", 500);
//        MINER_MINING_SPEED = BUILDER.comment("Miner Mining Speed In Ticks per Block (default 120)").define("miner_mining_speed", 120);
//        MINER_HEAT_DAMAGE_THRESHOLD = BUILDER.comment("Miner Amount of Heat Until Extra Damage to Drill (default 13000)").define("miner_heat_damage_threshold", 13000);
//        MINER_RADIUS = BUILDER.comment("Miner mining radius (default 1: 1x1 chunk, 2: 3x3, 3: 5x5 ...)").define("miner_radius", 1);
//        BUILDER.pop();
//
//        BUILDER.push("Steam Boiler");
//        STEAM_BOILER_WATER_TANK_CAPACITY = BUILDER.comment("Steam Boiler Water Tank Capacity (default 32000)").define("steam_boiler_water_tank_capacity", 32000);
//        STEAM_BOILER_STEAM_TANK_CAPACITY = BUILDER.comment("Steam Boiler Steam Tank Capacity (default 32000)").define("steam_boiler_steam_tank_capacity", 32000);
//        STEAM_BOILER_FUEL_TANK_CAPACITY = BUILDER.comment("Steam Boiler Fuel Tank Capacity (default 32000)").define("steam_boiler_fuel_tank_capacity", 32000);
//        STEAM_BOILER_MAX_HEAT = BUILDER.comment("Steam Boiler Max Heat (default 32000)").define("steam_boiler_max_heat", 32000);
//        STEAM_BOILER_WATER_PER_TICK = BUILDER.comment("Steam Water Consume Per Tick (default 100)").define("steam_boiler_water_per_tick", 100);
//        STEAM_BOILER_SOLID_FUEL_PER_TICK = BUILDER.comment("Steam Boiler Sold Fuel Burn Time Consume Per Tick (default 2)").define("steam_boiler_solid_fuel_per_tick", 2);
//        STEAM_BOILER_LIQUID_FUEL_PER_TICK = BUILDER.comment("Steam Boiler Liquid Fuel Burn Time onsume Per Tick (default 1)").define("steam_boiler_liquid_fuel_per_tick", 1);
//        STEAM_BOILER_WATER_STEAM_CONVERSION = BUILDER.comment("Steam Boiler Water to Steam Conversion Ratio (1 water to x steam)(default 5.0f)").define("steam_boiler_water_steam_conversion", 5f);
//        BUILDER.pop();
//
//        BUILDER.push("Steam Turbine");
//        STEAM_TURBINE_WATER_TANK_CAPACITY = BUILDER.comment("Steam Turbine Water Tank Capacity (default 32000)").define("steam_turbine_water_tank_capacity", 32000);
//        STEAM_TURBINE_STEAM_TANK_CAPACITY = BUILDER.comment("Steam Turbine Steam Tank Capacity (default 32000)").define("steam_turbine_steam_tank_capacity", 32000);
//        STEAM_TURBINE_ENERGY_CAPACITY = BUILDER.comment("Steam Turbine Energy Capacity (default 100000)").define("steam_turbine_energy_capacity", 100000);
//        STEAM_TURBINE_ENERGY_EXTRACT = BUILDER.comment("Steam Turbine Energy Extraction Speed (default 10240)").define("steam_turbine_energy_extract", 10240);
//        STEAM_TURBINE_MAX_ROTATION = BUILDER.comment("Steam Turbine Max Rotation (default 16000)").define("steam_turbine_max_rotation", 16000);
//        STEAM_TURBINE_ENERGY_PER_TICK = BUILDER.comment("Steam Turbine Energy Generation Per Tick (default 512)").define("steam_turbine_energy_per_tick", 512);
//        STEAM_TURBINE_STEAM_PER_TICK = BUILDER.comment("Steam Turbine Steam Consume Per Tick (default 250)").define("steam_turbine_steam_per_tick", 250);
//        STEAM_TURBINE_STEAM_WATER_CONVERSION = BUILDER.comment("Steam Turbine Steam to Water Conversion Ratio (x steam to 1 water) (default 5f)").define("steam_turbine_steam_water_conversion", 5f);
//        BUILDER.pop();
//
//
//        BUILDER.push("Dam");
//        DAM_INTAKE_WATER_PRODUCTION = BUILDER.comment("Dam Intake Water Production (default 40000)").define("dam_intake_water_production", 40000);
//        DAM_OUTLET_WATER_CONSUMPTION = BUILDER.comment("Dam Outlet Water Consumption (default 200000)").define("dam_outlet_water_consumption", 200000);
//        DAM_TURBINE_WATER_TANK_CAPACITY = BUILDER.comment("Dam Turbine Water Tank Capacity (default 240000)").define("dam_turbine_water_tank_capacity", 240000);
//        DAM_TURBINE_MAX_EFFICIENCY = BUILDER.comment("Amount of water per tick required to reach max rotation").define("dam_turbine_max_efficiency", 200000);
//
//        DAM_GENERATOR_ENERGY_CAPACITY = BUILDER.comment("Dam Generator Energy Capacity (default 1000000)").define("dam_generator_energy_capacity", 1000000);
//        DAM_GENERATOR_RF_PER_TICK = BUILDER.comment("Dam Generator Energy Per Tick (default 1024)").define("dam_generator_rf_per_tick", 1024);
//        DAM_GENERATOR_TRANSFER_RATE = BUILDER.comment("Dam Generator Energy Transfer Rate (default 2048)").define("dam_generator_transfer_rate", 2048);
//
//        HIGH_PRESSURE_PIPE_TRANSFER_RATE = BUILDER.comment("High Pressure Pipe Fluid Transfer Rate in mb (default 200000)").define("high_pressure_pipe_transfer_rate", 200000);
//        BUILDER.pop();
//
//        BUILDER.push("Cables");
//        LV_CABLE_TRANSFER_RATE = BUILDER.comment("LV Cable Transfer Rate (defaut 256)").define("lv_cable_transfer_tare", 256);
//        MV_CABLE_TRANSFER_RATE = BUILDER.comment("MV Cable Transfer Rate (defaut 1024)").define("mv_cable_transfer_tare", 1024);
//        HV_CABLE_TRANSFER_RATE = BUILDER.comment("HV Cable Transfer Rate (defaut 4096)").define("hv_cable_transfer_tare", 4096);
//        BUILDER.pop();
//
//        BUILDER.push("Solar");
//        SOLAR_DECORATIVE = BUILDER.comment("Make Solar panels decorative (default false)").define("solar_decorative", false);
//        SOLAR_FORCE_GENERATION = BUILDER.comment("Force Solar to Generate (-1 let game decide, 0 no energy, 1 RF, 2 RF ,3 RF ... ALSO Changes Solar Frames)(default -1)").define("solar_force_generation", -1);
//        BUILDER.pop();
//
//        BUILDER.push("Solar Frame");
//        SOLAR_FRAME_DECORATIVE = BUILDER.comment("Make Solar panel frames decorative (default false)").define("solar_frame_decorative", false);
//        SOLAR_FRAME_MULTIPLIER = BUILDER.comment("How many time the energy generation of a normal solar panel (default 2f)").define("solar_frame_multiplier", 2f);
//        SOLAR_FRAME_CAPACITY = BUILDER.comment("Solar Frame Max Capacity (default 600)").define("solar_frame_capacity", 600);
//        SOLAR_FRAME_TRANSFER_RATE = BUILDER.comment("Solar Frame Transfer Rate (default 1024)").define("solar_frame_transfer_rate", 1024);
//        BUILDER.pop();
//
//        BUILDER.push("Portable Generator");
//        PORTABLE_GENERATOR_ENERGY_PER_TICK = BUILDER.comment("Portable Generator Energy Per Tick (default 32)").define("portable_generator_energy_per_tick", 32);
//        BUILDER.pop();
//
//
//        BUILDER.push("Wind Turbine");
//        WIND_TURBINE_CAPACITY = BUILDER.comment("Wind Turbine Energy Capacity (default 32000)").define("wind_turbine_capacity", 32000);
//        WIND_TURBINE_TRANSFER_RATE = BUILDER.comment("Wind Turbine Energy Transfer Rate (default 1024)").define("wind_turbine_transfer_rate", 1024);
//        WIND_TURBINE_ENERGY_PER_TICK = BUILDER.comment("Wind Turbine Max Energy Generation Per Tick (default 128)").define("wind_turbine_energy_per_tick", 128);
//        BUILDER.pop();
//
//        BUILDER.push("Wind Turbine");
//        TRANSFORMER_TRANSFER_RATE = BUILDER.comment("HV Transformer Transfer Rate (default 1000000)").define("transformer_transfer_rate", 1000000);
//        BUILDER.pop();
//
//
//        BUILDER.push("Misc");
//        CATWALK_SPEED = BUILDER.comment("Catwalk speed factor (default 1.2f)").define("catwalk_speed", 1.2f);
//        INDUSTRIAL_FLOOR_COLLISION = BUILDER.comment("Enable Industrial Floor Collision (default false)").define("floor_collision", false);
//        BUILDER.pop();
//
//        BUILDER.push("Miner Ore List");
//        MINER_ORE_BLACKLIST = BUILDER.comment("Miner Ore Black list").defineList("miner_ore_blacklist", Config::DefaultBlackList, obj->true);
//        MINER_BLACKLIST_AS_WHITE = BUILDER.comment("Use Blacklist as Whitelist (default false)").define("blacklist_as_white", false);
//        BUILDER.pop();
//
//
//        SPEC = BUILDER.build();
//    }

    public static List<String> DefaultBlackList(){
        List<String> list = new ArrayList<>();
        list.add("minecraft:ancient_debris");

        return list;
    }

    public static Map<String, Integer> getFuelHash()
    {
        Map<String, Integer> fluidFuel = new HashMap<String, Integer>();
        fluidFuel.put("minecraft:lava", 200);
        fluidFuel.put("mekanism:ethene", 600);
        fluidFuel.put("mekanism:oxygen", 50);
        fluidFuel.put("mekanism:hydrogen", 100);

        fluidFuel.put("mekanismgenerators:bioethanol", 800);
        fluidFuel.put("mekanismgenerators:flowing_bioethanol", 800);

        fluidFuel.put("immersiveengineering:creosote", 150);
        fluidFuel.put("immersiveengineering:biodiesel", 800);
        fluidFuel.put("immersiveengineering:ethanol", 600);

        fluidFuel.put("immersivepetroleum:oil", 600);
        fluidFuel.put("immersivepetroleum:diesel", 900);
        fluidFuel.put("immersivepetroleum:gasoline", 1000);

        fluidFuel.put("pneumaticcraft:biodiesel", 800);
        fluidFuel.put("pneumaticcraft:ethanol", 600);
        fluidFuel.put("pneumaticcraft:oil", 600);
        fluidFuel.put("pneumaticcraft:diesel", 900);
        fluidFuel.put("pneumaticcraft:gasoline", 1000);
        fluidFuel.put("pneumaticcraft:kerosene", 1200);
        fluidFuel.put("pneumaticcraft:lpg", 1600);

        fluidFuel.put("vehicle:fuelium", 900);

        fluidFuel.put("thermal:creosote", 150);
        fluidFuel.put("thermal:crude_oil", 800);

        fluidFuel.put("industrialforegoing:biofuel", 800);
        fluidFuel.put("industrialforegoing:biofuel_fluid", 800);

        fluidFuel.put("forestry:bio_ethanol", 800);
        fluidFuel.put("forestry:biomass", 600);


//        fluidFuel.put("rocket_fuel", 3200);
//        fluidFuel.put("pyrotheum", 3200);
//        fluidFuel.put("refined_fuel", 1500);
//        fluidFuel.put("ic2biogas", 300);
//        fluidFuel.put("crude_oil", 500);
//        fluidFuel.put("refined_oil", 1000);
//        fluidFuel.put("oil", 500);
//        fluidFuel.put("oil_heavy", 600);
//        fluidFuel.put("oil_dense", 800);
//        fluidFuel.put("oil_distilled", 1200);
//        fluidFuel.put("coal", 400);
//        fluidFuel.put("refined_biofuel", 900);
//        fluidFuel.put("bio_diesel", 600);
//        fluidFuel.put("biodiesel", 600);
//        fluidFuel.put("diesel", 900);
//        fluidFuel.put("fuel", 1500);
//        fluidFuel.put("fluiddiesel", 900);
//        fluidFuel.put("fluidnitrodiesel", 1600);
//        fluidFuel.put("empoweredoil", 700);
//        fluidFuel.put("kerosene", 1500);
//        fluidFuel.put("lpg", 1800);
//        fluidFuel.put("gasoline", 1200);
//        fluidFuel.put("fire_water", 1200);
//        fluidFuel.put("ethanol", 900);
//        fluidFuel.put("bio.ethanol", 900);
        return fluidFuel;
    }
}
