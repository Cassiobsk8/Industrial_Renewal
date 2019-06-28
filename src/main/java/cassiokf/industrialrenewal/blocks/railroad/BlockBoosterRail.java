package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBoosterRail extends BlockRailPowered {

    protected String name;

    public BlockBoosterRail(String name, CreativeTabs tab) {

        this.name = name;
        setRegistryName(References.MODID, name);
        setUnlocalizedName(References.MODID + "." + name);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
        setCreativeTab(tab);
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
        double d15 = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
        /**TODO Precisa desacelerar **/
        if (!world.getBlockState(pos).getValue(BlockRailPowered.POWERED)) {
            return;
        }

        if (d15 > 0.01D) {
            cart.motionX += cart.motionX / d15 * 0.06D;
            cart.motionZ += cart.motionZ / d15 * 0.06D;
        }
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