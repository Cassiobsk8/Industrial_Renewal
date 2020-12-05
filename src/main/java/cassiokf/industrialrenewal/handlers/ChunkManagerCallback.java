package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityChunkLoader;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.Ticket;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ChunkManagerCallback implements PlayerOrderedLoadingCallback
{
    public static boolean isTicketValid(World blockAccess, Ticket ticket)
    {
        CompoundNBT modData = ticket.getModData();
        if (!modData.contains("blockPosition"))
        {
            return false;
        }
        BlockPos pos = NBTUtil.readBlockPos(modData.getCompound("blockPosition"));
        TileEntity te = blockAccess.getTileEntity(pos);
        return te instanceof TileEntityChunkLoader && ((TileEntityChunkLoader) te).isMaster();
    }

    public static void activateTicket(World world, Ticket ticket)
    {
        if (!isTicketValid(world, ticket)) return;

        CompoundNBT modData = ticket.getModData();
        BlockPos pos = NBTUtil.readBlockPos(modData.getCompound("blockPosition"));
        TileEntity te = world.getTileEntity(pos);

        if (!(te instanceof TileEntityChunkLoader) || !((TileEntityChunkLoader) te).isMaster())
        {
            return;
        }

        TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) te;

        int size = modData.getInt("size");

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

        final ServerPlayerEntity player = getOnlinePlayerByName(world.getServer(), playerName);
        if (player != null)
        {
            tileEntity.addTrackedPlayer(player);
        }
    }

    public static ServerPlayerEntity getOnlinePlayerByName(MinecraftServer server, String playerName)
    {
        ServerPlayerEntity locatedPlayer = null;
        if (server == null || server.getPlayerList() == null || playerName == null)
        {
            return null;
        }
        final PlayerList playerList = server.getPlayerList();

        for (final ServerPlayerEntity PlayerEntityMP : playerList.getPlayers())
        {
            if (playerName.equals(PlayerEntityMP.getName()))
            {
                locatedPlayer = PlayerEntityMP;
                break;
            }
        }
        return locatedPlayer;
    }

    @Override
    public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world)
    {
        final ListMultimap<String, Ticket> returnedTickets = ArrayListMultimap.create();

        if (IRConfig.Main.emergencyMode.get())
        {//Disable Chunk Load
            return returnedTickets;
        }

        for (final Map.Entry<String, Collection<Ticket>> playerTicketMap : tickets.asMap().entrySet())
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
    public void ticketsLoaded(List<Ticket> tickets, World world)
    {
        for (final Ticket ticket : tickets)
        {
            final CompoundNBT modData = ticket.getModData();
            if (!modData.contains("blockPosition"))
            {
                continue;
            }
            activateTicket(world, ticket);
        }
    }
}
