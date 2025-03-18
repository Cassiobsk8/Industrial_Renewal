package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.block.transport.BlockConveyor;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomItemStackHandler;
import net.cassiokf.industrialrenewal.util.enums.EnumConveyorType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockEntityConveyorBase extends BlockEntitySyncable implements ICapabilityProvider {
    
    public double stack3Pos;
    public double stack2Pos;
    public double stack1Pos;
    public double stack3YPos;
    public double stack2YPos;
    public double stack1YPos;
    private int tick;
    private int tick2;
    private boolean getInThisTick;
    
    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
    private final LazyOptional<IItemHandler> hopperInv = LazyOptional.of(this::createHopperHandler);
    
    public BlockEntityConveyorBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public static void dropInventoryItems(Level worldIn, BlockPos pos, IItemHandler inventory) {
        if (inventory == null) return;
        for (int i = 0; i < 3; ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            
            if (!itemstack.isEmpty()) {
                Block.popResource(worldIn, pos, itemstack);
                //                Utils.spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }
    
    private IItemHandler createHandler() {
        return new CustomItemStackHandler(3) {
            @Override
            public int getSlots() {
                return 1;
            }
            
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntityConveyorBase.this.sync();
                if (slot == 0) {
                    BlockEntityConveyorBase.this.getInThisTick = true;
                }
            }
        };
    }
    
    private IItemHandler createHopperHandler() {
        return new CustomItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntityConveyorBase.this.sync();
            }
        };
    }
    
    public void tickConveyor(int speed) {
        if (level == null) return;
        if (level.isClientSide) {
            doAnimation();
        }
        
        if (tick == 1) getInThisTick = false;
        if (tick % speed == 0) {
            tick = 0;
            if (!level.isClientSide) {
                moveItem();
            }
        }
        tick++;
    }
    
    public void tickHopper(int count) {
        if (!level.isClientSide) {
            if (tick2 % 8 == 0) {
                tick2 = 0;
                if (!getInvAbove(count)) getEntityItemAbove();
                hopperToConveyor();
            }
            tick2++;
        }
    }
    
    public void tickInserter() {
        if (level == null) return;
        if (!level.isClientSide) {
            insertItem();
        }
    }
    
    private void doAnimation() {
        ItemStack stack1 = getStackInSlot(0);
        ItemStack stack2 = getStackInSlot(1);
        ItemStack stack3 = getStackInSlot(2);
        int mode = getMode();
        float yPos;
        
        float speed = 0.33f / 4;
        if (!stack3.isEmpty()) {
            //            stack3Pos = Utils.lerp(stack3Pos, 0.99D, speed);
            stack3Pos += speed;
            stack3Pos = Mth.clamp(stack3Pos, 0.66f, 1f);
            yPos = mode == 0 ? 0.47f : mode == 1 ? 1.3f : 0.65f;
            if (mode == 0) stack3YPos = yPos;
            else stack3YPos = Utils.lerp(stack3YPos, yPos, speed);
        } else {
            stack3Pos = 0.66f;
            stack3YPos = mode == 0 ? 0.47f : 0.97f;
        }
        if (!stack2.isEmpty()) {
            //            stack2Pos = Utils.lerp(stack2Pos, 0.66D, speed);
            stack2Pos += speed;
            stack2Pos = Mth.clamp(stack2Pos, 0.33f, 0.66f);
            yPos = mode == 0 ? 0.47f : 0.97f;
            if (mode == 0) stack2YPos = yPos;
            else stack2YPos = Utils.lerp(stack2YPos, yPos, speed);
        } else {
            stack2Pos = 0.33f;
            if (mode == 1) stack2YPos = 0.65f;
            if (mode == 2) stack2YPos = 1.3f;
        }
        if (!stack1.isEmpty()) {
            //            stack1Pos = Utils.lerp(stack1Pos, 0.33D, speed);
            stack1Pos += speed;
            stack1Pos = Mth.clamp(stack1Pos, 0, 0.33f);
            yPos = mode == 0 ? 0.47f : mode == 1 ? 0.65f : 1.3f;
            if (mode == 0) stack1YPos = yPos;
            else stack1YPos = Utils.lerp(stack1YPos, yPos, speed);
        } else {
            stack1Pos = 0f;
            if (mode == 1) stack1YPos = 0.3f;
            if (mode == 2) stack1YPos = 1.65f;
        }
    }
    
    private void moveItem() {
        if (level == null) return;
        ItemStack frontPositionItem = inventory.orElse(null).getStackInSlot(2);
        BlockState ownState = level.getBlockState(worldPosition);
        if (!(ownState.getBlock() instanceof BlockConveyor)) return;
        
        Direction facing = ownState.getValue(BlockConveyor.FACING);
        if (!frontPositionItem.isEmpty()) {
            BlockPos frontPos = worldPosition.relative(facing);
            int mode = ownState.getValue(BlockConveyor.MODE);
            BlockPos targetConveyorPos = frontConveyor(facing, mode);
            if (targetConveyorPos != null) {
                BlockEntityConveyorBase te = null;
                if (level.getBlockEntity(targetConveyorPos) instanceof BlockEntityConveyorBase) {
                    te = (BlockEntityConveyorBase) level.getBlockEntity(targetConveyorPos);
                }
                
                if (te != null) {
                    if (te.getBlockFacing() == getBlockFacing() && te.transferItem(frontPositionItem, false)) { // IF IS STRAIGHT
                        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
                        frontPositionItem = ItemStack.EMPTY;
                    } else if (te.getBlockFacing() != getBlockFacing().getOpposite() && te.getStackInSlot(1).isEmpty()) { // IF IS CORNER
                        if (te.transferItem(frontPositionItem, 1, false)) {
                            inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
                            frontPositionItem = ItemStack.EMPTY;
                        }
                    }
                }
            } else if (level.getBlockState(frontPos).isAir()) {
                if (dropFrontItem(facing, frontPositionItem, frontPos)) {
                    frontPositionItem = ItemStack.EMPTY;
                }
            }
        }
        ItemStack MiddlePositionItem = inventory.orElse(null).getStackInSlot(1);
        if (frontPositionItem.isEmpty() && !MiddlePositionItem.isEmpty()) {
            moveItemInternaly(1, 2);
            MiddlePositionItem = ItemStack.EMPTY;
        }
        ItemStack backPositionItem = inventory.orElse(null).getStackInSlot(0);
        if (!backPositionItem.isEmpty() && MiddlePositionItem.isEmpty() && !getInThisTick) {
            moveItemInternaly(0, 1);
        }
        getInThisTick = false;
    }
    
    private void moveItemInternaly(int from, int to) {
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(to, inventory.orElse(null).getStackInSlot(from)));
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(from, ItemStack.EMPTY));
    }
    
    private BlockPos frontConveyor(Direction facing, int mode) {
        if (level == null) return null;
        BlockPos frontPos = worldPosition.relative(facing);
        if (mode == 1 || !(level.getBlockState(frontPos).getBlock() instanceof BlockConveyor)) {
            if (mode == 1) {
                frontPos = worldPosition.relative(facing).above();
            } else {
                frontPos = worldPosition.relative(facing).below();
            }
        } else {
            return frontPos;
        }
        BlockState frontState = level.getBlockState(frontPos);
        return (frontState.getBlock() instanceof BlockConveyor && frontState.getValue(BlockConveyor.FACING) == getBlockFacing()) ? frontPos : null;
    }
    
    public boolean transferItem(ItemStack stack, boolean simulate) {
        return transferItem(stack, 0, simulate);
    }
    
    public boolean transferItem(ItemStack stack, int slot, boolean simulate) {
        if (inventory.orElse(null).getStackInSlot(0).isEmpty() && !stack.isEmpty()) {
            if (!simulate) inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(slot, stack));
            getInThisTick = true;
            sync();
            return true;
        }
        return false;
    }
    
    public boolean dropFrontItem(Direction facing, ItemStack frontPositionItem, BlockPos frontPos) {
        if (level == null) return false;
        if (getBlockState().getValue(BlockConveyor.TYPE) == EnumConveyorType.INSERTER) return false;
        double multiplierX = BlockConveyor.getMotionX(facing);
        double multiplierZ = BlockConveyor.getMotionZ(facing);
        ItemEntity entityitem = new ItemEntity(level, frontPos.getX() + 0.5D, frontPos.getY() + 0.5D, frontPos.getZ() + 0.5D, frontPositionItem);
        entityitem.setDeltaMovement(multiplierX * 0.2, 0, multiplierZ * 0.2);
        level.addFreshEntity(entityitem);
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
        return true;
    }
    
    public void dropInventory() {
        dropInventoryItems(level, worldPosition, inventory.orElse(null));
    }
    
    @Override
    public void setRemoved() {
        dropInventory();
        super.setRemoved();
    }
    
    public Direction getBlockFacing() {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof BlockConveyor) return state.getValue(BlockConveyor.FACING);
        return Direction.NORTH;
    }
    
    public int getMode() {
        BlockState state = getBlockState();
        return state.getBlock() instanceof BlockConveyor ? state.getValue(BlockConveyor.MODE) : 0;
    }
    
    public ItemStack getStackInSlot(int slot) {
        return inventory.orElse(null).getStackInSlot(slot).copy();
    }
    
    public float getMinYOffset(int slot, int mode) {
        switch (slot) {
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
    
    public float getMaxYOffset(int mode) {
        if (mode == 0) return 0;
        else if (mode == 1) return 0.46f;
        return -0.46f;
    }
    
    private boolean getInvAbove(int count) {
        if (level == null) return false;
        IItemHandler hopper = hopperInv.orElse(null);
        if (hopperInv.isPresent() && hopper.getStackInSlot(0).isEmpty()) {
            BlockEntity te = level.getBlockEntity(worldPosition.above());
            if (te != null) {
                if (te.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).isPresent()) {
                    IItemHandler itemHandler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).orElse(null);
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack stack = itemHandler.extractItem(i, count, true);
                        if (hopperInv.isPresent()) {
                            IItemHandler itemHandler1 = hopperInv.orElse(null);
                            ItemStack left = itemHandler1.insertItem(0, stack, false);
                            if (!ItemStack.isSameItem(stack, left)) {
                                int toExtract = stack.getCount() - left.getCount();
                                itemHandler.extractItem(i, toExtract, false);
                                sync();
                                break;
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    private void hopperToConveyor() {
        IItemHandler iItemHandler = hopperInv.orElse(null);
        IItemHandler iItemHandler1 = inventory.orElse(null);
        
        if (hopperInv.isPresent() && inventory.isPresent() && !iItemHandler.getStackInSlot(0).isEmpty()) {
            ItemStack stack = iItemHandler.getStackInSlot(0).copy();
            ItemStack stack1 = iItemHandler1.insertItem(1, stack, false);
            iItemHandler.getStackInSlot(0).shrink(stack.getCount() - stack1.getCount());
        }
    }
    
    private void getEntityItemAbove() {
        if (level == null) return;
        IItemHandler itemHandler = hopperInv.orElse(null);
        if (hopperInv.isPresent() && itemHandler.getStackInSlot(0).isEmpty()) {
            BlockPos posAbove = worldPosition.above();
            List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, new AABB(posAbove.getX(), posAbove.getY(), posAbove.getZ(), posAbove.getX() + 2D, posAbove.getY() + 1D, posAbove.getZ() + 1D), EntitySelector.ENTITY_STILL_ALIVE);
            if (!list.isEmpty() && list.get(0) != null) {
                ItemEntity entityItem = list.get(0);
                ItemStack stack = entityItem.getItem().copy();
                ItemStack stack1 = itemHandler.insertItem(0, stack, false);
                if (stack1.isEmpty()) entityItem.discard();
                else entityItem.setItem(stack1);
            }
        }
    }
    
    
    private void insertItem() {
        if (level == null) return;
        if (!inventory.orElse(null).getStackInSlot(2).isEmpty()) {
            Direction facing = getBlockFacing();
            BlockEntity te = level.getBlockEntity(worldPosition.relative(facing));
            if (te != null) {
                IItemHandler itemHandler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).orElse(null);
                if (itemHandler != null) {
                    for (int j = 0; j < itemHandler.getSlots(); j++) {
                        ItemStack stack = inventory.orElse(null).extractItem(2, 64, true);
                        if (!stack.isEmpty() && itemHandler.isItemValid(j, stack)) {
                            ItemStack left = itemHandler.insertItem(j, stack, false);
                            if (!ItemStack.isSameItem(stack, left)) {
                                int toExtract = stack.getCount() - left.getCount();
                                inventory.orElse(null).extractItem(2, toExtract, false);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void dropContents() {
        if (level == null) return;
        inventory.ifPresent(inv -> {
            for (int i = 0; i < inv.getSlots(); i++) {
                Block.popResource(level, worldPosition, inv.getStackInSlot(i));
            }
        });
        hopperInv.ifPresent(inv -> {
            for (int i = 0; i < inv.getSlots(); i++) {
                Block.popResource(level, worldPosition, inv.getStackInSlot(i));
            }
        });
    }
    
    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (facing == null) return super.getCapability(capability, facing);
        
        if (capability.equals(ForgeCapabilities.ITEM_HANDLER) && facing != Direction.DOWN) return hopperInv.cast();
        
        if (facing == getBlockFacing().getOpposite() && capability == ForgeCapabilities.ITEM_HANDLER)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        inventory.ifPresent(h -> {
            CompoundTag tag = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            compoundTag.put("inv", tag);
        });
        hopperInv.ifPresent(h -> {
            CompoundTag tag = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            compoundTag.put("inv2", tag);
        });
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        CompoundTag invTag = compoundTag.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag));
        CompoundTag invTag2 = compoundTag.getCompound("inv2");
        hopperInv.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag2));
        super.load(compoundTag);
    }
}
