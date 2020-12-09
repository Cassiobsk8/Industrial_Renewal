package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockToggleableBase;
import cassiokf.industrialrenewal.tileentity.TileEntityValvePipeLarge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockValvePipeLarge extends BlockToggleableBase<TileEntityValvePipeLarge>
{

    public BlockValvePipeLarge()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Nullable
    @Override
    public TileEntityValvePipeLarge createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityValvePipeLarge();
    }
}