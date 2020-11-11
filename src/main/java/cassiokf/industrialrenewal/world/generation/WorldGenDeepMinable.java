package cassiokf.industrialrenewal.world.generation;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

public class WorldGenDeepMinable extends WorldGenMinable
{
    private final IBlockState oreBlock;
    private final Predicate<IBlockState> predicate;

    public WorldGenDeepMinable(IBlockState state, int blockCount)
    {
        this(state, blockCount, new BedrockPredicate());
    }

    public WorldGenDeepMinable(IBlockState state, int blockCount, Predicate<IBlockState> p_i45631_3_)
    {
        super(state, blockCount, p_i45631_3_);
        this.oreBlock = state;
        this.predicate = p_i45631_3_;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        IBlockState state = worldIn.getBlockState(position);
        if (state.getBlock().isReplaceableOreGen(state, worldIn, position, this.predicate))
        {
            worldIn.setBlockState(position, this.oreBlock, 2);
        }
        return true;
    }

    static class BedrockPredicate implements Predicate<IBlockState>
    {
        protected BedrockPredicate()
        {
        }

        public boolean apply(IBlockState state)
        {
            return true;
        }
    }
}
