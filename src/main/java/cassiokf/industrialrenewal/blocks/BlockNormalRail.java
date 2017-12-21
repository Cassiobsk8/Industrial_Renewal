package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.block.BlockRail;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockNormalRail extends BlockRail {

    protected String name;

    public BlockNormalRail(String name) {

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
        setCreativeTab(IndustrialRenewal.creativeTab);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public void registerItemModel(Item itemBlock) {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }
}