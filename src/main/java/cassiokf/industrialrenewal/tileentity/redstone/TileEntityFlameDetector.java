package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockFlameDetector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityFlameDetector extends TileEntity {

    private EnumFacing blockFacing = EnumFacing.DOWN;

    public boolean passRedstone() {
        IBlockState state = this.world.getBlockState(this.pos);
        EnumFacing inFace = state.getValue(BlockFlameDetector.FACING);
        IBlockState neightbor = world.getBlockState(pos.offset(inFace));
        Block block = neightbor.getBlock();
        return block instanceof BlockFire;
    }

    public void setBlockFacing(EnumFacing facing) {
        blockFacing = facing;
        markDirty();
    }

    public EnumFacing getBlockFacing() {
        return blockFacing;
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        tag.setInteger("baseFacing", blockFacing.getIndex());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        blockFacing = EnumFacing.byIndex(tag.getInteger("baseFacing"));
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
