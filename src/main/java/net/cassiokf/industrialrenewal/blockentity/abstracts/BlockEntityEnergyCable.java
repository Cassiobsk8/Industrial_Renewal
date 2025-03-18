package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.cassiokf.industrialrenewal.obj.CapResult;
import net.cassiokf.industrialrenewal.util.PipeUtils;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class BlockEntityEnergyCable extends BlockEntityMultiBlocksTube<BlockEntityEnergyCable> implements ICapabilityProvider {
    
    private int averageEnergy;
    private int potentialEnergy;
    public final CustomEnergyStorage energyStorage = new CustomEnergyStorage(getMaxEnergyToTransport(), getMaxEnergyToTransport(), getMaxEnergyToTransport()) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (maxReceive <= 0) return 0;
            return onEnergyReceived(maxReceive, simulate);
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }
    };
    public final LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(() -> energyStorage);
    private int oldPotential = -1;
    private int oldEnergy;
    private int tick;
    
    
    public BlockEntityEnergyCable(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state);
    }
    
    //    @Override
    //    public boolean instanceOf(BlockEntity te) {
    //        return te instanceof BlockEntityEnergyCable;
    //    }
    
    
    @Override
    public void doTick() {
        if (level != null && !level.isClientSide && isMaster()) {
            if (tick >= 10) {
                tick = 0;
                averageEnergy = outPut / 10;
                outPut = 0;
                if (averageEnergy != oldEnergy || potentialEnergy != oldPotential) {
                    oldPotential = potentialEnergy;
                    oldEnergy = averageEnergy;
                    sync();
                }
            }
            tick++;
        }
    }
    
    public int onEnergyReceived(int energy, boolean simulate) {
        if (!isMaster() || level.isClientSide) return 0;
        
        if (inUse) return 0; //to prevent stack overflow (IE)
        if (energy <= 0) {
            return 0;
        }
        inUse = true;
        int maxTransfer = Math.min(energy, getMaxEnergyToTransport());
        if (!simulate) potentialEnergy = maxTransfer;
        CapResult result = PipeUtils.moveEnergy(this, maxTransfer, getMaxEnergyToTransport(), simulate);
        if (!simulate) outPut += result.getOutPut();
        outPutCount = result.getValidReceivers();
        inUse = false;
        return result.getOutPut();
    }
    
    @Override
    public void checkForOutPuts(BlockPos bPos) {
        if (level == null) return;
        if (level.isClientSide) return;
        for (Direction face : Direction.values()) {
            BlockPos currentPos = worldPosition.relative(face);
            BlockEntity te = level.getBlockEntity(currentPos);
            boolean hasMachine = te != null && !(te instanceof BlockEntityEnergyCable) && te.getCapability(ForgeCapabilities.ENERGY, face.getOpposite()).isPresent();
            
            if (hasMachine && te.getCapability(ForgeCapabilities.ENERGY, face.getOpposite()).orElse(null).canReceive())
                if (!isMasterInvalid()) getMaster().addReceiver(te, face);
                else if (!isMasterInvalid()) getMaster().removeReceiver(te);
        }
        getMaster().cleanReceiversContainer();
    }
    
    @Override
    public void invalidateCaps() {
        energyStorageHandler.invalidate();
        super.invalidateCaps();
    }
    
    public abstract int getMaxEnergyToTransport();
    
    public String GetText() {
        return Utils.formatPreciseEnergyString(getMaster().averageEnergy);
    }
    
    public float getOutPutAngle() {
        return Utils.normalizeClamped(getMaster().averageEnergy, 0, getMaxEnergyToTransport()) * 90;
    }
    
    public Direction getBlockFacing() {
        return Direction.NORTH;
    }
    
    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (facing == null) return super.getCapability(capability, facing);
        
        if (capability == ForgeCapabilities.ENERGY && getMaster() != null)
            return getMaster().energyStorageHandler.cast();
        return super.getCapability(capability, facing);
    }
    
    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", averageEnergy);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        averageEnergy = compoundTag.getInt("energy");
        super.load(compoundTag);
    }
}
