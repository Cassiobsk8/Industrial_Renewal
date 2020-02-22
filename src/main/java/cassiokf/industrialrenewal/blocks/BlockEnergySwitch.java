package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityEnergySwitch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockEnergySwitch extends BlockToggleableBase<TileEntityEnergySwitch>
{
    public BlockEnergySwitch()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Nullable
    @Override
    public TileEntityEnergySwitch createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityEnergySwitch();
    }
}