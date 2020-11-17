package cassiokf.industrialrenewal.blocks.machines;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.tileentity.machines.TEIndustrialBatteryBank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;

import java.util.List;

public class BlockIndustrialBatteryBank extends BlockMultiTankBase<TEIndustrialBatteryBank>
{
    public BlockIndustrialBatteryBank(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format("info.industrialrenewal.fluid_tank.info"));
        tooltip.add(I18n.format("info.industrialrenewal.capacity")
                + ": "
                + Utils.formatEnergyString(TEIndustrialBatteryBank.capacity));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean instanceOf(Block block)
    {
        return block instanceof BlockIndustrialBatteryBank;
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public TEIndustrialBatteryBank createTileEntity(World world, IBlockState state)
    {
        return new TEIndustrialBatteryBank();
    }
}
