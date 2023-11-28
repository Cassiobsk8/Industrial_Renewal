package net.cassiokf.industrialrenewal.blockentity.dam;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDamGenerator extends BlockEntity3x3x3MachineBase<BlockEntityDamGenerator> {

    private final int energyCapacity = 10240;
    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(energyCapacity){
        @Override
        public void onEnergyChange() {
            BlockEntityDamGenerator.this.sync();
        }
        
        @Override
        public boolean canReceive() {
            return false;
        }
    };
    public LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional.of(()-> energyStorage);
    private int oldGeneration;
    private int generation;
    private int rotation;

    public static final int maxGeneration = 1024;//IRConfig.MainConfig.Main.damGeneratorEnergyPerTick;
    public static final int transferRate = 1024;


    public BlockEntityDamGenerator(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DAM_GENERATOR.get(), pos, state);
    }

    public void tick() {
        if(!level.isClientSide && isMaster()){
            chargeInternalBattery();
            passEnergyOut();
            if (generation != oldGeneration)
            {
                oldGeneration = generation;
                sync();
            }
            rotation = 0;
        }
    }
    
    public void chargeInternalBattery() {
        generation = (int) (Utils.normalizeClamped(rotation, 0, BlockEntityDamTurbine.MAX_PROCESSING) * maxGeneration);
        if (generation > 0)
            generation = energyStorage.addEnergy(generation, false);
        else generation = 0;
    }
    
    public void passEnergyOut() {
        BlockEntity te = level.getBlockEntity(worldPosition.above(2));
        if (te != null)
        {
            te.getCapability(ForgeCapabilities.ENERGY, Direction.DOWN).ifPresent(iEnergyStorage -> {
                int amount = iEnergyStorage.receiveEnergy(transferRate, true);
                iEnergyStorage.receiveEnergy(energyStorage.subtractEnergy(amount, false), false);
            });
        }
    }

    public void updateRotation(int newRotation){
        if(rotation != newRotation)
            rotation = newRotation;
    }

    public String getGenerationText()
    {
        return Utils.formatEnergyString(generation) + "/t";
    }

    public float getGenerationFill()
    {
        return Utils.normalizeClamped(generation, 0, maxGeneration) * 90;
    }

    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityDamGenerator;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        compoundTag.putInt("rotation", rotation);
        compoundTag.putInt("generation", generation);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        rotation = compoundTag.getInt("rotation");
        generation = compoundTag.getInt("generation");
        super.load(compoundTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockEntityDamGenerator masterTE = getMaster();
        if (masterTE == null || side == null) return super.getCapability(cap, side);

        if(cap == ForgeCapabilities.ENERGY &&
                side == Direction.UP &&
                worldPosition.equals(masterTE.getBlockPos().above()))
            return getMaster().energyHandler.cast();
        return super.getCapability(cap, side);
    }
}
