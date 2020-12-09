package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.tileentity.TEFluidTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidTank extends BlockMultiTankBase<TEFluidTank>
{
    public BlockFluidTank()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.fluid_tank.info")));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean instanceOf(Block block)
    {
        return block instanceof BlockFluidTank;
    }

    @Nullable
    @Override
    public TEFluidTank createTileEntity(BlockState state, IBlockReader world)
    {
        return new TEFluidTank();
    }
}
