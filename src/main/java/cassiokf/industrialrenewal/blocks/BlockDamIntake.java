package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityDamIntake;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("To improve the generation, make sure that you have 6 concrete blocks on each side and 10 above with 10 water adjacent to each of these concrete blocks");
        super.addInformation(stack, player, tooltip, advanced);
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
