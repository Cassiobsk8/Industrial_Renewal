package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockSaveContent;
import cassiokf.industrialrenewal.tileentity.TileEntityPortableGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPortableGenerator extends BlockSaveContent
{

    public BlockPortableGenerator(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    @Override
    public void doAdditionalFunction(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
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
    public Item getItemToDrop()
    {
        return Item.getItemFromBlock(this);
    }

    @Nullable
    @Override
    public TileEntityPortableGenerator createTileEntity(World world, IBlockState state)
    {
        return new TileEntityPortableGenerator();
    }
}
