package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.util.enums.enumproperty.EnumFaceRotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityToggleableBase extends TileEntitySyncable
{
    public boolean active = false;
    public boolean powered = false;
    private EnumFacing outputFace = null;
    private EnumFacing facing = EnumFacing.SOUTH;
    private EnumFaceRotation faceRotation = EnumFaceRotation.DOWN;

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

    public EnumFacing getOutPutFace()
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
        EnumFacing vFace = facing;
        EnumFaceRotation rFace = faceRotation;

        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.UP))
        {
            outputFace = EnumFacing.EAST;
            return;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.DOWN))
        {
            outputFace = EnumFacing.WEST;
            return;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.LEFT))
        {
            outputFace = EnumFacing.DOWN;
            return;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.RIGHT))
        {
            outputFace = EnumFacing.UP;
            return;
        }
        if ((vFace == EnumFacing.EAST && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.RIGHT)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.LEFT))
        {
            outputFace = EnumFacing.SOUTH;
            return;
        }
        if ((vFace == EnumFacing.WEST && rFace == EnumFaceRotation.UP)
                || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.DOWN)
                || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.LEFT)
                || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.RIGHT))
        {
            outputFace = EnumFacing.NORTH;
            return;
        }
        outputFace = EnumFacing.NORTH;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }

    public void setFacing(final EnumFacing facing)
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
        final IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
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
        powered = tag.getBoolean("powered");
        outputFace = EnumFacing.byIndex(tag.getInteger("output"));
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tag.setInteger("facing", facing.getIndex());
        tag.setInteger("faceRotation", faceRotation.ordinal());
        tag.setInteger("output", getOutPutFace().getIndex());
        tag.setBoolean("active", active);
        tag.setBoolean("powered", powered);

        return super.writeToNBT(tag);
    }
}
