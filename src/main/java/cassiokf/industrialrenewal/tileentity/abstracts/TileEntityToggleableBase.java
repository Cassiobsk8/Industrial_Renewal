package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.util.enums.enumproperty.EnumFaceRotation;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileEntityToggleableBase extends TileEntitySync
{
    public boolean active = false;
    public boolean powered = false;
    private Direction outputFace = null;
    private Direction facing = Direction.SOUTH;
    private EnumFaceRotation faceRotation = EnumFaceRotation.DOWN;

    public TileEntityToggleableBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public abstract void playSwitchSound();

    public void setActive(boolean value)
    {
        active = value;
        setOutputFace();
        this.markDirty();
    }

    public void setPowered(boolean value)
    {
        powered = value;
        this.markDirty();
    }

    public Direction getOutPutFace()
    {
        if (outputFace == null)
        {
            setOutputFace();
            markDirty();
        }
        return outputFace;
    }

    private void setOutputFace()
    {
        Direction vFace = facing;
        EnumFaceRotation rFace = faceRotation;

        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.UP))
        {
            outputFace = Direction.EAST;
            return;
        }
        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.DOWN))
        {
            outputFace = Direction.WEST;
            return;
        }
        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.WEST && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.EAST && rFace == EnumFaceRotation.LEFT))
        {
            outputFace = Direction.DOWN;
            return;
        }
        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.WEST && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.EAST && rFace == EnumFaceRotation.RIGHT))
        {
            outputFace = Direction.UP;
            return;
        }
        if ((vFace == Direction.EAST && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.WEST && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.LEFT))
        {
            outputFace = Direction.SOUTH;
            return;
        }
        if ((vFace == Direction.WEST && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.EAST && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.RIGHT))
        {
            outputFace = Direction.NORTH;
            return;
        }
        outputFace = Direction.NORTH;
    }

    public Direction getFacing()
    {
        return facing;
    }

    public void setFacing(final Direction facing)
    {
        this.facing = facing;
        setOutputFace();
        markDirty();
    }

    public EnumFaceRotation getFaceRotation()
    {
        return faceRotation;
    }

    public void setFaceRotation(final EnumFaceRotation faceRotation)
    {
        this.faceRotation = faceRotation;
        setOutputFace();
        markDirty();
    }

    private void notifyBlockUpdate()
    {
        final BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        notifyBlockUpdate();
    }

    @Override
    public void read(final CompoundNBT tag)
    {
        facing = Direction.byIndex(tag.getInt("facing"));
        faceRotation = EnumFaceRotation.values()[tag.getInt("faceRotation")];
        active = tag.getBoolean("active");
        powered = tag.getBoolean("powered");
        outputFace = Direction.byIndex(tag.getInt("output"));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        tag.putInt("facing", facing.getIndex());
        tag.putInt("faceRotation", faceRotation.ordinal());
        tag.putInt("output", getOutPutFace().getIndex());
        tag.putBoolean("active", active);
        tag.putBoolean("powered", powered);

        return super.write(tag);
    }
}
