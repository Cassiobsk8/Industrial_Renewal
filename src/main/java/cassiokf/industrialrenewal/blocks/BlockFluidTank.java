package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.tileentity.TEFluidTank;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class BlockFluidTank extends BlockMultiTankBase<TEFluidTank>
{
    public BlockFluidTank(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format("info.industrialrenewal.fluid_tank.info"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean instanceOf(Block block)
    {
        return block instanceof BlockFluidTank;
    }

    @Override
    public TEFluidTank createTileEntity(World world, IBlockState state)
    {
        return new TEFluidTank();
    }
}
