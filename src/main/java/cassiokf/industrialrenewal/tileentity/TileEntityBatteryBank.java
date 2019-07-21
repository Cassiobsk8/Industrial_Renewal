package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBatteryBank;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileEntityBatteryBank extends TileEntitySyncable implements ICapabilityProvider, ITickable
{
    private final VoltsEnergyContainer container;
    private final VoltsEnergyContainer dummyEnergy;
    private final Set<EnumFacing> outPutFacings = new HashSet<>();
    private boolean needSync = true;

    public TileEntityBatteryBank() {
        this.container = new VoltsEnergyContainer(IRConfig.MainConfig.Main.batteryBankCapacity, IRConfig.MainConfig.Main.batteryBankMaxInput, IRConfig.MainConfig.Main.batteryBankMaxOutput)
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
            if (needSync)
            {
                setState();
            }
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
            setState();
            return false;
        } else
        {
            outPutFacings.add(facing);
            setState();
            return true;
        }
    }

    private void setState()
    {
        if (!world.isRemote)
        {
            IBlockState state = world.getBlockState(pos);
            EnumFacing facing = state.getValue(BlockBatteryBank.FACING);
            state = state.withProperty(BlockBatteryBank.SOUTH, isFacingOutput(facing.getOpposite()))
                    .withProperty(BlockBatteryBank.NORTH, isFacingOutput(facing))
                    .withProperty(BlockBatteryBank.EAST, isFacingOutput(facing.rotateY()))
                    .withProperty(BlockBatteryBank.WEST, isFacingOutput(facing.rotateYCCW()))
                    .withProperty(BlockBatteryBank.UP, isFacingOutput(EnumFacing.UP))
                    .withProperty(BlockBatteryBank.DOWN, isFacingOutput(EnumFacing.DOWN));
            world.setBlockState(pos, state, 3);
            this.Sync();
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
        EnumFacing value = this.world.getBlockState(this.pos).getValue(BlockBatteryBank.FACING);
        return value;
    }

    public float GetTankFill() //0 ~ 180
    {
        float currentAmount = container.getEnergyStored() / 1000F;
        float totalCapacity = container.getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
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
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("StoredIR", this.container.serializeNBT());
        final int[] enabledFacingIndices = outPutFacings.stream()
                .mapToInt(EnumFacing::getIndex)
                .toArray();
        compound.setIntArray("OutputFacings", enabledFacingIndices);
        return super.writeToNBT(compound);
    }
}
