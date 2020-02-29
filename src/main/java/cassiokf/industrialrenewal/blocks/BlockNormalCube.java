package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockNormalCube extends BlockBase
{
    public BlockNormalCube()
    {
        super(Block.Properties.create(Material.IRON));
    }
}
