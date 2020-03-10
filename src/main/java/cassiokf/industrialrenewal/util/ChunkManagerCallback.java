package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityChunkLoader;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.PlayerOrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.*;
import java.util.Map.Entry;

public class ChunkManagerCallback implements PlayerOrderedLoadingCallback
{
    public static boolean isTicketValid(IBlockAccess blockAccess, Ticket ticket)
    {
        NBTTagCompound modData = ticket.getModData();
        if (!modData.hasKey("blockPosition"))
        {
            return false;
        }
        BlockPos pos = NBTUtil.getPosFromTag(modData.getCompoundTag("blockPosition"));
        TileEntity te = blockAccess.getTileEntity(pos);
        return te instanceof TileEntityChunkLoader && ((TileEntityChunkLoader) te).isMaster();
    }

    public static void activateTicket(World world, Ticket ticket)
    {
        if (!isTicketValid(world, ticket)) return;

        NBTTagCompound modData = ticket.getModData();
        BlockPos pos = NBTUtil.getPosFromTag(modData.getCompoundTag("blockPosition"));
        TileEntity te = world.getTileEntity(pos);

        if (!(te instanceof TileEntityChunkLoader) || !((TileEntityChunkLoader) te).isMaster())
        {
            return;
        }

        TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) te;

        int size = modData.getInteger("size");

        final ChunkPos chunk = new ChunkPos(pos);

        int minX = chunk.x - (int) (size / 2.0f);
        int maxX = chunk.x + (int) ((size - 1) / 2.0f);
        int minZ = chunk.z - (int) (size / 2.0f);
        int maxZ = chunk.z + (int) ((size - 1) / 2.0f);

        for (int z = minZ; z <= maxZ; ++z)
        {
            for (int x = minX; x <= maxX; ++x)
            {
                final ChunkPos ticketChunk = new ChunkPos(x, z);

                ForgeChunkManager.forceChunk(ticket, ticketChunk);
            }
        }

        tileEntity.setTicket(ticket);

        String playerName = ticket.getPlayerName();

        final EntityPlayerMP player = getOnlinePlayerByName(world.getMinecraftServer(), playerName);
        if (player != null)
        {
            tileEntity.addTrackedPlayer(player);
        }
    }

    public static EntityPlayerMP getOnlinePlayerByName(MinecraftServer server, String playerName)
    {
        EntityPlayerMP locatedPlayer = null;
        if (server == null || server.getPlayerList() == null || playerName == null)
        {
            return null;
        }
        final PlayerList playerList = server.getPlayerList();

        for (final EntityPlayerMP entityPlayerMP : playerList.getPlayers())
        {
            if (playerName.equals(entityPlayerMP.getName()))
            {
                locatedPlayer = entityPlayerMP;
                break;
            }
        }
        return locatedPlayer;
    }

    public static List<TileEntityChunkLoader> findNearbyChunkLoaders(World world, BlockPos pos)
    {
        final ChunkPos chunkPos = new ChunkPos(pos);
        final Set<Long> visitedChunks = Sets.newHashSet();
        final List<TileEntityChunkLoader> locatedChunkLoaders = Lists.newArrayList();
        final LinkedList<Chunk> chunksToVisit = Lists.newLinkedList();

        chunksToVisit.push(world.getChunk(chunkPos.x, chunkPos.z));

        while (!chunksToVisit.isEmpty())
        {
            final Chunk chunk = chunksToVisit.pop();
            final long l = ChunkPos.asLong(chunk.x, chunk.z);

            if (!chunk.isPopulated())
            {
                visitedChunks.add(l);
                continue;
            }

            if (visitedChunks.contains(l)) continue;

            //Find the size of all nearby chunk loaders.
            Map<BlockPos, TileEntity> tileEntityMap = chunk.getTileEntityMap();
            int chunkLoaderRadius = 0;
            for (final TileEntity tileEntity : Lists.newArrayList(tileEntityMap.values()))
            {
                if (tileEntity instanceof TileEntityChunkLoader && ((TileEntityChunkLoader) tileEntity).isMaster())
                {
                    final TileEntityChunkLoader te = (TileEntityChunkLoader) tileEntity;
                    final int size = te.getLoaderRadius();
                    if (size > chunkLoaderRadius) chunkLoaderRadius = size;
                    locatedChunkLoaders.add(te);
                }
            }

            visitedChunks.add(l);

            //Now add all nearby chunks that have not yet been visited that we could potentially reach
            //based on config setting sizes.
            final int checkRadius = (int) (chunkLoaderRadius + IRConfig.MainConfig.Main.chunkLoaderWidth / 2.0f);

            for (int z = -checkRadius; z <= checkRadius; ++z)
            {
                for (int x = -checkRadius; x <= checkRadius; ++x)
                {
                    if (x == 0 && z == 0) continue;

                    final long neighbourId = ChunkPos.asLong(chunk.x + x, chunk.z + z);
                    if (visitedChunks.contains(neighbourId)) continue;

                    tileEntityMap = chunk.getTileEntityMap();

                    int otherLoaderRadius = 0;
                    for (final TileEntity tileEntity : Lists.newArrayList(tileEntityMap.values()))
                    {
                        if (tileEntity instanceof TileEntityChunkLoader && ((TileEntityChunkLoader) tileEntity).isMaster())
                        {
                            final TileEntityChunkLoader te = (TileEntityChunkLoader) tileEntity;
                            final int size = te.getLoaderRadius();
                            if (size > otherLoaderRadius) otherLoaderRadius = size;
                        }
                    }

                    final int blea = Math.max(Math.abs(x), Math.abs(z));
                    final int checkSize = (int) (Math.floor(otherLoaderRadius / 2.0f) + 1 + Math.floor(chunkLoaderRadius / 2.0f) + 1);
                    if (checkSize > blea)
                    {
                        chunksToVisit.push(world.getChunk(chunk.x + x, chunk.z + z));
                    }
                }
            }
        }

        locatedChunkLoaders.removeIf(blockPos -> blockPos.getPos().equals(pos));

        return locatedChunkLoaders;
    }

    public static Iterable<TileEntityChunkLoader> getChainedGadgets(TileEntityChunkLoader chunkLoader)
    {
        final Set<Long> gadgetPos = Sets.newHashSet();
        final List<TileEntityChunkLoader> foundLoader = Lists.newArrayList();

        final Stack<TileEntityChunkLoader> LoadersToSearch = new Stack<>();
        LoadersToSearch.push(chunkLoader);

        final World world = chunkLoader.getWorld();
        while (!LoadersToSearch.empty())
        {
            final TileEntityChunkLoader gadget = LoadersToSearch.pop();
            final Long pos = gadget.getPos().toLong();
            if (gadgetPos.contains(pos)) continue;

            foundLoader.add(gadget);
            gadgetPos.add(pos);
            for (final BlockPos blockPos : gadget.getNearbyGadgets())
            {
                if (gadgetPos.contains(blockPos.toLong())) continue;

                final TileEntity tileEntity = world.getTileEntity(blockPos);
                if (tileEntity instanceof TileEntityChunkLoader && ((TileEntityChunkLoader) tileEntity).isMaster())
                {
                    LoadersToSearch.push((TileEntityChunkLoader) tileEntity);
                } else
                {
                    gadget.removeNearbyGadget(blockPos);
                }
            }
        }
        return foundLoader;
    }

    @Override
    public ListMultimap<String, ForgeChunkManager.Ticket> playerTicketsLoaded(ListMultimap<String, ForgeChunkManager.Ticket> tickets, World world)
    {
        final ListMultimap<String, Ticket> returnedTickets = ArrayListMultimap.create();

        if (IRConfig.MainConfig.Main.emergencyMode)
        {//Disable Chunk Load
            return returnedTickets;
        }

        for (final Entry<String, Collection<Ticket>> playerTicketMap : tickets.asMap().entrySet())
        {
            final String player = playerTicketMap.getKey();
            for (final Ticket ticket : playerTicketMap.getValue())
            {
                if (isTicketValid(world, ticket))
                {
                    returnedTickets.put(player, ticket);
                }
            }
        }

        return returnedTickets;
    }

    @Override
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world)
    {
        for (final Ticket ticket : tickets)
        {
            final NBTTagCompound modData = ticket.getModData();
            if (!modData.hasKey("blockPosition"))
            {
                continue;
            }

            activateTicket(world, ticket);
        }
    }
}
