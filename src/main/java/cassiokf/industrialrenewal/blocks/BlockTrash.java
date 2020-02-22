package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityTrash;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockTrash extends BlockAbstractHorizontalFacing
{
    public BlockTrash()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityTrash createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTrash();
    }
}
