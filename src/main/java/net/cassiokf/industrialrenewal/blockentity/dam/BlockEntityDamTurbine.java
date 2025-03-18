package net.cassiokf.industrialrenewal.blockentity.dam;

import net.cassiokf.industrialrenewal.block.dam.BlockDamGenerator;
import net.cassiokf.industrialrenewal.block.dam.BlockRotationalShaft;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.cassiokf.industrialrenewal.util.capability.CustomPressureFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDamTurbine extends BlockEntity3x3x3MachineBase<BlockEntityDamTurbine> {
    
    public static int MAX_PROCESSING = 1000;
    public static int MAX_EFFICIENCY = 10000;
    public final CustomFluidTank dummyTank = new CustomFluidTank(0);
    public final LazyOptional<CustomFluidTank> dummyHandler = LazyOptional.of(() -> dummyTank);
    private float oldRotation;
    private float rotation;
    //    private int outLetLimit = Config.DAM_TURBINE_WATER_OUTPUT_RATE.get();
    private int passedFluid = 0;
    private boolean hasFlow = false;
    public final CustomPressureFluidTank inTank = new CustomPressureFluidTank() {
        @Override
        public int receiveCompressedFluid(int amount, int y, FluidAction action) {
            return passCompressedFluid(amount, y, action);
        }
    };
    public final LazyOptional<CustomPressureFluidTank> inTankHandler = LazyOptional.of(() -> inTank);
    
    
    public BlockEntityDamTurbine(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DAM_TURBINE_TILE.get(), pos, state);
    }
    
    
    public void tick() {
        if (level == null) return;
        if (isMaster()) {
            if (!level.isClientSide) {
                doRotation();
                passRotation();
            } else {
                //updateSound(getPitch());
            }
        }
    }
    
    private void passRotation() {
        if (level == null) return;
        BlockPos top = worldPosition.above(2);
        for (int i = 0; i < 16; i++) {
            if (level.getBlockState(top).getBlock() instanceof BlockDamGenerator) {
                BlockEntity te = level.getBlockEntity(top.above());
                if (te instanceof BlockEntityDamGenerator && ((BlockEntityDamGenerator) te).isMaster()) {
                    BlockEntityDamGenerator generatorTE = (BlockEntityDamGenerator) level.getBlockEntity(top.above());
                    generatorTE.updateRotation((int) rotation);
                }
                break;
            } else if (level.getBlockState(top).getBlock() instanceof BlockRotationalShaft) {
                top = top.above();
            } else break;
        }
    }
    
    public int passCompressedFluid(int amount, int y, IFluidHandler.FluidAction action) {
        if (level == null) return 0;
        if (!isMaster()) return getMaster().passCompressedFluid(amount, y, action);
        int height = y - worldPosition.getY();
        if (height < 0) return 0;
        int realAmount = 0;
        Direction masterFace = getMasterFacing();
        BlockPos outPutPos = getMaster().getBlockPos().relative(masterFace.getCounterClockWise(), 2).relative(masterFace.getOpposite()).below();
        BlockEntity te = level.getBlockEntity(outPutPos);
        if (te != null) {
            IFluidHandler tank = te.getCapability(ForgeCapabilities.FLUID_HANDLER, masterFace.getClockWise()).orElse(null);
            if (tank != null && tank instanceof CustomPressureFluidTank cc) {
                realAmount = cc.receiveCompressedFluid(amount, worldPosition.getY(), action);
            }
        }
        
        passedFluid = realAmount;
        if (action.equals(IFluidHandler.FluidAction.EXECUTE) && realAmount > 0) hasFlow = true;
        return realAmount;
    }
    
    private void rotationDecay() {
        rotation *= (1 - 0.005f);
        rotation -= 0.05f;
        rotation = Mth.clamp(rotation, 0, MAX_PROCESSING);
        if (rotation != oldRotation) {
            oldRotation = rotation;
            sync();
        }
    }
    
    private void doRotation() {
        float norm = Utils.normalizeClamped(passedFluid, 0, MAX_EFFICIENCY);
        if (hasFlow) {
            float max = (MAX_PROCESSING * norm);
            if (Float.isNaN(rotation)) rotation = 0;
            if (rotation < max) {
                float clamped = Utils.normalizeClamped(rotation, 0.001f, norm);
                rotation *= (1 + (0.005f / clamped));
                rotation += Math.min(norm * 0.05f, max - rotation);
            } else if (rotation > max) {
                rotation -= 1;
            }
        } else if (rotation > 0) {
            rotationDecay();
        }
        rotation = Mth.clamp(rotation, 0, 6000);
        if (rotation != oldRotation) {
            oldRotation = rotation;
            sync();
        }
        hasFlow = false;
    }
    
    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityDamTurbine;
    }
    
    public String getRotationText() {
        return "Rot: " + (int) rotation + " rpm";
    }
    
    public float getRotationFill() {
        return getNormalizedRotation() * 180;
    }
    
    private float getNormalizedRotation() {
        return Utils.normalizeClamped(rotation, 0, MAX_PROCESSING);
    }
    
    //    @Override
    //    public float getPitch()
    //    {
    //        return getNormalizedRotation() * 0.7f;
    //    }
    
    //    @Override
    //    public float getVolume()
    //    {
    //        return volume;
    //    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockEntityDamTurbine master = getMaster();
        if (master == null || side == null) return super.getCapability(cap, side);
        Direction masterFace = getMasterFacing();
        if (cap == ForgeCapabilities.FLUID_HANDLER && worldPosition.equals(master.getBlockPos().relative(masterFace).relative(masterFace.getClockWise())) && side == masterFace)
            return getMaster().inTankHandler.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER && worldPosition.equals(master.getBlockPos().relative(masterFace.getCounterClockWise()).relative(masterFace.getOpposite()).below()) && side == masterFace.getCounterClockWise())
            return getMaster().dummyHandler.cast();
        return super.getCapability(cap, side);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putFloat("rotation", rotation);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        rotation = compoundTag.getFloat("rotation");
        super.load(compoundTag);
    }
}
