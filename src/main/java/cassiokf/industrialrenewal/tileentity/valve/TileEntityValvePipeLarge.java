package cassiokf.industrialrenewal.tileentity.valve;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nullable;


public class TileEntityValvePipeLarge extends TileFluidHandler {


    //public static final int CAPACITY = 1 * Fluid.BUCKET_VOLUME;

    public TileEntityValvePipeLarge() {
        tank = new FluidUtils(this, 10);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }
}
