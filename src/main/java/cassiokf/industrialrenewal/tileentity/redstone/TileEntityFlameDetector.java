package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockFlameDetector;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySyncable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

import static cassiokf.industrialrenewal.init.TileRegistration.FLAMEDETECTOR_TILE;

public class TileEntityFlameDetector extends TileEntitySyncable implements ITickableTileEntity
{

    private Direction blockFacing = Direction.DOWN;

    private int tick = 0;

    public TileEntityFlameDetector()
    {
        super(FLAMEDETECTOR_TILE.get());
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
        if (hasWorld() && !world.isRemote && ((tick % 10) == 0))
        {
            tick = 0;
            changeState(passRedstone());
        }
        tick++;
    }

    private void changeState(boolean value)
    {
        BlockState state = world.getBlockState(pos);
        boolean actualValue = state.get(BlockFlameDetector.ACTIVE);
        if (actualValue != value)
        {
            world.setBlockState(pos, state.with(BlockFlameDetector.ACTIVE, value), 3);
            this.Sync();
            world.notifyNeighborsOfStateChange(pos.offset(getBlockFacing()), state.getBlock());
        }
    }

    public Direction getBlockFacing()
    {
        return blockFacing;
    }

    public void setBlockFacing(Direction facing)
    {
        blockFacing = facing;
        markDirty();
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        tag.putInt("baseFacing", blockFacing.getIndex());
        return super.write(tag);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        blockFacing = Direction.byIndex(tag.getInt("baseFacing"));
    }
}
