package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBulkConveyor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityBulkConveyor extends TileEntitySyncable implements ICapabilityProvider, ITickable
{
    public float stack3Pos;
    public float stack2Pos;
    public float stack1Pos;
    public float stack3YPos;
    public float stack2YPos;
    public float stack1YPos;
    private int tick;
    private boolean getInThisTick;
    public ItemStackHandler inventory = new ItemStackHandler(3)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityBulkConveyor.this.Sync();
            if (slot == 0)
            {
                TileEntityBulkConveyor.this.getInThisTick = true;
            }
        }
    };

    @Override
    public void update()
    {
        if (tick == 1) getInThisTick = false;
        if (tick % 4 == 0)
        {
            tick = 0;
            if (!world.isRemote)
            {
                moveItem();
                getEntityItemAbove();
            }
        }
        tick++;
    }

    private void moveItem()
    {
        ItemStack frontPositionItem = inventory.getStackInSlot(2);
        if (!(world.getBlockState(pos).getBlock() instanceof BlockBulkConveyor)) return;
        EnumFacing facing = world.getBlockState(pos).getValue(BlockBulkConveyor.FACING);
        if (!frontPositionItem.isEmpty())
        {
            BlockPos frontPos = pos.offset(facing);
            int mode = world.getBlockState(pos).getActualState(world, pos).getValue(BlockBulkConveyor.MODE);
            if (isFrontConveyor(facing, mode))
            {
                TileEntityBulkConveyor te = (TileEntityBulkConveyor) world.getTileEntity(frontPos);

                if (te == null)
                {
                    if (mode == 1) te = (TileEntityBulkConveyor) world.getTileEntity(frontPos.up());
                    else te = (TileEntityBulkConveyor) world.getTileEntity(frontPos.down());
                }

                if (te != null && te.getBlockFacing() != getBlockFacing().getOpposite() && te.transferItem(frontPositionItem, false))
                {
                    inventory.setStackInSlot(2, ItemStack.EMPTY);
                    frontPositionItem = ItemStack.EMPTY;
                }
            } else if (world.getBlockState(frontPos).getBlock().isAir(world.getBlockState(frontPos), world, frontPos))
            {
                double multiplierX = BlockBulkConveyor.getMotionX(facing);
                double multiplierZ = BlockBulkConveyor.getMotionZ(facing);
                EntityItem entityitem = new EntityItem(world, frontPos.getX() + 0.5D, frontPos.getY() + 0.5D, frontPos.getZ() + 0.5D, frontPositionItem);
                entityitem.motionY = 0;
                entityitem.motionX = multiplierX * 0.2;
                entityitem.motionZ = multiplierZ * 0.2;
                world.spawnEntity(entityitem);
                inventory.setStackInSlot(2, ItemStack.EMPTY);
                frontPositionItem = ItemStack.EMPTY;
            }
        }
        ItemStack MiddlePositionItem = inventory.getStackInSlot(1);
        if (frontPositionItem.isEmpty() && !MiddlePositionItem.isEmpty())
        {
            moveItemInternaly(1, 2);
            MiddlePositionItem = ItemStack.EMPTY;
        }
        ItemStack backPositionItem = inventory.getStackInSlot(0);
        if (!backPositionItem.isEmpty() && MiddlePositionItem.isEmpty() && (!getInThisTick || (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH)))
        {
            moveItemInternaly(0, 1);
        }
        getInThisTick = false;
    }

    private void moveItemInternaly(int from, int to)
    {
        inventory.setStackInSlot(to, inventory.getStackInSlot(from));
        inventory.setStackInSlot(from, ItemStack.EMPTY);
    }


    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    public boolean transferItem(ItemStack stack, boolean simulate)
    {
        if (inventory.getStackInSlot(0).isEmpty() && !stack.isEmpty())
        {
            if (!simulate) inventory.setStackInSlot(0, stack);
            getInThisTick = true;
            Sync();
            return true;
        }
        return false;
    }

    private boolean isFrontConveyor(EnumFacing facing, int mode)
    {
        Block block = world.getBlockState(pos.offset(facing)).getBlock();

        if (!(block instanceof BlockBulkConveyor))
        {
            if (mode == 1) block = world.getBlockState(pos.offset(facing).up()).getBlock();
            else block = world.getBlockState(pos.offset(facing).down()).getBlock();
        }

        return block instanceof BlockBulkConveyor;
    }

    private void getEntityItemAbove()
    {
        List<Entity> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up().getX(), pos.up().getY() - 1D, pos.up().getZ(), pos.up().getX() + 1D, pos.up().getY() + 0.5D, pos.up().getZ() + 1D), EntitySelectors.IS_ALIVE);
        if (!list.isEmpty() && list.get(0) instanceof EntityItem)
        {
            EntityItem entityItem = (EntityItem) list.get(0);
            ItemStack stack = entityItem.getItem().copy();
            ItemStack stack1 = inventory.insertItem(0, stack, false);
            if (stack1.isEmpty()) entityItem.setDead();
            else entityItem.setItem(stack1);
        }
    }

    public int getMode()
    {
        IBlockState state = world.getBlockState(pos).getActualState(world, pos);
        return state.getBlock() instanceof BlockBulkConveyor ? state.getValue(BlockBulkConveyor.MODE) : 0;
    }

    public ItemStack getStackInSlot(int slot)
    {
        return inventory.getStackInSlot(slot).copy();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return false;
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.inventory.deserializeNBT(compound.getCompoundTag("inv"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("inv", this.inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    public EnumFacing getBlockFacing()
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockBulkConveyor) return state.getValue(BlockBulkConveyor.FACING);
        return EnumFacing.NORTH;
    }
}
