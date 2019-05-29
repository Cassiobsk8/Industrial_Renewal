package cassiokf.industrialrenewal.tileentity.energy.batterybank;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntitySyncable;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityBatteryBank extends TileEntitySyncable implements ICapabilityProvider, ITickable
{
    private final VoltsEnergyContainer container;

    public TileEntityBatteryBank() {
        this.container = new VoltsEnergyContainer(IRConfig.batteryBankCapacity, IRConfig.batteryBankMaxInput, IRConfig.batteryBankMaxOutput)
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
    }

    @Override
    public void update() {
        if (this.hasWorld() && !this.world.isRemote) {
            World world = this.getWorld();
            BlockPos pos = this.getPos();
            for (EnumFacing face : EnumFacing.VALUES) {
                TileEntity te = world.getTileEntity(pos.offset(face));
                if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())
                        && (!(world.getBlockState(pos.offset(face)).getBlock() instanceof BlockBatteryBank) || (face == EnumFacing.DOWN && world.getBlockState(pos.offset(face)).getBlock() instanceof BlockBatteryBank))) {
                    IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
                    if (eStorage != null && eStorage.canReceive()) {
                        this.container.extractEnergy(eStorage.receiveEnergy(this.container.extractEnergy(this.container.getMaxOutput(), true), false), false);
                    }
                }
            }
        }
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
        float currentAmount = container.getEnergyStored() / 1000;
        float totalCapacity = container.getMaxEnergyStored() / 1000;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability == CapabilityEnergy.ENERGY) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return (T) this.container;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.container.deserializeNBT(compound.getCompoundTag("StoredIR"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("StoredIR", this.container.serializeNBT());
        return super.writeToNBT(compound);
    }
}
