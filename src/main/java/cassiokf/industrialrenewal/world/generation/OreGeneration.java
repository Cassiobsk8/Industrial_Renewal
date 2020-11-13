package cassiokf.industrialrenewal.world.generation;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class OreGeneration implements IWorldGenerator
{
    public static Map<ChunkPos, ItemStack> CHUNKS_VEIN = new ConcurrentHashMap<>();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() == 0)
        {
            //generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private static void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        //if (world.isRemote || world.provider.getDimension() != 0)
        //    return;
        //if (IRConfig.MainConfig.Generation.regenerateDeepVein) OreGeneration.tryRetrogenDeepVein(world);
        //generateDeepVein(random, chunkX, chunkZ, world, false);
    }

    public static ItemStack getChunkVein(World world, BlockPos pos)
    {
        ChunkPos cPos = world.getChunk(pos).getPos();
        ItemStack stack = CHUNKS_VEIN.get(cPos);
        return stack != null ? stack : ItemStack.EMPTY;
    }

    public static ItemStack generateNewVein(World world)
    {
        if (!IRConfig.MainConfig.Generation.spawnDeepVein) return ItemStack.EMPTY;
        int chance = IRConfig.MainConfig.Generation.deepVeinSpawnRate;
        if (world.rand.nextInt(100) < chance)
        {
            int min = IRConfig.MainConfig.Generation.deepVeinMinOre;
            int oreQuantity = world.rand.nextInt(IRConfig.MainConfig.Generation.deepVeinMaxOre - min) + min;
            Item item = ModItems.DEEP_VEIN_ORES.get(world.rand.nextInt(ModItems.DEEP_VEIN_ORES.size() - 1));
            Block block = Block.getBlockFromItem(item);
            ItemStack stack = new ItemStack(block.getItemDropped(block.getDefaultState(), world.rand, 0));
            stack.setCount(oreQuantity);
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
