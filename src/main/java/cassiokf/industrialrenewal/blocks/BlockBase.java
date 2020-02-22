package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;


public abstract class BlockBase extends Block
{
    public static final VoxelShape NULL_SHAPE = Block.makeCuboidShape(0, 0, 0, 0, 0, 0);
    protected static final VoxelShape FULL_SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);

    public BlockBase(Block.Properties properties)
    {
        super(properties.hardnessAndResistance(2f, 10f));
    }
}