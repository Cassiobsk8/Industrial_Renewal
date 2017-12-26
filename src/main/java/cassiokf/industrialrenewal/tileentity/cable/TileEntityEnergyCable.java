package cassiokf.industrialrenewal.tileentity.cable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityEnergyCable extends TileEntity implements ICapabilityProvider, ITickable {

    private final BaseEnergyContainer container;

    public TileEntityEnergyCable() {
        this.container = new BaseEnergyContainer();
    }

    //IEnergyStorage
    @Override
    public void update() {
        if (this.hasWorld()) {
            final TileEntity tileEntityS = this.getWorld().getTileEntity(this.getPos().offset(EnumFacing.SOUTH));
            final TileEntity tileEntityN = this.getWorld().getTileEntity(this.getPos().offset(EnumFacing.NORTH));
            final TileEntity tileEntityE = this.getWorld().getTileEntity(this.getPos().offset(EnumFacing.EAST));
            final TileEntity tileEntityW = this.getWorld().getTileEntity(this.getPos().offset(EnumFacing.WEST));
            final TileEntity tileEntityU = this.getWorld().getTileEntity(this.getPos().offset(EnumFacing.UP));
            final TileEntity tileEntityD = this.getWorld().getTileEntity(this.getPos().offset(EnumFacing.DOWN));

            if (tileEntityS != null && !tileEntityS.isInvalid()) {
                if (tileEntityS.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.SOUTH)) {
                    IEnergyStorage consumer = tileEntityS.getCapability(CapabilityEnergy.ENERGY, EnumFacing.SOUTH);
                    if (consumer != null)
                        this.container.extractEnergy(consumer.receiveEnergy(this.container.getEnergyStored(), false), false);
                }
                if (tileEntityN.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH)) {
                    IEnergyStorage consumer = tileEntityN.getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH);
                    if (consumer != null)
                        this.container.extractEnergy(consumer.receiveEnergy(this.container.getEnergyStored(), false), false);
                }
                if (tileEntityE.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.EAST)) {
                    IEnergyStorage consumer = tileEntityE.getCapability(CapabilityEnergy.ENERGY, EnumFacing.EAST);
                    if (consumer != null)
                        this.container.extractEnergy(consumer.receiveEnergy(this.container.getEnergyStored(), false), false);
                }
                if (tileEntityW.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.WEST)) {
                    IEnergyStorage consumer = tileEntityW.getCapability(CapabilityEnergy.ENERGY, EnumFacing.WEST);
                    if (consumer != null)
                        this.container.extractEnergy(consumer.receiveEnergy(this.container.getEnergyStored(), false), false);
                }
                if (tileEntityU.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
                    IEnergyStorage consumer = tileEntityU.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
                    if (consumer != null)
                        this.container.extractEnergy(consumer.receiveEnergy(this.container.getEnergyStored(), false), false);
                }
                if (tileEntityD.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)) {
                    IEnergyStorage consumer = tileEntityD.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
                    if (consumer != null)
                        this.container.extractEnergy(consumer.receiveEnergy(this.container.getEnergyStored(), false), false);
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return true;
        return super.hasCapability(capability, facing);
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
        //this.container.deserializeNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        //compound.setInteger("StoredJAE", this.container.getEnergyStored());
        compound.setTag("StoredIR", this.container.serializeNBT());
        return super.writeToNBT(compound);
    }

}
