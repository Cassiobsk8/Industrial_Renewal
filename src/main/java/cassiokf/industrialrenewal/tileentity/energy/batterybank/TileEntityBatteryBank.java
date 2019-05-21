package cassiokf.industrialrenewal.tileentity.energy.batterybank;

import cassiokf.industrialrenewal.network.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketBatteryBank;
import cassiokf.industrialrenewal.network.PacketReturnBatteryBank;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class TileEntityBatteryBank extends TileEntity implements ICapabilityProvider, ITickable {
    private final VoltsEnergyContainer container;
    private int prevAmount;

    public TileEntityBatteryBank() {
        this.container = new VoltsEnergyContainer(1000000, 10240, 10240) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int ret = super.receiveEnergy(maxReceive, simulate);
                if (!simulate && ret > 0)
                    TileEntityBatteryBank.this.save();
                return ret;
            }
        };
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
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

    @Override
    public void onLoad() {
        if (world.isRemote) {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnBatteryBank(this));
        }
    }

    public void save() {
        markDirty();
        if (Math.abs(prevAmount - this.container.getEnergyStored()) >= 10) {
            prevAmount = this.container.getEnergyStored();
            if (!world.isRemote) {
                NetworkHandler.INSTANCE.sendToAllAround(new PacketBatteryBank(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 16));
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

    public NBTTagCompound GetTag() {
        return this.container.serializeNBT();
    }

    public void readTankFromNBT(NBTTagCompound tag) {
        this.container.deserializeNBT(tag);
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
