package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.tileentity.TileEntitySyncable;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public abstract class TileEntityBaseLoader extends TileEntitySyncable implements ICapabilityProvider
{
    public Direction blockFacing;
    public waitEnum waitE = waitEnum.NO_ACTIVITY;
    public boolean unload;
    public boolean loading;
    public String cartName = "";
    public int cartActivity;

    public TileEntityBaseLoader(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public waitEnum getWaitEnum()
    {
        if (waitE == null)
        {
            waitE = waitEnum.WAIT_FULL;
        }
        return waitE;
    }

    public void setWaitEnum(int value)
    {
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

    public abstract Direction getBlockFacing();

    public abstract boolean isUnload();

    public abstract boolean onMinecartPass(AbstractMinecartEntity entityMinecart, TileEntityLoaderRail loaderRail);

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putInt("EnumConfig", this.waitE.intValue);
        compound.putBoolean("unload", unload);
        compound.putBoolean("loading", loading);
        compound.putString("cartname", cartName);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        waitE = waitEnum.valueOf(compound.getInt("EnumConfig"));
        unload = compound.getBoolean("unload");
        loading = compound.getBoolean("loading");
        cartName = compound.getString("cartname");
        super.read(compound);
    }

    public enum waitEnum
    {
        WAIT_FULL(0),
        WAIT_EMPTY(1),
        NO_ACTIVITY(2),
        NEVER(3);

        public int intValue;

        waitEnum(int value)
        {
            intValue = value;
        }

        public static waitEnum valueOf(int waitNo)
        {
            if (waitNo > waitEnum.values().length - 1)
            {
                waitNo = 0;
            }
            for (waitEnum l : waitEnum.values())
            {
                if (l.intValue == waitNo) return l;
            }
            throw new IllegalArgumentException("waitEnum not found");
        }
    }

}
