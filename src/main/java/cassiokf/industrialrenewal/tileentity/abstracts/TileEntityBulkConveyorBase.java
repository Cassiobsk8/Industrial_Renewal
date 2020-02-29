package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.blocks.BlockBulkConveyor;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class TileEntityBulkConveyorBase extends TileEntitySyncable implements ICapabilityProvider, ITickableTileEntity
{
    public double stack3Pos;
    public double stack2Pos;
    public double stack1Pos;
    public double stack3YPos;
    public double stack2YPos;
    public double stack1YPos;
    private int tick;
    private boolean getInThisTick;

    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

    public TileEntityBulkConveyorBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory)
    {
        for (int i = 0; i < 3; ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                Utils.spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(3)
        {
            @Override
            public int getSlots()
            {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityBulkConveyorBase.this.Sync();
                if (slot == 0)
                {
                    TileEntityBulkConveyorBase.this.getInThisTick = true;
                }
            }
        };
    }

    @Override
    public void tick()
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
        ItemStack frontPositionItem = inventory.orElse(null).getStackInSlot(2);
        BlockState ownState = world.getBlockState(pos);
        if (!(ownState.getBlock() instanceof BlockBulkConveyor)) return;

        Direction facing = ownState.get(BlockBulkConveyor.FACING);
        if (!frontPositionItem.isEmpty())
        {
            BlockPos frontPos = pos.offset(facing);
            int mode = ownState.get(BlockBulkConveyor.MODE);
            BlockPos targetConveyorPos = frontConveyor(facing, mode);
            if (targetConveyorPos != null)
            {
                TileEntityBulkConveyorBase te = null;
                if (world.getTileEntity(targetConveyorPos) instanceof TileEntityBulkConveyorBase)
                {
                    te = (TileEntityBulkConveyorBase) world.getTileEntity(targetConveyorPos);
                }

                if (te != null)
                {
                    if (te.getBlockFacing() == getBlockFacing() && te.transferItem(frontPositionItem, false))
                    { // IF IS STRAIGHT
                        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
                        frontPositionItem = ItemStack.EMPTY;
                    } else if (te.getBlockFacing() != getBlockFacing().getOpposite() && te.getStackInSlot(1).isEmpty())
                    { // IF IS CORNER
                        if (te.transferItem(frontPositionItem, 1, false))
                        {
                            inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
                            frontPositionItem = ItemStack.EMPTY;
                        }
                    }
                }
            } else if (world.getBlockState(frontPos).getBlock().isAir(world.getBlockState(frontPos), world, frontPos))
            {
                if (dropFrontItem(facing, frontPositionItem, frontPos))
                {
                    frontPositionItem = ItemStack.EMPTY;
                }
            }
        }
        ItemStack MiddlePositionItem = inventory.orElse(null).getStackInSlot(1);
        if (frontPositionItem.isEmpty() && !MiddlePositionItem.isEmpty())
        {
            moveItemInternaly(1, 2);
            MiddlePositionItem = ItemStack.EMPTY;
        }
        ItemStack backPositionItem = inventory.orElse(null).getStackInSlot(0);
        if (!backPositionItem.isEmpty() && MiddlePositionItem.isEmpty() && !getInThisTick)
        {
            moveItemInternaly(0, 1);
        }
        getInThisTick = false;
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

    private void moveItemInternaly(int from, int to)
    {
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(to, inventory.orElse(null).getStackInSlot(from)));
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(from, ItemStack.EMPTY));
    }

    public IItemHandler getInventory()
    {
        return inventory.orElse(null);
    }

    public boolean transferItem(ItemStack stack, boolean simulate)
    {
        return transferItem(stack, 0, simulate);
    }

    public boolean transferItem(ItemStack stack, int slot, boolean simulate)
    {
        if (inventory.orElse(null).getStackInSlot(0).isEmpty() && !stack.isEmpty())
        {
            if (!simulate) inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(slot, stack));
            getInThisTick = true;
            Sync();
            return true;
        }
        return false;
    }

    public boolean dropFrontItem(Direction facing, ItemStack frontPositionItem, BlockPos frontPos)
    {
        double multiplierX = BlockBulkConveyor.getMotionX(facing);
        double multiplierZ = BlockBulkConveyor.getMotionZ(facing);
        ItemEntity entityitem = new ItemEntity(world, frontPos.getX() + 0.5D, frontPos.getY() + 0.5D, frontPos.getZ() + 0.5D, frontPositionItem);
        entityitem.setVelocity(multiplierX * 0.2, 0, multiplierZ * 0.2);
        world.addEntity(entityitem);
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
        return true;
    }

    public void dropInventory()
    {
        dropInventoryItems(world, pos, inventory.orElse(null));
    }

    public int getMode()
    {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockBulkConveyor ? state.get(BlockBulkConveyor.MODE) : 0;
    }

    public ItemStack getStackInSlot(int slot)
    {
        return inventory.orElse(null).getStackInSlot(slot).copy();
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (facing == getBlockFacing().getOpposite() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        inventory.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.write(compound);
    }

    public Direction getBlockFacing()
    {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof BlockBulkConveyor) return state.get(BlockBulkConveyor.FACING);
        return Direction.NORTH;
    }
}
