package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.blocks.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public abstract class BlockAbstractNotNormalCube extends BlockBase
{
    public BlockAbstractNotNormalCube(Properties properties)
    {
        super(properties.notSolid());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}