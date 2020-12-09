package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTEHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityTrash;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockTrash extends BlockTEHorizontalFacing<TileEntityTrash>
{
    public BlockTrash()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Nullable
    @Override
    public TileEntityTrash createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTrash();
    }
}
