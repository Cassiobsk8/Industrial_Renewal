package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.alarm.BlockAlarm;
import cassiokf.industrialrenewal.tileentity.cable.BlockEnergyCable;
import cassiokf.industrialrenewal.tileentity.cable.TileEntityEnergyCable;
import cassiokf.industrialrenewal.tileentity.filter.BlockFilter;
import cassiokf.industrialrenewal.tileentity.filter.BlockFilterDummy;
import cassiokf.industrialrenewal.tileentity.gates.and.BlockGateAnd;
import cassiokf.industrialrenewal.tileentity.gates.nand.BlockGateNand;
import cassiokf.industrialrenewal.tileentity.gates.not.BlockGateNot;
import cassiokf.industrialrenewal.tileentity.gates.or.BlockGateOr;
import cassiokf.industrialrenewal.tileentity.valve.BlockValvePipeLarge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {


    public static BlockOre oreCopper = new BlockOre("ore_copper", "oreCopper");
    public static BlockGateAnd gateAnd = new BlockGateAnd("gate_and");
    public static BlockGateNand gateNand = new BlockGateNand("gate_nand");
    public static BlockGateNot gateNot = new BlockGateNot("gate_not");
    public static BlockGateOr gateOr = new BlockGateOr("gate_or");
    public static BlockBase blockHazard = new BlockBase(Material.IRON, "block_hazard");
    public static BlockIndustrialFloor blockIndFloor = new BlockIndustrialFloor("industrial_floor");
    public static BlockChimney blockChimney = new BlockChimney("block_chimney");
    //public static RedstoneWire redstoneWire = new RedstoneWire("redstone_wire");
    public static BlockFluidPipe fluidPipe = new BlockFluidPipe("fluid_pipe");
    public static BlockEnergyCable energyCable = new BlockEnergyCable("energy_cable");
    public static BlockFloorPipe floorPipe = new BlockFloorPipe("floor_pipe");
    public static BlockFloorCable floorCable = new BlockFloorCable("floor_cable");
    public static BlockFloorLamp floorLamp = new BlockFloorLamp("floor_lamp");
    public static BlockAlarm alarm = new BlockAlarm("alarm");
    public static BlockFilter filter = new BlockFilter("fluid_filter");
    public static BlockCatWalk catWalk = new BlockCatWalk("catwalk");
    public static BlockCatWalkStair catwalkStair = new BlockCatWalkStair("catwalk_stair");

    public static BlockFilterDummy dummyFilter = new BlockFilterDummy("dummy_filter");

    public static BlockNormalRail normalRail = new BlockNormalRail("normal_rail");
    public static BlockCrossingRail crossingRail = new BlockCrossingRail("crossing_rail");
    public static BlockDetectorRail detectorRail = new BlockDetectorRail("detector_rail");
    public static BlockBoosterRail boosterRail = new BlockBoosterRail("booster_rail");

    public static BlockValvePipeLarge valveLarge = new BlockValvePipeLarge("valve_pipe_large");

    //public static TileEntityCartCargoContainer cargoContainer = new TileEntityCartCargoContainer(null);

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                oreCopper,
                blockHazard,
                blockIndFloor,
                blockChimney,
                normalRail,
                //cargoContainer,
                crossingRail,
                detectorRail,
                boosterRail,
                valveLarge,
                gateAnd,
                gateNand,
                gateNot,
                gateOr,
                alarm,
                filter,
                //redstoneWire,
                fluidPipe,
                energyCable,
                floorPipe,
                floorCable,
                floorLamp,
                dummyFilter,
                catWalk,
                catwalkStair
        );
        GameRegistry.registerTileEntity(valveLarge.getTileEntityClass(), valveLarge.getRegistryName().toString());
        GameRegistry.registerTileEntity(TileEntityEnergyCable.class, energyCable.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateAnd.getTileEntityClass(), gateAnd.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateNand.getTileEntityClass(), gateNand.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateNot.getTileEntityClass(), gateNot.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateOr.getTileEntityClass(), gateOr.getRegistryName().toString());
        GameRegistry.registerTileEntity(alarm.getTileEntityClass(), alarm.getRegistryName().toString());
        GameRegistry.registerTileEntity(filter.getTileEntityClass(), filter.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                oreCopper.createItemBlock(),
                blockHazard.createItemBlock(),
                blockIndFloor.createItemBlock(),
                blockChimney.createItemBlock(),
                normalRail.createItemBlock(),
                //cargoContainer.createItemBlock(),
                crossingRail.createItemBlock(),
                detectorRail.createItemBlock(),
                boosterRail.createItemBlock(),
                valveLarge.createItemBlock(),
                gateAnd.createItemBlock(),
                gateNand.createItemBlock(),
                gateNot.createItemBlock(),
                gateOr.createItemBlock(),
                alarm.createItemBlock(),
                filter.createItemBlock(),
                //redstoneWire.createItemBlock(),
                fluidPipe.createItemBlock(),
                energyCable.createItemBlock(),
                catWalk.createItemBlock(),
                catwalkStair.createItemBlock()
        );
    }

    public static void registerItemModels() {
        oreCopper.registerItemModel(Item.getItemFromBlock(oreCopper));
        blockHazard.registerItemModel(Item.getItemFromBlock(blockHazard));
        blockIndFloor.registerItemModel(Item.getItemFromBlock(blockIndFloor));
        blockChimney.registerItemModel(Item.getItemFromBlock(blockChimney));
        normalRail.registerItemModel(Item.getItemFromBlock(normalRail));
        //cargoContainer.registerItemModel(Item.getItemFromBlock(cargoContainer));
        crossingRail.registerItemModel(Item.getItemFromBlock(crossingRail));
        detectorRail.registerItemModel(Item.getItemFromBlock(detectorRail));
        boosterRail.registerItemModel(Item.getItemFromBlock(boosterRail));
        valveLarge.registerItemModel(Item.getItemFromBlock(valveLarge));
        gateAnd.registerItemModel(Item.getItemFromBlock(gateAnd));
        gateNand.registerItemModel(Item.getItemFromBlock(gateNand));
        gateNot.registerItemModel(Item.getItemFromBlock(gateNot));
        alarm.registerItemModel(Item.getItemFromBlock(alarm));
        filter.registerItemModel(Item.getItemFromBlock(filter));
        gateOr.registerItemModel(Item.getItemFromBlock(gateOr));
        //redstoneWire.registerItemModel(Item.getItemFromBlock(redstoneWire));
        fluidPipe.registerItemModel(Item.getItemFromBlock(fluidPipe));
        energyCable.registerItemModel(Item.getItemFromBlock(energyCable));
        catWalk.registerItemModel(Item.getItemFromBlock(catWalk));
        catwalkStair.registerItemModel(Item.getItemFromBlock(catwalkStair));
    }
}
