package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.util.interfaces.ISync;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntitySync extends TEBase implements ISync
{
    public TileEntitySync(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
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
            final BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 2);
            this.markDirty();
        }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(pos, -1, getUpdateTag());
    }


    @Override
    public void handleUpdateTag(CompoundNBT tag)
    {
        this.read(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT tag = super.getUpdateTag();
        write(tag);
        return tag;
    }
}
