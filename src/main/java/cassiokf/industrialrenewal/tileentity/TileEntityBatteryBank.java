package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntityConnected;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileEntityBatteryBank extends TileEntitySyncable implements ITickable
{
    private final VoltsEnergyContainer container;
    private final VoltsEnergyContainer dummyEnergy;
    private final Set<EnumFacing> outPutFacings = new HashSet<>();
    private EnumFacing blockFacing;

    public TileEntityBatteryBank()
    {
        this.container = new VoltsEnergyContainer(IRConfig.MainConfig.Main.batteryBankCapacity,
                IRConfig.MainConfig.Main.batteryBankMaxInput,
                IRConfig.MainConfig.Main.batteryBankMaxOutput)
        {
            @Override
            public void onEnergyChange()
            {
                if (!world.isRemote)
                {
                    TileEntityBatteryBank.this.Sync();
                }
            }
        };
        this.dummyEnergy = new VoltsEnergyContainer(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }

    @Override
    public void update() {
        if (this.hasWorld() && !this.world.isRemote) {
            for (EnumFacing face : outPutFacings)
            {
                TileEntity te = world.getTileEntity(pos.offset(face));
                if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite()))
                {
                    IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
                    if (eStorage != null && eStorage.canReceive()) {
                        this.container.extractEnergy(eStorage.receiveEnergy(this.container.extractEnergy(this.container.getMaxOutput(), true), false), false);
                    }
                }
            }
        }
    }

    public boolean toggleFacing(final EnumFacing facing)
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

    public boolean isFacingOutput(final @Nullable EnumFacing facing)
    {
        return outPutFacings.contains(facing) || facing == null;
    }

    public String GetText() {
        int energy = container.getEnergyStored();
        String text = energy + " FE";
        if (energy >= 1000 && energy < 1000000)
            text = energy / 1000 + "K FE";
        if (energy >= 1000000)
            text = energy / 1000000 + "M FE";
        return text;
    }

    public EnumFacing getBlockFacing() {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }

    public EnumFacing forceFaceCheck()
    {
        blockFacing = world.getBlockState(pos).getValue(BlockTileEntityConnected.FACING);
        return blockFacing;
    }

    public float GetTankFill() //0 ~ 180
    {
        float currentAmount = container.getEnergyStored() / 1000F;
        float totalCapacity = container.getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && isFacingOutput(facing)) return true;
        return (capability == CapabilityEnergy.ENERGY && facing != getBlockFacing().getOpposite()) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && isFacingOutput(facing))
            return CapabilityEnergy.ENERGY.cast(dummyEnergy);
        if (capability == CapabilityEnergy.ENERGY && facing != getBlockFacing().getOpposite())
            return CapabilityEnergy.ENERGY.cast(this.container);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.container.deserializeNBT(compound.getCompoundTag("StoredIR"));

        outPutFacings.clear();
        final int[] enabledFacingIndices = compound.getIntArray("OutputFacings");
        for (final int index : enabledFacingIndices)
        {
            outPutFacings.add(EnumFacing.byIndex(index));
        }
        blockFacing = EnumFacing.byHorizontalIndex(compound.getInteger("face"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("StoredIR", this.container.serializeNBT());
        final int[] enabledFacingIndices = outPutFacings.stream()
                .mapToInt(EnumFacing::getIndex)
                .toArray();
        compound.setIntArray("OutputFacings", enabledFacingIndices);
        compound.setInteger("face", getBlockFacing().getHorizontalIndex());
        return super.writeToNBT(compound);
    }
}
