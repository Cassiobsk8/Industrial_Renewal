package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.enums.enumproperty.EnumFaceRotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public abstract class TileEntityToggleableBase extends TileEntitySyncable
{
    private final Set<EnumFacing> enabledFacings = EnumSet.allOf(EnumFacing.class);
    public Boolean active = false;
    private EnumFacing facing = EnumFacing.SOUTH;
    private EnumFaceRotation faceRotation = EnumFaceRotation.DOWN;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    public abstract void playSwitchSound();

    public void setActive(boolean value)
    {
        active = value;
        this.markDirty();
    }

    public EnumFacing getOutPutFace()
    {
        EnumFacing vFace = this.getFacing();
        EnumFaceRotation rFace = getFaceRotation();

        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.UP))
        {
            return EnumFacing.EAST;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.DOWN))
        {
            return EnumFacing.WEST;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.LEFT))
        {
            return EnumFacing.DOWN;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.RIGHT))
        {
            return EnumFacing.UP;
        }
        if ((vFace == EnumFacing.EAST && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.LEFT))
        {
            return EnumFacing.SOUTH;
        }
        if ((vFace == EnumFacing.WEST && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.RIGHT))
        {
            return EnumFacing.NORTH;
        }
        return EnumFacing.NORTH;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }

    public void setFacing(final EnumFacing facing)
    {
        this.facing = facing;
        markDirty();
    }

    public boolean toggleFacing(final EnumFacing facing)
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

    public boolean disableFacing(final EnumFacing facing)
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

    public boolean activeFacing(final EnumFacing facing)
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

    public boolean isFacingEnabled(final @Nullable EnumFacing facing)
    {
        return enabledFacings.contains(facing) || facing == null;
    }

    public Set<EnumFacing> getEnabledFacings()
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
        final IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        notifyBlockUpdate();
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        facing = EnumFacing.byIndex(tag.getInteger("facing"));
        faceRotation = EnumFaceRotation.values()[tag.getInteger("faceRotation")];
        active = tag.getBoolean("active");

        enabledFacings.clear();

        final int[] enabledFacingIndices = tag.getIntArray("EnabledFacings");
        for (final int index : enabledFacingIndices)
        {
            enabledFacings.add(EnumFacing.byIndex(index));
        }
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        final int[] enabledFacingIndices = enabledFacings.stream().mapToInt(EnumFacing::getIndex).toArray();
        tag.setInteger("facing", facing.getIndex());
        tag.setInteger("faceRotation", faceRotation.ordinal());
        tag.setIntArray("EnabledFacings", enabledFacingIndices);
        tag.setBoolean("active", this.active);

        return super.writeToNBT(tag);
    }
}
