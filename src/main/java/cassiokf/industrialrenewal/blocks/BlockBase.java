package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockBase extends Block
{

    protected String name;

    public BlockBase(Material material, String name, CreativeTabs tab)
    {
        super(material);

        this.name = name;

        setRegistryName(References.MODID, name);
        setTranslationKey(References.MODID + "." + name);
        setCreativeTab(tab);
        setHardness(5f);
        setResistance(12f);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.hasTileEntity(state))
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TEBase) ((TEBase) te).onBlockBreak();
        }
        super.breakBlock(worldIn, pos, state);
    }

    public void registerItemModel(Item itemBlock)
    {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock()
    {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }
}