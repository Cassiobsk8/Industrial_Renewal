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

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityBulkConveyorHopper extends TileEntityBulkConveyor
{
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
            }
            tick2++;
        }
    }

    private boolean getInvAbove()
    {
        if (inventory.getStackInSlot(backNumber).isEmpty()) {
            TileEntity te = world.getTileEntity(pos.up());
            if (te != null) {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                if (itemHandler != null) {
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
        if (inventory.getStackInSlot(backNumber).isEmpty()) {
            List<Entity> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up().getX(), pos.up().getY(), pos.up().getZ(), pos.up().getX() + 2D, pos.up().getY() + 1D, pos.up().getZ() + 1D), EntitySelectors.IS_ALIVE);
            if (!list.isEmpty() && list.get(0) instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) list.get(0);
                ItemStack stack = entityItem.getItem().copy();
                ItemStack stack1 = inventory.insertItem(backNumber, stack, false);
                if (stack1.isEmpty()) entityItem.setDead();
                else entityItem.setItem(stack1);
            }
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && facing != EnumFacing.DOWN)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        return super.getCapability(capability, facing);
    }
}
