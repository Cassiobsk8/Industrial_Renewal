package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBulkConveyor;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBulkConveyor extends TileEntitySync implements ITickable
{
    public static final int frontNumber = 2;
    public static final int middleNumber = 1;
    public static final int backNumber = 0;
    private final int tickSpeed = 4;
    private int frontTick;
    private int middleTick;
    private int backTick;
    private float oldFrontTick;
    private float oldMiddleTick;
    private float oldBackTick;
    private float rFrontTick;
    private float rMiddleTick;
    public ItemStackHandler inventory = new ItemStackHandler(3)
    {
        @Override
        public int getSlots()
        {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return getStackInSlot(slot).isEmpty() && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityBulkConveyor.this.itemReceived(slot);
        }
    };
    private float rBackTick;
    private EnumFacing facing;

    @Override
    public void update()
    {
        //FRONT
        if (!inventory.getStackInSlot(frontNumber).isEmpty())
        {
            if (frontTick >= tickSpeed)
            {
                updateFrontStack();
            }
            if (frontTick < tickSpeed) frontTick++;
            oldFrontTick = rFrontTick;
            rFrontTick = Utils.normalize((float) frontTick, 0.1f, (float) tickSpeed);
        } else
        {
            oldFrontTick = 0;
            rFrontTick = 0;
        }
        //MIDDLE
        if (!inventory.getStackInSlot(middleNumber).isEmpty())
        {
            if (middleTick >= tickSpeed)
            {
                updateMiddleStack();
            }
            if (middleTick < tickSpeed) middleTick++;
            oldMiddleTick = rMiddleTick;
            rMiddleTick = Utils.normalize((float) middleTick, 0, (float) tickSpeed);
        }
        //BACK
        if (!inventory.getStackInSlot(backNumber).isEmpty())
        {
            if (backTick >= tickSpeed)
            {
                updateBackStack();
            }
            if (backTick < tickSpeed) backTick++;
            oldBackTick = rBackTick;
            rBackTick = Utils.normalize((float) backTick, 0, (float) tickSpeed);
        }
        sync();
    }

    public void itemReceived(int slot)
    {
        ItemStack stack = inventory.getStackInSlot(slot);
        if (!stack.isEmpty())
        {
            if (slot == frontNumber)
            {
                frontTick = 0;
                rFrontTick = 0;
            } else if (slot == middleNumber)
            {
                middleTick = 0;
                rMiddleTick = 0;
            } else if (slot == backNumber)
            {
                backTick = 0;
                rMiddleTick = 0;
            }
        }
        sync();
    }

    private boolean updateFrontStack()
    {
        if (world.isRemote) return false;
        ItemStack frontPositionItem = inventory.getStackInSlot(frontNumber);
        IBlockState ownState = world.getBlockState(pos);
        if (!(ownState.getBlock() instanceof BlockBulkConveyor)) return false;

        EnumFacing facing = getBlockFacing();
        if (!frontPositionItem.isEmpty())
        {
            BlockPos frontPos = pos.offset(facing);
            int mode = ownState.getActualState(world, pos).getValue(BlockBulkConveyor.MODE);
            BlockPos targetConveyorPos = frontConveyor(facing, mode);
            if (targetConveyorPos != null)
            {
                TileEntity tileEntity = world.getTileEntity(targetConveyorPos);
                if (tileEntity instanceof TileEntityBulkConveyor)
                {
                    TileEntityBulkConveyor te = (TileEntityBulkConveyor) tileEntity;
                    if (te.getBlockFacing() == getBlockFacing() && te.transferItem(frontPositionItem, false))
                    { // IF IS STRAIGHT
                        inventory.setStackInSlot(frontNumber, ItemStack.EMPTY);
                        return true;
                    } else if (te.getBlockFacing() != getBlockFacing().getOpposite() && te.getStackInSlot(middleNumber).isEmpty())
                    { // IF IS CORNER
                        if (te.transferItem(frontPositionItem, middleNumber, false))
                        {
                            inventory.setStackInSlot(frontNumber, ItemStack.EMPTY);
                            return true;
                        }
                    }
                }
            } else if (world.getBlockState(frontPos).getBlock().isAir(world.getBlockState(frontPos), world, frontPos))
            {
                dropFrontItem(facing, frontPositionItem, frontPos);
                return true;
            }
        }
        return false;
    }

    private boolean updateMiddleStack()
    {
        if (world.isRemote) return false;
        ItemStack MiddlePositionItem = inventory.getStackInSlot(middleNumber);
        ItemStack frontPositionItem = inventory.getStackInSlot(frontNumber);
        if (frontPositionItem.isEmpty() && !MiddlePositionItem.isEmpty())
        {
            moveItemInternally(middleNumber, frontNumber);
            return true;
        }
        return false;
    }

    private boolean updateBackStack()
    {
        if (world.isRemote) return false;
        ItemStack backPositionItem = inventory.getStackInSlot(backNumber);
        ItemStack MiddlePositionItem = inventory.getStackInSlot(middleNumber);
        if (!backPositionItem.isEmpty() && MiddlePositionItem.isEmpty())
        {
            moveItemInternally(backNumber, middleNumber);
            return true;
        }
        return false;
    }

    public float getStackOffset(int slot, boolean old)
    {
        switch (slot)
        {
            default:
            case 2:
                return old ? oldFrontTick : rFrontTick;
            case 1:
                return old ? oldMiddleTick : rMiddleTick;
            case 0:
                return old ? oldBackTick : rBackTick;
        }
    }

    public float getMinYOffset(int slot)
    {
        int mode = getMode();
        switch (slot)
        {
            default:
            case 2:
                return mode == 0 ? 0.47f : 0.97f;
            case 1:
                if (mode == 1) return 0.65f;
                if (mode == 2) return 1.3f;
                return 0.47f;
            case 0:
                if (mode == 1) return 0.3f;
                if (mode == 2) return 1.65f;
                return 0.47f;
        }
    }

    public float getMaxYOffset()
    {
        int mode = getMode();
        if (mode == 0) return 0;
        else if (mode == 1) return 0.33f;
        return -0.33f;
    }

    private BlockPos frontConveyor(EnumFacing facing, int mode)
    {
        BlockPos frontPos = pos.offset(facing);
        if (mode == 1 || !(world.getBlockState(frontPos).getBlock() instanceof BlockBulkConveyor))
        {
            if (mode == 1)
            {
                frontPos = pos.offset(facing).up();
            } else
            {
                frontPos = pos.offset(facing).down();
            }
        } else
        {
            return frontPos;
        }
        IBlockState frontState = world.getBlockState(frontPos);
        return (frontState.getBlock() instanceof BlockBulkConveyor
                && frontState.getValue(BlockBulkConveyor.FACING) == getBlockFacing())
                ? frontPos : null;
    }

    private void moveItemInternally(int from, int to)
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
        return transferItem(stack, 0, simulate);
    }

    public boolean transferItem(ItemStack stack, int slot, boolean simulate)
    {
        if (inventory.getStackInSlot(0).isEmpty() && !stack.isEmpty())
        {
            if (!simulate) inventory.setStackInSlot(slot, stack);
            return true;
        }
        return false;
    }

    public void dropFrontItem(EnumFacing facing, ItemStack frontPositionItem, BlockPos frontPos)
    {
        double multiplierX = BlockBulkConveyor.getMotionX(facing);
        double multiplierZ = BlockBulkConveyor.getMotionZ(facing);
        EntityItem entityitem = new EntityItem(world, frontPos.getX() + 0.5D, frontPos.getY() + 0.5D, frontPos.getZ() + 0.5D, frontPositionItem);
        entityitem.motionY = 0;
        entityitem.motionX = multiplierX * 0.2;
        entityitem.motionZ = multiplierZ * 0.2;
        world.spawnEntity(entityitem);
        inventory.setStackInSlot(2, ItemStack.EMPTY);
    }

    public void dropInventory()
    {
        Utils.dropInventoryItems(world, pos, inventory);
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
        return facing == getBlockFacing().getOpposite() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (facing == getBlockFacing().getOpposite() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        if (hasWorld() && world.isRemote)
        {
            frontTick = compound.getInteger("rFront");
            middleTick = compound.getInteger("rMiddle");
            backTick = compound.getInteger("rBack");

            rFrontTick = compound.getFloat("oFront");
            rMiddleTick = compound.getFloat("oMiddle");
            rBackTick = compound.getFloat("oBack");
        }

        this.inventory.deserializeNBT(compound.getCompoundTag("inv"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("rFront", frontTick);
        compound.setInteger("rMiddle", middleTick);
        compound.setInteger("rBack", backTick);

        compound.setFloat("oFront", rFrontTick);
        compound.setFloat("oMiddle", rMiddleTick);
        compound.setFloat("oBack", rBackTick);

        compound.setTag("inv", this.inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    public EnumFacing getBlockFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockBulkConveyor)
            return facing = state.getValue(BlockBulkConveyor.FACING);
        return EnumFacing.NORTH;
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;
    }
}
