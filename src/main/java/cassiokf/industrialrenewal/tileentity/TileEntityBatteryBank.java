package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockTileEntityConnected;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import static cassiokf.industrialrenewal.init.TileRegistration.BATTERYBANK_TILE;

public class TileEntityBatteryBank extends TileEntitySyncable implements ICapabilityProvider, ITickableTileEntity
{
    private final Set<Direction> outPutFacings = new HashSet<>();
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);
    private Direction blockFacing;

    public TileEntityBatteryBank()
    {
        super(BATTERYBANK_TILE.get());
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(IRConfig.Main.batteryBankCapacity.get(),
                IRConfig.Main.batteryBankMaxInput.get(),
                IRConfig.Main.batteryBankMaxOutput.get())
        {
            @Override
            public void onEnergyChange()
            {
                if (!TileEntityBatteryBank.this.world.isRemote)
                {
                    TileEntityBatteryBank.this.Sync();
                }
            }
        };
    }

    private IEnergyStorage createEnergyDummy()
    {
        return new CustomEnergyStorage(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }

    @Override
    public void tick()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            for (Direction face : outPutFacings)
            {
                TileEntity te = world.getTileEntity(pos.offset(face));
                if (te != null)
                {
                    IEnergyStorage thisStorage = energyStorage.orElse(null);
                    IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
                    if (eStorage != null && eStorage.canReceive())
                    {
                        thisStorage.extractEnergy(eStorage.receiveEnergy(thisStorage.extractEnergy(IRConfig.Main.batteryBankMaxOutput.get(), true), false), false);
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
            this.Sync();
            return false;
        } else
        {
            outPutFacings.add(facing);
            this.Sync();
            return true;
        }
    }

    public boolean isFacingOutput(final @Nullable Direction facing)
    {
        return outPutFacings.contains(facing) || facing == null;
    }

    public String GetText()
    {
        int energy = energyStorage.orElse(null).getEnergyStored();
        return Utils.formatEnergyString(energy);
    }

    public Direction getBlockFacing()
    {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }

    public Direction forceFaceCheck()
    {
        blockFacing = world.getBlockState(pos).get(BlockTileEntityConnected.FACING);
        return blockFacing;
    }

    public float GetTankFill() //0 ~ 180
    {
        float currentAmount = energyStorage.orElse(null).getEnergyStored() / 1000F;
        float totalCapacity = energyStorage.orElse(null).getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && isFacingOutput(facing))
            return dummyEnergy.cast();
        if (capability == CapabilityEnergy.ENERGY && facing != getBlockFacing().getOpposite())
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
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
    public CompoundNBT write(CompoundNBT compound)
    {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        final int[] enabledFacingIndices = outPutFacings.stream()
                .mapToInt(Direction::getIndex)
                .toArray();
        compound.putIntArray("OutputFacings", enabledFacingIndices);
        compound.putInt("face", getBlockFacing().getHorizontalIndex());
        return super.write(compound);
    }
}
