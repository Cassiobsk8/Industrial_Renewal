package cassiokf.industrialrenewal.tileentity.recordplayer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityRecordPlayer extends TileEntity implements ICapabilityProvider {

    public ItemStackHandler inventory;
    private boolean playAll = false;
    private boolean playing = false;

    private boolean played0 = false;
    private boolean played1 = false;
    private boolean played2 = false;
    private boolean played3 = false;

    public TileEntityRecordPlayer() {
        this.inventory = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                updateDiscsRender();
                markDirty();
            }
        };
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    private void updateDiscsRender() {
        IBlockState state = this.world.getBlockState(this.pos);

        boolean disc0 = !this.inventory.getStackInSlot(3).isEmpty();
        boolean disc1 = !this.inventory.getStackInSlot(2).isEmpty();
        boolean disc2 = !this.inventory.getStackInSlot(1).isEmpty();
        boolean disc3 = !this.inventory.getStackInSlot(0).isEmpty();

        IBlockState newState = state.withProperty(BlockRecordPlayer.DISK0, disc0)
                .withProperty(BlockRecordPlayer.DISK1, disc1)
                .withProperty(BlockRecordPlayer.DISK2, disc2)
                .withProperty(BlockRecordPlayer.DISK3, disc3);

        if (newState != state) {
            this.world.setBlockState(this.pos, newState, 3);
        }
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

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("rpinventory", this.inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    public boolean playDisk(int slot) {
        return playDisk(slot, false);
    }

    public void stop() {
        setAllToFalse();
        playAll = false;
        this.world.playEvent(1010, this.pos, 0);
    }

    @SideOnly(Side.CLIENT)
    private boolean playDisk(int slot, boolean simulate) {
        ItemStack diskStack = this.inventory.getStackInSlot(slot);
        if (!diskStack.isEmpty()) {
            if (!simulate) {
                ItemRecord diskItem = (ItemRecord) diskStack.getItem();
                //SoundEvent sound = diskItem.getSound();
                this.world.playEvent(1010, this.pos, Item.getIdFromItem(diskItem));
                setPlaying(slot);
            }
            return true;
        }
        return false;
    }

    private void setPlaying(int slot) {
        setAllToFalse();
        playing = true;
        switch (slot) {
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

    private void setAllToFalse() {
        playing = false;
        played0 = false;
        played1 = false;
        played2 = false;
        played3 = false;
    }

    private boolean canPlay() {
        return !playing && !played0 && !played1 && !played2 && !played3;
    }

    public void playAllDisk() {
        playAll = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.inventory.deserializeNBT(compound.getCompoundTag("rpinventory"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
    }
}
