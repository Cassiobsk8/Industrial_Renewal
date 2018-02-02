package cassiokf.industrialrenewal.tileentity.solarpanel;

import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntitySolarPanel extends TileEntity implements ICapabilityProvider, ITickable {

    private final VoltsEnergyContainer container;

    public TileEntitySolarPanel() {
        this.container = new VoltsEnergyContainer(256, 0, 15);
    }

    @Override
    public void update() {
        if (this.hasWorld()) {
            World world = this.getWorld();
            int connections = 0;
            if (world.provider.hasSkyLight()) {
                int i = world.getLightFor(EnumSkyBlock.SKY, pos) - world.getSkylightSubtracted();
                float f = world.getCelestialAngleRadians(1.0F);
                if (i > 0) {
                    float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
                    f = f + (f1 - f) * 0.2F;
                    i = Math.round((float) i * MathHelper.cos(f));
                }
                i = MathHelper.clamp(i, 0, 15);
                int result = this.container.getEnergyStored() + i;
                if (result > this.container.getMaxEnergyStored()) {
                    result = this.container.getMaxEnergyStored();
                }

                this.container.setEnergyStored(result);

            }
            connections = 0;
            for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
                final TileEntity tileEntity = world.getTileEntity(this.getPos().offset(facing));
                if (tileEntity != null && !tileEntity.isInvalid()) {
                    if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()) && !(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockSolarPanel)) {
                        connections = connections + 1;
                    }
                }
            }
            int energy = this.container.getEnergyStored();
            if (connections > 1) {
                energy = energy / connections;
            }
            //System.out.println("Stored " + this.container.getEnergyStored() + " Connections " + connections + " Result " + energy);
            for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
                final TileEntity tileEntity = world.getTileEntity(this.getPos().offset(facing));
                if (tileEntity != null && !tileEntity.isInvalid()) {
                    if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()) && !(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockSolarPanel)) {
                        final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
                        if (consumer != null) {

                            this.container.extractEnergy(consumer.receiveEnergy(energy, false), false);
                            //System.out.println("Facing " + facing + " Consumer: " + consumer.receiveEnergy(energy, true));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability == CapabilityEnergy.ENERGY && facing != EnumFacing.UP) || super.hasCapability(capability, facing);
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
