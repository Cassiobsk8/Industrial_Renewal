package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.gates.BlockGateNot;
import cassiokf.industrialrenewal.tileentity.alarm.BlockAlarm;
import cassiokf.industrialrenewal.tileentity.cable.BlockEnergyCable;
import cassiokf.industrialrenewal.tileentity.cable.TileEntityEnergyCable;
import cassiokf.industrialrenewal.tileentity.firstaidkit.BlockFirstAidKit;
import cassiokf.industrialrenewal.tileentity.gates.and.BlockGateAnd;
import cassiokf.industrialrenewal.tileentity.gates.nand.BlockGateNand;
import cassiokf.industrialrenewal.tileentity.gates.or.BlockGateOr;
import cassiokf.industrialrenewal.tileentity.gutter.BlockGutter;
import cassiokf.industrialrenewal.tileentity.solarpanel.BlockSolarPanel;
import cassiokf.industrialrenewal.tileentity.valve.BlockValvePipeLarge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {


    //public static BlockOre oreCopper = new BlockOre("ore_copper", "oreCopper");
    public static BlockGateAnd gateAnd = new BlockGateAnd("gate_and", References.CREATIVE_IR_TAB);
    public static BlockGateNand gateNand = new BlockGateNand("gate_nand", References.CREATIVE_IR_TAB);
    public static BlockGateNot gateNot = new BlockGateNot("gate_not", References.CREATIVE_IR_TAB);
    public static BlockGateOr gateOr = new BlockGateOr("gate_or", References.CREATIVE_IR_TAB);

    public static BlockBase blockHazard = new BlockBase(Material.IRON, "block_hazard", References.CREATIVE_IR_TAB);

    public static BlockIndustrialFloor blockIndFloor = new BlockIndustrialFloor("industrial_floor", References.CREATIVE_IR_TAB);
    public static BlockChimney blockChimney = new BlockChimney("block_chimney", References.CREATIVE_IR_TAB);
    public static BlockFirstAidKit firstAidKit = new BlockFirstAidKit("firstaid_kit", References.CREATIVE_IR_TAB);

    public static BlockFluidPipe fluidPipe = new BlockFluidPipe("fluid_pipe", References.CREATIVE_IR_TAB);
    public static BlockEnergyCable energyCable = new BlockEnergyCable("energy_cable", References.CREATIVE_IR_TAB);
    public static BlockFloorPipe floorPipe = new BlockFloorPipe("floor_pipe", References.CREATIVE_IR_TAB);
    public static BlockFloorCable floorCable = new BlockFloorCable("floor_cable", References.CREATIVE_IR_TAB);
    public static BlockFloorLamp floorLamp = new BlockFloorLamp("floor_lamp", References.CREATIVE_IR_TAB);

    public static BlockAlarm alarm = new BlockAlarm("alarm", References.CREATIVE_IR_TAB);

    public static BlockCatWalk catWalk = new BlockCatWalk("catwalk", References.CREATIVE_IR_TAB);
    public static BlockCatwalkStair catwalkStair = new BlockCatwalkStair("catwalk_stair", References.CREATIVE_IR_TAB);
    public static BlockPillar pillar = new BlockPillar("catwalk_pillar", References.CREATIVE_IR_TAB);
    public static BlockColumn column = new BlockColumn("catwalk_column", References.CREATIVE_IR_TAB);
    public static BlockCatwalkLadder iladder = new BlockCatwalkLadder("catwalk_ladder", References.CREATIVE_IR_TAB);
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
    public static BlockScaffold scaffold = new BlockScaffold("scaffold", References.CREATIVE_IR_TAB);
    public static BlockFrame frame = new BlockFrame("frame", References.CREATIVE_IR_TAB);
    public static BlockElectricFence efence = new BlockElectricFence("electric_fence", References.CREATIVE_IR_TAB);
    public static BlockElectricGate egate = new BlockElectricGate("electric_gate", References.CREATIVE_IR_TAB);
    public static BlockSolarPanel spanel = new BlockSolarPanel("solar_panel", References.CREATIVE_IR_TAB);

    public static BlockRedstoneWire redstonew = new BlockRedstoneWire("redstone_wire", References.CREATIVE_IR_TAB);

    public static BlockNormalRail normalRail = new BlockNormalRail("normal_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockCrossingRail crossingRail = new BlockCrossingRail("crossing_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockDetectorRail detectorRail = new BlockDetectorRail("detector_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
    public static BlockBoosterRail boosterRail = new BlockBoosterRail("booster_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);

    public static BlockValvePipeLarge valveLarge = new BlockValvePipeLarge("valve_pipe_large", References.CREATIVE_IR_TAB);

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                //oreCopper,
                blockHazard,
                blockIndFloor,
                blockChimney,
                normalRail,
                crossingRail,
                detectorRail,
                boosterRail,
                valveLarge,
                gateAnd,
                gateNand,
                gateNot,
                gateOr,
                alarm,
                fluidPipe,
                energyCable,
                floorPipe,
                floorCable,
                floorLamp,
                catWalk,
                catwalkStair,
                pillar,
                column,
                iladder,
                roof,
                gutter,
                light,
                fluorescent,
                dummy,
                catwalkGate,
                hatch,
                window,
                platform,
                brace,
                scaffold,
                frame,
                efence,
                egate,
                spanel,
                redstonew,
                firstAidKit
        );
        GameRegistry.registerTileEntity(valveLarge.getTileEntityClass(), valveLarge.getRegistryName().toString());
        GameRegistry.registerTileEntity(TileEntityEnergyCable.class, energyCable.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateAnd.getTileEntityClass(), gateAnd.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateNand.getTileEntityClass(), gateNand.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateOr.getTileEntityClass(), gateOr.getRegistryName().toString());
        GameRegistry.registerTileEntity(alarm.getTileEntityClass(), alarm.getRegistryName().toString());
        GameRegistry.registerTileEntity(gutter.getTileEntityClass(), gutter.getRegistryName().toString());
        GameRegistry.registerTileEntity(spanel.getTileEntityClass(), spanel.getRegistryName().toString());
        GameRegistry.registerTileEntity(firstAidKit.getTileEntityClass(), firstAidKit.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                //oreCopper.createItemBlock(),
                blockHazard.createItemBlock(),
                blockIndFloor.createItemBlock(),
                blockChimney.createItemBlock(),
                normalRail.createItemBlock(),
                crossingRail.createItemBlock(),
                detectorRail.createItemBlock(),
                boosterRail.createItemBlock(),
                valveLarge.createItemBlock(),
                gateAnd.createItemBlock(),
                gateNand.createItemBlock(),
                gateNot.createItemBlock(),
                gateOr.createItemBlock(),
                alarm.createItemBlock(),
                fluidPipe.createItemBlock(),
                energyCable.createItemBlock(),
                catWalk.createItemBlock(),
                catwalkStair.createItemBlock(),
                pillar.createItemBlock(),
                column.createItemBlock(),
                iladder.createItemBlock(),
                roof.createItemBlock(),
                gutter.createItemBlock(),
                light.createItemBlock(),
                fluorescent.createItemBlock(),
                catwalkGate.createItemBlock(),
                hatch.createItemBlock(),
                window.createItemBlock(),
                platform.createItemBlock(),
                brace.createItemBlock(),
                scaffold.createItemBlock(),
                frame.createItemBlock(),
                efence.createItemBlock(),
                egate.createItemBlock(),
                spanel.createItemBlock(),
                redstonew.createItemBlock(),
                firstAidKit.createItemBlock()
        );
    }

    public static void registerItemModels() {
        //oreCopper.registerItemModel(Item.getItemFromBlock(oreCopper));
        blockHazard.registerItemModel(Item.getItemFromBlock(blockHazard));
        blockIndFloor.registerItemModel(Item.getItemFromBlock(blockIndFloor));
        blockChimney.registerItemModel(Item.getItemFromBlock(blockChimney));
        normalRail.registerItemModel(Item.getItemFromBlock(normalRail));
        crossingRail.registerItemModel(Item.getItemFromBlock(crossingRail));
        detectorRail.registerItemModel(Item.getItemFromBlock(detectorRail));
        boosterRail.registerItemModel(Item.getItemFromBlock(boosterRail));
        valveLarge.registerItemModel(Item.getItemFromBlock(valveLarge));
        gateAnd.registerItemModel(Item.getItemFromBlock(gateAnd));
        gateNand.registerItemModel(Item.getItemFromBlock(gateNand));
        gateNot.registerItemModel(Item.getItemFromBlock(gateNot));
        alarm.registerItemModel(Item.getItemFromBlock(alarm));
        gateOr.registerItemModel(Item.getItemFromBlock(gateOr));
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
        egate.registerItemModel(Item.getItemFromBlock(egate));
        spanel.registerItemModel(Item.getItemFromBlock(spanel));
        redstonew.registerItemModel(Item.getItemFromBlock(redstonew));
        firstAidKit.registerItemModel(Item.getItemFromBlock(firstAidKit));
    }
}
