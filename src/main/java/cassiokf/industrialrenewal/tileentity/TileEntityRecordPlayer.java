package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileEntityRecordPlayer extends TileEntitySyncable implements ICapabilityProvider
{
    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
    private boolean playAll = false;
    private boolean playing = false;

    private boolean played0 = false;
    private boolean played1 = false;
    private boolean played2 = false;
    private boolean played3 = false;

    public TileEntityRecordPlayer()
    {
        super(TileEntityRegister.RECORD_PLAYER);
    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(4)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                if (!TileEntityRecordPlayer.this.world.isRemote)
                {
                    TileEntityRecordPlayer.this.Sync();
                }
            }
        };
    }

    public boolean hasDiskInSlot(int slot)
    {
        world.notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
        return !((IItemHandler) inventory).getStackInSlot(slot).isEmpty();
    }

    /*
    @Override
    public void tick() {
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

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
    }

    public boolean playDisk(int slot)
    {
        return playDisk(slot, false);
    }

    public void stop()
    {
        setAllToFalse();
        playAll = false;
        world.playEvent(1010, this.pos, 0);
    }

    private boolean playDisk(int slot, boolean simulate)
    {
        ItemStack diskStack = ((IItemHandler) inventory).getStackInSlot(slot);
        if (!diskStack.isEmpty())
        {
            if (!simulate)
            {
                MusicDiscItem diskItem = (MusicDiscItem) diskStack.getItem();
                //SoundEvent sound = diskItem.getSound();
                world.playEvent(1010, this.pos, Item.getIdFromItem(diskItem));
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
                return;
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
    public void read(CompoundNBT compound)
    {
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        inventory.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.write(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? inventory.cast() : super.getCapability(capability, facing);
    }
}
