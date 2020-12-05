package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityBulkConveyorHopper extends TileEntityBulkConveyor
{
    public ItemStackHandler hopperInv = new ItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityBulkConveyorHopper.this.markDirty();
        }
    };
    private int tick2;

    public TileEntityBulkConveyorHopper(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        super.tick();
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
            if (te != null)
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                if (itemHandler != null)
                {
                    int itemsPerTick = 8;
                    if (Utils.moveItemsBetweenInventories(itemHandler, hopperInv, itemsPerTick)) markDirty();
                    return true;
                }
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
            List<Entity> list = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.up().getX(), pos.up().getY(), pos.up().getZ(), pos.up().getX() + 2D, pos.up().getY() + 1D, pos.up().getZ() + 1D), EntitySelectors.IS_ALIVE);
            if (!list.isEmpty() && list.get(0) instanceof ItemEntity)
            {
                ItemEntity entityItem = (ItemEntity) list.get(0);
                ItemStack stack = entityItem.getItem().copy();
                ItemStack stack1 = hopperInv.insertItem(0, stack, false);
                if (stack1.isEmpty()) entityItem.remove();
                else entityItem.setItem(stack1);
            }
        }
    }

    @Override
    public void dropInventory()
    {
        Utils.dropInventoryItems(world, pos, hopperInv);
        super.dropInventory();
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && facing != Direction.DOWN)
            return LazyOptional.of(() -> hopperInv).cast();
        return super.getCapability(capability, facing);
    }
}
