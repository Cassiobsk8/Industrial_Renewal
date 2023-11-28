package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockEnergyLevel;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockEntityEnergyLevel extends BlockEntitySyncable {
    private Direction indicatorHorizontalFacing;
    private Direction baseFacing;
    private IEnergyStorage energyStorage;
    
    public BlockEntityEnergyLevel(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ENERGY_LEVEL.get(), pos, state);
    }
    
    public String GetText() {
        if (getEnergyStorage() != null) {
            int energy = energyStorage.getEnergyStored();
            String text = energy + "";
            if (energy >= 1000 && energy < 1000000) text = energy / 1000 + "K";
            if (energy >= 1000000) text = energy / 1000000 + "M";
            
            int energy2 = energyStorage.getMaxEnergyStored();
            String textM = energy2 + " FE";
            if (energy >= 1000 && energy2 < 1000000) textM = energy2 / 1000 + "K FE";
            if (energy2 >= 1000000) textM = energy2 / 1000000 + "M FE";
            
            return text + " / " + textM;
        } else return "No Battery";
    }
    
    public Direction getGaugeFacing() {
        if (indicatorHorizontalFacing != null) return indicatorHorizontalFacing;
        return forceIndicatorCheck();
    }
    
    public Direction forceIndicatorCheck() {
        indicatorHorizontalFacing = this.level.getBlockState(worldPosition).getValue(BlockEnergyLevel.GAUGE);
        return indicatorHorizontalFacing;
    }
    
    public Direction getBaseFacing() {
        if (baseFacing != null) return baseFacing;
        BlockState state = level.getBlockState(worldPosition);
        if (state.getBlock() instanceof BlockEnergyLevel) baseFacing = state.getValue(BlockEnergyLevel.FACING);
        return baseFacing;
    }
    
    public void setBaseFacing(Direction baseFacing) {
        this.baseFacing = baseFacing;
    }
    
    public float getTankFill() //0 ~ 180
    {
        if (getEnergyStorage() != null) {
            float currentAmount = energyStorage.getEnergyStored();
            float totalCapacity = energyStorage.getMaxEnergyStored();
            currentAmount = currentAmount / totalCapacity;
            return currentAmount;
        }
        return 0f;
    }
    
    private IEnergyStorage getEnergyStorage() {
        if (energyStorage != null) return energyStorage;
        return forceCheck();
    }
    
    public IEnergyStorage forceCheck() {
        if (level == null) return null;
        BlockEntity te = level.getBlockEntity(worldPosition.relative(getBaseFacing()));
        if (te != null) {
            IEnergyStorage handler = te.getCapability(ForgeCapabilities.ENERGY, baseFacing.getOpposite()).orElse(null);
            if (handler != null) {
                energyStorage = handler;
                return energyStorage;
            }
        }
        energyStorage = null;
        this.sync();
        return null;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putBoolean("energy", energyStorage != null);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        if (!compoundTag.getBoolean("energy")) energyStorage = null;
        super.load(compoundTag);
    }
}
