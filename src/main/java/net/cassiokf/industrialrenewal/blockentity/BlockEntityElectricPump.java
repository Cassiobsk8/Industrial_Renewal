package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockElectricPump;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BlockEntityElectricPump extends BlockEntitySyncable implements ICapabilityProvider {
    
    private final int energyPerTick = 10;
    private final int energyCapacity = 10000;
    private final int maxRadius = 100;
    private final boolean consumeWater = false;
    private final boolean replaceCobbleConfig = true;
    private final List<BlockPos> fluidSet = new ArrayList<>();
    private final CustomFluidTank tank = new CustomFluidTank(10000) {
        @Override
        protected void onContentsChanged() {
            BlockEntityElectricPump.this.setChanged();
        }
        
        @Override
        public boolean canDrain() {
            return false;
        }
        
        @Override
        public boolean canFill() {
            return false;
        }
    };
    private final LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(() -> tank);
    private int index = -1;
    private final int everyXtick = 10;
    
    //IEnergyStorage motorEnergy = null;
    private int tick;
    private Direction facing;
    private boolean isRunning = false;
    //private boolean oldIsRunning = false;
    private boolean starting = false;
    //private boolean oldStarting = false;
    private boolean firstLoad = false;
    private CustomEnergyStorage energyStorage = new CustomEnergyStorage(energyCapacity, energyCapacity, energyCapacity) {
        @Override
        public void onEnergyChange() {
            BlockEntityElectricPump.this.sync();
        }
        
        @Override
        public boolean canExtract() {
            return false;
        }
    };
    private final LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(() -> energyStorage);
    
    
    public BlockEntityElectricPump(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ELECTRIC_PUMP_TILE.get(), pos, state);
    }
    
    
    @Override
    public void onLoad() {
        super.onLoad();
    }
    
    public void setFirstLoad() {
        if (level == null) return;
        if (!level.isClientSide && getIdex() == 1) {
            BlockEntityElectricPump energyInputTile = (BlockEntityElectricPump) level.getBlockEntity(worldPosition.relative(getBlockState().getValue(BlockElectricPump.FACING).getOpposite()));
            if (energyInputTile != null) energyStorage = energyInputTile.energyStorage;
        }
    }
    
    public void tick() {
        if (level == null) return;
        if (!level.isClientSide && getIdex() == 1) {
            if (!firstLoad) {
                firstLoad = true;
                setFirstLoad();
                //this.onLoad();
            }
            isRunning = consumeEnergy();
            if (tick >= everyXtick) {
                tick = 0;
                if (isRunning) {
                    GetFluidDown();
                    passFluidUp();
                    this.sync();
                }
            }
            tick++;
        }
        //        else
        //        {
        //            if (getIdex() == 1)
        //            {
        //                handleSound();
        //            }
        //        }
    }
    
    private int getIdex() {
        if (index != -1) return index;
        BlockState state = getBlockState();
        index = state.getBlock() instanceof BlockElectricPump ? state.getValue(BlockElectricPump.INDEX) : -1;
        return index;
    }
    
    private boolean consumeEnergy() {
        if (energyStorage == null) return false;
        return energyStorage.getEnergyStored() >= energyPerTick && energyStorage.subtractEnergy(energyPerTick, false) > 0;
    }
    
    private void GetFluidDown() {
        if (level == null) return;
        if (tank.getFluidAmount() <= tank.getCapacity() && isRunning) {
            if (!consumeWater && level.getBlockState(worldPosition.below()).getBlock().equals(Blocks.WATER)) {
                tank.fillInternal(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                return;
            }
            //            Utils.debug("fluid set", getFluidSet() != null, !getFluidSet().isEmpty());
            if (getFluidSet() != null && !getFluidSet().isEmpty()) {
                BlockPos fluidPos = getFluidSet().get(0);
                //                Utils.debug("fluid pos", fluidPos);
                
                while (!instanceOf(fluidPos, true)) {
                    getFluidSet().remove(fluidPos);
                    if (getFluidSet() == null || getFluidSet().isEmpty()) return;
                    fluidPos = getFluidSet().get(0);
                }
                
                FluidState state = level.getFluidState(fluidPos);
                
                boolean consumeFluid = !(state.getType().equals(Fluids.WATER) && consumeWater);
                //&& IRConfig.Main.pumpInfinityWater.get());
                
                //                Utils.debug("downFluid", state.getType().getFluidType().getDescription().getString());
                if (state.isSource()) {
                    if (tank.fillInternal(new FluidStack(state.getType(), 1000), IFluidHandler.FluidAction.EXECUTE) > 0) {
                        if (state.getType().equals(Fluids.LAVA) && replaceCobbleConfig) {
                            level.setBlock(fluidPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                            getFluidSet().remove(fluidPos);
                        } else if (consumeFluid) {
                            level.setBlockAndUpdate(fluidPos, Blocks.AIR.defaultBlockState());
                            getFluidSet().remove(fluidPos);
                        }
                    }
                }
            }
        }
    }
    
    private List<BlockPos> getFluidSet() {
        if (fluidSet.isEmpty()) getAllFluids();
        return fluidSet;
    }
    
    private void getAllFluids() {
        if (level == null) return;
        if (level.getFluidState(worldPosition.below()) != Fluids.EMPTY.defaultFluidState()) {
            Stack<BlockPos> traversingFluids = new Stack<>();
            List<BlockPos> flowingPos = new ArrayList<>();
            traversingFluids.add(worldPosition.below());
            while (!traversingFluids.isEmpty()) {
                BlockPos fluidPos = traversingFluids.pop();
                if (instanceOf(fluidPos, true)) {
                    //                    Utils.debug("ADD TO SOURCE SET");
                    fluidSet.add(fluidPos);
                } else {
                    //                    Utils.debug("ADD TO FLOW SET");
                    flowingPos.add(fluidPos);
                }
                
                for (Direction d : Direction.values()) {
                    BlockPos newPos = fluidPos.relative(d);
                    if (instanceOf(newPos, false) && !fluidSet.contains(newPos) && !flowingPos.contains(newPos)) {
                        traversingFluids.add(newPos);
                    }
                }
            }
        }
    }
    
    private boolean instanceOf(BlockPos pos, boolean checkLevel) {
        if (level == null) return false;
        if (pos == null) return false;
        FluidState state = level.getFluidState(pos);
        return state != Fluids.EMPTY.defaultFluidState() && (!checkLevel || state.isSource()) && Utils.getDistanceSq(worldPosition, pos.getX(), pos.getY(), pos.getZ()) <= maxRadius * maxRadius;
    }
    
    private void passFluidUp() {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null) {
            upTank.fill(tank.drainInternal(tank.getCapacity() / everyXtick, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        }
    }
    
    @Override
    public void invalidateCaps() {
        energyStorageHandler.invalidate();
        tankHandler.invalidate();
        super.invalidateCaps();
    }
    
    @Override
    public void setRemoved() {
        super.setRemoved();
    }
    
    private BlockEntityElectricPump getMotor() {
        if (level == null) return null;
        BlockEntity te = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(BlockElectricPump.FACING).getOpposite()));
        if (te != null && te instanceof BlockEntityElectricPump)
            if (((BlockEntityElectricPump) te).index == 0) return (BlockEntityElectricPump) te;
        return null;
    }
    
    private IFluidHandler GetTankUp() {
        if (level == null) return null;
        BlockEntity upTE = level.getBlockEntity(worldPosition.above());
        if (upTE != null) {
            return upTE.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN).orElse(null);
        }
        return null;
    }
    
    private Direction getBlockFacing() {
        if (facing != null) return facing;
        facing = getBlockState().getValue(BlockElectricPump.FACING);
        return facing;
    }
    
    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
        int index = getIdex();
        
        if (facing == null) return super.getCapability(capability, facing);
        //Utils.debug("index, capability, facing", index, capability, facing);
        if (index == 1 && capability == ForgeCapabilities.FLUID_HANDLER && facing == Direction.UP)
            return LazyOptional.of(() -> tank).cast();
        Direction face = getBlockFacing();
        if (index == 0 && capability == ForgeCapabilities.ENERGY && facing == face.getOpposite())
            return energyStorageHandler.cast();
        return super.getCapability(capability, facing);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        CompoundTag tag = new CompoundTag();
        tank.writeToNBT(tag);
        compoundTag.put("fluid", tag);
        compoundTag.putBoolean("isRunning", isRunning);
        compoundTag.putBoolean("starting", starting);
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        CompoundTag tag = compoundTag.getCompound("fluid");
        tank.readFromNBT(tag);
        isRunning = compoundTag.getBoolean("isRunning");
        starting = compoundTag.getBoolean("starting");
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        
        super.load(compoundTag);
    }
}
