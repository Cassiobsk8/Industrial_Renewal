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
    public static final TileEntityType<TileEntityOreVein> ORE_VEIN = null;
    public static final TileEntityType<TileEntityEnergySwitch> ENERGY_SWITCH = null;
    public static final TileEntityType<TileEntityValvePipeLarge> VALVE_PIPE_LARGE = null;
    public static final TileEntityType<TileEntityEnergyCableLV> ENERGY_CABLE_LV = null;
    public static final TileEntityType<TileEntityEnergyCableMV> ENERGY_CABLE_MV = null;
    public static final TileEntityType<TileEntityEnergyCableHV> ENERGY_CABLE_HV = null;
    public static final TileEntityType<TileEntityEnergyCableLVGauge> ENERGY_CABLE_LV_GAUGE = null;
    public static final TileEntityType<TileEntityEnergyCableMVGauge> ENERGY_CABLE_MV_GAUGE = null;
    public static final TileEntityType<TileEntityEnergyCableHVGauge> ENERGY_CABLE_HV_GAUGE = null;
    public static final TileEntityType<TileEntityAlarm> ALARM = null;
    public static final TileEntityType<TileEntityGutter> GUTTER = null;
    public static final TileEntityType<TileEntityInfinityGenerator> INFINITY_GENERATOR = null;
    public static final TileEntityType<TileEntitySolarPanelBase> SOLAR_PANEL_BASE = null;
    public static final TileEntityType<TileEntitySolarPanelFrame> SOLAR_PANEL_FRAME = null;
    public static final TileEntityType<TileEntitySmallWindTurbine> SMALL_WIND_TURBINE = null;
    public static final TileEntityType<TileEntityWindTurbinePillar> WIND_TURBINE_PILLAR = null;
    public static final TileEntityType<TileEntityFirstAidKit> FIRST_AID_KIT = null;
    public static final TileEntityType<TileEntityRecordPlayer> RECORD_PLAYER = null;
    public static final TileEntityType<TileEntitySensorRain> SENSOR_RAIN = null;
    public static final TileEntityType<TileEntityCargoLoader> CARGO_LOADER = null;
    public static final TileEntityType<TileEntityFluidLoader> FLUID_LOADER = null;
    public static final TileEntityType<TileEntityLoaderRail> LOADER_RAIL = null;
    public static final TileEntityType<TileEntitySignalIndicator> SIGNAL_INDICATOR = null;
    public static final TileEntityType<TileEntityTrafficLight> TRAFFIC_LIGHT = null;
    public static final TileEntityType<TileEntityFuseBox> FUSE_BOX = null;
    public static final TileEntityType<TileEntityBoxConnector> BOX_CONNECTOR = null;
    public static final TileEntityType<TileEntityFlameDetector> FLAME_DETECTOR = null;
    public static final TileEntityType<TileEntityLocker> LOCKER = null;
    public static final TileEntityType<TileEntityEntityDetector> ENTITY_DETECTOR = null;
    public static final TileEntityType<TileEntityBarrel> BARREL = null;
    public static final TileEntityType<TileEntityGauge> GAUGE = null;
    public static final TileEntityType<TileEntityEnergyLevel> ENERGY_LEVEL = null;
    public static final TileEntityType<TileEntityElectricPump> ELECTRIC_PUMP = null;
    public static final TileEntityType<TileEntityTransformerHV> TRANSFORMER_HV = null;
    public static final TileEntityType<TileEntityBatteryBank> BATTERY_BANK = null;
    public static final TileEntityType<TileEntityFluidPipe> FLUID_PIPE = null;
    public static final TileEntityType<TileEntityFluidPipeBaseGauge> FLUID_PIPE_GAUGE = null;
    public static final TileEntityType<TileEntitySteamBoiler> STEAM_BOILER = null;
    public static final TileEntityType<TileEntitySteamTurbine> STEAM_TURBINE = null;
    public static final TileEntityType<TileEntityMining> MINING = null;
    public static final TileEntityType<TileEntityTrash> TRASH = null;
    public static final TileEntityType<TileEntityBunkBed> BUNK_BED = null;
    public static final TileEntityType<TileEntityBunkerHatch> BUNKER_HATCH = null;
    public static final TileEntityType<TileEntityCatWalk> CAT_WALK = null;
    public static final TileEntityType<TileEntityCatWalkStair> CAT_WALK_STAIR = null;
    public static final TileEntityType<TileEntityBulkConveyor> BULK_CONVEYOR = null;
    public static final TileEntityType<TileEntityBulkConveyorHopper> BULK_CONVEYOR_HOPPER = null;
    public static final TileEntityType<TileEntityBulkConveyorInserter> BULK_CONVEYOR_INSERTER = null;
    public static final TileEntityType<TileEntityDamIntake> DAM_INTAKE = null;
    public static final TileEntityType<TileEntityConcrete> CONCRETE = null;
    public static final TileEntityType<TileEntityWireIsolator> WIRE_BASE = null;
    public static final TileEntityType<TileEntityCableTray> CABLE_TRAY = null;

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
