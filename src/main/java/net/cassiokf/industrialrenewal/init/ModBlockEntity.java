package net.cassiokf.industrialrenewal.init;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.blockentity.*;
import net.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamGenerator;
import net.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamIntake;
import net.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamOutlet;
import net.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamTurbine;
import net.cassiokf.industrialrenewal.blockentity.transport.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntity {
    
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, IndustrialRenewal.MODID);
    
    public static void registerInit(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
    public static RegistryObject<BlockEntityType<BlockEntityTransformer>> TRANSFORMER_TILE = BLOCK_ENTITIES.register("transformer_tile", () -> BlockEntityType.Builder.of(BlockEntityTransformer::new, ModBlocks.TRANSFORMER.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityHVIsolator>> ISOLATOR_TILE = BLOCK_ENTITIES.register("isolator_tile", () -> BlockEntityType.Builder.of(BlockEntityHVIsolator::new, ModBlocks.HV_ISOLATOR.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntitySolarPanel>> SOLAR_PANEL = BLOCK_ENTITIES.register("solar_panel_tile", () -> BlockEntityType.Builder.of(BlockEntitySolarPanel::new, ModBlocks.SOLAR_PANEL.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntitySolarPanelFrame>> SOLAR_PANEL_FRAME = BLOCK_ENTITIES.register("solar_panel_frame", () -> BlockEntityType.Builder.of(BlockEntitySolarPanelFrame::new, ModBlocks.SPANEL_FRAME.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityBatteryBank>> BATTERY_BANK = BLOCK_ENTITIES.register("battery_bank_tile", () -> BlockEntityType.Builder.of(BlockEntityBatteryBank::new, ModBlocks.BATTERY_BANK.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableLVMeter>> ENERGYCABLE_LV_METER_TILE = BLOCK_ENTITIES.register("energycable_lv_meter_tile", () -> BlockEntityType.Builder.of(BlockEntityEnergyCableLVMeter::new, ModBlocks.ENERGYCABLE_LV_METER.get()).build(null));
    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableMVMeter>> ENERGYCABLE_MV_METER_TILE = BLOCK_ENTITIES.register("energycable_mv_meter_tile", () -> BlockEntityType.Builder.of(BlockEntityEnergyCableMVMeter::new, ModBlocks.ENERGYCABLE_MV_METER.get()).build(null));
    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableHVMeter>> ENERGYCABLE_HV_METER_TILE = BLOCK_ENTITIES.register("energycable_hv_meter_tile", () -> BlockEntityType.Builder.of(BlockEntityEnergyCableHVMeter::new, ModBlocks.ENERGYCABLE_HV_METER.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableLV>> ENERGYCABLE_LV_TILE = BLOCK_ENTITIES.register("energycable_lv_tile", () -> BlockEntityType.Builder.of(BlockEntityEnergyCableLV::new, ModBlocks.ENERGYCABLE_LV.get()).build(null));
    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableMV>> ENERGYCABLE_MV_TILE = BLOCK_ENTITIES.register("energycable_mv_tile", () -> BlockEntityType.Builder.of(BlockEntityEnergyCableMV::new, ModBlocks.ENERGYCABLE_MV.get()).build(null));
    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableHV>> ENERGYCABLE_HV_TILE = BLOCK_ENTITIES.register("energycable_hv_tile", () -> BlockEntityType.Builder.of(BlockEntityEnergyCableHV::new, ModBlocks.ENERGYCABLE_HV.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityFluidPipe>> FLUIDPIPE_TILE = BLOCK_ENTITIES.register("fluidpipe_tile", () -> BlockEntityType.Builder.of(BlockEntityFluidPipe::new, ModBlocks.FLUID_PIPE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BEFluidPipeGauge>> FLUIDPIPE_GAUGE_TILE = BLOCK_ENTITIES.register("fluidpipe_gauge_tile", () -> BlockEntityType.Builder.of(BEFluidPipeGauge::new, ModBlocks.FLUID_PIPE_GAUGE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityHighPressureFluidPipe>> HIGH_PRESSURE_PIPE = BLOCK_ENTITIES.register("high_pressure_pipe", () -> BlockEntityType.Builder.of(BlockEntityHighPressureFluidPipe::new, ModBlocks.HIGH_PRESSURE_PIPE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityBarrel>> BARREL_TILE = BLOCK_ENTITIES.register("barrel_tile", () -> BlockEntityType.Builder.of(BlockEntityBarrel::new, ModBlocks.BARREL.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityEnergySwitch>> ENERGY_SWITCH_TILE = BLOCK_ENTITIES.register("energy_switch_tile", () -> BlockEntityType.Builder.of(BlockEntityEnergySwitch::new, ModBlocks.ENERGY_SWITCH.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityFluidValve>> FLUID_VALVE_TILE = BLOCK_ENTITIES.register("fluid_valve_tile", () -> BlockEntityType.Builder.of(BlockEntityFluidValve::new, ModBlocks.FLUID_VALVE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityElectricPump>> ELECTRIC_PUMP_TILE = BLOCK_ENTITIES.register("electric_pump_tile", () -> BlockEntityType.Builder.of(BlockEntityElectricPump::new, ModBlocks.ELECTRIC_PUMP.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityTrash>> TRASH_TILE = BLOCK_ENTITIES.register("trash_tile", () -> BlockEntityType.Builder.of(BlockEntityTrash::new, ModBlocks.TRASH.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityWindTurbinePillar>> TURBINE_PILLAR_TILE = BLOCK_ENTITIES.register("turbine_pillar_tile", () -> BlockEntityType.Builder.of(BlockEntityWindTurbinePillar::new, ModBlocks.TURBINE_PILLAR.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityWindTurbineHead>> WIND_TURBINE_TILE = BLOCK_ENTITIES.register("wind_turbine_tile", () -> BlockEntityType.Builder.of(BlockEntityWindTurbineHead::new, ModBlocks.WIND_TURBINE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityPortableGenerator>> PORTABLE_GENERATOR_TILE = BLOCK_ENTITIES.register("portable_generator_tile", () -> BlockEntityType.Builder.of(BlockEntityPortableGenerator::new, ModBlocks.PORTABLE_GENERATOR.get()).build(null));
    
    
    public static RegistryObject<BlockEntityType<BlockEntityConveyor>> CONVEYOR_TILE = BLOCK_ENTITIES.register("conveyor_tile", () -> BlockEntityType.Builder.of(BlockEntityConveyor::new, ModBlocks.CONVEYOR_BASIC.get(), ModBlocks.CONVEYOR_FAST.get(), ModBlocks.CONVEYOR_EXPRESS.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntitySteamBoiler>> STEAM_BOILER_TILE = BLOCK_ENTITIES.register("steam_boiler_tile", () -> BlockEntityType.Builder.of(BlockEntitySteamBoiler::new, ModBlocks.STEAM_BOILER.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntitySteamTurbine>> STEAM_TURBINE_TILE = BLOCK_ENTITIES.register("steam_turbine_tile", () -> BlockEntityType.Builder.of(BlockEntitySteamTurbine::new, ModBlocks.STEAM_TURBINE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityMiner>> MINER_TILE = BLOCK_ENTITIES.register("miner_tile", () -> BlockEntityType.Builder.of(BlockEntityMiner::new, ModBlocks.MINER.get()).build(null));
    
    
    public static RegistryObject<BlockEntityType<BlockEntityIndustrialBatteryBank>> INDUSTRIAL_BATTERY_TILE = BLOCK_ENTITIES.register("ind_battery_tile", () -> BlockEntityType.Builder.of(BlockEntityIndustrialBatteryBank::new, ModBlocks.INDUSTRIAL_BATTERY_BANK.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityFluidTank>> FLUID_TANK_TILE = BLOCK_ENTITIES.register("fluid_tank_tile", () -> BlockEntityType.Builder.of(BlockEntityFluidTank::new, ModBlocks.FLUID_TANK.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityStorageChest>> STORAGE_CHEST_TILE = BLOCK_ENTITIES.register("storage_chest_tile", () -> BlockEntityType.Builder.of(BlockEntityStorageChest::new, ModBlocks.MINER.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityLathe>> LATHE_TILE = BLOCK_ENTITIES.register("lathe_tile", () -> BlockEntityType.Builder.of(BlockEntityLathe::new, ModBlocks.LATHE.get()).build(null));
    
    
    public static RegistryObject<BlockEntityType<BlockEntityDamIntake>> DAM_INTAKE = BLOCK_ENTITIES.register("dam_intake", () -> BlockEntityType.Builder.of(BlockEntityDamIntake::new, ModBlocks.DAM_INTAKE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityDamOutlet>> DAM_OUTLET = BLOCK_ENTITIES.register("dam_outlet", () -> BlockEntityType.Builder.of(BlockEntityDamOutlet::new, ModBlocks.DAM_OUTLET.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityDamTurbine>> DAM_TURBINE_TILE = BLOCK_ENTITIES.register("dam_turbine_tile", () -> BlockEntityType.Builder.of(BlockEntityDamTurbine::new, ModBlocks.DAM_TURBINE.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityDamGenerator>> DAM_GENERATOR = BLOCK_ENTITIES.register("dam_generator", () -> BlockEntityType.Builder.of(BlockEntityDamGenerator::new, ModBlocks.DAM_GENERATOR.get()).build(null));
    
    //    public static RegistryObject<BlockEntityType<BlockEntityCargoLoader>> CARGO_LOADER =
    //            BLOCK_ENTITIES.register("cargo_loader", ()-> BlockEntityType.Builder.of(
    //                    BlockEntityCargoLoader::new, ModBlocks.CARGO_LOADER.get()).build(null));
    //
    //    public static RegistryObject<BlockEntityType<BlockEntityFluidLoader>> FLUID_LOADER =
    //            BLOCK_ENTITIES.register("fluid_loader", ()-> BlockEntityType.Builder.of(
    //                    BlockEntityFluidLoader::new, ModBlocks.FLUID_LOADER.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityInfinityGenerator>> INFINITY_GENERATOR = BLOCK_ENTITIES.register("infinity_generator", () -> BlockEntityType.Builder.of(BlockEntityInfinityGenerator::new, ModBlocks.INFINITY_GENERATOR.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityEnergyLevel>> ENERGY_LEVEL = BLOCK_ENTITIES.register("energy_level", () -> BlockEntityType.Builder.of(BlockEntityEnergyLevel::new, ModBlocks.ENERGY_LEVEL.get()).build(null));
    
    public static RegistryObject<BlockEntityType<BlockEntityFluidGauge>> FLUID_GAUGE = BLOCK_ENTITIES.register("fluid_gauge", () -> BlockEntityType.Builder.of(BlockEntityFluidGauge::new, ModBlocks.FLUID_GAUGE.get()).build(null));
    
    

    
}
