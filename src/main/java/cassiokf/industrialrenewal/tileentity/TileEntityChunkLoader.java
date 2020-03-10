package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.ChunkManagerCallback;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TileEntityChunkLoader extends TileEntity implements ITickable
{
    private static final int ACTIVE_STATE_CHANGED = 1;

    @Nonnull
    private final List<Ticket> tickets = Lists.newArrayList();

    @Nonnull
    private final List<WeakReference<EntityPlayer>> trackedPlayers = new LinkedList<>();

    private long expireTime = -1;

    private boolean isActive;
    private boolean scheduleNeighbourCheck = true;
    private List<BlockPos> nearbyGadgets = Lists.newArrayList();
    private boolean checked = false;
    private boolean master;

    public void setTicket(@Nonnull Ticket ticket)
    {
        //Release any existing tickets for this player.
        final String playerName = ticket.getPlayerName();
        final Iterator<Ticket> iterator = tickets.iterator();
        while (iterator.hasNext())
        {
            final Ticket next = iterator.next();
            if (next.getPlayerName() == playerName)
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
        //Only process on the server
        if (world.isRemote) return;
        //If there isn't any tickets, the device is already disabled and no work to be done
        //until it is reactivated next.
        if (tickets.isEmpty()) return;

        //Only process this chunk loader every 32 ticks
        // Should probably increase this to every minute or so.
        final long totalWorldTime = world.getTotalWorldTime();
        if ((totalWorldTime & 31) != 31)
        {
            return;
        }
        if (scheduleNeighbourCheck)
        {
            checkNeighbours();
        }

        final boolean trackedPlayersOnline = checkOnlinePlayers();

        final boolean gadgetIsValid = checkPlayersLoggedOn(trackedPlayersOnline);

        //At this point, no players have been found,
        //If there isn't an expiry time, it's time to set one.
        if (!gadgetIsValid && expireTime == -1)
        {
            final int timeout = IRConfig.MainConfig.Main.hoursBeforeChunkLoadingDeactivation * 60 * 60 * 20;
            //final int timeout = 10 * 20;
            expireTime = totalWorldTime + timeout;
        }

        //If the expire time has expired and we've reached this far
        //It's time to kill the ticket.
        if (expireTime != -1 && totalWorldTime >= expireTime)
        {
            disable();
        }
    }

    private void checkNeighbours()
    {
        scheduleNeighbourCheck = false;
        final List<TileEntityChunkLoader> nearbyLoaders = ChunkManagerCallback.findNearbyChunkLoaders(world, pos);

        final List<BlockPos> loadersToRemove = Lists.newArrayList();

        final List<BlockPos> nearbyLoadersLocations = Lists.newArrayList(this.nearbyGadgets);

        for (final BlockPos nearbyLoader : nearbyLoadersLocations)
        {
            if (nearbyLoaders.stream().noneMatch(te -> te.pos.equals(nearbyLoader)))
            {
                loadersToRemove.add(nearbyLoader);
            }
        }

        boolean isDirty = false;
        for (final TileEntityChunkLoader nearbyGadget : nearbyLoaders)
        {
            nearbyGadget.notifyNeighbourAdded(pos);
            if (!nearbyLoadersLocations.contains(nearbyGadget.pos))
            {
                nearbyLoadersLocations.add(nearbyGadget.pos);
                isDirty = true;
            }
        }

        if (!loadersToRemove.isEmpty())
        {
            isDirty = true;
            nearbyLoadersLocations.removeAll(loadersToRemove);
        }

        if (isDirty)
        {
            markDirty();
            this.nearbyGadgets = nearbyLoadersLocations;
        }
    }

    private void notifyNeighbourAdded(BlockPos pos)
    {
        final List<BlockPos> nearbyGadgets = Lists.newArrayList(this.nearbyGadgets);
        if (!nearbyGadgets.contains(pos))
        {
            nearbyGadgets.add(pos);
            markDirty();
            this.nearbyGadgets = nearbyGadgets;
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
            //If we're no longer tracking the ticket owner, but there is a ticket,
            //Check to see if the player has come online.

            final EntityPlayer playerEntityByName = ChunkManagerCallback.getOnlinePlayerByName(world.getMinecraftServer(), ticket.getPlayerName());
            //If they're found
            if (playerEntityByName != null)
            {
                //reset the expiry
                expireTime = -1;
                //start tracking the player
                trackedPlayers.add(new WeakReference<>(playerEntityByName));
                if (!world.isRemote)
                {
                    //Logger.info("Chunk Loader at %s is revived because %s returned", pos, playerEntityByName.getName());
                }
                //Start the animation again
                world.addBlockEvent(pos, getBlockType(), ACTIVE_STATE_CHANGED, 1);
                //Block any further processing
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
            //If we're tracking the ticket owner, check to see if they're still online
            final WeakReference<EntityPlayer> playerWeakReference = trackedPlayerIterator.next();
            EntityPlayer player = playerWeakReference.get();

            if (player != null)
            {
                player = ChunkManagerCallback.getOnlinePlayerByName(world.getMinecraftServer(), player.getName());

                //If we couldn't find them, clear the reference, we're not tracking them any more.
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

        //Disable the animation
        world.addBlockEvent(pos, getBlockType(), ACTIVE_STATE_CHANGED, 0);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        expireTime = compound.hasKey("expireTime") ? compound.getLong("expireTime") : -1;
        final List<BlockPos> nearbyGadgets = Lists.newArrayList();
        if (compound.hasKey("knownNeighbours"))
        {
            final NBTTagList tagList = compound.getTagList("knownNeighbours", 10);
            for (int i = 0; i < tagList.tagCount(); i++)
            {
                nearbyGadgets.add(NBTUtil.getPosFromTag(tagList.getCompoundTagAt(i)));
            }
        }
        this.master = compound.getBoolean("master");
        this.nearbyGadgets = nearbyGadgets;
        scheduleNeighbourCheck = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("master", world.getBlockState(pos).getValue(BlockChunkLoader.MASTER));
        compound.setLong("expireTime", expireTime);
        final List<BlockPos> gadgetsToSave = this.getNearbyGadgets();
        if (gadgetsToSave != null && !gadgetsToSave.isEmpty())
        {
            final NBTTagList tagList = new NBTTagList();
            for (final BlockPos nearbyGadget : gadgetsToSave)
            {
                tagList.appendTag(NBTUtil.createPosTag(nearbyGadget));
            }

            compound.setTag("knownNeighbours", tagList);
        }
        return compound;
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == ACTIVE_STATE_CHANGED)
        {
            isActive = type == 1;
            //Logger.info("Active state of chunk loader at %s is now %b", pos, isActive);

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

    public boolean isActive()
    {
        return isActive;
    }

    public boolean hasTicket(EntityPlayer player)
    {
        if (player == null) return false;
        for (final Ticket ticket : tickets)
        {
            if (ticket.getPlayerName() == player.getName()) return true;
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

    public int getLoaderRadius()
    {
        int size = 0;
        for (final Ticket ticket : tickets)
        {
            size = Math.max(size, ticket.getModData().getInteger("size"));
        }
        return size;
    }

    public List<BlockPos> getNearbyGadgets()
    {
        return ImmutableList.copyOf(nearbyGadgets);
    }

    public void removeNearbyGadget(BlockPos blockPos)
    {

        final List<BlockPos> newList = Lists.newArrayList(nearbyGadgets);
        newList.remove(blockPos);
        nearbyGadgets = newList;
    }
}
