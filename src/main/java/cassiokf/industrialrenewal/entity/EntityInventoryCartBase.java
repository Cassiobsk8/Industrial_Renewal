package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class EntityInventoryCartBase extends TrainBase implements IInventory
{
    public final ItemStackHandler inventory = new ItemStackHandler(getSizeInventory())
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            if (stack.isEmpty())
            {
                return false;
            }
            return EntityInventoryCartBase.this.isItemValidForSlot(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            EntityInventoryCartBase.this.Sync();
        }
    };

    public EntityInventoryCartBase(World worldIn)
    {
        super(worldIn);
    }

    public EntityInventoryCartBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public void Sync()
    {
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory)
                : super.getCapability(capability, facing);
    }

    //IINVENTORY
    @Override
    public Type getType()
    {
        return Type.CHEST;
    }

    @Override
    public abstract int getSizeInventory();

    @Override
    public boolean isEmpty()
    {
        return Utils.IsInventoryEmpty(inventory);
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return inventory.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return inventory.extractItem(index, inventory.getStackInSlot(index).getMaxStackSize(), false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        inventory.setStackInSlot(index, stack);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return inventory.getSlotLimit(0);
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return !player.isSpectator();
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
    }
}
