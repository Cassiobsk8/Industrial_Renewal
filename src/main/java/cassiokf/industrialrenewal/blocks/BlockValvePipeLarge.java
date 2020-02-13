package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityValvePipeLarge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockValvePipeLarge extends BlockToggleableBase<TileEntityValvePipeLarge>
{

    public BlockValvePipeLarge(Block.Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntityValvePipeLarge createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityValvePipeLarge();
    }
}