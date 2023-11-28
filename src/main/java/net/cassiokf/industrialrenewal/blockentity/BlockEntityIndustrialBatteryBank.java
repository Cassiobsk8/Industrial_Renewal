package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityTowerBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class BlockEntityIndustrialBatteryBank extends BlockEntityTowerBase<BlockEntityIndustrialBatteryBank> {
    //public ArrayList<TileEntityIndustrialBatteryBank> tower = null;
    
    public static final int CAPACITY_PER_BATTERY = 6480000; //5000000
    // Typical car battery is 720Wh = 2592000J = 6480000RF
    private static final int maxTransfer = 10240;
    private static final int maxBatteries = 24;
    
    private final CustomEnergyStorage customDummyStorage = new CustomEnergyStorage(0, 0, 0);
    private CustomEnergyStorage customEnergyStorage = new CustomEnergyStorage(0, maxTransfer, maxTransfer) {
        @Override
        public void onEnergyChange() {
            sync();
        }
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (maxReceive <= 0) return 0;
            return getBase().onEnergyIn(maxReceive, simulate);
        }
        
        @Override
        public boolean canExtract() {
            return false;
        }
    };
    
    private final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(() -> customEnergyStorage);
    private final LazyOptional<IEnergyStorage> dummyStorage = LazyOptional.of(() -> customDummyStorage);
    
    public int input;
    private int avrIn;
    private int oldIn;
    private int outPut;
    private int avrOut;
    private int oldOutPut;
    private int batteries = 0;
    
    private int tick;
    public boolean firstLoad = false;
    
    public BlockEntityIndustrialBatteryBank(BlockPos pos, BlockState state) {
        super(ModBlockEntity.INDUSTRIAL_BATTERY_TILE.get(), pos, state);
    }
    
    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityIndustrialBatteryBank;
    }
    
    
    public int onEnergyIn(int received, boolean simulate) {
        if (level == null) return 0;
        received = Math.min(received, maxTransfer);
        int in;
        if (batteries > 0) in = customEnergyStorage.addEnergy(received, simulate);
        else {
            in = outPutEnergy(null, received, simulate);
            if (!simulate && !level.isClientSide) outPut += in;
        }
        if (!simulate && !level.isClientSide) input += in;
        return in;
    }
    
    //
    private int outPutEnergy(CustomEnergyStorage container, int maxReceive, boolean simulate) {
        if (level == null) return 0;
        BlockPos masterPos = masterTE.getBlockPos();
        Direction face = getMasterFacing();
        BlockPos outPos = masterPos.relative(face.getClockWise()).above(2 + (tower != null ? (tower.size()-1) : 0) * 3);
        BlockEntity te = level.getBlockEntity(outPos);
        
        int out = 0;
        if (te != null) {
            //Utils.debug("OUTPUT ENERGY", te, te.getBlockPos());
            IEnergyStorage energyStorage = te.getCapability(ForgeCapabilities.ENERGY, Direction.DOWN).orElse(null);
            if (energyStorage != null) {
                if (container == null) out = energyStorage.receiveEnergy(maxReceive, simulate);
                else
                    out = container.subtractEnergy(energyStorage.receiveEnergy(container.subtractEnergy(maxReceive, true), simulate), simulate);
            }
        }
        return out;
    }
    
    public boolean placeBattery(Player player, ItemStack batteryStack) {
        if (level == null) return false;
        if (!level.isClientSide) {
            if (batteries >= maxBatteries) return false;
            batteries++;
            if (!player.isCreative()) batteryStack.shrink(1);
            customEnergyStorage.setMaxCapacity(batteries * CAPACITY_PER_BATTERY);
            sync();
            getBase().recalculateTowerCapacity();
        }
        return true;
    }
    
    public boolean removeBattery(Player player) {
        if (level == null) return false;
        if (!level.isClientSide) {
            if (batteries <= 0) return false;
            batteries--;
            if (!player.isCreative()) player.getInventory().add(new ItemStack(ModItems.BATTERY_LITHIUM.get()));
            customEnergyStorage.setMaxCapacity(batteries * CAPACITY_PER_BATTERY);
            sync();
            getBase().recalculateTowerCapacity();
        }
        return true;
    }
    
    private void recalculateTowerCapacity() {
        if (level == null) return;
        if (!level.isClientSide) {
            int sum = getSumMaxEnergy();
            customEnergyStorage.setMaxCapacity(sum);
            sync();
        }
    }
    
    public void setFirstLoad() {
        if (level == null) return;
        if (!level.isClientSide && isMaster()) {
            if (isBase()) {
                if (tower == null || tower.isEmpty()) loadTower();
            } else this.tower = getBase().tower;
        }
    }
    
    @Override
    public void loadTower() {
        BlockEntityIndustrialBatteryBank chunk = this;
        tower = new ArrayList<>();
        while (chunk != null) {
            if (!tower.contains(chunk)) tower.add(chunk);
            chunk = chunk.getAbove();
        }
    }
    
    public void tick() {
        if (level != null && !level.isClientSide && isMaster() && isBase()) {
            if(!firstLoad){
                firstLoad = true;
                setFirstLoad();
            }
            if (batteries > 0 && customEnergyStorage.getEnergyStored() > 0) {
                outPut += outPutEnergy(customEnergyStorage, maxTransfer, false);
            }
            
            if (tick >= 10) {
                tick = 0;
                avrIn = input / 10;
                avrOut = outPut / 10;
                input = 0;
                outPut = 0;
                
                if (avrOut != oldOutPut || avrIn != oldIn) {
                    oldOutPut = avrOut;
                    oldIn = avrIn;
                    sync();
                }
            }
            tick++;
        }
    }
    
    public int getInput() {
        if (getTop() == null) return 0;
        return getTop().input;
    }
    
    public int getSumMaxEnergy() {
        //int max = 0;
        if (tower == null || tower.isEmpty()) return 0;
        
        int max = tower.stream().map(te -> (te.customEnergyStorage.getMaxEnergyStored())).reduce(0, Integer::sum);
        return max;
    }
    
    public int getSumCurrentEnergy() {
        //int current = 0;
        if (tower == null || tower.isEmpty()) return 0;
        
        int current = tower.stream().map(te -> (te.customEnergyStorage.getEnergyStored())).reduce(0, Integer::sum);
        return current;
    }
    
    public int getRealCapacity() {
        return CAPACITY_PER_BATTERY * batteries;
    }
    
    public int getBatteries() {
        return batteries;
    }
    
    public String getEnergyText() {
        //return Utils.formatEnergyString(customEnergyStorage.getEnergyStored()).replace(" FE", "") + " / " + Utils.formatEnergyString(customEnergyStorage.getMaxEnergyStored());
        return Utils.formatEnergyString(customEnergyStorage.getEnergyStored()).replace(" FE", "") + " / " + Utils.formatEnergyString(customEnergyStorage.getMaxEnergyStored());
    }
    
    public float getBatteryFill() {
        //return Utils.normalizeClamped(customEnergyStorage.getEnergyStored(), 0, customEnergyStorage.getMaxEnergyStored());
        return Utils.normalizeClamped(customEnergyStorage.getEnergyStored(), 0, customEnergyStorage.getMaxEnergyStored());
    }
    
    public float getOutPutAngle() {
        return Utils.normalizeClamped(avrOut, 0, 10240) * 90;
    }
    
    public String getOutPutText() {
        return Utils.formatPreciseEnergyString(avrOut) + "/t";
    }
    
    public float getInPutAngle() {
        return Utils.normalizeClamped(avrIn, 0, 10240) * 90;
    }
    
    public String getInPutText() {
        return Utils.formatPreciseEnergyString(avrIn) + "/t";
    }
    
    public String getInPutIndicatorText() {
        return "Input";
    }
    
    public String getOutPutIndicatorText() {
        return "Output";
    }
    
    public Boolean isFull() {
        return customEnergyStorage.getEnergyStored() >= customEnergyStorage.getMaxEnergyStored();
    }
    
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockEntityIndustrialBatteryBank masterTE = getMaster();
        if (masterTE == null) return super.getCapability(cap, side);
        
        if (side == null) return super.getCapability(cap, side);
        
        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getBlockPos();
        
        if (cap == ForgeCapabilities.ENERGY && worldPosition.equals(masterPos.relative(face.getCounterClockWise()).above()) && side == Direction.UP) {
            // Input
            return getMaster().energyStorage.cast();
        }
        if (cap == ForgeCapabilities.ENERGY && worldPosition.equals(masterPos.relative(face.getClockWise()).above()) && side == Direction.UP) {
            // Output
            return getMaster().dummyStorage.cast();
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", customEnergyStorage.getEnergyStored());
        compoundTag.putInt("capacity", customEnergyStorage.getMaxEnergyStored());
        
        compoundTag.putInt("out", avrOut);
        compoundTag.putInt("in", avrIn);
        compoundTag.putInt("battery", batteries);
        
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        avrOut = compoundTag.getInt("out");
        avrIn = compoundTag.getInt("in");
        batteries = compoundTag.getInt("battery");
        customEnergyStorage.setMaxCapacity(compoundTag.getInt("capacity"));
        customEnergyStorage.setEnergy(compoundTag.getInt("energy"));
        super.load(compoundTag);
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
