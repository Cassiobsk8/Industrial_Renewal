package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractNotNormalCube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockFrame extends BlockAbstractNotNormalCube
{
    public BlockFrame()
    {
        super(Block.Properties.create(Material.IRON).notSolid());
    }
}
