package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityBulkConveyorHopper extends TileEntityBulkConveyor
{
    public ItemStackHandler hopperInv = new ItemStackHandler(1);
    private int tick2;

    @Override
    public void update()
    {
        super.update();
        if (!world.isRemote)
        {
            if (tick2 % 4 == 0)
            {
                tick2 = 0;
                if (!getInvAbove()) getEntityItemAbove();
                hopperToConveyor();
            }
            tick2++;
        }
    }

    private boolean getInvAbove()
    {
        if (hopperInv.getStackInSlot(0).isEmpty())
        {
            TileEntity te = world.getTileEntity(pos.up());
            if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                if (itemHandler != null)
                {
                    int itemsPerTick = 8;
                    for (int i = 0; i < itemHandler.getSlots(); i++)
                    {
                        ItemStack stack = itemHandler.extractItem(i, itemsPerTick, true);
                        ItemStack left = hopperInv.insertItem(0, stack, false);
                        if (!ItemStack.areItemStacksEqual(stack, left))
                        {
                            int toExtract = stack.getCount() - left.getCount();
                            itemHandler.extractItem(i, toExtract, false);
                            markDirty();
                            break;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void hopperToConveyor()
    {
        if (!hopperInv.getStackInSlot(0).isEmpty())
        {
            ItemStack stack = hopperInv.getStackInSlot(0).copy();
            ItemStack stack1 = inventory.insertItem(1, stack, false);
            hopperInv.getStackInSlot(0).shrink(stack.getCount() - stack1.getCount());
        }
    }

    private void getEntityItemAbove()
    {
        if (hopperInv.getStackInSlot(0).isEmpty())
        {
            List<Entity> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up().getX(), pos.up().getY(), pos.up().getZ(), pos.up().getX() + 2D, pos.up().getY() + 1D, pos.up().getZ() + 1D), EntitySelectors.IS_ALIVE);
            if (!list.isEmpty() && list.get(0) instanceof EntityItem)
            {
                EntityItem entityItem = (EntityItem) list.get(0);
                ItemStack stack = entityItem.getItem().copy();
                ItemStack stack1 = hopperInv.insertItem(0, stack, false);
                if (stack1.isEmpty()) entityItem.setDead();
                else entityItem.setItem(stack1);
            }
        }
    }

    @Override
    public void dropInventory()
    {
        super.dropInventory();
        Utils.dropInventoryItems(world, pos, hopperInv);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && facing != EnumFacing.DOWN;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && facing != EnumFacing.DOWN)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(hopperInv);
        return super.getCapability(capability, facing);
    }
}
