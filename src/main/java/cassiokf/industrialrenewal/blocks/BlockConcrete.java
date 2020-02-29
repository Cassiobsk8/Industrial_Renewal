package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.TileEntityConcrete;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockConcrete extends BlockTileEntity<TileEntityConcrete>
{
    public BlockConcrete()
    {
        super(Block.Properties.create(Material.ROCK));
    }

    @Nullable
    @Override
    public TileEntityConcrete createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityConcrete();
    }
}
