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

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityBulkConveyorHopper extends TileEntityBulkConveyor
{
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
            }
            tick2++;
        }
    }

    private boolean getInvAbove()
    {
        if (inventory.getStackInSlot(backNumber).isEmpty())
        {
            TileEntity te = world.getTileEntity(pos.up());
            if (te != null)
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                if (itemHandler != null)
                {
                    int itemsPerTick = 8;
                    if (Utils.moveItemsBetweenInventories(itemHandler, inventory, itemsPerTick)) markDirty();
                    return true;
                }
            }
        }
        return false;
    }

    private void getEntityItemAbove()
    {
        if (inventory.getStackInSlot(backNumber).isEmpty())
        {
            List<Entity> list = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.up().getX(), pos.up().getY(), pos.up().getZ(), pos.up().getX() + 2D, pos.up().getY() + 1D, pos.up().getZ() + 1D), Entity::isAlive);
            if (!list.isEmpty() && list.get(0) instanceof ItemEntity)
            {
                ItemEntity entityItem = (ItemEntity) list.get(0);
                ItemStack stack = entityItem.getItem().copy();
                ItemStack stack1 = inventory.insertItem(backNumber, stack, false);
                if (stack1.isEmpty()) entityItem.remove();
                else entityItem.setItem(stack1);
            }
        }
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && facing != Direction.DOWN)
            return LazyOptional.of(() -> inventory).cast();
        return super.getCapability(capability, facing);
    }
}
