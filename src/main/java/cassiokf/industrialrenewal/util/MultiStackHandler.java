package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.util.interfaces.ISync;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MultiStackHandler extends ItemStackHandler
{
    private final TileEntity te;
    private int maxStack;
    private boolean sync;
    private int count;

    public MultiStackHandler(int maxStack, boolean sync, TileEntity te)
    {
        super(1);
        this.maxStack = maxStack;
        this.sync = sync;
        this.te = te;
    }

    public ItemStack insertItem(@Nonnull ItemStack stack)
    {
        return insertItem(0, stack, false);
    }

    public ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate)
    {
        return insertItem(0, stack, simulate);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        validateSlotIndex(0);

        ItemStack existing = this.stacks.get(0);

        int limit = getStackLimit(0, stack);

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= count;
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                this.stacks.set(0, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, 1) : stack);
                count = reachedLimit ? limit : stack.getCount();
            }
            else
            {
                count += reachedLimit ? limit : stack.getCount();
            }
            onContentsChanged(0);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public ItemStack extractItem(int amount)
    {
        return extractItem(0, amount, false);
    }

    public ItemStack extractItem(int amount, boolean simulate)
    {
        return extractItem(0, amount, simulate);
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(0);

        ItemStack existing = this.stacks.get(0);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, maxStack);

        if (count <= toExtract)
        {
            if (!simulate)
            {
                this.stacks.set(0, ItemStack.EMPTY);
                count = 0;
                onContentsChanged(0);
            }
            return existing;
        }
        else
        {
            if (!simulate)
            {
                this.stacks.set(0, ItemHandlerHelper.copyStackWithSize(existing, 1));
                count -= toExtract;
                onContentsChanged(0);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return maxStack;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int value)
    {
        count = value;
        onContentsChanged(0);
    }

    public void removeFromCount(int value)
    {
        count -= value;
        onContentsChanged(0);
    }

    public void addToCount(int value)
    {
        count += value;
        onContentsChanged(0);
    }

    public void setSlotLimit(int value)
    {
        maxStack = value;
        onContentsChanged(0);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack)
    {
        return getSlotLimit(0);
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        super.onContentsChanged(slot);
        if (sync && te instanceof ISync) ((ISync) te).sync();
        else te.markDirty();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = super.serializeNBT();
        compound.setInteger("maxStack", maxStack);
        compound.setBoolean("sync", sync);
        compound.setInteger("count", count);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        maxStack = nbt.getInteger("maxStack");
        sync = nbt.getBoolean("sync");
        count = nbt.getInteger("count");
        super.deserializeNBT(nbt);
    }
}
