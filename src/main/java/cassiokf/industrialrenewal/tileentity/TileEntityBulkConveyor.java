package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBulkConveyor;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBulkConveyor extends TileEntitySync implements ITickableTileEntity
{
    public static final int frontNumber = 2;
    public static final int middleNumber = 1;
    public static final int backNumber = 0;
    private static final int tickSpeed = 4;
    private int frontTick;
    private int middleTick;
    private int backTick;
    private float oldFrontTick;
    private float oldMiddleTick;
    private float oldBackTick;
    private float rFrontTick;
    private float rMiddleTick;
    public CustomItemStackHandler inventory = new CustomItemStackHandler(3)
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
            TileEntityBulkConveyor.this.markDirty();
            TileEntityBulkConveyor.this.itemReceived(slot);
        }
    };
    private float rBackTick;
    private Direction facing;

    public TileEntityBulkConveyor(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        boolean moved = false;
        //FRONT
        if (!inventory.getStackInSlot(frontNumber).isEmpty())
        {
            if (frontTick >= tickSpeed)
            {
                moved = updateFrontStack();
            }
            if (frontTick < tickSpeed) frontTick++;
            oldFrontTick = rFrontTick;
            rFrontTick = Utils.normalizeClamped((float) frontTick, 0, (float) tickSpeed);
            if (moved) return;
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
                moved = updateMiddleStack();
            }
            if (middleTick < tickSpeed) middleTick++;
            oldMiddleTick = rMiddleTick;
            rMiddleTick = Utils.normalizeClamped((float) middleTick, 0, (float) tickSpeed);
            if (moved) return;
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
            rBackTick = Utils.normalizeClamped((float) backTick, 0, (float) tickSpeed);
        }
    }

    public void itemReceived(int slot)
    {
        if (slot == frontNumber)
        {
            frontTick = 0;
            oldFrontTick = 0;
            rFrontTick = 0;
        } else if (slot == middleNumber)
        {
            middleTick = 0;
            oldMiddleTick = 0;
            rMiddleTick = 0;
        } else if (slot == backNumber)
        {
            backTick = 0;
            oldBackTick = 0;
            rMiddleTick = 0;
        }
        sync();
    }

    private boolean updateFrontStack()
    {
        if (world.isRemote) return false;
        ItemStack frontPositionItem = inventory.getStackInSlot(frontNumber);
        BlockState ownState = getBlockState();
        if (!(ownState.getBlock() instanceof BlockBulkConveyor)) return false;

        Direction facing = getBlockFacing();
        if (!frontPositionItem.isEmpty())
        {
            BlockPos frontPos = pos.offset(facing);
            int mode = ownState.get(BlockBulkConveyor.MODE);
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
            } else if (world.getBlockState(frontPos).getMaterial() == Material.AIR)
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

    private void updateBackStack()
    {
        if (world.isRemote) return;
        ItemStack backPositionItem = inventory.getStackInSlot(backNumber);
        ItemStack MiddlePositionItem = inventory.getStackInSlot(middleNumber);
        if (!backPositionItem.isEmpty() && MiddlePositionItem.isEmpty())
        {
            moveItemInternally(backNumber, middleNumber);
        }
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

    public float getMinYOffset(int slot, int mode)
    {
        switch (slot)
        {
            default:
            case 2:
                return mode == 0 ? 0.60f : 1.1f;
            case 1:
                if (mode == 1) return 0.78f;
                if (mode == 2) return 1.43f;
                return 0.61f;
            case 0:
                if (mode == 1) return 0.43f;
                if (mode == 2) return 1.78f;
                return 0.60f;
        }
    }

    public float getMaxYOffset(int mode)
    {
        if (mode == 0) return 0;
        else if (mode == 1) return 0.46f;
        return -0.46f;
    }

    private BlockPos frontConveyor(Direction facing, int mode)
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
        BlockState frontState = world.getBlockState(frontPos);
        return (frontState.getBlock() instanceof BlockBulkConveyor
                && frontState.get(BlockBulkConveyor.FACING) == getBlockFacing())
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

    public void dropFrontItem(Direction facing, ItemStack frontPositionItem, BlockPos frontPos)
    {
        double multiplierX = BlockBulkConveyor.getMotionX(facing);
        double multiplierZ = BlockBulkConveyor.getMotionZ(facing);
        ItemEntity entityitem = new ItemEntity(world, frontPos.getX() + 0.5D, frontPos.getY() + 0.5D, frontPos.getZ() + 0.5D, frontPositionItem);
        entityitem.setMotion(multiplierX * 0.2f, 0, multiplierZ * 0.2f);
        world.addEntity(entityitem);
        inventory.setStackInSlot(2, ItemStack.EMPTY);
    }

    public void dropInventory()
    {
        Utils.dropInventoryItems(world, pos, inventory);
    }

    public int getMode()
    {
        BlockState state = getBlockState();
        return state.getBlock() instanceof BlockBulkConveyor ? state.get(BlockBulkConveyor.MODE) : 0;
    }

    public ItemStack getStackInSlot(int slot)
    {
        return inventory.getStackInSlot(slot).copy();
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (facing == getBlockFacing().getOpposite() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> inventory).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        frontTick = compound.getInt("rFront");
        middleTick = compound.getInt("rMiddle");
        backTick = compound.getInt("rBack");
        rFrontTick = compound.getFloat("oFront");
        rMiddleTick = compound.getFloat("oMiddle");
        rBackTick = compound.getFloat("oBack");

        this.inventory.deserializeNBT(compound.getCompound("inv"));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putInt("rFront", frontTick);
        compound.putInt("rMiddle", middleTick);
        compound.putInt("rBack", backTick);

        compound.putFloat("oFront", rFrontTick);
        compound.putFloat("oMiddle", rMiddleTick);
        compound.putFloat("oBack", rBackTick);

        compound.put("inv", this.inventory.serializeNBT());
        return super.write(compound);
    }

    public Direction getBlockFacing()
    {
        if (facing != null) return facing;
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockBulkConveyor)
            return facing = state.get(BlockBulkConveyor.FACING);
        return Direction.NORTH;
    }

    public void setFacing(Direction facing)
    {
        this.facing = facing;
    }

    @Override
    public double getMaxRenderDistanceSquared()
    {
        return super.getMaxRenderDistanceSquared() * IRConfig.Render.conveyorsItemsRenderMult.get();
    }
}
