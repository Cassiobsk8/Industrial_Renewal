package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityConcrete;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockConcrete extends BlockTileEntity<TileEntityConcrete>
{
    public BlockConcrete(Block.Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntityConcrete createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityConcrete();
    }
}
