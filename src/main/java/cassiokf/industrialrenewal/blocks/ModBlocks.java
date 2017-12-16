package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.alarm.BlockAlarm;
import cassiokf.industrialrenewal.tileentity.carts.CartTransportContainer;
import cassiokf.industrialrenewal.tileentity.gates.and.BlockGateAnd;
import cassiokf.industrialrenewal.tileentity.valve.BlockValvePipeLarge;
import cassiokf.industrialrenewal.tileentity.counter.BlockCounter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {


    public static BlockOre oreCopper = new BlockOre("ore_copper", "oreCopper");
    public static BlockGateAnd gateAnd = new BlockGateAnd("gate_and");
    public static BlockOre blockHazard = new BlockOre("block_hazard",null);
    public static BlockAlarm alarm = new BlockAlarm("alarm");

    public static BlockNormalRail normalRail = new BlockNormalRail("normal_rail");
    public static BlockCrossingRail crossingRail = new BlockCrossingRail("crossing_rail");

    public static BlockValvePipeLarge valveLarge = new BlockValvePipeLarge("valve_pipe_large");

    //public static CartTransportContainer cartContainer = new CartTransportContainer();

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                oreCopper,
                blockHazard,
                normalRail,
                //cartContainer,
                crossingRail,
                valveLarge,
                gateAnd,
                alarm
        );
        GameRegistry.registerTileEntity(valveLarge.getTileEntityClass(), valveLarge.getRegistryName().toString());
        GameRegistry.registerTileEntity(gateAnd.getTileEntityClass(), gateAnd.getRegistryName().toString());
        GameRegistry.registerTileEntity(alarm.getTileEntityClass(), alarm.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                oreCopper.createItemBlock(),
                blockHazard.createItemBlock(),
                normalRail.createItemBlock(),
                //cartContainer.createItemBlock(),
                crossingRail.createItemBlock(),
                valveLarge.createItemBlock(),
                gateAnd.createItemBlock(),
                alarm.createItemBlock()
        );
    }

    public static void registerItemModels() {
        oreCopper.registerItemModel(Item.getItemFromBlock(oreCopper));
        blockHazard.registerItemModel(Item.getItemFromBlock(blockHazard));
        normalRail.registerItemModel(Item.getItemFromBlock(normalRail));
        //cartContainer.registerItemModel(Item.getItemFromBlock(cartContainer));
        crossingRail.registerItemModel(Item.getItemFromBlock(crossingRail));
        valveLarge.registerItemModel(Item.getItemFromBlock(valveLarge));
        gateAnd.registerItemModel(Item.getItemFromBlock(gateAnd));
        alarm.registerItemModel(Item.getItemFromBlock(alarm));
    }
}
