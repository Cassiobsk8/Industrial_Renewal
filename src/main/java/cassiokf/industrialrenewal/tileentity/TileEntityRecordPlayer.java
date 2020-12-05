package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityRecordPlayer extends TileEntitySync
{
    public final CustomItemStackHandler inventory = new CustomItemStackHandler(4)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            if (!world.isRemote)
            {
                TileEntityRecordPlayer.this.sync();
            }
        }
    };
    private boolean playAll = false;
    private boolean playing = false;

    private boolean played0 = false;
    private boolean played1 = false;
    private boolean played2 = false;
    private boolean played3 = false;

    public TileEntityRecordPlayer(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public boolean hasDiskInSlot(int slot)
    {
        //this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        return !this.inventory.getStackInSlot(slot).isEmpty();
    }

    /*
    @Override
    public void update() {
        if (playAll) {
            if (canPlay() && playDisk(0, true)) {
                playDisk(0);
            } else if (canPlay() && playDisk(1, true)) {
                playDisk(1);
            } else if (canPlay() && playDisk(2, true)) {
                playDisk(2);
            } else if (canPlay() && playDisk(3, true)) {
                playDisk(3);
            } else {
                ItemRecord item = (ItemRecord) this.inventory.getStackInSlot(0).getItem();

            }
        }
    }*/

    public boolean playDisk(int slot)
    {
        return playDisk(slot, false);
    }

    public void stop()
    {
        setAllToFalse();
        playAll = false;
        this.world.playEvent(1010, this.pos, 0);
    }

    private boolean playDisk(int slot, boolean simulate)
    {
        ItemStack diskStack = this.inventory.getStackInSlot(slot);
        if (!diskStack.isEmpty())
        {
            if (!simulate)
            {
                MusicDiscItem diskItem = (MusicDiscItem) diskStack.getItem();
                //SoundEvent sound = diskItem.getSound();
                world.playEvent(1010, pos, Item.getIdFromItem(diskItem));
                setPlaying(slot);
            }
            return true;
        }
        return false;
    }

    private void setPlaying(int slot)
    {
        setAllToFalse();
        playing = true;
        switch (slot)
        {
            case 0:
                played0 = true;
                return;
            case 1:
                played1 = true;
                return;
            case 2:
                played2 = true;
                return;
            case 3:
                played3 = true;
        }
    }

    private void setAllToFalse()
    {
        playing = false;
        played0 = false;
        played1 = false;
        played2 = false;
        played3 = false;
    }

    private boolean canPlay()
    {
        return !playing && !played0 && !played1 && !played2 && !played3;
    }

    public void playAllDisk()
    {
        playAll = true;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.put("rpinventory", this.inventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        this.inventory.deserializeNBT(compound.getCompound("rpinventory"));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? LazyOptional.of(() -> inventory).cast() : super.getCapability(capability, facing);
    }
}
