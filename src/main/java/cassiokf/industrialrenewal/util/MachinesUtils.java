package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.tileentity.TEDeepVein;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class MachinesUtils
{
    public static TEDeepVein getDeepVein(World world, BlockPos pos)
    {
        Chunk chunk = world.getChunk(pos);
        int a = chunk.x * 16;
        int b = chunk.z * 16;
        BlockPos actualPosition = new BlockPos(a + 8, 1, b + 8);
        TileEntity te = world.getTileEntity(actualPosition);
        if (te instanceof TEDeepVein)
        {
            return (TEDeepVein) te;
        }
        return null;
    }

    public static List<BlockPos> getBlocksIn3x3x3Centered(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int y = -1; y < 2; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    list.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                }
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x2x3CenteredPlus1OnTop(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int y = -1; y < 2; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    if (y != 1 || (z == 0 && x == 0))
                    {
                        list.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                    }
                }
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x1x3Centered(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int z = -1; z < 2; z++)
        {
            for (int x = -1; x < 2; x++)
            {
                list.add(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x2x2Centered(BlockPos pos, EnumFacing facing)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        boolean isSided = facing == EnumFacing.EAST || facing == EnumFacing.WEST;
        boolean invert = facing == EnumFacing.NORTH || facing == EnumFacing.WEST;
        for (int y = 0; y < 2; y++)
        {
            for (int z = 0; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    int finalX = (isSided ? z : x);
                    int finalZ = (isSided ? x : z);
                    list.add(new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ)));
                }
            }
        }
        return list;
    }
}
