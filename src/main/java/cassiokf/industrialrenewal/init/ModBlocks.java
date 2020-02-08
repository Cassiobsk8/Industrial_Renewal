package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks
{

    public static final Block blockHazard = new BlockBase(Block.Properties.create(Material.IRON)).setRegistryName("block_hazard");
    public static final Block cautionHazard = new BlockBase(Block.Properties.create(Material.IRON)).setRegistryName("caution_hazard");
    public static final Block defectiveHazard = new BlockBase(Block.Properties.create(Material.IRON)).setRegistryName("defective_hazard");
    public static final Block safetyHazard = new BlockBase(Block.Properties.create(Material.IRON)).setRegistryName("safety_hazard");
    public static final Block radiationHazard = new BlockBase(Block.Properties.create(Material.IRON)).setRegistryName("radiation_hazard");
    public static final Block aisleHazard = new BlockBase(Block.Properties.create(Material.IRON)).setRegistryName("aisle_hazard");
    public static final Block fireHazard = new BlockBase(Block.Properties.create(Material.IRON)).setRegistryName("fire_hazard");

    //public static final Block concrete = new BlockConcrete(Material.ROCK, "concrete", References.CREAATIVE_IRWIP_GROUP);
//
    public static final Block blockIndFloor = new BlockIndustrialFloor("industrial_floor", References.CREATIVE_IR_GROUP);
    //public static final Block blockChimney = new BlockChimney("block_chimney", References.CREATIVE_IR_GROUP);
    //public static final Block firstAidKit = new BlockFirstAidKit("firstaid_kit", References.CREATIVE_IR_GROUP);
    //public static final Block fireExtinguisher = new BlockFireExtinguisher("fire_extinguisher", References.CREATIVE_IR_GROUP);
    //public static final Block locker = new BlockLocker("locker", References.CREATIVE_IR_GROUP);
//
    //public static final Block fluidPipe = new BlockFluidPipe("fluid_pipe", References.CREATIVE_IR_GROUP);
    //public static final Block fluidPipeGauge = new BlockFluidPipeGauge("fluid_pipe_gauge", References.CREATIVE_IR_GROUP);
//
    //public static final Block energyCableLV = new BlockEnergyCable(EnumEnergyCableType.LV, "energy_cable_lv", References.CREATIVE_IR_GROUP);
    //public static final Block energyCableMV = new BlockEnergyCable(EnumEnergyCableType.MV, "energy_cable", References.CREATIVE_IR_GROUP);
    //public static final Block energyCableHV = new BlockEnergyCable(EnumEnergyCableType.HV, "energy_cable_hv", References.CREATIVE_IR_GROUP);
//
    //public static final Block energyCableGaugeLV = new BlockEnergyCableGauge(EnumEnergyCableType.LV, "energy_cable_gauge_lv", References.CREATIVE_IR_GROUP);
    //public static final Block energyCableGaugeMV = new BlockEnergyCableGauge(EnumEnergyCableType.MV, "energy_cable_gauge", References.CREATIVE_IR_GROUP);
    //public static final Block energyCableGaugeHV = new BlockEnergyCableGauge(EnumEnergyCableType.HV, "energy_cable_gauge_hv", References.CREATIVE_IR_GROUP);
//
    //public static final Block pillarEnergyCableLV = new BlockPillarEnergyCable(EnumEnergyCableType.LV, "iron_pillar_energy_lv", References.CREATIVE_IR_GROUP);
    //public static final Block pillarEnergyCableMV = new BlockPillarEnergyCable(EnumEnergyCableType.MV, "iron_pillar_energy", References.CREATIVE_IR_GROUP);
    //public static final Block pillarEnergyCableHV = new BlockPillarEnergyCable(EnumEnergyCableType.HV, "iron_pillar_energy_hv", References.CREATIVE_IR_GROUP);
//
    //public static final Block pillarFluidPipe = new BlockPillarFluidPipe("iron_pillar_pipe", References.CREATIVE_IR_GROUP);
    //public static final Block floorPipe = new BlockFloorPipe("floor_pipe", References.CREATIVE_IR_GROUP);
    //public static final Block floorCableLV = new BlockFloorCable(EnumEnergyCableType.LV, "floor_cable_lv", References.CREATIVE_IR_GROUP);
    //public static final Block floorCableMV = new BlockFloorCable(EnumEnergyCableType.MV, "floor_cable", References.CREATIVE_IR_GROUP);
    //public static final Block floorCableHV = new BlockFloorCable(EnumEnergyCableType.HV, "floor_cable_hv", References.CREATIVE_IR_GROUP);
    //public static final Block floorLamp = new BlockFloorLamp("floor_lamp", References.CREATIVE_IR_GROUP);
    //public static final Block hvIsolator = new BlockWireBase("isolator_hv", References.CREATIVE_IR_GROUP);
//
    //public static final Block alarm = new BlockAlarm("alarm", References.CREATIVE_IR_GROUP);
    //public static final Block recordPlayer = new BlockRecordPlayer("record_player", References.CREATIVE_IR_GROUP);
//
    //public static final Block catWalk = new BlockCatWalk("catwalk", References.CREATIVE_IR_GROUP);
    //public static final Block catWalkSteel = new BlockCatWalk("catwalk_steel", References.CREATIVE_IR_GROUP);
    //public static final Block handRail = new BlockHandRail("handrail", References.CREATIVE_IR_GROUP);
    //public static final Block handRailSteel = new BlockHandRail("handrail_steel", References.CREATIVE_IR_GROUP);
    //public static final Block catwalkStair = new BlockCatwalkStair("catwalk_stair", References.CREATIVE_IR_GROUP);
    //public static final Block catwalkStairSteel = new BlockCatwalkStair("catwalk_stair_steel", References.CREATIVE_IR_GROUP);
    //public static final Block pillar = new BlockPillar("catwalk_pillar", References.CREATIVE_IR_GROUP);
    //public static final Block steel_pillar = new BlockPillar("catwalk_steel_pillar", References.CREATIVE_IR_GROUP);
    //public static final Block column = new BlockColumn("catwalk_column", References.CREATIVE_IR_GROUP);
    //public static final Block columSteel = new BlockColumn("catwalk_column_steel", References.CREATIVE_IR_GROUP);
    //public static final Block iladder = new BlockCatwalkLadder("catwalk_ladder", References.CREATIVE_IR_GROUP);
    //public static final Block sladder = new BlockCatwalkLadder("catwalk_ladder_steel", References.CREATIVE_IR_GROUP);
    //public static final Block roof = new BlockRoof("roof", References.CREATIVE_IR_GROUP);
    //public static final Block gutter = new BlockGutter("gutter", References.CREATIVE_IR_GROUP);
    //public static final Block light = new BlockLight("light", References.CREATIVE_IR_GROUP);
    //public static final Block fluorescent = new BlockFluorescent("fluorescent", References.CREATIVE_IR_GROUP);
    //public static final Block dummy = new BlockDummy("dummy", References.CREATIVE_IR_GROUP);
    //public static final Block catwalkGate = new BlockCatwalkGate("catwalk_gate", References.CREATIVE_IR_GROUP);
    //public static final Block hatch = new BlockCatwalkHatch("catwalk_hatch", References.CREATIVE_IR_GROUP);
    //public static final Block window = new BlockWindow("window", References.CREATIVE_IR_GROUP);
    //public static final Block platform = new BlockPlatform("platform", References.CREATIVE_IR_GROUP);
    //public static final Block brace = new BlockBrace("brace", References.CREATIVE_IR_GROUP);
    //public static final Block braceSteel = new BlockBrace("brace_steel", References.CREATIVE_IR_GROUP);
    //public static final Block scaffold = new BlockScaffold("scaffold", References.CREATIVE_IR_GROUP);
    //public static final Block frame = new BlockFrame("frame", References.CREATIVE_IR_GROUP);
    //public static final Block bunkBed = new BlockBunkBed("bunkbed", References.CREATIVE_IR_GROUP);
    //public static final Block bunkerHatch = new BlockBunkerHatch("bunker_hatch", References.CREATIVE_IR_GROUP);
//
    //public static final Block barrel = new BlockBarrel("barrel", References.CREATIVE_IR_GROUP);
    //public static final Block trash = new BlockTrash("trash", References.CREATIVE_IR_GROUP);
    //public static final Block gauge = new BlockGauge("fluid_gauge", References.CREATIVE_IR_GROUP);
    //public static final Block energyLevel = new BlockEnergyLevel("energy_level", References.CREATIVE_IR_GROUP);
//
    //public static final Block efence = new BlockElectricFence("electric_fence", References.CREATIVE_IR_GROUP);
    //public static final Block bigFenceColumn = new BlockElectricBigFenceColumn("fence_big_column", References.CREATIVE_IR_GROUP);
    //public static final Block bigFenceWire = new BlockElectricBigFenceWire("fence_big_wire", References.CREATIVE_IR_GROUP);
    //public static final Block bigFenceCorner = new BlockElectricBigFenceCorner("fence_big_corner", References.CREATIVE_IR_GROUP);
    //public static final Block concreteWall = new BlockBaseWall("wall_concrete", References.CREATIVE_IR_GROUP);
    //public static final Block egate = new BlockElectricGate("electric_gate", References.CREATIVE_IR_GROUP);
    //public static final Block razorWire = new BlockRazorWire("razor_wire", References.CREATIVE_IR_GROUP);
//
    //public static final Block damIntake = new BlockDamIntake("dam_intake", References.CREAATIVE_IRWIP_GROUP);
//
    //public static final Block infinityGenerator = new BlockInfinityGenerator("infinity_generator", References.CREATIVE_IR_GROUP);
    //public static final Block spanel = new BlockSolarPanel("solar_panel", References.CREATIVE_IR_GROUP);
    //public static final Block fpanel = new BlockSolarPanelFrame("solar_panel_frame", References.CREATIVE_IR_GROUP);
    //public static final Block sWindTurbine = new BlockSmallWindTurbine("small_wind_turbine", References.CREATIVE_IR_GROUP);
    //public static final Block turbinePillar = new BlockWindTurbinePillar("small_wind_turbine_pillar", References.CREATIVE_IR_GROUP);
    //public static final Block electricPump = new BlockElectricPump("electric_pump", References.CREAATIVE_IRWIP_GROUP);
    //public static final Block batteryBank = new BlockBatteryBank("battery_bank", References.CREATIVE_IR_GROUP);
//
    //public static final Block sensorRain = new BlockSensorRain("sensor_rain", References.CREATIVE_IR_GROUP);
    //public static final Block signalIndicator = new BlockSignalIndicator("signal_indicator", References.CREATIVE_IR_GROUP);
    //public static final Block trafficLight = new BlockTrafficLight("traffic_light", References.CREATIVE_IR_GROUP);
    //public static final Block fuseBox = new BlockFuseBox("fuse_box", References.CREATIVE_IR_GROUP);
    //public static final Block fuseBoxConduitExtension = new BlockFuseBoxConduitExtension("conduit_extension", References.CREATIVE_IR_GROUP);
    //public static final Block fuseBoxConnector = new BlockFuseBoxConnector("conduit_connector", References.CREATIVE_IR_GROUP);
    //public static final Block flameDetector = new BlockFlameDetector("flame_detector", References.CREATIVE_IR_GROUP);
    //public static final Block entityDetector = new BlockEntityDetector("entity_detector", References.CREATIVE_IR_GROUP);
    //public static final Block buttonRed = new BlockButtonRed("button_red", References.CREATIVE_IR_GROUP);
//
    //public static final Block normalRail = new BlockNormalRail("normal_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block crossingRail = new BlockCrossingRail("crossing_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block detectorRail = new BlockDetectorRail("detector_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block boosterRail = new BlockBoosterRail("booster_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block bufferStopRail = new BlockBufferStopRail("buffer_stop_rail", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block loaderRail = new BlockLoaderRail("rail_loader", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block railGate = new BlockRailGate("rail_gate", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block cargoLoader = new BlockCargoLoader("cargo_loader", References.CREATIVE_IRLOCOMOTIVE_GROUP);
    //public static final Block fluidLoader = new BlockFluidLoader("fluid_loader", References.CREATIVE_IRLOCOMOTIVE_GROUP);
//
    //public static final Block valveLarge = new BlockValvePipeLarge("valve_pipe_large", References.CREATIVE_IR_GROUP);
    //public static final Block energySwitch = new BlockEnergySwitch("energy_switch", References.CREATIVE_IR_GROUP);
//
    //public static final Block conveyorV = new BlockBulkConveyor("conveyor_bulk", References.CREATIVE_IR_GROUP, EnumBulkConveyorType.NORMAL);
    //public static final Block conveyorVHopper = new BlockBulkConveyor("conveyor_bulk_hopper", References.CREATIVE_IR_GROUP, EnumBulkConveyorType.HOPPER);
    //public static final Block conveyorVInserter = new BlockBulkConveyor("conveyor_bulk_inserter", References.CREATIVE_IR_GROUP, EnumBulkConveyorType.INSERTER);
//
    //public static final Block signHV = new BlockSignBase("sign_hv", References.CREATIVE_IR_GROUP);
    //public static final Block signRA = new BlockSignBase("sign_ra", References.CREATIVE_IR_GROUP);
    //public static final Block signC = new BlockSignBase("sign_c", References.CREATIVE_IR_GROUP);
//
    //public static final Block baseEotM = new BlockEotM("eotm", References.CREATIVE_IR_GROUP);
//
    //public static final Block steamBlock = new BlockFluid("steam", FluidInit.STEAM, References.CREAATIVE_IRWIP_GROUP);
//
    //public static final Block steamBoiler = new BlockSteamBoiler("steam_boiler", References.CREATIVE_IR_GROUP);
    //public static final Block steamTurbine = new BlockSteamTurbine("steam_turbine", References.CREATIVE_IR_GROUP);
    //public static final Block mining = new BlockMining("mining", References.CREAATIVE_IRWIP_GROUP);
    //public static final Block transformerHV = new BlockTransformerHV("transformer_hv", References.CREATIVE_IR_GROUP);
//
    //public static final BlockChunkLoader chunkLoader = new BlockChunkLoader("chunk_loader", References.CREATIVE_IR_GROUP);
//
    //public static final BlockOreVein veinHematite = new BlockOreVein("orevein_hematite", References.CREAATIVE_IRWIP_GROUP);

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
                createItemBlock(blockHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(aisleHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(cautionHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(defectiveHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(fireHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(radiationHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(safetyHazard, References.CREATIVE_IR_GROUP)
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

    public static Item createItemBlock(Block block, ItemGroup group)
    {
        return new BlockItem(block, new Item.Properties().group(group)).setRegistryName(block.getRegistryName());
    }
/*
    public static void registerItemModels() {
        //veinHematite.registerItemModel(Item.getItemFromBlock(veinHematite));
        blockHazard.registerItemModel(Item.getItemFromBlock(blockHazard));
        //blockIndFloor.registerItemModel(Item.getItemFromBlock(blockIndFloor));
        //blockChimney.registerItemModel(Item.getItemFromBlock(blockChimney));
        //normalRail.registerItemModel(Item.getItemFromBlock(normalRail));
        //crossingRail.registerItemModel(Item.getItemFromBlock(crossingRail));
        //detectorRail.registerItemModel(Item.getItemFromBlock(detectorRail));
        //boosterRail.registerItemModel(Item.getItemFromBlock(boosterRail));
        //energySwitch.registerItemModel(Item.getItemFromBlock(energySwitch));
        //valveLarge.registerItemModel(Item.getItemFromBlock(valveLarge));
        //alarm.registerItemModel(Item.getItemFromBlock(alarm));
        //fluidPipe.registerItemModel(Item.getItemFromBlock(fluidPipe));
        //energyCableLV.registerItemModel(Item.getItemFromBlock(energyCableLV));
        //energyCableMV.registerItemModel(Item.getItemFromBlock(energyCableMV));
        //energyCableHV.registerItemModel(Item.getItemFromBlock(energyCableHV));
        //catWalk.registerItemModel(Item.getItemFromBlock(catWalk));
        //catwalkStair.registerItemModel(Item.getItemFromBlock(catwalkStair));
        //pillar.registerItemModel(Item.getItemFromBlock(pillar));
        //column.registerItemModel(Item.getItemFromBlock(column));
        //iladder.registerItemModel(Item.getItemFromBlock(iladder));
        //roof.registerItemModel(Item.getItemFromBlock(roof));
        //bunkBed.registerItemModel(Item.getItemFromBlock(bunkBed));
        //bunkerHatch.registerItemModel(Item.getItemFromBlock(bunkerHatch));
        //gutter.registerItemModel(Item.getItemFromBlock(gutter));
        //light.registerItemModel(Item.getItemFromBlock(light));
        //fluorescent.registerItemModel(Item.getItemFromBlock(fluorescent));
        //catwalkGate.registerItemModel(Item.getItemFromBlock(catwalkGate));
        //hatch.registerItemModel(Item.getItemFromBlock(hatch));
        //window.registerItemModel(Item.getItemFromBlock(window));
        //platform.registerItemModel(Item.getItemFromBlock(platform));
        //brace.registerItemModel(Item.getItemFromBlock(brace));
        //scaffold.registerItemModel(Item.getItemFromBlock(scaffold));
        //frame.registerItemModel(Item.getItemFromBlock(frame));
        //efence.registerItemModel(Item.getItemFromBlock(efence));
        //bigFenceColumn.registerItemModel(Item.getItemFromBlock(bigFenceColumn));
        //bigFenceCorner.registerItemModel(Item.getItemFromBlock(bigFenceCorner));
        //bigFenceWire.registerItemModel(Item.getItemFromBlock(bigFenceWire));
        //concreteWall.registerItemModel(Item.getItemFromBlock(concreteWall));
        //egate.registerItemModel(Item.getItemFromBlock(egate));
        //razorWire.registerItemModel(Item.getItemFromBlock(razorWire));
        //damIntake.registerItemModel(Item.getItemFromBlock(damIntake));
        //infinityGenerator.registerItemModel(Item.getItemFromBlock(infinityGenerator));
        //spanel.registerItemModel(Item.getItemFromBlock(spanel));
        //fpanel.registerItemModel(Item.getItemFromBlock(fpanel));
        //sWindTurbine.registerItemModel(Item.getItemFromBlock(sWindTurbine));
        //turbinePillar.registerItemModel(Item.getItemFromBlock(turbinePillar));
        //firstAidKit.registerItemModel(Item.getItemFromBlock(firstAidKit));
        //bufferStopRail.registerItemModel(Item.getItemFromBlock(bufferStopRail));
        //signHV.registerItemModel(Item.getItemFromBlock(signHV));
        //recordPlayer.registerItemModel(Item.getItemFromBlock(recordPlayer));
        //sensorRain.registerItemModel(Item.getItemFromBlock(sensorRain));
        //loaderRail.registerItemModel(Item.getItemFromBlock(loaderRail));
        //railGate.registerItemModel(Item.getItemFromBlock(railGate));
        //cargoLoader.registerItemModel(Item.getItemFromBlock(cargoLoader));
        //fluidLoader.registerItemModel(Item.getItemFromBlock(fluidLoader));
        //signalIndicator.registerItemModel(Item.getItemFromBlock(signalIndicator));
        //trafficLight.registerItemModel(Item.getItemFromBlock(trafficLight));
        //fuseBox.registerItemModel(Item.getItemFromBlock(fuseBox));
        //fuseBoxConduitExtension.registerItemModel(Item.getItemFromBlock(fuseBoxConduitExtension));
        //fuseBoxConnector.registerItemModel(Item.getItemFromBlock(fuseBoxConnector));
        cautionHazard.registerItemModel(Item.getItemFromBlock(cautionHazard));
        defectiveHazard.registerItemModel(Item.getItemFromBlock(defectiveHazard));
        safetyHazard.registerItemModel(Item.getItemFromBlock(safetyHazard));
        radiationHazard.registerItemModel(Item.getItemFromBlock(radiationHazard));
        aisleHazard.registerItemModel(Item.getItemFromBlock(aisleHazard));
        fireHazard.registerItemModel(Item.getItemFromBlock(fireHazard));
        //handRail.registerItemModel(Item.getItemFromBlock(handRail));
        //flameDetector.registerItemModel(Item.getItemFromBlock(flameDetector));
        //locker.registerItemModel(Item.getItemFromBlock(locker));
        //entityDetector.registerItemModel(Item.getItemFromBlock(entityDetector));
        //steel_pillar.registerItemModel(Item.getItemFromBlock(steel_pillar));
        //braceSteel.registerItemModel(Item.getItemFromBlock(braceSteel));
        //columSteel.registerItemModel(Item.getItemFromBlock(columSteel));
        //concrete.registerItemModel(Item.getItemFromBlock(concrete));
        //catWalkSteel.registerItemModel(Item.getItemFromBlock(catWalkSteel));
        //handRailSteel.registerItemModel(Item.getItemFromBlock(handRailSteel));
        //sladder.registerItemModel(Item.getItemFromBlock(sladder));
        //catwalkStairSteel.registerItemModel(Item.getItemFromBlock(catwalkStairSteel));
        //barrel.registerItemModel(Item.getItemFromBlock(barrel));
        //trash.registerItemModel(Item.getItemFromBlock(trash));
        //gauge.registerItemModel(Item.getItemFromBlock(gauge));
        //energyLevel.registerItemModel(Item.getItemFromBlock(energyLevel));
        //buttonRed.registerItemModel(Item.getItemFromBlock(buttonRed));
        //batteryBank.registerItemModel(Item.getItemFromBlock(batteryBank));
        //electricPump.registerItemModel(Item.getItemFromBlock(electricPump));
        //transformerHV.registerItemModel(Item.getItemFromBlock(transformerHV));
        //steamBoiler.registerItemModel(Item.getItemFromBlock(steamBoiler));
        //steamTurbine.registerItemModel(Item.getItemFromBlock(steamTurbine));
        //mining.registerItemModel(Item.getItemFromBlock(mining));
        //steamBlock.registerItemModel(Item.getItemFromBlock(steamBlock));
        //baseEotM.registerItemModel(Item.getItemFromBlock(baseEotM));
        //chunkLoader.registerItemModel(Item.getItemFromBlock(chunkLoader));
        //conveyorV.registerItemModel(Item.getItemFromBlock(conveyorV));
        //hvIsolator.registerItemModel(Item.getItemFromBlock(hvIsolator));
    }

    public void registerItemModel(Item itemBlock) {
        registerItemRenderer(itemBlock, 0, itemBlock.getRegistryName().toString());
    }

    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(References.MODID + ":" + id, "inventory"));

    }*/
}
