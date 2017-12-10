package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.counter.BlockCounter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static BlockOre oreCopper = new BlockOre("ore_copper", "oreCopper");

    public static BlockOre blockHazard = new BlockOre("block_hazard",null);

    public static BlockNormalRail normalRail = new BlockNormalRail("normal_rail");
    public static BlockCrossingRail crossingRail = new BlockCrossingRail("crossing_rail");

    public static BlockValvePipeLarge valveLarge = new BlockValvePipeLarge("valve_pipe_large");
    public static BlockValvePipeOpenLarge valveLargeOpen = new BlockValvePipeOpenLarge("valve_pipe_large_open");

    public static BlockCounter bcounter = new BlockCounter();

    //public static CartBase cartContainer = new CartBase();

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                oreCopper,
                blockHazard,
                normalRail,
                bcounter,
                //cartContainer,
                crossingRail,
                valveLarge,
                valveLargeOpen
        );
        GameRegistry.registerTileEntity(bcounter.getTileEntityClass(), bcounter.getRegistryName().toString());
        //GameRegistry.registerTileEntity(cartContainer.getTileEntityClass(), cartContainer.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                oreCopper.createItemBlock(),
                blockHazard.createItemBlock(),
                normalRail.createItemBlock(),
                bcounter.createItemBlock(),
               // cartContainer.createItemBlock(),
                crossingRail.createItemBlock(),
                valveLarge.createItemBlock(),
                valveLargeOpen.createItemBlock()
        );
    }

    public static void registerItemModels() {
        oreCopper.registerItemModel(Item.getItemFromBlock(oreCopper));
        blockHazard.registerItemModel(Item.getItemFromBlock(blockHazard));
        normalRail.registerItemModel(Item.getItemFromBlock(normalRail));
        bcounter.registerItemModel(Item.getItemFromBlock(bcounter));
        //cartContainer.registerItemModel(Item.getItemFromBlock(cartContainer));
        crossingRail.registerItemModel(Item.getItemFromBlock(crossingRail));
        valveLarge.registerItemModel(Item.getItemFromBlock(valveLarge));
        valveLargeOpen.registerItemModel(Item.getItemFromBlock(valveLargeOpen));
    }
}
