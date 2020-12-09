package cassiokf.industrialrenewal.blocks.railroad;

import net.minecraft.block.Block;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.material.Material;

public class BlockDetectorRail extends DetectorRailBlock
{
    public BlockDetectorRail()
    {
        super(Block.Properties.create(Material.IRON).notSolid());
    }
}