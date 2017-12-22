package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;


public class BlockChimney extends Block {

    protected String name;

    public BlockChimney(String name) {
        super(Material.IRON);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(0.8f);
        setCreativeTab(IndustrialRenewal.creativeTab);

    }

    @Override
    public int tickRate(World world) {
        return 10;
    }

    /**
     * todo Arrumar para não ser mais random
     **/
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        for (int la = 0; la < 20; ++la) {
            double d0 = (double) ((float) i) + (double) (random.nextFloat()) * 1.000000001490116D;
            double d1 = ((double) ((float) j) + (double) (random.nextFloat()) * 1.000000001490116D) + 0.5D;
            double d2 = (double) ((float) k) + (double) (random.nextFloat()) * 1.000000001490116D;
            double d3 = 0.2199999988079071D;
            double d4 = 0.27000001072883606D;
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
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