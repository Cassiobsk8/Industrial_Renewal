package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityDamIntake;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDamIntake extends BlockHorizontalFacing
{
    public BlockDamIntake(String name, CreativeTabs tab)
    {
        super(name, tab, Material.ROCK);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityDamIntake createTileEntity(World world, IBlockState state)
    {
        return new TileEntityDamIntake();
    }
}
