package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.tileentity.*;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityLoaderRail;
import cassiokf.industrialrenewal.tileentity.redstone.*;
import cassiokf.industrialrenewal.tileentity.tubes.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static cassiokf.industrialrenewal.References.MODID;

public class TileRegistration
{
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final RegistryObject<TileEntityType<TileEntityOreVein>> VEINHEMATITE_TILE = TILES.register("orevein_hematite", () -> TileEntityType.Builder.create(TileEntityOreVein::new, BlocksRegistration.VEINHEMATITE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergySwitch>> ENERGYSWITCH_TILE = TILES.register("energy_switch", () -> TileEntityType.Builder.create(TileEntityEnergySwitch::new, BlocksRegistration.ENERGYSWITCH.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityValvePipeLarge>> VALVELARGE_TILE = TILES.register("valve_pipe_large", () -> TileEntityType.Builder.create(TileEntityValvePipeLarge::new, BlocksRegistration.VALVELARGE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergyCableLV>> ENERGYCABLELV_TILE = TILES.register("energy_cable_lv", () -> TileEntityType.Builder.create(TileEntityEnergyCableLV::new, BlocksRegistration.ENERGYCABLELV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergyCableMV>> ENERGYCABLEMV_TILE = TILES.register("energy_cable_mv", () -> TileEntityType.Builder.create(TileEntityEnergyCableMV::new, BlocksRegistration.ENERGYCABLEMV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergyCableHV>> ENERGYCABLEHV_TILE = TILES.register("energy_cable_hv", () -> TileEntityType.Builder.create(TileEntityEnergyCableHV::new, BlocksRegistration.ENERGYCABLEHV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergyCableLVGauge>> ENERGYCABLEGAUGELV_TILE = TILES.register("energy_cable_gauge_lv", () -> TileEntityType.Builder.create(TileEntityEnergyCableLVGauge::new, BlocksRegistration.ENERGYCABLEGAUGELV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergyCableMVGauge>> ENERGYCABLEGAUGEMV_TILE = TILES.register("energy_cable_gauge_mv", () -> TileEntityType.Builder.create(TileEntityEnergyCableMVGauge::new, BlocksRegistration.ENERGYCABLEGAUGEMV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergyCableHVGauge>> ENERGYCABLEGAUGEHV_TILE = TILES.register("energy_cable_gauge_hv", () -> TileEntityType.Builder.create(TileEntityEnergyCableHVGauge::new, BlocksRegistration.ENERGYCABLEGAUGEHV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityAlarm>> ALARM_TILE = TILES.register("alarm", () -> TileEntityType.Builder.create(TileEntityAlarm::new, BlocksRegistration.ALARM.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityGutter>> GUTTER_TILE = TILES.register("gutter", () -> TileEntityType.Builder.create(TileEntityGutter::new, BlocksRegistration.GUTTER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityInfinityGenerator>> INFINITYGENERATOR_TILE = TILES.register("infinity_generator", () -> TileEntityType.Builder.create(TileEntityInfinityGenerator::new, BlocksRegistration.INFINITYGENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntitySolarPanelBase>> SPANEL_TILE = TILES.register("solar_panel", () -> TileEntityType.Builder.create(TileEntitySolarPanelBase::new, BlocksRegistration.SPANEL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntitySolarPanelFrame>> FPANEL_TILE = TILES.register("solar_panel_frame", () -> TileEntityType.Builder.create(TileEntitySolarPanelFrame::new, BlocksRegistration.FPANEL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntitySmallWindTurbine>> SWINDTURBINE_TILE = TILES.register("small_wind_turbine", () -> TileEntityType.Builder.create(TileEntitySmallWindTurbine::new, BlocksRegistration.SWINDTURBINE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityWindTurbinePillar>> TURBINEPILLAR_TILE = TILES.register("small_wind_turbine_pillar", () -> TileEntityType.Builder.create(TileEntityWindTurbinePillar::new, BlocksRegistration.TURBINEPILLAR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFirstAidKit>> FIRSTAIDKIT_TILE = TILES.register("firstaid_kit", () -> TileEntityType.Builder.create(TileEntityFirstAidKit::new, BlocksRegistration.FIRSTAIDKIT.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityRecordPlayer>> RECORDPLAYER_TILE = TILES.register("record_player", () -> TileEntityType.Builder.create(TileEntityRecordPlayer::new, BlocksRegistration.RECORDPLAYER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntitySensorRain>> SENSORRAIN_TILE = TILES.register("sensor_rain", () -> TileEntityType.Builder.create(TileEntitySensorRain::new, BlocksRegistration.SENSORRAIN.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityCargoLoader>> CARGOLOADER_TILE = TILES.register("cargo_loader", () -> TileEntityType.Builder.create(TileEntityCargoLoader::new, BlocksRegistration.CARGOLOADER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFluidLoader>> FLUIDLOADER_TILE = TILES.register("fluid_loader", () -> TileEntityType.Builder.create(TileEntityFluidLoader::new, BlocksRegistration.FLUIDLOADER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLoaderRail>> LOADERRAIL_TILE = TILES.register("rail_loader", () -> TileEntityType.Builder.create(TileEntityLoaderRail::new, BlocksRegistration.LOADERRAIL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFuseBox>> FUSEBOX_TILE = TILES.register("fuse_box", () -> TileEntityType.Builder.create(TileEntityFuseBox::new, BlocksRegistration.FUSEBOX.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBoxConnector>> FUSEBOXCONNECTOR_TILE = TILES.register("conduit_connector", () -> TileEntityType.Builder.create(TileEntityBoxConnector::new, BlocksRegistration.FUSEBOXCONNECTOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFlameDetector>> FLAMEDETECTOR_TILE = TILES.register("flame_detector", () -> TileEntityType.Builder.create(TileEntityFlameDetector::new, BlocksRegistration.FLAMEDETECTOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLocker>> LOCKER_TILE = TILES.register("locker", () -> TileEntityType.Builder.create(TileEntityLocker::new, BlocksRegistration.LOCKER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEntityDetector>> ENTITYDETECTOR_TILE = TILES.register("entity_detector", () -> TileEntityType.Builder.create(TileEntityEntityDetector::new, BlocksRegistration.ENTITYDETECTOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBarrel>> BARREL_TILE = TILES.register("barrel", () -> TileEntityType.Builder.create(TileEntityBarrel::new, BlocksRegistration.BARREL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityGauge>> GAUGE_TILE = TILES.register("fluid_gauge", () -> TileEntityType.Builder.create(TileEntityGauge::new, BlocksRegistration.GAUGE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityEnergyLevel>> ENERGYLEVEL_TILE = TILES.register("energy_level", () -> TileEntityType.Builder.create(TileEntityEnergyLevel::new, BlocksRegistration.ENERGYLEVEL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityElectricPump>> ELECTRICPUMP_TILE = TILES.register("electric_pump", () -> TileEntityType.Builder.create(TileEntityElectricPump::new, BlocksRegistration.ELECTRICPUMP.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityTransformerHV>> TRANSFORMERHV_TILE = TILES.register("transformer_hv", () -> TileEntityType.Builder.create(TileEntityTransformerHV::new, BlocksRegistration.TRANSFORMERHV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBatteryBank>> BATTERYBANK_TILE = TILES.register("battery_bank", () -> TileEntityType.Builder.create(TileEntityBatteryBank::new, BlocksRegistration.BATTERYBANK.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFluidPipe>> FLUIDPIPE_TILE = TILES.register("fluid_pipe", () -> TileEntityType.Builder.create(TileEntityFluidPipe::new, BlocksRegistration.FLUIDPIPE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFluidPipeBaseGauge>> FLUIDPIPEGAUGE_TILE = TILES.register("fluid_pipe_gauge", () -> TileEntityType.Builder.create(TileEntityFluidPipeBaseGauge::new, BlocksRegistration.FLUIDPIPEGAUGE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntitySteamBoiler>> STEAMBOILER_TILE = TILES.register("steam_boiler", () -> TileEntityType.Builder.create(TileEntitySteamBoiler::new, BlocksRegistration.STEAMBOILER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntitySteamTurbine>> STEAMTURBINE_TILE = TILES.register("steam_turbine", () -> TileEntityType.Builder.create(TileEntitySteamTurbine::new, BlocksRegistration.STEAMTURBINE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityMining>> MINING_TILE = TILES.register("mining", () -> TileEntityType.Builder.create(TileEntityMining::new, BlocksRegistration.MINING.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityTrash>> TRASH_TILE = TILES.register("trash", () -> TileEntityType.Builder.create(TileEntityTrash::new, BlocksRegistration.TRASH.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBunkBed>> BUNKBED_TILE = TILES.register("bunkbed", () -> TileEntityType.Builder.create(TileEntityBunkBed::new, BlocksRegistration.BUNKBED.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBunkerHatch>> BUNKERHATCH_TILE = TILES.register("bunker_hatch", () -> TileEntityType.Builder.create(TileEntityBunkerHatch::new, BlocksRegistration.BUNKERHATCH.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityCatWalk>> CATWALK_TILE = TILES.register("catwalk", () -> TileEntityType.Builder.create(TileEntityCatWalk::new, BlocksRegistration.CATWALK.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityCatWalkStair>> CATWALKSTAIR_TILE = TILES.register("catwalk_stair", () -> TileEntityType.Builder.create(TileEntityCatWalkStair::new, BlocksRegistration.CATWALKSTAIR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBulkConveyor>> CONVEYORV_TILE = TILES.register("conveyor_bulk", () -> TileEntityType.Builder.create(TileEntityBulkConveyor::new, BlocksRegistration.CONVEYORV.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBulkConveyorHopper>> CONVEYORVHOPPER_TILE = TILES.register("conveyor_bulk_hopper", () -> TileEntityType.Builder.create(TileEntityBulkConveyorHopper::new, BlocksRegistration.CONVEYORVHOPPER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityBulkConveyorInserter>> CONVEYORVINSERTER_TILE = TILES.register("conveyor_bulk_inserter", () -> TileEntityType.Builder.create(TileEntityBulkConveyorInserter::new, BlocksRegistration.CONVEYORVINSERTER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityDamIntake>> DAMINTAKE_TILE = TILES.register("dam_intake", () -> TileEntityType.Builder.create(TileEntityDamIntake::new, BlocksRegistration.DAMINTAKE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityConcrete>> CONCRETE_TILE = TILES.register("concrete", () -> TileEntityType.Builder.create(TileEntityConcrete::new, BlocksRegistration.CONCRETE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityWireIsolator>> HVISOLATOR_TILE = TILES.register("isolator_hv", () -> TileEntityType.Builder.create(TileEntityWireIsolator::new, BlocksRegistration.HVISOLATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityCableTray>> CABLETRAY_TILE = TILES.register("cable_tray", () -> TileEntityType.Builder.create(TileEntityCableTray::new, BlocksRegistration.CABLETRAY.get()).build(null));
    public static final RegistryObject<TileEntityType<TEBigFenceColumn>> BIGFENCE_COLUMN = TILES.register("fence_big_column", () -> TileEntityType.Builder.create(TEBigFenceColumn::new, BlocksRegistration.BIGFENCECOLUMN.get()).build(null));
    public static final RegistryObject<TileEntityType<TEBigFenceCorner>> BIGFENCE_CORNER = TILES.register("fence_big_corner", () -> TileEntityType.Builder.create(TEBigFenceCorner::new, BlocksRegistration.BIGFENCECORNER.get()).build(null));
    public static final RegistryObject<TileEntityType<TEFloorPipe>> FLOOR_PIPE = TILES.register("floor_pipe", () -> TileEntityType.Builder.create(TEFloorPipe::new, BlocksRegistration.FLOORPIPE.get()).build(null));
    public static final RegistryObject<TileEntityType<TEPillarPipe>> PILLAR_PIPE = TILES.register("iron_pillar_pipe", () -> TileEntityType.Builder.create(TEPillarPipe::new, BlocksRegistration.FLOORPIPE.get()).build(null));

    public static void init()
    {
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


}
