package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.ChunkManagerCallback;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TileEntityChunkLoader extends TEBase implements ITickable
{
    private static final int ACTIVE_STATE_CHANGED = 1;

    @Nonnull
    private final List<Ticket> tickets = Lists.newArrayList();

    @Nonnull
    private final List<WeakReference<EntityPlayer>> trackedPlayers = new LinkedList<>();

    private long expireTime = -1;

    private boolean isActive;
    private boolean master;

    public void setTicket(@Nonnull Ticket ticket)
    {
        final String playerName = ticket.getPlayerName();
        final Iterator<Ticket> iterator = tickets.iterator();
        while (iterator.hasNext())
        {
            final Ticket next = iterator.next();
            if (next.getPlayerName().equals(playerName))
            {
                ForgeChunkManager.releaseTicket(next);
                iterator.remove();
            }
        }
        tickets.add(ticket);

        world.addBlockEvent(pos, getBlockType(), ACTIVE_STATE_CHANGED, 1);
        expireTime = -1;
    }

    public void addTrackedPlayer(@Nullable EntityPlayer player)
    {
        for (final WeakReference<EntityPlayer> trackedPlayerReference : trackedPlayers)
        {
            final EntityPlayer trackedPlayer = trackedPlayerReference.get();
            if (trackedPlayer != null && player != null && player.getName() == trackedPlayer.getName())
            {
                return;
            }
        }
        trackedPlayers.add(new WeakReference<>(player));
    }

    @Override
    public void update()
    {
        if (IRConfig.MainConfig.Main.emergencyMode || !master) return;
        if (world.isRemote) return;
        if (tickets.isEmpty()) return;

        if (!IRConfig.MainConfig.Main.needPlayerToActivateChunkLoading)
        {
            return;
        }

        final long totalWorldTime = world.getTotalWorldTime();
        if ((totalWorldTime & 31) != 31)
        {
            return;
        }

        final boolean trackedPlayersOnline = checkOnlinePlayers();

        final boolean gadgetIsValid = checkPlayersLoggedOn(trackedPlayersOnline);

        if (!gadgetIsValid && expireTime == -1)
        {
            final int timeout = IRConfig.MainConfig.Main.hoursBeforeChunkLoadingDeactivation * 60 * 60 * 20;
            expireTime = totalWorldTime + timeout;
        }

        if (expireTime != -1 && totalWorldTime >= expireTime)
        {
            disable();
        }
    }

    private boolean checkPlayersLoggedOn(boolean trackedPlayersOnline)
    {
        if (trackedPlayersOnline)
        {
            return true;
        }

        boolean gadgetIsValid = false;

        for (final Ticket ticket : tickets)
        {
            final EntityPlayer playerEntityByName = ChunkManagerCallback.getOnlinePlayerByName(world.getMinecraftServer(), ticket.getPlayerName());

            if (playerEntityByName != null)
            {

                expireTime = -1;

                trackedPlayers.add(new WeakReference<>(playerEntityByName));

                world.addBlockEvent(pos, getBlockType(), ACTIVE_STATE_CHANGED, 1);

                gadgetIsValid = true;
            }
        }
        return gadgetIsValid;

    }

    private boolean checkOnlinePlayers()
    {
        boolean trackedPlayersOnline = false;

        final Iterator<WeakReference<EntityPlayer>> trackedPlayerIterator = trackedPlayers.iterator();
        while (trackedPlayerIterator.hasNext())
        {
            final WeakReference<EntityPlayer> playerWeakReference = trackedPlayerIterator.next();
            EntityPlayer player = playerWeakReference.get();

            if (player != null)
            {
                player = ChunkManagerCallback.getOnlinePlayerByName(world.getMinecraftServer(), player.getName());

                if (player == null)
                {
                    playerWeakReference.clear();
                    trackedPlayerIterator.remove();
                } else
                {
                    trackedPlayersOnline = true;
                }
            }
        }

        return trackedPlayersOnline;
    }

    private void disable()
    {
        for (final Ticket ticket : tickets)
        {
            ForgeChunkManager.releaseTicket(ticket);
        }
        tickets.clear();

        world.addBlockEvent(pos, getBlockType(), ACTIVE_STATE_CHANGED, 0);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        expireTime = compound.hasKey("expireTime") ? compound.getLong("expireTime") : -1;
        this.master = compound.getBoolean("master");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("master", world.getBlockState(pos).getValue(BlockChunkLoader.MASTER));
        compound.setLong("expireTime", expireTime);
        return compound;
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == ACTIVE_STATE_CHANGED)
        {
            isActive = type == 1;
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        final NBTTagCompound updateTag = super.getUpdateTag();
        updateTag.setBoolean("isActive", isActive);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        isActive = tag.getBoolean("isActive");
        super.handleUpdateTag(tag);
    }

    public boolean isExpired()
    {
        return tickets.isEmpty();
    }

    public boolean isMaster()
    {
        return master;
    }

    public void setMaster(boolean master)
    {
        this.master = master;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public boolean hasTicket(EntityPlayer player)
    {
        if (player == null) return false;
        for (final Ticket ticket : tickets)
        {
            if (ticket.getPlayerName().equals(player.getName())) return true;
        }
        return false;
    }

    public void expireAllTickets()
    {
        for (final Ticket ticket : tickets)
        {
            ForgeChunkManager.releaseTicket(ticket);
        }
        tickets.clear();
        trackedPlayers.clear();
    }
}
