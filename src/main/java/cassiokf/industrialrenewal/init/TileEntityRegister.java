package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.tileentity.*;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityLoaderRail;
import cassiokf.industrialrenewal.tileentity.redstone.*;
import cassiokf.industrialrenewal.tileentity.tubes.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static cassiokf.industrialrenewal.init.ModBlocks.*;

@ObjectHolder(References.MODID)
public class TileEntityRegister
{
    public static TileEntityType<TileEntityOreVein> ORE_VEIN;
    public static TileEntityType<TileEntityEnergySwitch> ENERGY_SWITCH;
    public static TileEntityType<TileEntityValvePipeLarge> VALVE_PIPE_LARGE;
    public static TileEntityType<TileEntityEnergyCableLV> ENERGY_CABLE_LV;
    public static TileEntityType<TileEntityEnergyCableMV> ENERGY_CABLE_MV;
    public static TileEntityType<TileEntityEnergyCableHV> ENERGY_CABLE_HV;
    public static TileEntityType<TileEntityEnergyCableLVGauge> ENERGY_CABLE_LV_GAUGE;
    public static TileEntityType<TileEntityEnergyCableMVGauge> ENERGY_CABLE_MV_GAUGE;
    public static TileEntityType<TileEntityEnergyCableHVGauge> ENERGY_CABLE_HV_GAUGE;
    public static TileEntityType<TileEntityAlarm> ALARM;
    public static TileEntityType<TileEntityGutter> GUTTER;
    public static TileEntityType<TileEntityInfinityGenerator> INFINITY_GENERATOR;
    public static TileEntityType<TileEntitySolarPanelBase> SOLAR_PANEL_BASE;
    public static TileEntityType<TileEntitySolarPanelFrame> SOLAR_PANEL_FRAME;
    public static TileEntityType<TileEntitySmallWindTurbine> SMALL_WIND_TURBINE;
    public static TileEntityType<TileEntityWindTurbinePillar> WIND_TURBINE_PILLAR;
    public static TileEntityType<TileEntityFirstAidKit> FIRST_AID_KIT;
    public static TileEntityType<TileEntityRecordPlayer> RECORD_PLAYER;
    public static TileEntityType<TileEntitySensorRain> SENSOR_RAIN;
    public static TileEntityType<TileEntityCargoLoader> CARGO_LOADER;
    public static TileEntityType<TileEntityFluidLoader> FLUID_LOADER;
    public static TileEntityType<TileEntityLoaderRail> LOADER_RAIL;
    public static TileEntityType<TileEntitySignalIndicator> SIGNAL_INDICATOR;
    public static TileEntityType<TileEntityTrafficLight> TRAFFIC_LIGHT;
    public static TileEntityType<TileEntityFuseBox> FUSE_BOX;
    public static TileEntityType<TileEntityBoxConnector> BOX_CONNECTOR;
    public static TileEntityType<TileEntityFlameDetector> FLAME_DETECTOR;
    public static TileEntityType<TileEntityLocker> LOCKER;
    public static TileEntityType<TileEntityEntityDetector> ENTITY_DETECTOR;
    public static TileEntityType<TileEntityBarrel> BARREL;
    public static TileEntityType<TileEntityGauge> GAUGE;
    public static TileEntityType<TileEntityEnergyLevel> ENERGY_LEVEL;
    public static TileEntityType<TileEntityElectricPump> ELECTRIC_PUMP;
    public static TileEntityType<TileEntityTransformerHV> TRANSFORMER_HV;
    public static TileEntityType<TileEntityBatteryBank> BATTERY_BANK;
    public static TileEntityType<TileEntityFluidPipe> FLUID_PIPE;
    public static TileEntityType<TileEntityFluidPipeBaseGauge> FLUID_PIPE_GAUGE;
    public static TileEntityType<TileEntitySteamBoiler> STEAM_BOILER;
    public static TileEntityType<TileEntitySteamTurbine> STEAM_TURBINE;
    public static TileEntityType<TileEntityMining> MINING;
    public static TileEntityType<TileEntityTrash> TRASH;
    public static TileEntityType<TileEntityBunkBed> BUNK_BED;
    public static TileEntityType<TileEntityBunkerHatch> BUNKER_HATCH;
    public static TileEntityType<TileEntityCatWalk> CAT_WALK;
    public static TileEntityType<TileEntityCatWalkStair> CAT_WALK_STAIR;
    public static TileEntityType<TileEntityBulkConveyor> BULK_CONVEYOR;
    public static TileEntityType<TileEntityBulkConveyorHopper> BULK_CONVEYOR_HOPPER;
    public static TileEntityType<TileEntityBulkConveyorInserter> BULK_CONVEYOR_INSERTER;
    public static TileEntityType<TileEntityDamIntake> DAM_INTAKE;
    public static TileEntityType<TileEntityConcrete> CONCRETE;
    public static TileEntityType<TileEntityWireIsolator> WIRE_BASE;
    public static TileEntityType<TileEntityCableTray> CABLE_TRAY;

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> registry)
    {
        final TileEntityType<?>[] tile_entities = {
                TileEntityType.Builder.create(TileEntityOreVein::new, veinHematite).build(null).setRegistryName(veinHematite.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergySwitch::new, energySwitch).build(null).setRegistryName(energySwitch.getRegistryName()),
                TileEntityType.Builder.create(TileEntityValvePipeLarge::new, valveLarge).build(null).setRegistryName(valveLarge.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergyCableLV::new, energyCableLV).build(null).setRegistryName(energyCableLV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergyCableMV::new, energyCableMV).build(null).setRegistryName(energyCableMV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergyCableHV::new, energyCableHV).build(null).setRegistryName(energyCableHV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergyCableLVGauge::new, energyCableGaugeLV).build(null).setRegistryName(energyCableGaugeLV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergyCableMVGauge::new, energyCableGaugeMV).build(null).setRegistryName(energyCableGaugeMV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergyCableHVGauge::new, energyCableGaugeHV).build(null).setRegistryName(energyCableGaugeHV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityAlarm::new, alarm).build(null).setRegistryName(alarm.getRegistryName()),
                TileEntityType.Builder.create(TileEntityGutter::new, gutter).build(null).setRegistryName(gutter.getRegistryName()),
                TileEntityType.Builder.create(TileEntityInfinityGenerator::new, infinityGenerator).build(null).setRegistryName(infinityGenerator.getRegistryName()),
                TileEntityType.Builder.create(TileEntitySolarPanelBase::new, spanel).build(null).setRegistryName(spanel.getRegistryName()),
                TileEntityType.Builder.create(TileEntitySolarPanelFrame::new, fpanel).build(null).setRegistryName(fpanel.getRegistryName()),
                TileEntityType.Builder.create(TileEntitySmallWindTurbine::new, sWindTurbine).build(null).setRegistryName(sWindTurbine.getRegistryName()),
                TileEntityType.Builder.create(TileEntityWindTurbinePillar::new, turbinePillar).build(null).setRegistryName(turbinePillar.getRegistryName()),
                TileEntityType.Builder.create(TileEntityFirstAidKit::new, firstAidKit).build(null).setRegistryName(firstAidKit.getRegistryName()),
                TileEntityType.Builder.create(TileEntityRecordPlayer::new, recordPlayer).build(null).setRegistryName(recordPlayer.getRegistryName()),
                TileEntityType.Builder.create(TileEntitySensorRain::new, sensorRain).build(null).setRegistryName(sensorRain.getRegistryName()),
                TileEntityType.Builder.create(TileEntityCargoLoader::new, cargoLoader).build(null).setRegistryName(cargoLoader.getRegistryName()),
                TileEntityType.Builder.create(TileEntityFluidLoader::new, fluidLoader).build(null).setRegistryName(fluidLoader.getRegistryName()),
                TileEntityType.Builder.create(TileEntityLoaderRail::new, loaderRail).build(null).setRegistryName(loaderRail.getRegistryName()),
                TileEntityType.Builder.create(TileEntitySignalIndicator::new, signalIndicator).build(null).setRegistryName(signalIndicator.getRegistryName()),
                TileEntityType.Builder.create(TileEntityTrafficLight::new, trafficLight).build(null).setRegistryName(trafficLight.getRegistryName()),
                TileEntityType.Builder.create(TileEntityFuseBox::new, fuseBox).build(null).setRegistryName(fuseBox.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBoxConnector::new, fuseBoxConnector).build(null).setRegistryName(fuseBoxConnector.getRegistryName()),
                TileEntityType.Builder.create(TileEntityFlameDetector::new, flameDetector).build(null).setRegistryName(flameDetector.getRegistryName()),
                TileEntityType.Builder.create(TileEntityLocker::new, locker).build(null).setRegistryName(locker.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEntityDetector::new, entityDetector).build(null).setRegistryName(entityDetector.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBarrel::new, barrel).build(null).setRegistryName(barrel.getRegistryName()),
                TileEntityType.Builder.create(TileEntityGauge::new, gauge).build(null).setRegistryName(gauge.getRegistryName()),
                TileEntityType.Builder.create(TileEntityEnergyLevel::new, energyLevel).build(null).setRegistryName(energyLevel.getRegistryName()),
                TileEntityType.Builder.create(TileEntityElectricPump::new, electricPump).build(null).setRegistryName(electricPump.getRegistryName()),
                TileEntityType.Builder.create(TileEntityTransformerHV::new, transformerHV).build(null).setRegistryName(transformerHV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBatteryBank::new, batteryBank).build(null).setRegistryName(batteryBank.getRegistryName()),
                TileEntityType.Builder.create(TileEntityFluidPipe::new, fluidPipe).build(null).setRegistryName(fluidPipe.getRegistryName()),
                TileEntityType.Builder.create(TileEntityFluidPipeBaseGauge::new, fluidPipeGauge).build(null).setRegistryName(fluidPipeGauge.getRegistryName()),
                TileEntityType.Builder.create(TileEntitySteamBoiler::new, steamBoiler).build(null).setRegistryName(steamBoiler.getRegistryName()),
                TileEntityType.Builder.create(TileEntitySteamTurbine::new, steamTurbine).build(null).setRegistryName(steamTurbine.getRegistryName()),
                TileEntityType.Builder.create(TileEntityMining::new, mining).build(null).setRegistryName(mining.getRegistryName()),
                TileEntityType.Builder.create(TileEntityTrash::new, trash).build(null).setRegistryName(trash.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBunkBed::new, bunkBed).build(null).setRegistryName(bunkBed.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBunkerHatch::new, bunkerHatch).build(null).setRegistryName(bunkerHatch.getRegistryName()),
                //TileEntityType.Builder.create(TileEntityChunkLoader::new, chunkLoader).build(null).setRegistryName(chunkLoader.getRegistryName()),
                TileEntityType.Builder.create(TileEntityCatWalk::new, catWalk).build(null).setRegistryName(catWalk.getRegistryName()),
                TileEntityType.Builder.create(TileEntityCatWalkStair::new, catwalkStair).build(null).setRegistryName(catwalkStair.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBulkConveyor::new, conveyorV).build(null).setRegistryName(conveyorV.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBulkConveyorHopper::new, conveyorVHopper).build(null).setRegistryName(conveyorVHopper.getRegistryName()),
                TileEntityType.Builder.create(TileEntityBulkConveyorInserter::new, conveyorVInserter).build(null).setRegistryName(conveyorVInserter.getRegistryName()),
                TileEntityType.Builder.create(TileEntityDamIntake::new, damIntake).build(null).setRegistryName(damIntake.getRegistryName()),
                TileEntityType.Builder.create(TileEntityConcrete::new, concrete).build(null).setRegistryName(concrete.getRegistryName()),
                TileEntityType.Builder.create(TileEntityWireIsolator::new, hvIsolator).build(null).setRegistryName(hvIsolator.getRegistryName()),
                TileEntityType.Builder.create(TileEntityCableTray::new, cableTray).build(null).setRegistryName(cableTray.getRegistryName())
        };

        registry.registerAll(tile_entities);
    }
}
