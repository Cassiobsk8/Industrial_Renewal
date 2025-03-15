package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockSolarPanel;
import net.cassiokf.industrialrenewal.block.BlockSolarPanelFrame;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.cassiokf.industrialrenewal.util.capability.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockEntitySolarPanelFrame extends BlockEntityMultiBlocksTube<BlockEntitySolarPanelFrame> {
    
    private final Set<BlockEntitySolarPanelFrame> panelReady = new HashSet<>();
    private int tick;
    private Direction blockFacing;
    
    private final int MAX_CAPACITY = 10000;
    private final int MAX_TRANSFER_RATE = 10000;
    private final float MULTIPLIER = 2;
    
    private final boolean DECORATIVE = false;
    
    public BlockEntitySolarPanelFrame(BlockPos pos, BlockState state) {
        super(ModBlockEntity.SOLAR_PANEL_FRAME.get(), pos, state);
    }
    
    private CustomItemStackHandler createHandler() {
        return new CustomItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (stack.isEmpty()) return false;
                return Block.byItem(stack.getItem()) instanceof BlockSolarPanel;
            }
            
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntitySolarPanelFrame.this.sync();
            }
        };
    }
    
    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(MAX_CAPACITY, 0, MAX_TRANSFER_RATE) {
        @Override
        public void onEnergyChange() {
            BlockEntitySolarPanelFrame.this.sync();
        }
    };
    
    public final LazyOptional<CustomItemStackHandler> panelInv = LazyOptional.of(this::createHandler);
    private final LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(() -> energyStorage);
    
    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntitySolarPanelFrame;
    }
    
    @Override
    public void doTick() {
        if (DECORATIVE) return;;
        if (level == null) return;
        if (!level.isClientSide) {
            if (isMaster()) {
                //Utils.debug("PosSet", getPosSet().keySet());
                IEnergyStorage thisEnergy = energyStorageHandler.orElse(null);
                int size = panelReady.size();
                energyStorageHandler.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(MAX_CAPACITY * size, thisEnergy.getEnergyStored())));
                if (size > 0) getEnergyFromSun();
                for (BlockEntity tileEntity : getReceivers().keySet()) {
                    if (tileEntity != null && !tileEntity.isRemoved()) {
                        Direction facing = getReceivers().get(tileEntity);
                        final IEnergyStorage consumer = tileEntity.getCapability(ForgeCapabilities.ENERGY, facing.getOpposite()).orElse(null);
                        if (consumer != null) {
                            thisEnergy.extractEnergy(consumer.receiveEnergy(thisEnergy.extractEnergy(MAX_TRANSFER_RATE, true), false), false);
                        }
                    }
                }
            }
            
            if (tick >= 20) {
                tick = 0;
                checkIfIsReady();
            }
            tick++;
        }
    }
    
    public void checkIfIsReady() {
        if (level == null) return;
        //Utils.debug("CHECK", hasPanel(), level.canSeeSky(worldPosition.relative(Direction.UP)), level.getBrightness(LightType.SKY, worldPosition) > 0);
        if (hasPanel() && level.canSeeSky(worldPosition.relative(Direction.UP)) && level.getBrightness(LightLayer.SKY, worldPosition) > 0) {
            getMaster().getPanelReadySet().add(this);
        } else getMaster().getPanelReadySet().remove(this);
    }
    
    public void getEnergyFromSun() {
        IEnergyStorage thisEnergy = getMaster().energyStorageHandler.orElse(null);
        //Utils.debug("GET ENERGY", level.canSeeSky(worldPosition.relative(Direction.UP)), level.getBrightness(LightType.SKY, worldPosition) > 0, (thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored()), thisEnergy.getMaxEnergyStored(), thisEnergy.getEnergyStored());
        //        if (level.canSeeSky(worldPosition.relative(Direction.UP))
        //                && level.getBrightness(LightType.SKY, worldPosition) > 0 && (thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored()))
        if ((thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored())) {
            int result = Math.round((BlockEntitySolarPanel.getGeneration(level, worldPosition) * panelReady.size()) * getMultiplier());
            //Utils.debug("result", result);
            final int energy = result;
            energyStorageHandler.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(energy));
        }
    }
    
    @Override
    public void checkForOutPuts(BlockPos pos) {
        if (level == null) return;
        getPanelReadySet().clear();
        if (level.isClientSide) return;
        for (Direction face : Direction.values()) {
            Direction facing = getBlockFacing();
            //            Utils.debug("FACE", face, facing, face == facing, face == facing.getClockWise(), face == facing.getCounterClockWise());
            boolean canConnect = face == facing || face == facing.getClockWise() || face == facing.getCounterClockWise();
            if (!canConnect) continue;
            
            BlockPos currentPos = pos.relative(face);
            
            BlockState state = level.getBlockState(currentPos);
            BlockEntity te = level.getBlockEntity(currentPos);
            boolean hasMachine = !(state.getBlock() instanceof BlockSolarPanelFrame) && te != null && te.getCapability(ForgeCapabilities.ENERGY, face.getOpposite()).isPresent();
            if (hasMachine) {
                if (!isMasterInvalid()) {
                    getMaster().addReceiver(te, face);
                }
            } else {
                if (!isMasterInvalid()) {
                    getMaster().removeReceiver(te);
                    //getMaster().getPanelReadySet().remove(this);
                }
            }
        }
    }
    
    public Direction getBlockFacing() {
        if (blockFacing == null) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockSolarPanelFrame)
                blockFacing = state.getValue(BlockSolarPanelFrame.FACING);
            else blockFacing = Direction.NORTH;
        }
        return blockFacing;
    }
    
    @Override
    public void removeReceiver(BlockEntity machine) {
        super.removeReceiver(machine);
    }
    
    private float getMultiplier() {
        return MULTIPLIER;
        //return IRConfig.Main.panelFrameMultiplier.get();
    }
    
    public Set<BlockEntitySolarPanelFrame> getPanelReadySet() {
        return panelReady;
    }
    
    public boolean hasPanel() {
        return !panelInv.orElse(null).getStackInSlot(0).isEmpty();
    }
    
    public ItemStack getPanel() {
        return panelInv.orElse(null).getStackInSlot(0);
    }
    
    public IItemHandler getPanelHandler() {
        return panelInv.orElse(null);
    }
    
    @Override
    public void breakBlock() {
        //getMaster().getPanelReadySet().remove(worldPosition);
        if (!panelInv.orElse(null).getStackInSlot(0).isEmpty())
            Utils.dropInventoryItems(level, worldPosition, panelInv.orElse(null));
        super.breakBlock();
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.putInt("energy", energyStorage.getEnergyStored());
        panelInv.ifPresent(h -> {
            CompoundTag tag = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            compound.put("inv", tag);
        });
        super.saveAdditional(compound);
    }
    
    @Override
    public void load(CompoundTag compound) {
        energyStorage.setEnergy(compound.getInt("energy"));
        panelInv.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(compound.getCompound("inv")));
        super.load(compound);
    }
    
    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        Direction face = getBlockFacing();
        
        if (facing == null) return super.getCapability(capability, facing);
        
        boolean canConnect = facing == face || facing == face.getClockWise() || facing == face.getCounterClockWise();
        if (capability == ForgeCapabilities.ENERGY && canConnect) return getMaster().energyStorageHandler.cast();
        return super.getCapability(capability, facing);
    }
}
