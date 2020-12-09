package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTEHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileEntityBatteryBank extends TileEntitySync implements ITickableTileEntity
{
    private final CustomEnergyStorage container = new CustomEnergyStorage(IRConfig.Main.batteryBankCapacity.get(),
            IRConfig.Main.batteryBankMaxInput.get(),
            IRConfig.Main.batteryBankMaxOutput.get())
    {
        @Override
        public void onEnergyChange()
        {
            if (!world.isRemote)
            {
                TileEntityBatteryBank.this.sync();
            }
        }
    };
    private final CustomEnergyStorage dummyEnergy = new CustomEnergyStorage(0, 0, 0)
    {
        @Override
        public boolean canReceive()
        {
            return false;
        }
    };
    private final Set<Direction> outPutFacings = new HashSet<>();
    private Direction blockFacing;

    public TileEntityBatteryBank(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (this.hasWorld() && !this.world.isRemote) {
            for (Direction face : outPutFacings)
            {
                TileEntity te = world.getTileEntity(pos.offset(face));
                if (te != null)
                {
                    IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
                    if (eStorage != null && eStorage.canReceive())
                    {
                        this.container.extractEnergy(eStorage.receiveEnergy(this.container.extractEnergy(this.container.getMaxOutput(), true), false), false);
                    }
                }
            }
        }
    }

    public boolean toggleFacing(final Direction facing)
    {
        if (outPutFacings.contains(facing))
        {
            outPutFacings.remove(facing);
            this.sync();
            return false;
        } else
        {
            outPutFacings.add(facing);
            this.sync();
            return true;
        }
    }

    public boolean isFacingOutput(final @Nullable Direction facing)
    {
        return outPutFacings.contains(facing) || facing == null;
    }

    public String GetText() {
        return Utils.formatEnergyString(container.getEnergyStored());
    }

    public Direction getBlockFacing() {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }

    public Direction forceFaceCheck()
    {
        blockFacing = getBlockState().get(BlockTEHorizontalFacing.FACING);
        return blockFacing;
    }

    public float getBatteryFill()
    {
        return Utils.normalize(container.getEnergyStored(), 0, container.getMaxEnergyStored());
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY && isFacingOutput(facing))
            return LazyOptional.of(() -> dummyEnergy).cast();
        if (capability == CapabilityEnergy.ENERGY && facing != getBlockFacing().getOpposite())
            return LazyOptional.of(() -> container).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.container.deserializeNBT(compound.getCompound("StoredIR"));

        outPutFacings.clear();
        final int[] enabledFacingIndices = compound.getIntArray("OutputFacings");
        for (final int index : enabledFacingIndices)
        {
            outPutFacings.add(Direction.byIndex(index));
        }
        blockFacing = Direction.byHorizontalIndex(compound.getInt("face"));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("StoredIR", this.container.serializeNBT());
        final int[] enabledFacingIndices = outPutFacings.stream()
                .mapToInt(Direction::getIndex)
                .toArray();
        compound.putIntArray("OutputFacings", enabledFacingIndices);
        compound.putInt("face", getBlockFacing().getHorizontalIndex());
        return super.write(compound);
    }
}
