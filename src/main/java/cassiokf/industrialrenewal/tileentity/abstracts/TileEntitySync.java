package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.util.interfaces.ISync;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntitySync extends TEBase implements ISync
{

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public BlockPos getThisPosition()
    {
        return getPos();
    }

    @Override
    public World getThisWorld()
    {
        return getWorld();
    }

    @Override
    public void sync()
    {
        if (!world.isRemote)
        {
            final IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 2);
            this.markDirty();
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, -1, getUpdateTag());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.getUpdateTag();
        writeToNBT(tag);
        return tag;
    }
}
