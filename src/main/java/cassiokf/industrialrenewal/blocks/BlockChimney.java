package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;


public class BlockChimney extends BlockBase {

    public BlockChimney(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
        setLightOpacity(255);
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
        for (int la = 0; la < 50; ++la)
        {
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
}