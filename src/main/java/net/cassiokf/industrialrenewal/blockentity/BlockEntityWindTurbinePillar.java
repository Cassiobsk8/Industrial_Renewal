package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockWindTurbinePillar;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.obj.CapResult;
import net.cassiokf.industrialrenewal.util.PipeUtils;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockEntityWindTurbinePillar extends BlockEntityMultiBlocksTube<BlockEntityWindTurbinePillar> implements ICapabilityProvider {
    
    private static final Direction[] FACES_TO_CHECK = new Direction[]{Direction.UP, Direction.DOWN};
    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(1024, 1024, 1024) {
        @Override
        public void onEnergyChange() {
            BlockEntityWindTurbinePillar.this.setChanged();
        }
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return BlockEntityWindTurbinePillar.this.onEnergyReceived(maxReceive, simulate);
        }
    };
    private final LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(() -> energyStorage);
    private static final LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(BlockEntityWindTurbinePillar::createEnergyDummy);
    private int potentialEnergy;
    private int oldPotential = -1;
    private int averageEnergy;
    private int oldEnergy;
    private float amount;//For Lerp
    private int tick;
    private BlockPos turbinePos;
    private boolean isBase;
    
    public BlockEntityWindTurbinePillar(BlockPos pos, BlockState state) {
        super(ModBlockEntity.TURBINE_PILLAR_TILE.get(), pos, state);
    }
    
    private static IEnergyStorage createEnergyDummy() {
        return new CustomEnergyStorage(0, 0, 0).noReceive().noExtraction();
    }
    
    public int onEnergyReceived(int maxReceive, boolean simulate) {
        if (level == null || level.isClientSide() || maxReceive <= 0) return 0;
        if (!isMaster()) getMaster().onEnergyReceived(maxReceive, simulate);
        if (inUse) return 0; // to prevent stackOverflow (IE)
        inUse = true;
        if (!simulate) potentialEnergy = maxReceive;
        CapResult result = PipeUtils.moveEnergy(this, maxReceive, energyStorage.getMaxEnergyStored(), simulate);
        if (!simulate) outPut += result.getOutPut();
        outPutCount = result.getValidReceivers();
        inUse = false;
        return result.getOutPut();
    }
    
    @Override
    public void beforeInitialize()
    {
        getIsBase();
        sync();
    }
    
    @Override
    public void doTick() {
        if (level == null || level.isClientSide()) return;
        if (isMaster()) {
            if (isBase) {
                if (tick % 10 == 0) {
                    tick = 0;
                    averageEnergy = outPut / 10;
                    outPut = 0;
                    if (averageEnergy != oldEnergy || potentialEnergy != oldPotential) {
                        oldPotential = potentialEnergy;
                        oldEnergy = averageEnergy;
                        this.sync();
                    }
                }
                tick++;
            }
        }
    }
    
    @Override
    public Direction[] getFacesToCheck() {
        return FACES_TO_CHECK;
    }
    
    
    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityWindTurbinePillar;
    }
    
    @Override
    public void checkForOutPuts(BlockPos bPos) {
        if (level == null || level.isClientSide) return;
        isBase = getIsBase();
        if (isBase) {
            forceNewTurbinePos();
            for (Direction face : Direction.Plane.HORIZONTAL) {
                BlockPos currentPos = worldPosition.relative(face);
                
                BlockState state = level.getBlockState(currentPos);
                BlockEntity te = level.getBlockEntity(currentPos);
                boolean hasMachine = !(state.getBlock() instanceof BlockWindTurbinePillar) && te != null && te.getCapability(ForgeCapabilities.ENERGY, face.getOpposite()).isPresent();
                
                if (hasMachine && te.getCapability(ForgeCapabilities.ENERGY, face.getOpposite()).orElse(null).canReceive())
                    if (!isMasterInvalid())
                        getMaster().addReceiver(te, face);
                else if (te != null && !isMasterInvalid())
                    getMaster().removeReceiver(te);
            }
        }
        this.sync();
    }
    
    @Override
    public void onFirstLoad()
    {
        if (getIsBase() && getMaster() != this) setMaster(this);
    }
    
    private BlockPos getTurbinePos() {
        if (turbinePos != null) return turbinePos;
        return forceNewTurbinePos();
    }
    
    
    private BlockPos forceNewTurbinePos() {
        if (level == null) return turbinePos;
        int n = 1;
        while (level.getBlockEntity(worldPosition.above(n)) instanceof BlockEntityWindTurbinePillar) {
            n++;
        }
        if (level.getBlockEntity(worldPosition.above(n)) instanceof BlockEntityWindTurbineHead)
            turbinePos = worldPosition.above(n);
        else turbinePos = null;
        return turbinePos;
    }
    
    public Direction getBlockFacing() {
        return getBlockState().getValue(BlockWindTurbinePillar.FACING);
    }
    
    public float getGenerationforGauge() {
        try {
            float currentAmount = Utils.normalizeClamped(getMaster().averageEnergy, 0, 128);
            amount = Utils.lerp(amount, currentAmount, 0.1f);
            return Math.min(amount, 1) * 90f;
        }
        catch (Exception e) {
            return 0;
        }
    }
    
    public float getPotentialValue() {
        try {
            float currentAmount = Utils.normalizeClamped(getMaster().potentialEnergy, 0, 128);
            return currentAmount * 90f;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public int getEnergyGenerated() {
        if (getMaster() == null) return 0;
        return getMaster().outPut;
    }
    
    public String getText() {
        if (getMaster() == null || getMaster().getTurbinePos() == null) return "No Turbine";
        return Utils.formatEnergyString(getEnergyGenerated());
    }
    
    public boolean isBase() {
        return isBase;
    }
    
    public boolean getIsBase() {
        if (level == null) return false;
        BlockState state = level.getBlockState(worldPosition.below());
        isBase = !(state.getBlock() instanceof BlockWindTurbinePillar);
        return isBase;
    }
    
    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (facing == null) return super.getCapability(capability, facing);
        
        if (capability == ForgeCapabilities.ENERGY && (facing == Direction.UP))
            return getMaster().energyStorageHandler.cast();
        if (capability == ForgeCapabilities.ENERGY && (isBase()))
            return dummyEnergy.cast();
        return super.getCapability(capability, facing);
    }
    
    @Override
    public void load(CompoundTag compound) {
        if (level == null) return;
        this.averageEnergy = compound.getInt("energy_average");
        this.potentialEnergy = compound.getInt("potential");
        super.load(compound);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.putInt("energy_average", averageEnergy);
        compound.putInt("potential", potentialEnergy);
        super.saveAdditional(compound);
    }
    
//    private boolean canConnectTo(final Direction neighborDirection) {
//        if (level == null) return false;
//        final BlockPos neighborPos = worldPosition.relative(neighborDirection);
//        final BlockState neighborState = level.getBlockState(neighborPos);
//
//        if (neighborDirection == Direction.DOWN) {
//            return !(neighborState.getBlock() instanceof BlockWindTurbinePillar);
//        }
//        BlockEntity te = level.getBlockEntity(neighborPos);
//        return te != null && te.getCapability(ForgeCapabilities.ENERGY, neighborDirection.getOpposite()).isPresent();
//    }
}

