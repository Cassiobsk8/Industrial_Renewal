package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockSaveContent;
import cassiokf.industrialrenewal.handlers.FluidGenerator;
import cassiokf.industrialrenewal.tileentity.TileEntityPortableGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPortableGenerator extends BlockSaveContent
{
    public BlockPortableGenerator(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    @Override
    public void doAdditionalFunction(World worldIn, BlockPos pos,  BlockState state, PlayerEntity playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing == state.getValue(FACING).getOpposite())
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityPortableGenerator)
            {
                ((TileEntityPortableGenerator) te).changeGenerator();
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + ": "
                + (FluidGenerator.energyPerTick)
                + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        boolean result = super.rotateBlock(world, pos, axis);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityPortableGenerator) ((TileEntityPortableGenerator) te).getBlockFacing(true);
        return result;
    }

    @Override
    public Item getItemToDrop()
    {
        return Item.getItemFromBlock(this);
    }

    @Nullable
    @Override
    public TileEntityPortableGenerator createTileEntity(World world,  BlockState state)
    {
        return new TileEntityPortableGenerator();
    }
}
