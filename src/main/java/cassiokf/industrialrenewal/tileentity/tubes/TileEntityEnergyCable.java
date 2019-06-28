package cassiokf.industrialrenewal.tileentity.tubes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
        //TODO arrumar o codigo pra distribur FE pelos cabos
        if (this.hasWorld()) {
            final TileEntity tileEntityS = this.getWorld().getTileEntity(this.getPos().offset(EnumFacing.SOUTH));


            if (tileEntityS != null && !tileEntityS.isInvalid()) {
                if (tileEntityS.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.SOUTH)) {
                    IEnergyStorage consumer = tileEntityS.getCapability(CapabilityEnergy.ENERGY, EnumFacing.SOUTH);
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

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readFromNBT(pkt.getNbtCompound());
    }

}
