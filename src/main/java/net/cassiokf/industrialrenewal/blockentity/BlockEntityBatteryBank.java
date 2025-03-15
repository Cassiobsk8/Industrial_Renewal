package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
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
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class BlockEntityBatteryBank extends BlockEntitySyncable {

    public static final int CONFIG_CAPACITY = 1000000;
    public static final int MAX_TRANSFER = 1000;

    private final Set<Direction> outPutFacings = new HashSet<>();

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(CONFIG_CAPACITY, MAX_TRANSFER, MAX_TRANSFER).setBlockEntity(this);

    private final CustomEnergyStorage dummyStorage = new CustomEnergyStorage(0).noExtraction().noReceive();

    private final LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);

    private final LazyOptional<CustomEnergyStorage> dummyHandler = LazyOptional.of(()->dummyStorage);

    private Direction blockFacing;


    public BlockEntityBatteryBank(BlockPos pos, BlockState state){
        super(ModBlockEntity.BATTERY_BANK.get(), pos, state);
    }

    public void tick(){
        if (level == null || level.isClientSide) return;
    
        for (Direction face : outPutFacings) {
                BlockEntity te = level.getBlockEntity(worldPosition.relative(face));
            if (te == null) continue;
    
            energyStorageHandler.ifPresent(thisStorage -> {
                te.getCapability(ForgeCapabilities.ENERGY, face.getOpposite()).ifPresent(eStorage -> {
                    if (eStorage.canReceive()) {
                        int energyExtracted = thisStorage.extractEnergy(MAX_TRANSFER, true);
                        int energyReceived = eStorage.receiveEnergy(energyExtracted, false);
                        thisStorage.extractEnergy(energyReceived, false);
                }
                });
            });
        }
    }

    public boolean toggleFacing(final Direction facing)
    {
        if (outPutFacings.contains(facing))
        {
            outPutFacings.remove(facing);
            this.sync();
            this.requestModelDataUpdate();
            return false;
        } else
        {
            outPutFacings.add(facing);
            this.sync();
            this.requestModelDataUpdate();
            return true;
        }
    }

    public boolean isFacingOutput(final @Nullable Direction facing) {
        return outPutFacings.contains(facing) || facing == null;
    }

    public Direction getBlockFacing()
    {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }

    public Direction forceFaceCheck()
    {
        blockFacing = getBlockState().getValue(BlockAbstractHorizontalFacing.FACING);
        return blockFacing;
    }

    // Display ========================================================================

    public String getText() {
        IEnergyStorage energyStorage1 = energyStorageHandler.orElse(null);
        if(energyStorage1 == null)
            return "NULL";
        int energy = energyStorage1.getEnergyStored();
        return Utils.formatEnergyString(energy);
    }

    public float getTankFill() //0 ~ 180
    {
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage == null)
            return 0;

//        Utils.debug("iEnergyStorage",iEnergyStorage == null, iEnergyStorage.getEnergyStored(), iEnergyStorage.getMaxEnergyStored());
        float currentAmount = iEnergyStorage.getEnergyStored() / 1000F;
        float totalCapacity = iEnergyStorage.getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
//        energyStorageHandler.ifPresent(h ->
//                compoundTag.put("energy", ((INBTSerializable<CompoundTag>) h).serializeNBT()));
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        final int[] enabledFacingIndices = outPutFacings.stream()
                .mapToInt(Direction::get3DDataValue)
                .toArray();

        compoundTag.putIntArray("OutputFacings", enabledFacingIndices);
        compoundTag.putInt("face", getBlockFacing().get2DDataValue());
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
//        energyStorageHandler.ifPresent(h ->
//                ((INBTSerializable<CompoundTag>) h).deserializeNBT(compoundTag.getCompound("energy")));
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        outPutFacings.clear();

        final int[] enabledFacingIndices = compoundTag.getIntArray("OutputFacings");
        for (int fd : enabledFacingIndices)
        {
            outPutFacings.add(Direction.from3DDataValue(fd));
        }
        blockFacing = Direction.from2DDataValue(compoundTag.getInt("face"));
        super.load(compoundTag);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyStorageHandler.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (side == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ENERGY && isFacingOutput(side))
            return dummyHandler.cast();
        if (cap == ForgeCapabilities.ENERGY && side != getBlockFacing().getOpposite())
            return energyStorageHandler.cast();
        return super.getCapability(cap, side);
    }
}
