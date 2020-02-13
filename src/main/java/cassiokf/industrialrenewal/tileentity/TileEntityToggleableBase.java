package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.enums.enumproperty.EnumFaceRotation;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public abstract class TileEntityToggleableBase extends TileEntitySyncable
{
    private final Set<Direction> enabledFacings = EnumSet.allOf(Direction.class);
    public Boolean active = false;
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
        this.markDirty();
    }

    public Direction getOutPutFace()
    {
        Direction vFace = this.getFacing();
        EnumFaceRotation rFace = getFaceRotation();

        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.UP))
        {
            return Direction.EAST;
        }
        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.DOWN))
        {
            return Direction.WEST;
        }
        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.WEST && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.EAST && rFace == EnumFaceRotation.LEFT))
        {
            return Direction.DOWN;
        }
        if ((vFace == Direction.NORTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.SOUTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.WEST && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.EAST && rFace == EnumFaceRotation.RIGHT))
        {
            return Direction.UP;
        }
        if ((vFace == Direction.EAST && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.WEST && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.RIGHT)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.LEFT))
        {
            return Direction.SOUTH;
        }
        if ((vFace == Direction.WEST && rFace == EnumFaceRotation.UP)
                || (vFace == Direction.EAST && rFace == EnumFaceRotation.DOWN)
                || (vFace == Direction.UP && rFace == EnumFaceRotation.LEFT)
                || (vFace == Direction.DOWN && rFace == EnumFaceRotation.RIGHT))
        {
            return Direction.NORTH;
        }
        return Direction.NORTH;
    }

    public Direction getFacing()
    {
        return facing;
    }

    public void setFacing(final Direction facing)
    {
        this.facing = facing;
        markDirty();
    }

    public boolean toggleFacing(final Direction facing)
    {
        if (enabledFacings.contains(facing))
        {
            enabledFacings.remove(facing);
            this.markDirty();
            return false;
        } else
        {
            enabledFacings.add(facing);
            this.markDirty();
            return true;
        }
    }

    public boolean disableFacing(final Direction facing)
    {
        if (enabledFacings.contains(facing))
        {
            enabledFacings.remove(facing);
            this.markDirty();
            return false;
        } else
        {
            return true;
        }
    }

    public boolean activeFacing(final Direction facing)
    {
        if (enabledFacings.contains(facing))
        {
            return false;
        } else
        {
            enabledFacings.add(facing);
            this.markDirty();
            return true;
        }
    }

    public boolean isFacingEnabled(final @Nullable Direction facing)
    {
        return enabledFacings.contains(facing) || facing == null;
    }

    public Set<Direction> getEnabledFacings()
    {
        return enabledFacings;
    }

    public EnumFaceRotation getFaceRotation()
    {
        return faceRotation;
    }

    public void setFaceRotation(final EnumFaceRotation faceRotation)
    {
        this.faceRotation = faceRotation;
        markDirty();
    }

    private void notifyBlockUpdate()
    {
        final BlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        notifyBlockUpdate();
    }

    @Override
    public void read(CompoundNBT tag)
    {
        facing = Direction.byIndex(tag.getInt("facing"));
        faceRotation = EnumFaceRotation.values()[tag.getInt("faceRotation")];
        active = tag.getBoolean("active");

        enabledFacings.clear();

        final int[] enabledFacingIndices = tag.getIntArray("EnabledFacings");
        for (final int index : enabledFacingIndices)
        {
            enabledFacings.add(Direction.byIndex(index));
        }
        super.read(tag);
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        final int[] enabledFacingIndices = enabledFacings.stream().mapToInt(Direction::getIndex).toArray();
        tag.putInt("facing", facing.getIndex());
        tag.putInt("faceRotation", faceRotation.ordinal());
        tag.putIntArray("EnabledFacings", enabledFacingIndices);
        tag.putBoolean("active", this.active);

        return super.write(tag);
    }
}
