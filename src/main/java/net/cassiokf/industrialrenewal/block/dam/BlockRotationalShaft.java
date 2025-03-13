package net.cassiokf.industrialrenewal.block.dam;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractNotFullCube;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockRotationalShaft extends BlockAbstractNotFullCube {
    public BlockRotationalShaft() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion().strength(1f));
    }
}
