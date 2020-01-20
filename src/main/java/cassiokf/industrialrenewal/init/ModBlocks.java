package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks
{

    public static BlockBase blockHazard = new BlockBase(Material.IRON, "block_hazard", References.CREATIVE_IR_GROUP);
    public static BlockBase cautionHazard = new BlockBase(Material.IRON, "caution_hazard", References.CREATIVE_IR_GROUP);
    public static BlockBase defectiveHazard = new BlockBase(Material.IRON, "defective_hazard", References.CREATIVE_IR_GROUP);
    public static BlockBase safetyHazard = new BlockBase(Material.IRON, "safety_hazard", References.CREATIVE_IR_GROUP);
    public static BlockBase radiationHazard = new BlockBase(Material.IRON, "radiation_hazard", References.CREATIVE_IR_GROUP);
    public static BlockBase aisleHazard = new BlockBase(Material.IRON, "aisle_hazard", References.CREATIVE_IR_GROUP);
    public static BlockBase fireHazard = new BlockBase(Material.IRON, "fire_hazard", References.CREATIVE_IR_GROUP);

    //public static BlockConcrete concrete = new BlockConcrete(Material.ROCK, "concrete", References.CREAATIVE_IRWIP_GROUP);
//
    //public static BlockIndustrialFloor blockIndFloor = new BlockIndustrialFloor("industrial_floor", References.CREATIVE_IR_GROUP);
    //public static BlockChimney blockChimney = new BlockChimney("block_chimney", References.CREATIVE_IR_GROUP);
    //public static BlockFirstAidKit firstAidKit = new BlockFirstAidKit("firstaid_kit", References.CREATIVE_IR_GROUP);
    //public static BlockFireExtinguisher fireExtinguisher = new BlockFireExtinguisher("fire_extinguisher", References.CREATIVE_IR_GROUP);
    //public static BlockLocker locker = new BlockLocker("locker", References.CREATIVE_IR_GROUP);
//
    //public static BlockFluidPipe fluidPipe = new BlockFluidPipe("fluid_pipe", References.CREATIVE_IR_GROUP);
    //public static BlockFluidPipeGauge fluidPipeGauge = new BlockFluidPipeGauge("fluid_pipe_gauge", References.CREATIVE_IR_GROUP);
//
    //public static BlockEnergyCable energyCableLV = new BlockEnergyCable(EnumEnergyCableType.LV, "energy_cable_lv", References.CREATIVE_IR_GROUP);
    //public static BlockEnergyCable energyCableMV = new BlockEnergyCable(EnumEnergyCableType.MV, "energy_cable", References.CREATIVE_IR_GROUP);
    //public static BlockEnergyCable energyCableHV = new BlockEnergyCable(EnumEnergyCableType.HV, "energy_cable_hv", References.CREATIVE_IR_GROUP);
//
    //public static BlockEnergyCableGauge energyCableGaugeLV = new BlockEnergyCableGauge(EnumEnergyCableType.LV, "energy_cable_gauge_lv", References.CREATIVE_IR_GROUP);
    //public static BlockEnergyCableGauge energyCableGaugeMV = new BlockEnergyCableGauge(EnumEnergyCableType.MV, "energy_cable_gauge", References.CREATIVE_IR_GROUP);
    //public static BlockEnergyCableGauge energyCableGaugeHV = new BlockEnergyCableGauge(EnumEnergyCableType.HV, "energy_cable_gauge_hv", References.CREATIVE_IR_GROUP);
//
    //public static BlockPillarEnergyCable pillarEnergyCableLV = new BlockPillarEnergyCable(EnumEnergyCableType.LV, "iron_pillar_energy_lv", References.CREATIVE_IR_GROUP);
    //public static BlockPillarEnergyCable pillarEnergyCableMV = new BlockPillarEnergyCable(EnumEnergyCableType.MV, "iron_pillar_energy", References.CREATIVE_IR_GROUP);
    //public static BlockPillarEnergyCable pillarEnergyCableHV = new BlockPillarEnergyCable(EnumEnergyCableType.HV, "iron_pillar_energy_hv", References.CREATIVE_IR_GROUP);
//
    //public static BlockPillarFluidPipe pillarFluidPipe = new BlockPillarFluidPipe("iron_pillar_pipe", References.CREATIVE_IR_GROUP);
    //public static BlockFloorPipe floorPipe = new BlockFloorPipe("floor_pipe", References.CREATIVE_IR_GROUP);
    //public static BlockFloorCable floorCableLV = new BlockFloorCable(EnumEnergyCableType.LV, "floor_cable_lv", References.CREATIVE_IR_GROUP);
    //public static BlockFloorCable floorCableMV = new BlockFloorCable(EnumEnergyCableType.MV, "floor_cable", References.CREATIVE_IR_GROUP);
    //public static BlockFloorCable floorCableHV = new BlockFloorCable(EnumEnergyCableType.HV, "floor_cable_hv", References.CREATIVE_IR_GROUP);
    //public static BlockFloorLamp floorLamp = new BlockFloorLamp("floor_lamp", References.CREATIVE_IR_GROUP);
    //public static BlockWireBase hvIsolator = new BlockWireBase("isolator_hv", References.CREATIVE_IR_GROUP);
//
    //public static BlockAlarm alarm = new BlockAlarm("alarm", References.CREATIVE_IR_GROUP);
    //public static BlockRecordPlayer recordPlayer = new BlockRecordPlayer("record_player", References.CREATIVE_IR_GROUP);
//
    //public static BlockCatWalk catWalk = new BlockCatWalk("catwalk", References.CREATIVE_IR_GROUP);
    //public static BlockCatWalk catWalkSteel = new BlockCatWalk("catwalk_steel", References.CREATIVE_IR_GROUP);
    //public static BlockHandRail handRail = new BlockHandRail("handrail", References.CREATIVE_IR_GROUP);
    //public static BlockHandRail handRailSteel = new BlockHandRail("handrail_steel", References.CREATIVE_IR_GROUP);
    //public static BlockCatwalkStair catwalkStair = new BlockCatwalkStair("catwalk_stair", References.CREATIVE_IR_GROUP);
    //public static BlockCatwalkStair catwalkStairSteel = new BlockCatwalkStair("catwalk_stair_steel", References.CREATIVE_IR_GROUP);
    //public static BlockPillar pillar = new BlockPillar("catwalk_pillar", References.CREATIVE_IR_GROUP);
    //public static BlockPillar steel_pillar = new BlockPillar("catwalk_steel_pillar", References.CREATIVE_IR_GROUP);
    //public static BlockColumn column = new BlockColumn("catwalk_column", References.CREATIVE_IR_GROUP);
    //public static BlockColumn columSteel = new BlockColumn("catwalk_column_steel", References.CREATIVE_IR_GROUP);
    //public static BlockCatwalkLadder iladder = new BlockCatwalkLadder("catwalk_ladder", References.CREATIVE_IR_GROUP);
    //public static BlockCatwalkLadder sladder = new BlockCatwalkLadder("catwalk_ladder_steel", References.CREATIVE_IR_GROUP);
    //public static BlockRoof roof = new BlockRoof("roof", References.CREATIVE_IR_GROUP);
    //public static BlockGutter gutter = new BlockGutter("gutter", References.CREATIVE_IR_GROUP);
    //public static BlockLight light = new BlockLight("light", References.CREATIVE_IR_GROUP);
    //public static BlockFluorescent fluorescent = new BlockFluorescent("fluorescent", References.CREATIVE_IR_GROUP);
    //public static BlockDummy dummy = new BlockDummy("dummy", References.CREATIVE_IR_GROUP);
    //public static BlockCatwalkGate catwalkGate = new BlockCatwalkGate("catwalk_gate", References.CREATIVE_IR_GROUP);
    //public static BlockCatwalkHatch hatch = new BlockCatwalkHatch("catwalk_hatch", References.CREATIVE_IR_GROUP);
    //public static BlockWindow window = new BlockWindow("window", References.CREATIVE_IR_GROUP);
    //public static BlockPlatform platform = new BlockPlatform("platform", References.CREATIVE_IR_GROUP);
    //public static BlockBrace brace = new BlockBrace("brace", References.CREATIVE_IR_GROUP);
    //public static BlockBrace braceSteel = new BlockBrace("brace_steel", References.CREATIVE_IR_GROUP);
    //public static BlockScaffold scaffold = new BlockScaffold("scaffold", References.CREATIVE_IR_GROUP);
    //public static BlockFrame frame = new BlockFrame("frame", References.CREATIVE_IR_GROUP);
    //public static BlockBunkBed bunkBed = new BlockBunkBed("bunkbed", References.CREATIVE_IR_GROUP);
    //public static BlockBunkerHatch bunkerHatch = new BlockBunkerHatch("bunker_hatch", References.CREATIVE_IR_GROUP);
//
    //public static BlockBarrel barrel = new BlockBarrel("barrel", References.CREATIVE_IR_GROUP);
    //public static BlockTrash trash = new BlockTrash("trash", References.CREATIVE_IR_GROUP);
    //public static BlockGauge gauge = new BlockGauge("fluid_gauge", References.CREATIVE_IR_GROUP);
    //public static BlockEnergyLevel energyLevel = new BlockEnergyLevel("energy_level", References.CREATIVE_IR_GROUP);
//
    //public static BlockElectricFence efence = new BlockElectricFence("electric_fence", References.CREATIVE_IR_GROUP);
    //public static BlockElectricBigFenceColumn bigFenceColumn = new BlockElectricBigFenceColumn("fence_big_column", References.CREATIVE_IR_GROUP);
    //public static BlockElectricBigFenceWire bigFenceWire = new BlockElectricBigFenceWire("fence_big_wire", References.CREATIVE_IR_GROUP);
    //public static BlockElectricBigFenceCorner bigFenceCorner = new BlockElectricBigFenceCorner("fence_big_corner", References.CREATIVE_IR_GROUP);
    //public static BlockBaseWall concreteWall = new BlockBaseWall("wall_concrete", References.CREATIVE_IR_GROUP);
    //public static BlockElectricGate egate = new BlockElectricGate("electric_gate", References.CREATIVE_IR_GROUP);
    //public static BlockRazorWire razorWire = new BlockRazorWire("razor_wire", References.CREATIVE_IR_GROUP);
//
    //public static BlockDamIntake damIntake = new BlockDamIntake("dam_intake", References.CREAATIVE_IRWIP_GROUP);
//
    //public static BlockInfinityGenerator infinityGenerator = new BlockInfinityGenerator("infinity_generator", References.CREATIVE_IR_GROUP);
    //public static BlockSolarPanel spanel = new BlockSolarPanel("solar_panel", References.CREATIVE_IR_GROUP);
    //public static BlockSolarPanelFrame fpanel = new BlockSolarPanelFrame("solar_panel_frame", References.CREATIVE_IR_GROUP);
    //public static BlockSmallWindTurbine sWindTurbine = new BlockSmallWindTurbine("small_wind_turbine", References.CREATIVE_IR_GROUP);
    //public static BlockWindTurbinePillar turbinePillar = new BlockWindTurbinePillar("small_wind_turbine_pillar", References.CREATIVE_IR_GROUP);
    //public static BlockElectricPump electricPump = new BlockElectricPump("electric_pump", References.CREAATIVE_IRWIP_GROUP);
    //public static BlockBatteryBank batteryBank = new BlockBatteryBank("battery_bank", References.CREATIVE_IR_GROUP);
//
    //public static BlockSensorRain sensorRain = new BlockSensorRain("sensor_rain", References.CREATIVE_IR_GROUP);
    //public static BlockSignalIndicator signalIndicator = new BlockSignalIndicator("signal_indicator", References.CREATIVE_IR_GROUP);
    //public static BlockTrafficLight trafficLight = new BlockTrafficLight("traffic_light", References.CREATIVE_IR_GROUP);
    //public static BlockFuseBox fuseBox = new BlockFuseBox("fuse_box", References.CREATIVE_IR_GROUP);
    //public static BlockFuseBoxConduitExtension fuseBoxConduitExtension = new BlockFuseBoxConduitExtension("conduit_extension", References.CREATIVE_IR_GROUP);
    //public static BlockFuseBoxConnector fuseBoxConnector = new BlockFuseBoxConnector("conduit_connector", References.CREATIVE_IR_GROUP);
    //public static BlockFlameDetector flameDetector = new BlockFlameDetector("flame_detector", References.CREATIVE_IR_GROUP);
    //public static BlockEntityDetector entityDetector = new BlockEntityDetector("entity_detector", References.CREATIVE_IR_GROUP);
    //public static BlockButtonRed buttonRed = new BlockButtonRed("button_red", References.CREATIVE_IR_GROUP);
//
    //public static BlockNormalRail normalRail = new BlockNormalRail("normal_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockCrossingRail crossingRail = new BlockCrossingRail("crossing_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockDetectorRail detectorRail = new BlockDetectorRail("detector_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockBoosterRail boosterRail = new BlockBoosterRail("booster_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockBufferStopRail bufferStopRail = new BlockBufferStopRail("buffer_stop_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockLoaderRail loaderRail = new BlockLoaderRail("rail_loader", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockRailGate railGate = new BlockRailGate("rail_gate", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockCargoLoader cargoLoader = new BlockCargoLoader("cargo_loader", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static BlockFluidLoader fluidLoader = new BlockFluidLoader("fluid_loader", References.CREATIVE_IRLOCOMOTIVE_GROUP);
//
    //public static BlockValvePipeLarge valveLarge = new BlockValvePipeLarge("valve_pipe_large", References.CREATIVE_IR_GROUP);
    //public static BlockEnergySwitch energySwitch = new BlockEnergySwitch("energy_switch", References.CREATIVE_IR_GROUP);
//
    //public static BlockBulkConveyor conveyorV = new BlockBulkConveyor("conveyor_bulk", References.CREATIVE_IR_GROUP, EnumBulkConveyorType.NORMAL);
    //public static BlockBulkConveyor conveyorVHopper = new BlockBulkConveyor("conveyor_bulk_hopper", References.CREATIVE_IR_GROUP, EnumBulkConveyorType.HOPPER);
    //public static BlockBulkConveyor conveyorVInserter = new BlockBulkConveyor("conveyor_bulk_inserter", References.CREATIVE_IR_GROUP, EnumBulkConveyorType.INSERTER);
//
    //public static BlockSignBase signHV = new BlockSignBase("sign_hv", References.CREATIVE_IR_GROUP);
    //public static BlockSignBase signRA = new BlockSignBase("sign_ra", References.CREATIVE_IR_GROUP);
    //public static BlockSignBase signC = new BlockSignBase("sign_c", References.CREATIVE_IR_GROUP);
//
    //public static BlockEotM baseEotM = new BlockEotM("eotm", References.CREATIVE_IR_GROUP);
//
    //public static final BlockFluid steamBlock = new BlockFluid("steam", FluidInit.STEAM, References.CREAATIVE_IRWIP_GROUP);
//
    //public static BlockSteamBoiler steamBoiler = new BlockSteamBoiler("steam_boiler", References.CREATIVE_IR_GROUP);
    //public static BlockSteamTurbine steamTurbine = new BlockSteamTurbine("steam_turbine", References.CREATIVE_IR_GROUP);
    //public static BlockMining mining = new BlockMining("mining", References.CREAATIVE_IRWIP_GROUP);
    //public static BlockTransformerHV transformerHV = new BlockTransformerHV("transformer_hv", References.CREATIVE_IR_GROUP);
//
    //public static BlockChunkLoader chunkLoader = new BlockChunkLoader("chunk_loader", References.CREATIVE_IR_GROUP);
//
    //public static BlockOreVein veinHematite = new BlockOreVein("orevein_hematite", References.CREAATIVE_IRWIP_GROUP);

    public static void register(IForgeRegistry<Block> registry)
    {
        registry.registerAll(
                //veinHematite,
                blockHazard,
                aisleHazard,
                cautionHazard,
                defectiveHazard,
                fireHazard,
                radiationHazard,
                safetyHazard
                //concrete,
                ////Frames
                ////Iron
                //platform,
                //roof,
                //frame,
                //hatch,
                //window,
//
                //catWalk,
                //handRail,
                //catwalkStair,
                //iladder,
                //catwalkGate,
                //pillar,
                //pillarEnergyCableLV,
                //pillarEnergyCableMV,
                //pillarEnergyCableHV,
                //pillarFluidPipe,
                //column,
                //brace,
                ////Steel
                //catWalkSteel,
                //handRailSteel,
                //catwalkStairSteel,
                //sladder,
                //steel_pillar,
                //columSteel,
                //braceSteel,
                ////Redstone
                //alarm,
                //entityDetector,
                //flameDetector,
                //sensorRain,
                //buttonRed,
                //fuseBox,
                //fuseBoxConduitExtension,
                //fuseBoxConnector,
                //signalIndicator,
                //trafficLight,
                ////Utils
                //fireExtinguisher,
                //firstAidKit,
                //recordPlayer,
                //locker,
                //bunkBed,
                //bunkerHatch,
                //blockChimney,
                //scaffold,
                //efence,
                //bigFenceColumn,
                //bigFenceCorner,
                //concreteWall,
                //bigFenceWire,
                //egate,
                //razorWire,
                //damIntake,
                //signC,
                //signHV,
                //signRA,
                //fluorescent,
                //light,
                //chunkLoader,
                ////Floor
                //blockIndFloor,
                //floorCableLV,
                //floorCableMV,
                //floorCableHV,
                //floorLamp,
                //floorPipe,
                ////Pipes
                //energyCableLV,
                //energyCableMV,
                //energyCableHV,
                //energyCableGaugeLV,
                //energyCableGaugeMV,
                //energyCableGaugeHV,
                //fluidPipe,
                //fluidPipeGauge,
                //energySwitch,
                //valveLarge,
                //conveyorV,
                //conveyorVHopper,
                //conveyorVInserter,
                //hvIsolator,
                ////Energy
                //infinityGenerator,
                //spanel,
                //fpanel,
                //sWindTurbine,
                //turbinePillar,
                //batteryBank,
                //electricPump,
                //transformerHV,
                ////Machines
                //steamBoiler,
                //steamTurbine,
                //mining,
                ////Railroad
                //normalRail,
                //boosterRail,
                //crossingRail,
                //detectorRail,
                //loaderRail,
                //railGate,
                //bufferStopRail,
                //cargoLoader,
                //fluidLoader,
                ////dummys
                //dummy,
                ////Fluids Block
                //barrel,
                //trash,
                //gauge,
                //energyLevel,
                //gutter,
                ////Fluids
                ////steamBlock,
                ////Patreons
                //baseEotM
        );
        //registry.register(steamBlock);
    }

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> registry)
    {
        //registry.register(TileEntityType.Builder.create(TileEntityOreVein::new, veinHematite).build(null).setRegistryName(veinHematite.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergySwitch::new, energySwitch).build(null).setRegistryName(energySwitch.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityValvePipeLarge::new, valveLarge).build(null).setRegistryName(valveLarge.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergyCableLV::new, energyCableLV).build(null).setRegistryName(energyCableLV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergyCableMV::new, energyCableMV).build(null).setRegistryName(energyCableMV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergyCableHV::new, energyCableHV).build(null).setRegistryName(energyCableHV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergyCableLVGauge::new, energyCableGaugeLV).build(null).setRegistryName(energyCableGaugeLV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergyCableMVGauge::new, energyCableGaugeMV).build(null).setRegistryName(energyCableGaugeMV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergyCableHVGauge::new, energyCableGaugeHV).build(null).setRegistryName(energyCableGaugeHV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityAlarm::new, alarm).build(null).setRegistryName(alarm.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityGutter::new, gutter).build(null).setRegistryName(gutter.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityInfinityGenerator::new, infinityGenerator).build(null).setRegistryName(infinityGenerator.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntitySolarPanelBase::new, spanel).build(null).setRegistryName(spanel.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntitySolarPanelFrame::new, fpanel).build(null).setRegistryName(fpanel.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntitySmallWindTurbine::new, sWindTurbine).build(null).setRegistryName(sWindTurbine.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityWindTurbinePillar::new, turbinePillar).build(null).setRegistryName(turbinePillar.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityFirstAidKit::new, firstAidKit).build(null).setRegistryName(firstAidKit.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityRecordPlayer::new, recordPlayer).build(null).setRegistryName(recordPlayer.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntitySensorRain::new, sensorRain).build(null).setRegistryName(sensorRain.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityCargoLoader::new, cargoLoader).build(null).setRegistryName(cargoLoader.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityFluidLoader::new, fluidLoader).build(null).setRegistryName(fluidLoader.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityLoaderRail::new, loaderRail).build(null).setRegistryName(loaderRail.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntitySignalIndicator::new, signalIndicator).build(null).setRegistryName(signalIndicator.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityTrafficLight::new, trafficLight).build(null).setRegistryName(trafficLight.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityFuseBox::new, fuseBox).build(null).setRegistryName(fuseBox.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBoxConnector::new, fuseBoxConnector).build(null).setRegistryName(fuseBoxConnector.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityFlameDetector::new, flameDetector).build(null).setRegistryName(flameDetector.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityLocker::new, locker).build(null).setRegistryName(locker.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEntityDetector::new, entityDetector).build(null).setRegistryName(entityDetector.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBarrel::new, barrel).build(null).setRegistryName(barrel.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityGauge::new, gauge).build(null).setRegistryName(gauge.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityEnergyLevel::new, energyLevel).build(null).setRegistryName(energyLevel.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityElectricPump::new, electricPump).build(null).setRegistryName(electricPump.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityTransformerHV::new, transformerHV).build(null).setRegistryName(transformerHV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBatteryBank::new, batteryBank).build(null).setRegistryName(batteryBank.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityFluidPipe::new, fluidPipe).build(null).setRegistryName(fluidPipe.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityFluidPipeGauge::new, fluidPipeGauge).build(null).setRegistryName(fluidPipeGauge.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntitySteamBoiler::new, steamBoiler).build(null).setRegistryName(steamBoiler.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntitySteamTurbine::new, steamTurbine).build(null).setRegistryName(steamTurbine.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityMining::new, mining).build(null).setRegistryName(mining.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityTrash::new, trash).build(null).setRegistryName(trash.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBunkBed::new, bunkBed).build(null).setRegistryName(bunkBed.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBunkerHatch::new, bunkerHatch).build(null).setRegistryName(bunkerHatch.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityChunkLoader::new, chunkLoader).build(null).setRegistryName(chunkLoader.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityCatWalk::new, catWalk).build(null).setRegistryName(catWalk.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityCatWalkStair::new, catwalkStair).build(null).setRegistryName(catwalkStair.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBulkConveyor::new, conveyorV).build(null).setRegistryName(conveyorV.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBulkConveyorHopper::new, conveyorVHopper).build(null).setRegistryName(conveyorVHopper.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityBulkConveyorInserter::new, conveyorVInserter).build(null).setRegistryName(conveyorVInserter.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityDamIntake::new, damIntake).build(null).setRegistryName(damIntake.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityConcrete::new, concrete).build(null).setRegistryName(concrete.getRegistryName()));
        //registry.register(TileEntityType.Builder.create(TileEntityWireBase::new, hvIsolator).build(null).setRegistryName(hvIsolator.getRegistryName()));
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                //veinHematite.createItemBlock(),
                blockHazard.createItemBlock(),
                aisleHazard.createItemBlock(),
                cautionHazard.createItemBlock(),
                defectiveHazard.createItemBlock(),
                fireHazard.createItemBlock(),
                radiationHazard.createItemBlock(),
                safetyHazard.createItemBlock()
                //concrete.createItemBlock(),
                //platform.createItemBlock(),
                //roof.createItemBlock(),
                //frame.createItemBlock(),
                //hatch.createItemBlock(),
                //bunkerHatch.createItemBlock(),
                //window.createItemBlock(),
                //catWalk.createItemBlock(),
                //handRail.createItemBlock(),
                //catwalkStair.createItemBlock(),
                //iladder.createItemBlock(),
                //catwalkGate.createItemBlock(),
                //pillar.createItemBlock(),
                //column.createItemBlock(),
                //brace.createItemBlock(),
                //catWalkSteel.createItemBlock(),
                //handRailSteel.createItemBlock(),
                //catwalkStairSteel.createItemBlock(),
                //sladder.createItemBlock(),
                //steel_pillar.createItemBlock(),
                //columSteel.createItemBlock(),
                //braceSteel.createItemBlock(),
                //alarm.createItemBlock(),
                //entityDetector.createItemBlock(),
                //flameDetector.createItemBlock(),
                //sensorRain.createItemBlock(),
                //fuseBox.createItemBlock(),
                //fuseBoxConduitExtension.createItemBlock(),
                //fuseBoxConnector.createItemBlock(),
                //signalIndicator.createItemBlock(),
                //trafficLight.createItemBlock(),
                //firstAidKit.createItemBlock(),
                //recordPlayer.createItemBlock(),
                //locker.createItemBlock(),
                //bunkBed.createItemBlock(),
                //gutter.createItemBlock(),
                //blockChimney.createItemBlock(),
                //scaffold.createItemBlock(),
                //efence.createItemBlock(),
                //bigFenceColumn.createItemBlock(),
                //bigFenceCorner.createItemBlock(),
                //concreteWall.createItemBlock(),
                //bigFenceWire.createItemBlock(),
                //egate.createItemBlock(),
                //razorWire.createItemBlock(),
                //damIntake.createItemBlock(),
                //signHV.createItemBlock(),
                //fluorescent.createItemBlock(),
                //light.createItemBlock(),
                //blockIndFloor.createItemBlock(),
                //hvIsolator.createItemBlock(),
                //energyCableLV.createItemBlock(),
                //energyCableMV.createItemBlock(),
                //energyCableHV.createItemBlock(),
                //fluidPipe.createItemBlock(),
                //energySwitch.createItemBlock(),
                //valveLarge.createItemBlock(),
                //infinityGenerator.createItemBlock(),
                //spanel.createItemBlock(),
                //fpanel.createItemBlock(),
                //sWindTurbine.createItemBlock(),
                //turbinePillar.createItemBlock(),
                //normalRail.createItemBlock(),
                //boosterRail.createItemBlock(),
                //crossingRail.createItemBlock(),
                //detectorRail.createItemBlock(),
                //loaderRail.createItemBlock(),
                //railGate.createItemBlock(),
                //bufferStopRail.createItemBlock(),
                //cargoLoader.createItemBlock(),
                //fluidLoader.createItemBlock(),
                //gauge.createItemBlock(),
                //energyLevel.createItemBlock(),
                //buttonRed.createItemBlock(),
                //batteryBank.createItemBlock(),
                //electricPump.createItemBlock(),
                //trash.createItemBlock(),
                //transformerHV.createItemBlock(),
                //steamBoiler.createItemBlock(),
                //steamTurbine.createItemBlock(),
                //mining.createItemBlock(),
                //steamBlock.createItemBlock(),
                //baseEotM.createItemBlock(),
                //chunkLoader.createItemBlock(),
                //conveyorV.createItemBlock()
        );
    }
/*
    public static void registerItemModels() {
        veinHematite.registerItemModel(Item.getItemFromBlock(veinHematite));
        blockHazard.registerItemModel(Item.getItemFromBlock(blockHazard));
        blockIndFloor.registerItemModel(Item.getItemFromBlock(blockIndFloor));
        blockChimney.registerItemModel(Item.getItemFromBlock(blockChimney));
        normalRail.registerItemModel(Item.getItemFromBlock(normalRail));
        crossingRail.registerItemModel(Item.getItemFromBlock(crossingRail));
        detectorRail.registerItemModel(Item.getItemFromBlock(detectorRail));
        boosterRail.registerItemModel(Item.getItemFromBlock(boosterRail));
        energySwitch.registerItemModel(Item.getItemFromBlock(energySwitch));
        valveLarge.registerItemModel(Item.getItemFromBlock(valveLarge));
        alarm.registerItemModel(Item.getItemFromBlock(alarm));
        fluidPipe.registerItemModel(Item.getItemFromBlock(fluidPipe));
        energyCableLV.registerItemModel(Item.getItemFromBlock(energyCableLV));
        energyCableMV.registerItemModel(Item.getItemFromBlock(energyCableMV));
        energyCableHV.registerItemModel(Item.getItemFromBlock(energyCableHV));
        catWalk.registerItemModel(Item.getItemFromBlock(catWalk));
        catwalkStair.registerItemModel(Item.getItemFromBlock(catwalkStair));
        pillar.registerItemModel(Item.getItemFromBlock(pillar));
        column.registerItemModel(Item.getItemFromBlock(column));
        iladder.registerItemModel(Item.getItemFromBlock(iladder));
        roof.registerItemModel(Item.getItemFromBlock(roof));
        bunkBed.registerItemModel(Item.getItemFromBlock(bunkBed));
        bunkerHatch.registerItemModel(Item.getItemFromBlock(bunkerHatch));
        gutter.registerItemModel(Item.getItemFromBlock(gutter));
        light.registerItemModel(Item.getItemFromBlock(light));
        fluorescent.registerItemModel(Item.getItemFromBlock(fluorescent));
        catwalkGate.registerItemModel(Item.getItemFromBlock(catwalkGate));
        hatch.registerItemModel(Item.getItemFromBlock(hatch));
        window.registerItemModel(Item.getItemFromBlock(window));
        platform.registerItemModel(Item.getItemFromBlock(platform));
        brace.registerItemModel(Item.getItemFromBlock(brace));
        scaffold.registerItemModel(Item.getItemFromBlock(scaffold));
        frame.registerItemModel(Item.getItemFromBlock(frame));
        efence.registerItemModel(Item.getItemFromBlock(efence));
        bigFenceColumn.registerItemModel(Item.getItemFromBlock(bigFenceColumn));
        bigFenceCorner.registerItemModel(Item.getItemFromBlock(bigFenceCorner));
        bigFenceWire.registerItemModel(Item.getItemFromBlock(bigFenceWire));
        concreteWall.registerItemModel(Item.getItemFromBlock(concreteWall));
        egate.registerItemModel(Item.getItemFromBlock(egate));
        razorWire.registerItemModel(Item.getItemFromBlock(razorWire));
        damIntake.registerItemModel(Item.getItemFromBlock(damIntake));
        infinityGenerator.registerItemModel(Item.getItemFromBlock(infinityGenerator));
        spanel.registerItemModel(Item.getItemFromBlock(spanel));
        fpanel.registerItemModel(Item.getItemFromBlock(fpanel));
        sWindTurbine.registerItemModel(Item.getItemFromBlock(sWindTurbine));
        turbinePillar.registerItemModel(Item.getItemFromBlock(turbinePillar));
        firstAidKit.registerItemModel(Item.getItemFromBlock(firstAidKit));
        bufferStopRail.registerItemModel(Item.getItemFromBlock(bufferStopRail));
        signHV.registerItemModel(Item.getItemFromBlock(signHV));
        recordPlayer.registerItemModel(Item.getItemFromBlock(recordPlayer));
        sensorRain.registerItemModel(Item.getItemFromBlock(sensorRain));
        loaderRail.registerItemModel(Item.getItemFromBlock(loaderRail));
        railGate.registerItemModel(Item.getItemFromBlock(railGate));
        cargoLoader.registerItemModel(Item.getItemFromBlock(cargoLoader));
        fluidLoader.registerItemModel(Item.getItemFromBlock(fluidLoader));
        signalIndicator.registerItemModel(Item.getItemFromBlock(signalIndicator));
        trafficLight.registerItemModel(Item.getItemFromBlock(trafficLight));
        fuseBox.registerItemModel(Item.getItemFromBlock(fuseBox));
        fuseBoxConduitExtension.registerItemModel(Item.getItemFromBlock(fuseBoxConduitExtension));
        fuseBoxConnector.registerItemModel(Item.getItemFromBlock(fuseBoxConnector));
        cautionHazard.registerItemModel(Item.getItemFromBlock(cautionHazard));
        defectiveHazard.registerItemModel(Item.getItemFromBlock(defectiveHazard));
        safetyHazard.registerItemModel(Item.getItemFromBlock(safetyHazard));
        radiationHazard.registerItemModel(Item.getItemFromBlock(radiationHazard));
        aisleHazard.registerItemModel(Item.getItemFromBlock(aisleHazard));
        fireHazard.registerItemModel(Item.getItemFromBlock(fireHazard));
        handRail.registerItemModel(Item.getItemFromBlock(handRail));
        flameDetector.registerItemModel(Item.getItemFromBlock(flameDetector));
        locker.registerItemModel(Item.getItemFromBlock(locker));
        entityDetector.registerItemModel(Item.getItemFromBlock(entityDetector));
        steel_pillar.registerItemModel(Item.getItemFromBlock(steel_pillar));
        braceSteel.registerItemModel(Item.getItemFromBlock(braceSteel));
        columSteel.registerItemModel(Item.getItemFromBlock(columSteel));
        concrete.registerItemModel(Item.getItemFromBlock(concrete));
        catWalkSteel.registerItemModel(Item.getItemFromBlock(catWalkSteel));
        handRailSteel.registerItemModel(Item.getItemFromBlock(handRailSteel));
        sladder.registerItemModel(Item.getItemFromBlock(sladder));
        catwalkStairSteel.registerItemModel(Item.getItemFromBlock(catwalkStairSteel));
        barrel.registerItemModel(Item.getItemFromBlock(barrel));
        trash.registerItemModel(Item.getItemFromBlock(trash));
        gauge.registerItemModel(Item.getItemFromBlock(gauge));
        energyLevel.registerItemModel(Item.getItemFromBlock(energyLevel));
        buttonRed.registerItemModel(Item.getItemFromBlock(buttonRed));
        batteryBank.registerItemModel(Item.getItemFromBlock(batteryBank));
        electricPump.registerItemModel(Item.getItemFromBlock(electricPump));
        transformerHV.registerItemModel(Item.getItemFromBlock(transformerHV));
        steamBoiler.registerItemModel(Item.getItemFromBlock(steamBoiler));
        steamTurbine.registerItemModel(Item.getItemFromBlock(steamTurbine));
        mining.registerItemModel(Item.getItemFromBlock(mining));
        steamBlock.registerItemModel(Item.getItemFromBlock(steamBlock));
        baseEotM.registerItemModel(Item.getItemFromBlock(baseEotM));
        chunkLoader.registerItemModel(Item.getItemFromBlock(chunkLoader));
        conveyorV.registerItemModel(Item.getItemFromBlock(conveyorV));
        hvIsolator.registerItemModel(Item.getItemFromBlock(hvIsolator));
    }*/
}
