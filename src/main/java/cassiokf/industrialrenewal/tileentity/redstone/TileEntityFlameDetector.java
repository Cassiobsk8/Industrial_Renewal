package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockFlameDetector;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TileEntityFlameDetector extends TileEntitySync implements ITickableTileEntity
{

    private Direction blockFacing = Direction.DOWN;

    private int tick = 0;

    public TileEntityFlameDetector(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public boolean passRedstone()
    {
        BlockState state = world.getBlockState(pos);
        Direction inFace = state.get(BlockFlameDetector.FACING);
        BlockState neightbor = world.getBlockState(pos.offset(inFace));
        Block block = neightbor.getBlock();
        return block instanceof FireBlock;
    }

    @Override
    public void tick()
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
        BlockState state = this.world.getBlockState(this.pos);
        boolean actualValue = state.get(BlockFlameDetector.ACTIVE);
        if (actualValue != value)
        {
            this.world.setBlockState(this.pos, state.with(BlockFlameDetector.ACTIVE, value), 3);
            this.sync();
            world.notifyNeighborsOfStateChange(this.pos.offset(getBlockFacing()), state.getBlock());
        }
    }

    public void setBlockFacing(Direction facing) {
        blockFacing = facing;
        markDirty();
    }

    public Direction getBlockFacing() {
        return blockFacing;
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag) {
        tag.putInt("baseFacing", blockFacing.getIndex());
        return super.write(tag);
    }

    @Override
    public void read(final CompoundNBT tag) {
        super.read(tag);
        blockFacing = Direction.byIndex(tag.getInt("baseFacing"));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }
}
