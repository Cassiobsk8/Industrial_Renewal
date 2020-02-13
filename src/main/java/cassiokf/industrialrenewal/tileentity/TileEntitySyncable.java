package cassiokf.industrialrenewal.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileEntitySyncable extends TileEntity
{
    public TileEntitySyncable(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public void Sync()
    {
        if (!world.isRemote)
        {
            final BlockState state = getBlockState();
            world.notifyBlockUpdate(this.pos, state, state, 2);
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
