package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static cassiokf.industrialrenewal.init.TileRegistration.CONVEYORVHOPPER_TILE;

public class TileEntityBulkConveyorHopper extends TileEntityBulkConveyorBase
{
    private LazyOptional<IItemHandler> hopperInv = LazyOptional.of(this::createHopperHandler);
    private int tick2;

    public TileEntityBulkConveyorHopper()
    {
        super(CONVEYORVHOPPER_TILE.get());
    }

    private IItemHandler createHopperHandler()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityBulkConveyorHopper.this.markDirty();
            }
        };
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
        if (hopperInv.orElse(null).getStackInSlot(0).isEmpty())
        {
            TileEntity te = world.getTileEntity(pos.up());
            if (te != null)
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                if (itemHandler != null)
                {
                    int itemsPerTick = 8;
                    for (int i = 0; i < itemHandler.getSlots(); i++)
                    {
                        ItemStack stack = itemHandler.extractItem(i, itemsPerTick, true);
                        ItemStack left = hopperInv.orElse(null).insertItem(0, stack, false);
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
        if (!hopperInv.orElse(null).getStackInSlot(0).isEmpty())
        {
            ItemStack stack = hopperInv.orElse(null).getStackInSlot(0).copy();
            ItemStack stack1 = inventory.orElse(null).insertItem(1, stack, false);
            hopperInv.orElse(null).getStackInSlot(0).shrink(stack.getCount() - stack1.getCount());
        }
    }

    private void getEntityItemAbove()
    {
        if (hopperInv.orElse(null).getStackInSlot(0).isEmpty())
        {
            List<Entity> list = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.up().getX(), pos.up().getY(), pos.up().getZ(), pos.up().getX() + 2D, pos.up().getY() + 1D, pos.up().getZ() + 1D), EntityPredicates.IS_ALIVE);
            if (!list.isEmpty() && list.get(0) instanceof ItemEntity)
            {
                ItemEntity entityItem = (ItemEntity) list.get(0);
                ItemStack stack = entityItem.getItem().copy();
                ItemStack stack1 = hopperInv.orElse(null).insertItem(0, stack, false);
                if (stack1.isEmpty()) entityItem.remove();
                else entityItem.setItem(stack1);
            }
        }
    }

    @Override
    public void dropInventory()
    {
        Utils.dropInventoryItems(world, pos, hopperInv.orElse(null));
        super.dropInventory();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        hopperInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT invTag = compound.getCompound("inv");
        hopperInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && facing != Direction.DOWN)
            return hopperInv.cast();
        return super.getCapability(capability, facing);
    }
}
