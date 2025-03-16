package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockWindTurbineHead;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.item.ItemWindBlade;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.cassiokf.industrialrenewal.util.capability.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class BlockEntityWindTurbineHead extends BlockEntitySyncable {
    private float rotation;
    private float oldRotation = -1f;
    private int energyGenerated;
    
    public static int energyGeneration = 128;
    private int tickToDamage;
    
    public BlockEntityWindTurbineHead(BlockPos pos, BlockState state) {
        super(ModBlockEntity.WIND_TURBINE_TILE.get(), pos, state);
    }
    
    private static final CustomEnergyStorage energyStorage = new CustomEnergyStorage(energyGeneration, energyGeneration, energyGeneration).noReceive().noExtraction();
    
    private final LazyOptional<IItemHandler> bladeInv = LazyOptional.of(this::createHandler);
    private static final LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(() -> energyStorage);
    
    private IItemHandler createHandler() {
        return new CustomItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() instanceof ItemWindBlade;
            }
            
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntityWindTurbineHead.this.sync();
            }
        };
    }
    
    public void dropContents() {
        if (level == null) return;
        bladeInv.ifPresent(inv -> {
            Block.popResource(level, worldPosition, inv.getStackInSlot(0));
        });
    }
    
    public void tick() {
        if (level == null) return;
        
        if (!level.isClientSide()) {
            handleServerSideTick();
        } else {
            handleClientSideTick();
        }
    }
    
    private void handleServerSideTick() {
        if (hasBlade()) {
            generateEnergy();
            handleBladeDamage();
        } else {
            energyGenerated = 0;
        }
    }
    
    private void generateEnergy() {
        int energyGen = Math.round(energyGeneration * getEfficiency());
        if (energyGen > 0) {
            BlockEntity te = level.getBlockEntity(worldPosition.below());
            if (te != null) {
                IEnergyStorage downE = te.getCapability(ForgeCapabilities.ENERGY, Direction.UP).orElse(null);
                if (downE != null && downE.canReceive()) {
                    downE.receiveEnergy(energyGen, false);
                }
            }
        }
    }
    
    private void handleBladeDamage() {
        if (++tickToDamage >= 1200 && energyGenerated > 0) {
            tickToDamage = 0;
            bladeInv.ifPresent(inv -> {
                ItemStack stack = inv.getStackInSlot(0);
                if (!stack.isEmpty() && stack.hurt(1, this.getLevel().random, null)) {
                    stack.shrink(1);
                }
            });
        }
    }
    
    private void handleClientSideTick() {
        if (hasBlade()) {
            oldRotation = rotation;
            rotation += 6f * Math.max(0.1f, getEfficiency());
            if (rotation >= 360f) {
                rotation -= 360f;
                oldRotation -= 360f;
            }
        }
    }
    
    public IItemHandler getBladeHandler() {
        return bladeInv.orElse(null);
    }
    
    public float getRotation() {
        return rotation;
    }
    
    public float getOldRotation() {
        return oldRotation;
    }
    
    public boolean hasBlade() {
        if (bladeInv.isPresent()) {
            IItemHandler iItemHandler = bladeInv.orElse(null);
            return !iItemHandler.getStackInSlot(0).isEmpty();
        }
        return false;
    }
    
    private float getEfficiency() {
        if (level == null) return 0;
        float weatherModifier;
        if (level.isThundering()) {
            weatherModifier = 2f;
        } else if (level.isRaining()) {
            weatherModifier = 1.5f;
        } else {
            weatherModifier = 1f;
        }
        
        float heightModifier;
        float posMin = -2040f;
        if (worldPosition.getY() - 62 <= 0) heightModifier = 0.1f;
        else heightModifier = (worldPosition.getY() - posMin) / (255 - posMin);
        heightModifier = Mth.clamp(heightModifier, 0, 1);
        
        return weatherModifier * heightModifier;
    }
    
    public Direction getBlockFacing() {
        return getBlockState().getValue(BlockWindTurbineHead.FACING);
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) return bladeInv.cast();
        if (capability == ForgeCapabilities.ENERGY && facing == Direction.DOWN) return energyStorageHandler.cast();
        return super.getCapability(capability, facing);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        bladeInv.ifPresent(h -> {
            CompoundTag tag = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            compoundTag.put("inv", tag);
        });
        compoundTag.putInt("generation", this.energyGenerated);
        //        compoundTag.putFloat("rotation", this.rotation);
        compoundTag.putInt("damageTick", tickToDamage);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        CompoundTag invTag = compoundTag.getCompound("inv");
        bladeInv.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag));
        energyGenerated = compoundTag.getInt("generation");
        //        rotation = compoundTag.getFloat("rotation");
        tickToDamage = compoundTag.getInt("damageTick");
        super.load(compoundTag);
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
