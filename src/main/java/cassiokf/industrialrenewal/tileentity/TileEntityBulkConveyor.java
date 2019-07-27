package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBulkConveyor;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityBulkConveyor extends TileEntitySyncable implements ICapabilityProvider, ITickable
{
    public double stack3Pos;
    public double stack2Pos;
    public double stack1Pos;
    public double stack3YPos;
    public double stack2YPos;
    public double stack1YPos;
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
        if (world.isRemote)
        {
            doAnimation();
        }

        if (tick == 1) getInThisTick = false;
        if (tick % 4 == 0)
        {
            tick = 0;
            if (!world.isRemote)
            {
                moveItem();
            }
        }
        tick++;
    }

    private void doAnimation()
    {
        ItemStack stack1 = getStackInSlot(0);
        ItemStack stack2 = getStackInSlot(1);
        ItemStack stack3 = getStackInSlot(2);
        int mode = getMode();
        float yPos;

        float speed = 0.33f;
        if (!stack3.isEmpty())
        {
            stack3Pos = Utils.lerp(stack3Pos, -0.9D, speed);
            yPos = mode == 0 ? 0.47f : mode == 1 ? 1.3f : 0.65f;
            if (mode == 0) stack3YPos = yPos;
            else stack3YPos = Utils.lerp(stack3YPos, yPos, speed);
        } else
        {
            stack3Pos = -0.57f;
            stack3YPos = mode == 0 ? 0.47f : 0.97f;
        }
        if (!stack2.isEmpty())
        {
            stack2Pos = Utils.lerp(stack2Pos, -0.57D, speed);
            yPos = mode == 0 ? 0.47f : 0.97f;
            if (mode == 0) stack2YPos = yPos;
            else stack2YPos = Utils.lerp(stack2YPos, yPos, speed);
        } else
        {
            stack2Pos = -0.25f;
            if (mode == 1) stack2YPos = 0.65f;
            if (mode == 2) stack2YPos = 1.3f;
        }
        if (!stack1.isEmpty())
        {
            stack1Pos = Utils.lerp(stack1Pos, -0.25D, speed);
            yPos = mode == 0 ? 0.47f : mode == 1 ? 0.65f : 1.3f;
            if (mode == 0) stack1YPos = yPos;
            else stack1YPos = Utils.lerp(stack1YPos, yPos, speed);
        } else
        {
            stack1Pos = 0f;
            if (mode == 1) stack1YPos = 0.3f;
            if (mode == 2) stack1YPos = 1.65f;
        }
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
                TileEntityBulkConveyor te = null;
                if (mode != 1 && world.getTileEntity(frontPos) instanceof TileEntityBulkConveyor)
                {
                    te = (TileEntityBulkConveyor) world.getTileEntity(frontPos);
                } else
                {
                    if (mode == 1 && world.getTileEntity(frontPos.up()) instanceof TileEntityBulkConveyor)
                    {
                        te = (TileEntityBulkConveyor) world.getTileEntity(frontPos.up());
                    } else if (world.getTileEntity(frontPos.down()) instanceof TileEntityBulkConveyor)
                    {
                        te = (TileEntityBulkConveyor) world.getTileEntity(frontPos.down());
                    }
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

    public void dropInventory()
    {
        Utils.dropInventoryItems(world, pos, inventory);
    }

    private boolean isFrontConveyor(EnumFacing facing, int mode)
    {
        IBlockState state = world.getBlockState(pos.offset(facing));

        if (mode == 1 || !(state.getBlock() instanceof BlockBulkConveyor))
        {
            if (mode == 1)
            {
                state = world.getBlockState(pos.offset(facing).up());
            } else
            {
                state = world.getBlockState(pos.offset(facing).down());
            }
        } else return true;
        return state.getBlock() instanceof BlockBulkConveyor && state.getValue(BlockBulkConveyor.FACING) == getBlockFacing();
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
