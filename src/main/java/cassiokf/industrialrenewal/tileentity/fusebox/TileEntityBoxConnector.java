package cassiokf.industrialrenewal.tileentity.fusebox;

import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBoxConnector extends TileEntity {

    private boolean active;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public void onLoad() {
        passRedstone();
    }

    public int passRedstone() {
        int value;
        if (getActive()) {
            value = getSignalWithAllTheMath(getPowerIn());
        } else {
            value = 0;
        }
        setState(value);
        return value;
    }

    public int getSignalWithAllTheMath(int input) {
        ItemStackHandler inv = getInventory();
        if (inv != null) {
            for (int i = 0; i < inv.getSlots(); i++) {
                Item item = inv.getStackInSlot(i).getItem();
                if (item == ModItems.cartridge_minus) {
                    input--;
                }
                if (item == ModItems.cartridge_plus) {
                    input++;
                }
                if (item == ModItems.cartridge_half) {
                    input = input / 2;
                }
                if (item == ModItems.cartridge_double) {
                    input = input * 2;
                }
            }
        }
        if (input > 15) {
            input = 15;
        }
        if (input < 0) {
            input = 0;
        }
        return input;
    }

    private ItemStackHandler getInventory() {
        TileEntityFuseBox te = getTE();
        if (te != null) {
            return te.getInv();
        }
        return null;
    }

    private void setState(int value) {
        IBlockState state = this.world.getBlockState(this.pos).getActualState(this.world, this.pos);
        if (state.getValue(BlockFuseBoxConnector.POWER) != value) {
            this.world.setBlockState(this.pos, state.withProperty(BlockFuseBoxConnector.POWER, value));
        }
    }

    public int getPowerIn() {
        IBlockState state = this.world.getBlockState(this.pos);
        EnumFacing inFace = state.getValue(BlockFuseBoxConnector.FACING).rotateYCCW();
        IBlockState neighbor = world.getBlockState(pos.offset(inFace));
        Block block = neighbor.getBlock();
        if (block == Blocks.REDSTONE_BLOCK || block == Blocks.REDSTONE_TORCH) {
            return 15;
        } else {
            return block == Blocks.REDSTONE_WIRE ? neighbor.getValue(BlockRedstoneWire.POWER) : this.world.getStrongPower(this.pos.offset(inFace), inFace);
        }
    }

    private void notifyChange() {
        this.world.notifyNeighborsOfStateChange(this.pos, this.blockType, true);
        markDirty();
    }

    public boolean getActive() {
        TileEntityFuseBox te = getTE();
        if (te != null) {
            boolean nActive = te.getActive();
            if (nActive != active) {
                setActive(nActive);
            }
        }
        return active;
    }

    public void setActive(boolean value) {
        active = value;
        notifyChange();
    }

    private TileEntityFuseBox getTE() {
        int i = 1;
        while (i < 64) {
            Block block = this.world.getBlockState(this.pos.up(i)).getBlock();
            if (block instanceof BlockFuseBox) {
                return (TileEntityFuseBox) this.world.getTileEntity(this.pos.up(i));
            }
            i++;
        }
        return null;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("active", active);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        active = compound.getBoolean("active");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }
}
