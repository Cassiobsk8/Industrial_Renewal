package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;


public class BlockChimney extends BlockBase
{
    public BlockChimney()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        for (int la = 0; la < 50; ++la)
        {
            double d0 = (double) ((float) i) + (double) (rand.nextFloat()) * 1.000000001490116D;
            double d1 = ((double) ((float) j) + (double) (rand.nextFloat()) * 1.000000001490116D) + 0.5D;
            double d2 = (double) ((float) k) + (double) (rand.nextFloat()) * 1.000000001490116D;
            double d3 = 0.2199999988079071D;
            double d4 = 0.27000001072883606D;
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}