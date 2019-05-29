package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.*;
import cassiokf.industrialrenewal.blocks.rails.*;
import cassiokf.industrialrenewal.fluids.BlockFluid;
import cassiokf.industrialrenewal.tileentity.Fluid.barrel.BlockBarrel;
import cassiokf.industrialrenewal.tileentity.Fluid.fluidpipe.BlockFluidPipe;
import cassiokf.industrialrenewal.tileentity.Fluid.valve.BlockValvePipeLarge;
import cassiokf.industrialrenewal.tileentity.alarm.BlockAlarm;
import cassiokf.industrialrenewal.tileentity.cable.BlockEnergyCable;
import cassiokf.industrialrenewal.tileentity.cable.TileEntityEnergyCable;
import cassiokf.industrialrenewal.tileentity.energy.batterybank.BlockBatteryBank;
import cassiokf.industrialrenewal.tileentity.energy.pump.BlockElectricPump;
import cassiokf.industrialrenewal.tileentity.energy.solarpanel.BlockSolarPanel;
import cassiokf.industrialrenewal.tileentity.firstaidkit.BlockFirstAidKit;
import cassiokf.industrialrenewal.tileentity.fusebox.BlockFuseBox;
import cassiokf.industrialrenewal.tileentity.fusebox.BlockFuseBoxConnector;
import cassiokf.industrialrenewal.tileentity.gauge.BlockGauge;
import cassiokf.industrialrenewal.tileentity.gutter.BlockGutter;
import cassiokf.industrialrenewal.tileentity.locker.BlockLocker;
import cassiokf.industrialrenewal.tileentity.machines.steamboiler.BlockSteamBoilerElectric;
import cassiokf.industrialrenewal.tileentity.railroad.cargoloader.BlockCargoLoader;
import cassiokf.industrialrenewal.tileentity.railroad.fluidloader.BlockFluidLoader;
import cassiokf.industrialrenewal.tileentity.railroad.railloader.BlockLoaderRail;
import cassiokf.industrialrenewal.tileentity.recordplayer.BlockRecordPlayer;
import cassiokf.industrialrenewal.tileentity.sensors.entitydetector.BlockEntityDetector;
import cassiokf.industrialrenewal.tileentity.sensors.flamedetector.BlockFlameDetector;
import cassiokf.industrialrenewal.tileentity.sensors.rain.BlockSensorRain;
import cassiokf.industrialrenewal.tileentity.signalindicator.BlockSignalIndicator;
import cassiokf.industrialrenewal.tileentity.trafficlight.BlockTrafficLight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static BlockBase blockHazard = new BlockBase(Material.IRON, "block_hazard", References.CREATIVE_IR_TAB);
    public static BlockBase cautionHazard = new BlockBase(Material.IRON, "caution_hazard", References.CREATIVE_IR_TAB);
    public static BlockBase defectiveHazard = new BlockBase(Material.IRON, "defective_hazard", References.CREATIVE_IR_TAB);
    public static BlockBase safetyHazard = new BlockBase(Material.IRON, "safety_hazard", References.CREATIVE_IR_TAB);
    public static BlockBase radiationHazard = new BlockBase(Material.IRON, "radiation_hazard", References.CREATIVE_IR_TAB);
    public static BlockBase aisleHazard = new BlockBase(Material.IRON, "aisle_hazard", References.CREATIVE_IR_TAB);
    public static BlockBase fireHazard = new BlockBase(Material.IRON, "fire_hazard", References.CREATIVE_IR_TAB);
    public static BlockBase concrete = new BlockBase(Material.ROCK, "concrete", References.CREAATIVE_IRWIP_TAB);

    public static BlockIndustrialFloor blockIndFloor = new BlockIndustrialFloor("industrial_floor", References.CREAATIVE_IRWIP_TAB);
    public static BlockChimney blockChimney = new BlockChimney("block_chimney", References.CREATIVE_IR_TAB);
    public static BlockFirstAidKit firstAidKit = new BlockFirstAidKit("firstaid_kit", References.CREATIVE_IR_TAB);
    public static BlockFireExtinguisher fireExtinguisher = new BlockFireExtinguisher("fire_extinguisher", References.CREATIVE_IR_TAB);
    public static BlockLocker locker = new BlockLocker("locker", References.CREATIVE_IR_TAB);

    public static BlockFluidPipe fluidPipe = new BlockFluidPipe("fluid_pipe", References.CREAATIVE_IRWIP_TAB);
    public static BlockEnergyCable energyCable = new BlockEnergyCable("energy_cable", References.CREAATIVE_IRWIP_TAB);
    public static BlockFloorPipe floorPipe = new BlockFloorPipe("floor_pipe", References.CREAATIVE_IRWIP_TAB);
    public static BlockFloorCable floorCable = new BlockFloorCable("floor_cable", References.CREAATIVE_IRWIP_TAB);
    public static BlockFloorLamp floorLamp = new BlockFloorLamp("floor_lamp", References.CREAATIVE_IRWIP_TAB);

    public static BlockAlarm alarm = new BlockAlarm("alarm", References.CREATIVE_IR_TAB);
    public static BlockRecordPlayer recordPlayer = new BlockRecordPlayer("record_player", References.CREATIVE_IR_TAB);

    public static BlockCatWalk catWalk = new BlockCatWalk("catwalk", References.CREATIVE_IR_TAB);
    public static BlockCatWalk catWalkSteel = new BlockCatWalk("catwalk_steel", References.CREATIVE_IR_TAB);
    public static BlockHandRail handRail = new BlockHandRail("handrail", References.CREATIVE_IR_TAB);
    public static BlockHandRail handRailSteel = new BlockHandRail("handrail_steel", References.CREATIVE_IR_TAB);
    public static BlockCatwalkStair catwalkStair = new BlockCatwalkStair("catwalk_stair", References.CREATIVE_IR_TAB);
    public static BlockCatwalkStair catwalkStairSteel = new BlockCatwalkStair("catwalk_stair_steel", References.CREATIVE_IR_TAB);
    public static BlockPillar pillar = new BlockPillar("catwalk_pillar", References.CREATIVE_IR_TAB);
    public static BlockPillar steel_pillar = new BlockPillar("catwalk_steel_pillar", References.CREATIVE_IR_TAB);
    public static BlockColumn column = new BlockColumn("catwalk_column", References.CREATIVE_IR_TAB);
    public static BlockColumn columSteel = new BlockColumn("catwalk_column_steel", References.CREATIVE_IR_TAB);
    public static BlockCatwalkLadder iladder = new BlockCatwalkLadder("catwalk_ladder", References.CREATIVE_IR_TAB);
    public static BlockCatwalkLadder sladder = new BlockCatwalkLadder("catwalk_ladder_steel", References.CREATIVE_IR_TAB);
    public static BlockRoof roof = new BlockRoof("roof", References.CREATIVE_IR_TAB);
    public static BlockGutter gutter = new BlockGutter("gutter", References.CREATIVE_IR_TAB);
    public static BlockLight light = new BlockLight("light", References.CREATIVE_IR_TAB);
    public static BlockFluorescent fluorescent = new BlockFluorescent("fluorescent", References.CREATIVE_IR_TAB);
    public static BlockDummy dummy = new BlockDummy("dummy", References.CREATIVE_IR_TAB);
    public static BlockCatwalkGate catwalkGate = new BlockCatwalkGate("catwalk_gate", References.CREATIVE_IR_TAB);
    public static BlockCatwalkHatch hatch = new BlockCatwalkHatch("catwalk_hatch", References.CREATIVE_IR_TAB);
    public static BlockWindow window = new BlockWindow("window", References.CREATIVE_IR_TAB);
    public static BlockPlatform platform = new BlockPlatform("platform", References.CREATIVE_IR_TAB);
    public static BlockBrace brace = new BlockBrace("brace", References.CREATIVE_IR_TAB);
    public static BlockBrace braceSteel = new BlockBrace("brace_steel", References.CREATIVE_IR_TAB);
    public static BlockScaffold scaffold = new BlockScaffold("scaffold", References.CREATIVE_IR_TAB);
    public static BlockFrame frame = new BlockFrame("frame", References.CREATIVE_IR_TAB);
    public static BlockBarrel barrel = new BlockBarrel("barrel", References.CREATIVE_IR_TAB);
    public static BlockGauge gauge = new BlockGauge("fluid_gauge", References.CREATIVE_IR_TAB);

    public static BlockElectricFence efence = new BlockElectricFence("electric_fence", References.CREATIVE_IR_TAB);
    public static BlockElectricBigFenceColumn bigFenceColumn = new BlockElectricBigFenceColumn("fence_big_column", References.CREATIVE_IR_TAB);
    public static BlockElectricBigFenceWire bigFenceWire = new BlockElectricBigFenceWire("fence_big_wire", References.CREATIVE_IR_TAB);
    public static BlockElectricBigFenceCorner bigFenceCorner = new BlockElectricBigFenceCorner("fence_big_corner", References.CREATIVE_IR_TAB);
    public static BlockElectricGate egate = new BlockElectricGate("electric_gate", References.CREATIVE_IR_TAB);

    public static BlockSolarPanel spanel = new BlockSolarPanel("solar_panel", References.CREAATIVE_IRWIP_TAB);
    public static BlockElectricPump electricPump = new BlockElectricPump("electric_pump", References.CREAATIVE_IRWIP_TAB);
    public static BlockBatteryBank batteryBank = new BlockBatteryBank("battery_bank", References.CREAATIVE_IRWIP_TAB);

    public static BlockSensorRain sensorRain = new BlockSensorRain("sensor_rain", References.CREATIVE_IR_TAB);
    public static BlockSignalIndicator signalIndicator = new BlockSignalIndicator("signal_indicator", References.CREATIVE_IR_TAB);
    public static BlockTrafficLight trafficLight = new BlockTrafficLight("traffic_light", References.CREATIVE_IR_TAB);
    public static BlockFuseBox fuseBox = new BlockFuseBox("fuse_box", References.CREATIVE_IR_TAB);
    public static BlockFuseBoxConduitExtension fuseBoxConduitExtension = new BlockFuseBoxConduitExtension("conduit_extension", References.CREATIVE_IR_TAB);
    public static BlockFuseBoxConnector fuseBoxConnector = new BlockFuseBoxConnector("conduit_connector", References.CREATIVE_IR_TAB);
    public static BlockFlameDetector flameDetector = new BlockFlameDetector("flame_detector", References.CREATIVE_IR_TAB);
    public static BlockEntityDetector entityDetector = new BlockEntityDetector("entity_detector", References.CREATIVE_IR_TAB);
    public static BlockButtonRed buttonRed = new BlockButtonRed("button_red", References.CREATIVE_IR_TAB);

    public static BlockNormalRail normalRail = new BlockNormalRail("normal_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockCrossingRail crossingRail = new BlockCrossingRail("crossing_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockDetectorRail detectorRail = new BlockDetectorRail("detector_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockBoosterRail boosterRail = new BlockBoosterRail("booster_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockBufferStopRail bufferStopRail = new BlockBufferStopRail("buffer_stop_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockLoaderRail loaderRail = new BlockLoaderRail("rail_loader", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockCargoLoader cargoLoader = new BlockCargoLoader("cargo_loader", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockFluidLoader fluidLoader = new BlockFluidLoader("fluid_loader", References.CREATIVE_IRLOCOMOTIVE_TAB);

    public static BlockValvePipeLarge valveLarge = new BlockValvePipeLarge("valve_pipe_large", References.CREATIVE_IR_TAB);

    public static BlockSignBase signHV = new BlockSignBase("sign_hv", References.CREATIVE_IR_TAB);
    public static BlockSignBase signRA = new BlockSignBase("sign_ra", References.CREATIVE_IR_TAB);
    public static BlockSignBase signC = new BlockSignBase("sign_c", References.CREATIVE_IR_TAB);

    public static final BlockFluid steamBlock = new BlockFluid("steam", FluidInit.STEAM, References.CREAATIVE_IRWIP_TAB);
    public static BlockSteamBoilerElectric steamBoilerElectric = new BlockSteamBoilerElectric("steam_boiler_electric", References.CREAATIVE_IRWIP_TAB);

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
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
                barrel,
                gauge,
                gutter,
                blockChimney,
                scaffold,
                efence,
                bigFenceColumn,
                bigFenceCorner,
                bigFenceWire,
                egate,
                signC,
                signHV,
                signRA,
                fluorescent,
                light,
                //Floor
                blockIndFloor,
                floorCable,
                floorLamp,
                floorPipe,
                //Pipes
                energyCable,
                fluidPipe,
                valveLarge,
                //Energy
                spanel,
                batteryBank,
                electricPump,
                //Machines
                steamBoilerElectric,
                //Railroad
                normalRail,
                boosterRail,
                crossingRail,
                detectorRail,
                loaderRail,
                bufferStopRail,
                cargoLoader,
                fluidLoader,
                //dummys
                dummy,
                //Fluids
                steamBlock
        );

        GameRegistry.registerTileEntity(valveLarge.getTileEntityClass(), valveLarge.getRegistryName().toString());
        GameRegistry.registerTileEntity(TileEntityEnergyCable.class, energyCable.getRegistryName().toString());
        GameRegistry.registerTileEntity(alarm.getTileEntityClass(), alarm.getRegistryName().toString());
        GameRegistry.registerTileEntity(gutter.getTileEntityClass(), gutter.getRegistryName().toString());
        GameRegistry.registerTileEntity(spanel.getTileEntityClass(), spanel.getRegistryName().toString());
        GameRegistry.registerTileEntity(firstAidKit.getTileEntityClass(), firstAidKit.getRegistryName().toString());
        GameRegistry.registerTileEntity(recordPlayer.getTileEntityClass(), recordPlayer.getRegistryName().toString());
        GameRegistry.registerTileEntity(sensorRain.getTileEntityClass(), sensorRain.getRegistryName().toString());
        GameRegistry.registerTileEntity(cargoLoader.getTileEntityClass(), cargoLoader.getRegistryName().toString());
        GameRegistry.registerTileEntity(fluidLoader.getTileEntityClass(), fluidLoader.getRegistryName().toString());
        GameRegistry.registerTileEntity(loaderRail.getTileEntityClass(), loaderRail.getRegistryName().toString());
        GameRegistry.registerTileEntity(signalIndicator.getTileEntityClass(), signalIndicator.getRegistryName().toString());
        GameRegistry.registerTileEntity(trafficLight.getTileEntityClass(), trafficLight.getRegistryName().toString());
        GameRegistry.registerTileEntity(fuseBox.getTileEntityClass(), fuseBox.getRegistryName().toString());
        GameRegistry.registerTileEntity(fuseBoxConnector.getTileEntityClass(), fuseBoxConnector.getRegistryName().toString());
        GameRegistry.registerTileEntity(flameDetector.getTileEntityClass(), flameDetector.getRegistryName().toString());
        GameRegistry.registerTileEntity(locker.getTileEntityClass(), locker.getRegistryName().toString());
        GameRegistry.registerTileEntity(entityDetector.getTileEntityClass(), entityDetector.getRegistryName().toString());
        GameRegistry.registerTileEntity(barrel.getTileEntityClass(), barrel.getRegistryName().toString());
        GameRegistry.registerTileEntity(gauge.getTileEntityClass(), gauge.getRegistryName().toString());
        GameRegistry.registerTileEntity(electricPump.getTileEntityClass(), electricPump.getRegistryName().toString());
        GameRegistry.registerTileEntity(batteryBank.getTileEntityClass(), batteryBank.getRegistryName().toString());
        GameRegistry.registerTileEntity(fluidPipe.getTileEntityClass(), fluidPipe.getRegistryName().toString());
        GameRegistry.registerTileEntity(steamBoilerElectric.getTileEntityClass(), steamBoilerElectric.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                blockHazard.createItemBlock(),
                aisleHazard.createItemBlock(),
                cautionHazard.createItemBlock(),
                defectiveHazard.createItemBlock(),
                fireHazard.createItemBlock(),
                radiationHazard.createItemBlock(),
                safetyHazard.createItemBlock(),
                concrete.createItemBlock(),
                platform.createItemBlock(),
                roof.createItemBlock(),
                frame.createItemBlock(),
                hatch.createItemBlock(),
                window.createItemBlock(),
                catWalk.createItemBlock(),
                handRail.createItemBlock(),
                catwalkStair.createItemBlock(),
                iladder.createItemBlock(),
                catwalkGate.createItemBlock(),
                pillar.createItemBlock(),
                column.createItemBlock(),
                brace.createItemBlock(),
                catWalkSteel.createItemBlock(),
                handRailSteel.createItemBlock(),
                catwalkStairSteel.createItemBlock(),
                sladder.createItemBlock(),
                steel_pillar.createItemBlock(),
                columSteel.createItemBlock(),
                braceSteel.createItemBlock(),
                alarm.createItemBlock(),
                entityDetector.createItemBlock(),
                flameDetector.createItemBlock(),
                sensorRain.createItemBlock(),
                fuseBox.createItemBlock(),
                fuseBoxConduitExtension.createItemBlock(),
                fuseBoxConnector.createItemBlock(),
                signalIndicator.createItemBlock(),
                trafficLight.createItemBlock(),
                firstAidKit.createItemBlock(),
                recordPlayer.createItemBlock(),
                locker.createItemBlock(),
                gutter.createItemBlock(),
                blockChimney.createItemBlock(),
                scaffold.createItemBlock(),
                efence.createItemBlock(),
                bigFenceColumn.createItemBlock(),
                bigFenceCorner.createItemBlock(),
                bigFenceWire.createItemBlock(),
                egate.createItemBlock(),
                signHV.createItemBlock(),
                fluorescent.createItemBlock(),
                light.createItemBlock(),
                blockIndFloor.createItemBlock(),
                energyCable.createItemBlock(),
                fluidPipe.createItemBlock(),
                valveLarge.createItemBlock(),
                spanel.createItemBlock(),
                normalRail.createItemBlock(),
                boosterRail.createItemBlock(),
                crossingRail.createItemBlock(),
                detectorRail.createItemBlock(),
                loaderRail.createItemBlock(),
                bufferStopRail.createItemBlock(),
                cargoLoader.createItemBlock(),
                fluidLoader.createItemBlock(),
                gauge.createItemBlock(),
                buttonRed.createItemBlock(),
                batteryBank.createItemBlock(),
                electricPump.createItemBlock(),
                steamBoilerElectric.createItemBlock(),
                steamBlock.createItemBlock()
        );
    }

    public static void registerItemModels() {
        blockHazard.registerItemModel(Item.getItemFromBlock(blockHazard));
        blockIndFloor.registerItemModel(Item.getItemFromBlock(blockIndFloor));
        blockChimney.registerItemModel(Item.getItemFromBlock(blockChimney));
        normalRail.registerItemModel(Item.getItemFromBlock(normalRail));
        crossingRail.registerItemModel(Item.getItemFromBlock(crossingRail));
        detectorRail.registerItemModel(Item.getItemFromBlock(detectorRail));
        boosterRail.registerItemModel(Item.getItemFromBlock(boosterRail));
        valveLarge.registerItemModel(Item.getItemFromBlock(valveLarge));
        alarm.registerItemModel(Item.getItemFromBlock(alarm));
        fluidPipe.registerItemModel(Item.getItemFromBlock(fluidPipe));
        energyCable.registerItemModel(Item.getItemFromBlock(energyCable));
        catWalk.registerItemModel(Item.getItemFromBlock(catWalk));
        catwalkStair.registerItemModel(Item.getItemFromBlock(catwalkStair));
        pillar.registerItemModel(Item.getItemFromBlock(pillar));
        column.registerItemModel(Item.getItemFromBlock(column));
        iladder.registerItemModel(Item.getItemFromBlock(iladder));
        roof.registerItemModel(Item.getItemFromBlock(roof));
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
        egate.registerItemModel(Item.getItemFromBlock(egate));
        spanel.registerItemModel(Item.getItemFromBlock(spanel));
        firstAidKit.registerItemModel(Item.getItemFromBlock(firstAidKit));
        bufferStopRail.registerItemModel(Item.getItemFromBlock(bufferStopRail));
        signHV.registerItemModel(Item.getItemFromBlock(signHV));
        recordPlayer.registerItemModel(Item.getItemFromBlock(recordPlayer));
        sensorRain.registerItemModel(Item.getItemFromBlock(sensorRain));
        loaderRail.registerItemModel(Item.getItemFromBlock(loaderRail));
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
        gauge.registerItemModel(Item.getItemFromBlock(gauge));
        buttonRed.registerItemModel(Item.getItemFromBlock(buttonRed));
        batteryBank.registerItemModel(Item.getItemFromBlock(batteryBank));
        electricPump.registerItemModel(Item.getItemFromBlock(electricPump));
        steamBoilerElectric.registerItemModel(Item.getItemFromBlock(steamBoilerElectric));
        steamBlock.registerItemModel(Item.getItemFromBlock(steamBlock));
    }
}
