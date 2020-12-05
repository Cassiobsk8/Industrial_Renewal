package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.ChunkManagerCallback;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.Ticket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TileEntityChunkLoader extends TEBase implements ITickableTileEntity
{
    private static final int ACTIVE_STATE_CHANGED = 1;

    @Nonnull
    private final List<Ticket> tickets = Lists.newArrayList();

    @Nonnull
    private final List<WeakReference<PlayerEntity>> trackedPlayers = new LinkedList<>();

    private long expireTime = -1;

    private boolean isActive;
    private boolean master;

    public TileEntityChunkLoader(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

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

        world.addBlockEvent(pos, getBlockState().getBlock(), ACTIVE_STATE_CHANGED, 1);
        expireTime = -1;
    }

    public void addTrackedPlayer(@Nullable PlayerEntity player)
    {
        for (final WeakReference<PlayerEntity> trackedPlayerReference : trackedPlayers)
        {
            final PlayerEntity trackedPlayer = trackedPlayerReference.get();
            if (trackedPlayer != null && player != null && player.getName() == trackedPlayer.getName())
            {
                return;
            }
        }
        trackedPlayers.add(new WeakReference<>(player));
    }

    @Override
    public void tick()
    {
        if (IRConfig.Main.emergencyMode.get() || !master) return;
        if (world.isRemote) return;
        if (tickets.isEmpty()) return;

        if (!IRConfig.Main.needPlayerToActivateChunkLoading.get())
        {
            return;
        }

        final long totalWorldTime = world.getGameTime();
        if ((totalWorldTime & 31) != 31)
        {
            return;
        }

        final boolean trackedPlayersOnline = checkOnlinePlayers();

        final boolean gadgetIsValid = checkPlayersLoggedOn(trackedPlayersOnline);

        if (!gadgetIsValid && expireTime == -1)
        {
            final int timeout = IRConfig.Main.hoursBeforeChunkLoadingDeactivation.get() * 60 * 60 * 20;
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
            final PlayerEntity playerEntityByName = ChunkManagerCallback.getOnlinePlayerByName(world.getServer(), ticket.getPlayerName());

            if (playerEntityByName != null)
            {

                expireTime = -1;

                trackedPlayers.add(new WeakReference<>(playerEntityByName));

                world.addBlockEvent(pos, getBlockState().getBlock(), ACTIVE_STATE_CHANGED, 1);

                gadgetIsValid = true;
            }
        }
        return gadgetIsValid;

    }

    private boolean checkOnlinePlayers()
    {
        boolean trackedPlayersOnline = false;

        final Iterator<WeakReference<PlayerEntity>> trackedPlayerIterator = trackedPlayers.iterator();
        while (trackedPlayerIterator.hasNext())
        {
            final WeakReference<PlayerEntity> playerWeakReference = trackedPlayerIterator.next();
            PlayerEntity player = playerWeakReference.get();

            if (player != null)
            {
                player = ChunkManagerCallback.getOnlinePlayerByName(world.getServer(), player.getName().getFormattedText());

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
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        expireTime = compound.contains("expireTime") ? compound.getLong("expireTime") : -1;
        this.master = compound.getBoolean("master");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putBoolean("master", getBlockState().get(BlockChunkLoader.MASTER));
        compound.putLong("expireTime", expireTime);
        return compound;
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == ACTIVE_STATE_CHANGED)
        {
            isActive = type == 1;
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
            return true;
        }
        return false;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        final CompoundNBT updateTag = super.getUpdateTag();
        updateTag.putBoolean("isActive", isActive);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag)
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

    public boolean hasTicket(PlayerEntity player)
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
