package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModTags;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockEntitySteamTurbine extends BlockEntity3x3x3MachineBase<BlockEntitySteamTurbine> {
    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(100000, 10240, 10240).setBlockEntity(this).noExtraction().noReceive();
    //    private static final float volume = IRConfig.MainConfig.Sounds.turbineVolume * IRConfig.MainConfig.Sounds.masterVolumeMult;
    private final LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyContainer);
    private final CustomFluidTank waterTank = new CustomFluidTank(32000).setBlockEntity(this).noFill();
    private final FluidStack waterStack = new FluidStack(Fluids.WATER, Utils.BUCKET_VOLUME);
    
    private int rotation;
    private int oldRotation;
    private static final int maxRotation = 16000;
    private static final int energyPerTick = 512;//IRConfig.MainConfig.Main.steamTurbineEnergyPerTick;
    private static final int steamPerTick = 250;//IRConfig.MainConfig.Main.steamTurbineSteamPerTick;
    private final CustomFluidTank steamTank = new CustomFluidTank(steamPerTick*2) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            //TODO MAKE STEAM TAG
            return stack != null && stack.getFluid().defaultFluidState().is(ModTags.Fluids.STEAM_TAG);
        }
    }.setBlockEntity(this).noDrain();
    private float steamReceivedNorm = 0f;
    
    public BlockEntitySteamTurbine(BlockPos pos, BlockState state)
    {
        super(ModBlockEntity.STEAM_TURBINE_TILE.get(), pos, state);
    }
    
    public void tick() {
        if (this.isMaster()) {
            if (!level.isClientSide()) {
                steamToRotation();
                
                generateEnergyBasedOnRotation();
                
                extractEnergy();
                
                extractWater();
                
                if (oldRotation != rotation) {
                    oldRotation = rotation;
                    this.sync();
                }
            } else {
//                updateSound();
            }
        }
    }
    
    public void steamToRotation() {
        int torqueSum;
        int steamAmount;
        
        if (rotation > 15990) {
            steamAmount = steamPerTick;//Utils.roundtoInteger(steamPerTick * ((maxRotation - rotation) / 10)); //Throttle Governing of Steam Turbine
        } else steamAmount = steamPerTick;
        
        FluidStack fluidStack = steamTank.drainInternal(steamAmount, IFluidHandler.FluidAction.EXECUTE);
        
        if (fluidStack == null) {
            return;
        }
        
        steamReceivedNorm = Utils.normalizeClamped(fluidStack.getAmount(), 0, steamPerTick);
        torqueSum = (int) (steamReceivedNorm * 10);
        waterStack.setAmount(Utils.roundtoInteger((fluidStack.getAmount() / 5f) * 0.98f));
        waterTank.fillInternal(waterStack, IFluidHandler.FluidAction.EXECUTE);
        rotation += torqueSum;
    }
    
    private void generateEnergyBasedOnRotation() {
        if (rotation >= 6000) {
            int energy = getEnergyProduction();
            energyContainer.addEnergy(energy, false);
            load(energy);
        }
        rotationDecay();
        rotation = Mth.clamp(rotation, 0, maxRotation);
    }
    
    private void extractEnergy() {
        Direction facing = getMasterFacing();
        BlockEntity eTE = level.getBlockEntity(worldPosition.relative(facing.getOpposite()).below().relative(facing.getCounterClockWise(), 2));
        if (eTE != null && energyContainer.getEnergyStored() > 0) {
            IEnergyStorage upTank = eTE.getCapability(ForgeCapabilities.ENERGY, facing.getClockWise()).orElse(null);
            if (upTank != null)
                energyContainer.subtractEnergy(upTank.receiveEnergy(energyContainer.subtractEnergy(10240, true), false), false);
        }
    }
    
    private void extractWater() {
        Direction facing = getMasterFacing();
        BlockEntity wTE = level.getBlockEntity(worldPosition.relative(facing, 2).below());
        if (wTE != null && waterTank.getFluidAmount() > 0) {
            IFluidHandler wTank = wTE.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite()).orElse(null);
            if (wTank != null) waterTank.drain(wTank.fill(waterTank.drain(2000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        }
    }
    
//    @Override
//    public float getPitch() {
//        return Math.max(getRotation(), 0.1F);
//    }
    
//    @Override
//    public float getVolume() {
//        return volume;
//    }
    
//    private void updateSound() {
//        if (!world.isRemote) return;
//        if (this.rotation > 0) {
//            IRSoundHandler.playRepeatableSound(IRSoundRegister.MOTOR_ROTATION_RESOURCEL, volume, getPitch(), pos);
//        } else {
//            IRSoundHandler.stopTileSound(pos);
//        }
//    }
    
    @Override
    public void onMasterBreak() {
//        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
    }
    
    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntitySteamTurbine;
    }
    
    private int getEnergyProduction() {
        int energy = Math.round(energyPerTick * getRotation());
        int remainingEnergy = this.energyContainer.getMaxEnergyStored() - this.energyContainer.getEnergyStored();
        float factor = this.waterTank.getFluidAmount() == 0 ? 1f : Math.max(0.5f, Math.min(1f, ((float) this.waterTank.getCapacity() / (float) this.waterTank.getFluidAmount()) - 0.5f));
        energy = Utils.roundtoInteger(energy * factor);
        if (remainingEnergy < energy) { //Throttles the current when the buffer starts to be full
            energy = remainingEnergy;
        }
        energy = Mth.clamp(energy, 0, energyPerTick);
        return energy;
    }
    
    private void load(int load) //Slows down the steam turbine depending on the applied load
    {
        int torque;
        float loadFactor = Utils.normalizeClamped(load, 0, energyPerTick);
        if (loadFactor > 0f) {
            torque = (int) (1 + loadFactor * 6);
        } else torque = 0;
        rotation -= torque;
    }
    
    private void rotationDecay() {
        int decay;
        if (rotation < 6000) decay = 3;
        else decay = (int) (1 + getRotation() * 2);
        rotation -= decay;
    }
    
    public String getWaterText() {
        return Utils.WATER_NAME;
    }
    
    public String getSteamText() {
        return Utils.STEAM_NAME;
    }
    
    public String getGenerationText() {
        int energy = (rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored()) ? getEnergyProduction() : 0;
        return Utils.formatEnergyString(energy) + "/t";
    }
    
    public String getEnergyText() {
        int energy = this.energyContainer.getEnergyStored();
        return Utils.formatEnergyString(energy);
    }
    
    public String getRotationText() {
        return (rotation / 10) + " rpm";
    }
    
    public float getEnergyFill() //0 ~ 1
    {
        return Utils.normalizeClamped(energyContainer.getEnergyStored(), 0, energyContainer.getMaxEnergyStored());
    }
    
    private float getRotation() {
        return Utils.normalizeClamped(this.rotation, 0, maxRotation);
    }
    
    public float getGenerationFill() //0 ~ 180
    {
        float currentAmount = ((rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored()) ? getEnergyProduction() : 0);
        return Utils.normalizeClamped(currentAmount, 0, energyPerTick) * 90f;
    }
    
    public float getWaterFill() //0 ~ 180
    {
        return Utils.normalizeClamped(waterTank.getFluidAmount(), 0, waterTank.getCapacity()) * 180f;
    }
    
    public float getSteamFill() //0 ~ 180
    {
        return steamReceivedNorm * 180f;
    }
    
    public float getRotationFill() //0 ~ 180
    {
        return getRotation() * 140f;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        CompoundTag waterTag = new CompoundTag();
        this.waterTank.writeToNBT(waterTag);
        compoundTag.put("water", waterTag);
        compoundTag.putInt("energy", energyContainer.getEnergyStored());
        compoundTag.putInt("heat", rotation);
        compoundTag.putFloat("steamOnTick", steamReceivedNorm);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compound) {
        CompoundTag waterTag = compound.getCompound("water");
        this.waterTank.readFromNBT(waterTag);
        energyContainer.setEnergy(compound.getInt("energy"));
        this.rotation = compound.getInt("heat");
        this.steamReceivedNorm = compound.getFloat("steamOnTick");
        super.load(compound);
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, @Nullable final Direction facing) {
        BlockEntitySteamTurbine masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getBlockPos();
        
        if (facing == null)
            return super.getCapability(capability, facing);
        
        if (facing == Direction.UP && worldPosition.equals(masterPos.above())
                && capability == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(() -> masterTE.steamTank).cast();
        if (facing == face && worldPosition.equals(masterPos.below().relative(face))
                && capability == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (facing == face.getCounterClockWise()
                && worldPosition.equals(masterPos.below().relative(face.getOpposite()).relative(face.getCounterClockWise()))
                && capability == ForgeCapabilities.ENERGY)
            return masterTE.energyStorageHandler.cast();
        
        return super.getCapability(capability, facing);
    }
}
