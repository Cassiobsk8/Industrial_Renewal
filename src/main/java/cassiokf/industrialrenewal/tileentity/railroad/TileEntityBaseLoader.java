package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.tileentity.TileEntitySyncable;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityBaseLoader extends TileEntitySyncable
{
    public EnumFacing blockFacing;
    public waitEnum waitE = waitEnum.NO_ACTIVITY;
    public boolean unload;
    public boolean loading;
    public String cartName = "";
    public int cartActivity;

    public waitEnum getWaitEnum() {
        if (waitE == null) {
            waitE = waitEnum.WAIT_FULL;
        }
        return waitE;
    }

    public void setWaitEnum(int value) {
        waitE = waitEnum.valueOf(value);
    }

    public void setNextWaitEnum()
    {
        int old = getWaitEnum().ordinal();
        waitE = waitEnum.valueOf(old + 1);
        Sync();
    }

    public void changeUnload()
    {
        unload = !unload;
        Sync();
    }

    public abstract EnumFacing getBlockFacing();

    public abstract boolean isUnload();

    public abstract boolean onMinecartPass(EntityMinecart entityMinecart, TileEntityLoaderRail loaderRail);

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("EnumConfig", this.waitE.intValue);
        compound.setBoolean("unload", unload);
        compound.setBoolean("loading", loading);
        compound.setString("cartname", cartName);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        waitE = waitEnum.valueOf(compound.getInteger("EnumConfig"));
        unload = compound.getBoolean("unload");
        loading = compound.getBoolean("loading");
        cartName = compound.getString("cartname");
        super.readFromNBT(compound);
    }

    public enum waitEnum {
        WAIT_FULL(0),
        WAIT_EMPTY(1),
        NO_ACTIVITY(2),
        NEVER(3);

        public int intValue;

        waitEnum(int value) {
            intValue = value;
        }

        public static waitEnum valueOf(int waitNo) {
            if (waitNo > waitEnum.values().length - 1) {
                waitNo = 0;
            }
            for (waitEnum l : waitEnum.values()) {
                if (l.intValue == waitNo) return l;
            }
            throw new IllegalArgumentException("waitEnum not found");
        }
    }

}
