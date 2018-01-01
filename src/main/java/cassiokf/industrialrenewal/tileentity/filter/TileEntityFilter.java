package cassiokf.industrialrenewal.tileentity.filter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nullable;

import static cassiokf.industrialrenewal.tileentity.filter.BlockFilter.FACING;

public class TileEntityFilter extends TileFluidHandler implements /*IFluidHandler,*/ ITickable {

    public FluidTankTile tank;
    private boolean hasFluid = false;
    private boolean changed = false;
    private boolean doRender = false;

    TileEntityFilter() {
        this.tank = new FluidTankTile(1000);
        this.tank.setTileEntity(this);
        //tank = new ValveUtils(this, 1000);
    }

    @Override
    public void update() {
        if (this.hasWorld()) {
            EnumFacing facetofill = this.getWorld().getBlockState(this.getPos()).getValue(FACING).rotateY();
            final TileEntity tileEntityS = this.getWorld().getTileEntity(this.getPos().offset(facetofill));
            if (tileEntityS != null) {
                if (tileEntityS.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facetofill.getOpposite())) {
                    IFluidHandler consumer = tileEntityS.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facetofill.getOpposite());
                    if (consumer != null && tank != null) {
                        tank.drain(consumer.fill(tank.drain(tank.getCapacity(), false), true), true);
                    }
                }
            }
        }
        /*
        doRender = false;
        changed = hasFluid;
        if (tank != null && tank.getFluidAmount() > 0) {
            hasFluid = true;
        }
        if (tank.getFluidAmount() == 0 || tank == null) {
            hasFluid = false;
        }
        if (changed != hasFluid) {
            doRender = true;
        }
        //System.out.println("END " + doRender);
        if (doRender) {
            final IBlockState state = this.getWorld().getBlockState(getPos());
            this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 1);
            markDirty();
        }
        */
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Nullable
    public FluidStack getFluidName() {
        if (tank.getFluidAmount() > 0) {
            return tank.getFluid();
        } else {
            return null;
        }
    }

    @Nullable
    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        notifyBlockUpdate();
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
        notifyBlockUpdate();
    }

    private void notifyBlockUpdate() {
        final IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("FluidData")) {
            this.tank = tag.hasKey("FluidData")
                    ? new FluidTankTile(FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("FluidData")), 1000)
                    : new FluidTankTile(1000);
        }
        this.tank.setTileEntity(this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (this.tank != null) {
            if (this.tank.getFluid() != null) {
                final NBTTagCompound tankTag = new NBTTagCompound();
                this.tank.getFluid().writeToNBT(tankTag);
                tag.setTag("FluidData", tankTag);
            }
        }
        return tag;
    }

    //Capability

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) {
            if (this.getWorld().getBlockState(this.getPos()).getValue(FACING) == EnumFacing.NORTH || this.getWorld().getBlockState(this.getPos()).getValue(FACING) == EnumFacing.SOUTH) {
                if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                    return true;
                }
                return false;
            } else {
                if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
                    return true;
                }
                return false;
            }
        }

        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == this.getWorld().getBlockState(this.getPos()).getValue(FACING).rotateY() || facing == this.getWorld().getBlockState(this.getPos()).getValue(FACING).rotateY().getOpposite()) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
            }
            return super.getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }
    //IFluidHandler
/*
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null)
            return 0;
        int canAccept = resource.amount;
        if (canAccept <= 0)
            return 0;

        return this.tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        //needsUpdate = true;
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{new FluidTankProperties(tank.getInfo().fluid, tank.getInfo().capacity)};
    }
*/
}
