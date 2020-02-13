package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityEnergySwitch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockEnergySwitch extends BlockToggleableBase<TileEntityEnergySwitch>
{
    public BlockEnergySwitch(Block.Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntityEnergySwitch createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityEnergySwitch();
    }
}