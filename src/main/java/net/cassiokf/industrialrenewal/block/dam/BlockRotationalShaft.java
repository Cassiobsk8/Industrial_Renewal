package net.cassiokf.industrialrenewal.block.dam;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractNotFullCube;

public class BlockRotationalShaft extends BlockAbstractNotFullCube {
    public BlockRotationalShaft() {
        super(metalBasicProperties.noOcclusion().strength(1f));
    }
}
