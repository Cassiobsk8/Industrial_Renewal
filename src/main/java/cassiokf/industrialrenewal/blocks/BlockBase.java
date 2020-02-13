package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;


public class BlockBase extends Block
{
    public BlockBase(Block.Properties properties)
    {
        super(properties.hardnessAndResistance(2f, 10f));
    }
}