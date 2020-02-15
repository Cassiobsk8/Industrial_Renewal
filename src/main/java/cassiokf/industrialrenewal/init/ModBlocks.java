package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.*;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.*;
import cassiokf.industrialrenewal.blocks.railroad.*;
import cassiokf.industrialrenewal.blocks.redstone.*;
import cassiokf.industrialrenewal.enums.EnumBulkConveyorType;
import cassiokf.industrialrenewal.enums.EnumEnergyCableType;
import cassiokf.industrialrenewal.fluids.BlockSteam;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(References.MODID)
public class ModBlocks
{
    public static Block blockHazard = new BlockNormalCube().setRegistryName(References.MODID, "block_hazard");
    public static Block cautionHazard = new BlockNormalCube().setRegistryName(References.MODID, "caution_hazard");
    public static Block defectiveHazard = new BlockNormalCube().setRegistryName(References.MODID, "defective_hazard");
    public static Block safetyHazard = new BlockNormalCube().setRegistryName(References.MODID, "safety_hazard");
    public static Block radiationHazard = new BlockNormalCube().setRegistryName(References.MODID, "radiation_hazard");
    public static Block aisleHazard = new BlockNormalCube().setRegistryName(References.MODID, "aisle_hazard");
    public static Block fireHazard = new BlockNormalCube().setRegistryName(References.MODID, "fire_hazard");

    public static Block concrete = new BlockConcrete(Block.Properties.create(Material.ROCK)).setRegistryName(References.MODID, "concrete");

    public static Block blockIndFloor = new BlockIndustrialFloor(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "industrial_floor");
    public static Block blockChimney = new BlockChimney(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "block_chimney");
    public static Block firstAidKit = new BlockFirstAidKit(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "firstaid_kit");
    public static Block fireExtinguisher = new BlockFireExtinguisher(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fire_extinguisher");
    public static Block locker = new BlockLocker(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "locker");

    public static Block fluidPipe = new BlockFluidPipe(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fluid_pipe");
    public static Block fluidPipeGauge = new BlockFluidPipeGauge(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fluid_pipe_gauge");

    public static Block energyCableLV = new BlockEnergyCable(EnumEnergyCableType.LV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_cable_lv");
    public static Block energyCableMV = new BlockEnergyCable(EnumEnergyCableType.MV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_cable");
    public static Block energyCableHV = new BlockEnergyCable(EnumEnergyCableType.HV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_cable_hv");

    public static Block energyCableGaugeLV = new BlockEnergyCableGauge(EnumEnergyCableType.LV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_cable_gauge_lv");
    public static Block energyCableGaugeMV = new BlockEnergyCableGauge(EnumEnergyCableType.MV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_cable_gauge");
    public static Block energyCableGaugeHV = new BlockEnergyCableGauge(EnumEnergyCableType.HV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_cable_gauge_hv");

    public static Block pillarEnergyCableLV = new BlockPillarEnergyCable(EnumEnergyCableType.LV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "iron_pillar_energy_lv");
    public static Block pillarEnergyCableMV = new BlockPillarEnergyCable(EnumEnergyCableType.MV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "iron_pillar_energy");
    public static Block pillarEnergyCableHV = new BlockPillarEnergyCable(EnumEnergyCableType.HV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "iron_pillar_energy_hv");

    public static Block cableTray = new BlockCableTray(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "cable_tray");
    public static Block pillarFluidPipe = new BlockPillarFluidPipe(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "iron_pillar_pipe");
    public static Block floorPipe = new BlockFloorPipe(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "floor_pipe");
    public static Block floorCableLV = new BlockFloorCable(EnumEnergyCableType.LV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "floor_cable_lv");
    public static Block floorCableMV = new BlockFloorCable(EnumEnergyCableType.MV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "floor_cable");
    public static Block floorCableHV = new BlockFloorCable(EnumEnergyCableType.HV, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "floor_cable_hv");
    public static Block floorLamp = new BlockFloorLamp(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "floor_lamp");
    public static Block hvIsolator = new BlockWireBase(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "isolator_hv");

    public static Block alarm = new BlockAlarm(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "alarm");
    public static Block recordPlayer = new BlockRecordPlayer(Block.Properties.create(Material.MISCELLANEOUS)).setRegistryName(References.MODID, "record_player");

    public static Block catWalk = new BlockCatWalk(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk");
    public static Block catWalkSteel = new BlockCatWalk(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_steel");
    public static Block handRail = new BlockHandRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "handrail");
    public static Block handRailSteel = new BlockHandRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "handrail_steel");
    public static Block catwalkStair = new BlockCatwalkStair(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_stair");
    public static Block catwalkStairSteel = new BlockCatwalkStair(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_stair_steel");
    public static Block pillar = new BlockPillar(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_pillar");
    public static Block steel_pillar = new BlockPillar(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_steel_pillar");
    public static Block column = new BlockColumn(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_column");
    public static Block columSteel = new BlockColumn(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_column_steel");
    public static Block iladder = new BlockCatwalkLadder(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_ladder");
    public static Block sladder = new BlockCatwalkLadder(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_ladder_steel");
    public static Block roof = new BlockRoof(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "roof");
    public static Block gutter = new BlockGutter(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "gutter");
    public static Block light = new BlockLight(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "light");
    public static Block fluorescent = new BlockFluorescent(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fluorescent");
    public static Block dummy = new BlockDummy(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "dummy");
    public static Block catwalkGate = new BlockCatwalkGate(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_gate");
    public static Block hatch = new BlockCatwalkHatch(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "catwalk_hatch");
    public static Block window = new BlockWindow(Block.Properties.create(Material.GLASS, DyeColor.GRAY)).setRegistryName(References.MODID, "window");
    public static Block platform = new BlockPlatform(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "platform");
    public static Block brace = new BlockBrace(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "brace");
    public static Block braceSteel = new BlockBrace(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "brace_steel");
    public static Block scaffold = new BlockScaffold(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "scaffold");
    public static Block frame = new BlockFrame(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "frame");
    public static Block bunkBed = new BlockBunkBed(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "bunkbed");
    public static Block bunkerHatch = new BlockBunkerHatch(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "bunker_hatch");

    public static Block barrel = new BlockBarrel(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "barrel");
    public static Block trash = new BlockTrash(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "trash");
    public static Block gauge = new BlockGauge(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fluid_gauge");
    public static Block energyLevel = new BlockEnergyLevel(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_level");

    public static Block efence = new BlockElectricFence(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "electric_fence");
    public static Block bigFenceColumn = new BlockElectricBigFenceColumn(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fence_big_column");
    public static Block bigFenceWire = new BlockElectricBigFenceWire(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fence_big_wire");
    public static Block bigFenceCorner = new BlockElectricBigFenceCorner(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fence_big_corner");
    public static Block concreteWall = new BlockBaseWall(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "wall_concrete");
    public static Block egate = new BlockElectricGate(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "electric_gate");
    public static Block razorWire = new BlockRazorWire(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "razor_wire");

    public static Block damIntake = new BlockDamIntake(Block.Properties.create(Material.ROCK)).setRegistryName(References.MODID, "dam_intake");

    public static Block infinityGenerator = new BlockInfinityGenerator(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "infinity_generator");
    public static Block spanel = new BlockSolarPanel(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "solar_panel");
    public static Block fpanel = new BlockSolarPanelFrame(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "solar_panel_frame");
    public static Block sWindTurbine = new BlockSmallWindTurbine(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "small_wind_turbine");
    public static Block turbinePillar = new BlockWindTurbinePillar(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "small_wind_turbine_pillar");
    public static Block electricPump = new BlockElectricPump(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "electric_pump");
    public static Block batteryBank = new BlockBatteryBank(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "battery_bank");

    public static Block sensorRain = new BlockSensorRain(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "sensor_rain");
    public static Block signalIndicator = new BlockSignalIndicator(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "signal_indicator");
    public static Block trafficLight = new BlockTrafficLight(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "traffic_light");
    public static Block fuseBox = new BlockFuseBox(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fuse_box");
    public static Block fuseBoxConduitExtension = new BlockFuseBoxConduitExtension(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "conduit_extension");
    public static Block fuseBoxConnector = new BlockFuseBoxConnector(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "conduit_connector");
    public static Block flameDetector = new BlockFlameDetector(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "flame_detector");
    public static Block entityDetector = new BlockEntityDetector(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "entity_detector");
    public static Block buttonRed = new BlockButtonRed(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "button_red");

    public static Block normalRail = new BlockNormalRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "normal_rail");
    public static Block crossingRail = new BlockCrossingRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "crossing_rail");
    public static Block detectorRail = new BlockDetectorRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "detector_rail");
    public static Block boosterRail = new BlockBoosterRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "booster_rail");
    public static Block bufferStopRail = new BlockBufferStopRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "buffer_stop_rail");
    public static Block loaderRail = new BlockLoaderRail(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "rail_loader");
    public static Block railGate = new BlockRailGate(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "rail_gate");
    public static Block cargoLoader = new BlockCargoLoader(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "cargo_loader");
    public static Block fluidLoader = new BlockFluidLoader(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "fluid_loader");

    public static Block valveLarge = new BlockValvePipeLarge(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "valve_pipe_large");
    public static Block energySwitch = new BlockEnergySwitch(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "energy_switch");

    public static Block conveyorV = new BlockBulkConveyor(EnumBulkConveyorType.NORMAL, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "conveyor_bulk");
    public static Block conveyorVHopper = new BlockBulkConveyor(EnumBulkConveyorType.HOPPER, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "conveyor_bulk_hopper");
    public static Block conveyorVInserter = new BlockBulkConveyor(EnumBulkConveyorType.INSERTER, Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "conveyor_bulk_inserter");

    public static Block signHV = new BlockSignBase(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "sign_hv");
    public static Block signRA = new BlockSignBase(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "sign_ra");
    public static Block signC = new BlockSignBase(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "sign_c");

    public static Block baseEotM = new BlockEotM(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "eotm");

    //public static final Block steamBlock = new BlockFluid(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID,"steam");

    public static Block steamBoiler = new BlockSteamBoiler(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "steam_boiler");
    public static Block steamTurbine = new BlockSteamTurbine(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "steam_turbine");
    public static Block mining = new BlockMining(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "mining");
    public static Block transformerHV = new BlockTransformerHV(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID, "transformer_hv");

    //public static final Block chunkLoader = new BlockChunkLoader(Block.Properties.create(Material.IRON)).setRegistryName(References.MODID,"chunk_loader");

    public static Block veinHematite = new BlockOreVein().setRegistryName(References.MODID, "orevein_hematite");

    public static Block steamBlock = new BlockSteam().setRegistryName(References.MODID, "steam");

    public static void register(IForgeRegistry<Block> registry)
    {
        final Block[] blocks = {
                veinHematite,
                blockHazard,
                aisleHazard,
                cautionHazard,
                defectiveHazard,
                fireHazard,
                radiationHazard,
                safetyHazard,
                concrete,
                //Frames
                //Iron
                platform,
                roof,
                frame,
                hatch,
                window,
                catWalk,
                handRail,
                catwalkStair,
                iladder,
                catwalkGate,
                pillar,
                pillarEnergyCableLV,
                pillarEnergyCableMV,
                pillarEnergyCableHV,
                pillarFluidPipe,
                column,
                brace,
                //Steel
                catWalkSteel,
                handRailSteel,
                catwalkStairSteel,
                sladder,
                steel_pillar,
                columSteel,
                braceSteel,
                //Redstone
                alarm,
                entityDetector,
                flameDetector,
                sensorRain,
                buttonRed,
                fuseBox,
                fuseBoxConduitExtension,
                fuseBoxConnector,
                signalIndicator,
                trafficLight,
                //Utils
                fireExtinguisher,
                firstAidKit,
                recordPlayer,
                locker,
                bunkBed,
                bunkerHatch,
                blockChimney,
                scaffold,
                efence,
                bigFenceColumn,
                bigFenceCorner,
                concreteWall,
                bigFenceWire,
                egate,
                razorWire,
                damIntake,
                signC,
                signHV,
                signRA,
                fluorescent,
                light,
                //chunkLoader,
                //Floor
                blockIndFloor,
                floorCableLV,
                floorCableMV,
                floorCableHV,
                floorLamp,
                floorPipe,
                //Pipes
                energyCableLV,
                energyCableMV,
                energyCableHV,
                energyCableGaugeLV,
                energyCableGaugeMV,
                energyCableGaugeHV,
                fluidPipe,
                cableTray,
                fluidPipeGauge,
                energySwitch,
                valveLarge,
                conveyorV,
                conveyorVHopper,
                conveyorVInserter,
                hvIsolator,
                //Energy
                infinityGenerator,
                spanel,
                fpanel,
                sWindTurbine,
                turbinePillar,
                batteryBank,
                electricPump,
                transformerHV,
                //Machines
                steamBoiler,
                steamTurbine,
                mining,
                //Railroad
                normalRail,
                boosterRail,
                crossingRail,
                detectorRail,
                loaderRail,
                railGate,
                bufferStopRail,
                cargoLoader,
                fluidLoader,
                //dummys
                dummy,
                //Fluids Block
                barrel,
                trash,
                gauge,
                energyLevel,
                gutter,
                //Patreons
                baseEotM,
                //Fluids
                steamBlock
        };

        registry.registerAll(blocks);
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                //veinHematite.createItemBlock(),
                createItemBlock(veinHematite, References.CREATIVE_IR_GROUP),
                createItemBlock(blockHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(aisleHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(cautionHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(defectiveHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(fireHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(radiationHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(safetyHazard, References.CREATIVE_IR_GROUP),
                createItemBlock(concrete, References.CREATIVE_IR_GROUP),
                createItemBlock(platform, References.CREATIVE_IR_GROUP),
                createItemBlock(roof, References.CREATIVE_IR_GROUP),
                createItemBlock(frame, References.CREATIVE_IR_GROUP),
                createItemBlock(hatch, References.CREATIVE_IR_GROUP),
                createItemBlock(bunkerHatch, References.CREATIVE_IR_GROUP),
                createItemBlock(window, References.CREATIVE_IR_GROUP),
                createItemBlock(catWalk, References.CREATIVE_IR_GROUP),
                createItemBlock(handRail, References.CREATIVE_IR_GROUP),
                createItemBlock(catwalkStair, References.CREATIVE_IR_GROUP),
                createItemBlock(iladder, References.CREATIVE_IR_GROUP),
                createItemBlock(catwalkGate, References.CREATIVE_IR_GROUP),
                createItemBlock(pillar, References.CREATIVE_IR_GROUP),
                createItemBlock(column, References.CREATIVE_IR_GROUP),
                createItemBlock(brace, References.CREATIVE_IR_GROUP),
                createItemBlock(catWalkSteel, References.CREATIVE_IR_GROUP),
                createItemBlock(handRailSteel, References.CREATIVE_IR_GROUP),
                createItemBlock(catwalkStairSteel, References.CREATIVE_IR_GROUP),
                createItemBlock(sladder, References.CREATIVE_IR_GROUP),
                createItemBlock(steel_pillar, References.CREATIVE_IR_GROUP),
                createItemBlock(columSteel, References.CREATIVE_IR_GROUP),
                createItemBlock(braceSteel, References.CREATIVE_IR_GROUP),
                createItemBlock(alarm, References.CREATIVE_IR_GROUP),
                createItemBlock(entityDetector, References.CREATIVE_IR_GROUP),
                createItemBlock(flameDetector, References.CREATIVE_IR_GROUP),
                createItemBlock(sensorRain, References.CREATIVE_IR_GROUP),
                createItemBlock(fuseBox, References.CREATIVE_IR_GROUP),
                createItemBlock(fuseBoxConduitExtension, References.CREATIVE_IR_GROUP),
                createItemBlock(fuseBoxConnector, References.CREATIVE_IR_GROUP),
                createItemBlock(signalIndicator, References.CREATIVE_IR_GROUP),
                createItemBlock(trafficLight, References.CREATIVE_IR_GROUP),
                createItemBlock(firstAidKit, References.CREATIVE_IR_GROUP),
                createItemBlock(recordPlayer, References.CREATIVE_IR_GROUP),
                createItemBlock(locker, References.CREATIVE_IR_GROUP),
                createItemBlock(bunkBed, References.CREATIVE_IR_GROUP),
                createItemBlock(gutter, References.CREATIVE_IR_GROUP),
                createItemBlock(blockChimney, References.CREATIVE_IR_GROUP),
                createItemBlock(scaffold, References.CREATIVE_IR_GROUP),
                createItemBlock(efence, References.CREATIVE_IR_GROUP),
                createItemBlock(bigFenceColumn, References.CREATIVE_IR_GROUP),
                createItemBlock(bigFenceCorner, References.CREATIVE_IR_GROUP),
                createItemBlock(concreteWall, References.CREATIVE_IR_GROUP),
                createItemBlock(bigFenceWire, References.CREATIVE_IR_GROUP),
                createItemBlock(egate, References.CREATIVE_IR_GROUP),
                createItemBlock(razorWire, References.CREATIVE_IR_GROUP),
                createItemBlock(damIntake, References.CREATIVE_IR_GROUP),
                createItemBlock(signHV, References.CREATIVE_IR_GROUP),
                createItemBlock(fluorescent, References.CREATIVE_IR_GROUP),
                createItemBlock(light, References.CREATIVE_IR_GROUP),
                createItemBlock(blockIndFloor, References.CREATIVE_IR_GROUP),
                createItemBlock(hvIsolator, References.CREATIVE_IR_GROUP),
                createItemBlock(energyCableLV, References.CREATIVE_IR_GROUP),
                createItemBlock(energyCableMV, References.CREATIVE_IR_GROUP),
                createItemBlock(energyCableHV, References.CREATIVE_IR_GROUP),
                createItemBlock(fluidPipe, References.CREATIVE_IR_GROUP),
                createItemBlock(cableTray, References.CREATIVE_IR_GROUP),
                createItemBlock(energySwitch, References.CREATIVE_IR_GROUP),
                createItemBlock(valveLarge, References.CREATIVE_IR_GROUP),
                createItemBlock(infinityGenerator, References.CREATIVE_IR_GROUP),
                createItemBlock(spanel, References.CREATIVE_IR_GROUP),
                createItemBlock(fpanel, References.CREATIVE_IR_GROUP),
                createItemBlock(sWindTurbine, References.CREATIVE_IR_GROUP),
                createItemBlock(turbinePillar, References.CREATIVE_IR_GROUP),
                createItemBlock(normalRail, References.CREATIVE_IR_GROUP),
                createItemBlock(boosterRail, References.CREATIVE_IR_GROUP),
                createItemBlock(crossingRail, References.CREATIVE_IR_GROUP),
                createItemBlock(detectorRail, References.CREATIVE_IR_GROUP),
                createItemBlock(loaderRail, References.CREATIVE_IR_GROUP),
                createItemBlock(railGate, References.CREATIVE_IR_GROUP),
                createItemBlock(bufferStopRail, References.CREATIVE_IR_GROUP),
                createItemBlock(cargoLoader, References.CREATIVE_IR_GROUP),
                createItemBlock(fluidLoader, References.CREATIVE_IR_GROUP),
                createItemBlock(gauge, References.CREATIVE_IR_GROUP),
                createItemBlock(energyLevel, References.CREATIVE_IR_GROUP),
                createItemBlock(buttonRed, References.CREATIVE_IR_GROUP),
                createItemBlock(batteryBank, References.CREATIVE_IR_GROUP),
                createItemBlock(electricPump, References.CREATIVE_IR_GROUP),
                createItemBlock(trash, References.CREATIVE_IR_GROUP),
                createItemBlock(transformerHV, References.CREATIVE_IR_GROUP),
                createItemBlock(steamBoiler, References.CREATIVE_IR_GROUP),
                createItemBlock(steamTurbine, References.CREATIVE_IR_GROUP),
                createItemBlock(mining, References.CREATIVE_IR_GROUP),
                //createItemBlock(steamBlock, References.CREATIVE_IR_GROUP),
                createItemBlock(baseEotM, References.CREATIVE_IR_GROUP),
                //createItemBlock(chunkLoader, References.CREATIVE_IR_GROUP),
                createItemBlock(conveyorV, References.CREATIVE_IR_GROUP)
        );
    }

    public static Item createItemBlock(Block block, ItemGroup group)
    {
        return new BlockItem(block, new Item.Properties().group(group)).setRegistryName(block.getRegistryName());
    }
}
