package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractNotNormalCube;
import cassiokf.industrialrenewal.tileentity.TileEntityInfinityGenerator;
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

public class BlockInfinityGenerator extends BlockAbstractNotNormalCube
{
    public BlockInfinityGenerator()
    {
        super(Block.Properties.create(Material.IRON).lightValue(14));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.produces")
                + " "
                + Integer.MAX_VALUE
                + " FE/t"));
        tooltip.add(new StringTextComponent("This is not a scam, believe me"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityInfinityGenerator createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityInfinityGenerator();
    }
}
