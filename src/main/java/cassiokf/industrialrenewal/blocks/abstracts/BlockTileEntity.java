package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public abstract class BlockTileEntity<TE extends TileEntity> extends BlockAbstractNotNormalCube
{

    public BlockTileEntity(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(BlockState state, IBlockReader world);
}