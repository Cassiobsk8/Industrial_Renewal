package cassiokf.industrialrenewal.world.generation;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Iterator;
import java.util.Random;

import static cassiokf.industrialrenewal.References.MODID;

public class OreGeneration implements IWorldGenerator
{
    private static int i = 0;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() == 0)
        {
            generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private static void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.isRemote || world.provider.getDimension() != 0)
            return;
        if (IRConfig.MainConfig.Generation.regenerateDeepVein) OreGeneration.tryRetrogenDeepVein(world);
        generateDeepVein(random, chunkX, chunkZ, world, false);
    }

    public static void tryRetrogenDeepVein(World world)
    {
        i = 0;
        if (world instanceof WorldServer)
        {
            Iterator<Chunk> chunks = ((WorldServer) world).getChunkProvider().loadedChunks.values().stream().iterator();
            chunks.forEachRemaining(t -> OreGeneration.generateDeepVein(world.rand, t.x, t.z, world, true));
        }
        IRConfig.MainConfig.Generation.regenerateDeepVein = false;
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
        world.playerEntities.forEach(t -> Utils.sendChatMessage(t, "ReGeneration Done in " + i + " Chunks"));
    }

    public static void generateDeepVein(Random random, int chunkX, int chunkZ, World world, boolean remove)
    {
        if (!IRConfig.MainConfig.Generation.spawnDeepVein) return;

        BlockPos pos = new BlockPos(chunkX * 16 + 8, 1, chunkZ * 16 + 8);
        if (remove) world.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
        //if (remove) world.setBlockState(new BlockPos(chunkX * 16 + 8, 150, chunkZ * 16 + 8), Blocks.AIR.getDefaultState());
        int chance = IRConfig.MainConfig.Generation.deepVeinSpawnRate;
        if (random.nextInt(100) <= chance)
        {
            WorldGenDeepMinable worldGenMinable = new WorldGenDeepMinable(ModBlocks.deepVein.getDefaultState(), 1);
            worldGenMinable.generate(world, random, pos);
            //worldGenMinable.generate(world, random, new BlockPos(chunkX * 16 + 8, 150, chunkZ * 16 + 8));
            //System.out.println(TextFormatting.BLUE + "Generated at: " + pos);
        }
        if (remove)
        {
            i++;
            System.out.println(TextFormatting.BLUE + "ReGeneration Done in " + chunkX + " " + chunkZ);
        }
    }
}
