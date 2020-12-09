package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockFlameDetector;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityFlameDetector extends TileEntitySync implements ITickable
{

    private EnumFacing blockFacing = EnumFacing.DOWN;

    private int tick = 0;

    public boolean passRedstone()
    {
        IBlockState state = world.getBlockState(pos);
        EnumFacing inFace = state.getValue(BlockFlameDetector.FACING);
        IBlockState neightbor = world.getBlockState(pos.offset(inFace));
        Block block = neightbor.getBlock();
        return block instanceof BlockFire;
    }

    @Override
    public void update()
    {
        if (!this.world.isRemote && ((tick % 10) == 0))
        {
            tick = 0;
            changeState(passRedstone());
        }
        tick++;
    }

    private void changeState(boolean value)
    {
        IBlockState state = this.world.getBlockState(this.pos).getActualState(this.world, this.pos);
        boolean actualValue = state.getValue(BlockFlameDetector.ACTIVE);
        if (actualValue != value)
        {
            this.world.setBlockState(this.pos, state.withProperty(BlockFlameDetector.ACTIVE, value), 3);
            this.sync();
            world.notifyNeighborsOfStateChange(this.pos.offset(getBlockFacing()), state.getBlock(), true);
        }
    }

    public EnumFacing getBlockFacing()
    {
        return blockFacing;
    }

    public void setBlockFacing(EnumFacing facing)
    {
        blockFacing = facing;
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tag.setInteger("baseFacing", blockFacing.getIndex());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        blockFacing = EnumFacing.byIndex(tag.getInteger("baseFacing"));
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }
}
