package cassiokf.industrialrenewal.tileentity.valve;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.util.EnumFaceRotation;
import cassiokf.industrialrenewal.util.ValveUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import static cassiokf.industrialrenewal.tileentity.valve.BlockValvePipeLarge.ACTIVE;

public class TileEntityValvePipeLarge extends TileFluidHandler implements IFluidHandler, ITickable {

    private final Set<EnumFacing> enabledFacings = EnumSet.allOf(EnumFacing.class);
    private EnumFacing facing = EnumFacing.SOUTH;
    private EnumFaceRotation faceRotation = EnumFaceRotation.UP;
    private Boolean active = false;

    public TileEntityValvePipeLarge() {
        tank = new ValveUtils(this, 1000);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public void update() {

        if (this.hasWorld()) {
            EnumFacing facetofill = getOutPutFace(this.getWorld(), this.getPos());
            boolean Vactive = this.getWorld().getBlockState(pos).getValue(ACTIVE);
            final TileEntity tileEntityS = this.getWorld().getTileEntity(this.getPos().offset(facetofill));

            if (tileEntityS != null && !tileEntityS.isInvalid() && Vactive) {
                active = true;
                if (tileEntityS.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facetofill.getOpposite())) {
                    IFluidHandler consumer = tileEntityS.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facetofill.getOpposite());
                    if (consumer != null) {
                        tank.drain(consumer.fill(tank.drain(tank.getCapacity(), false), true), true);
                    }
                }
            }
            if (!Vactive) {
                active = false;
            }
        }
    }

    public void playSwitchSound() {
        Random r = new Random();
        float pitch = r.nextFloat() * (1.2f - 0.8f) + 0.8f;
        this.getWorld().playSound(null, this.getPos(), IRSoundHandler.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1F, pitch);
    }

    public EnumFacing getOutPutFace(World world, BlockPos pos) {
        EnumFacing vFace = this.getFacing();
        EnumFaceRotation rFace = getFaceRotation();

        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.UP) || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.DOWN) || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.UP) || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.UP)) {
            return EnumFacing.EAST;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.DOWN) || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.UP) || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.DOWN) || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.DOWN)) {
            return EnumFacing.WEST;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.LEFT) || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.LEFT) || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.LEFT) || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.LEFT)) {
            return EnumFacing.DOWN;
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.RIGHT) || (vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.RIGHT) || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.RIGHT) || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.RIGHT)) {
            return EnumFacing.UP;
        }
        if ((vFace == EnumFacing.EAST && rFace == EnumFaceRotation.UP) || (vFace == EnumFacing.WEST && rFace == EnumFaceRotation.DOWN) || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.RIGHT) || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.LEFT)) {
            return EnumFacing.SOUTH;
        }
        if ((vFace == EnumFacing.WEST && rFace == EnumFaceRotation.UP) || (vFace == EnumFacing.EAST && rFace == EnumFaceRotation.DOWN) || (vFace == EnumFacing.UP && rFace == EnumFaceRotation.LEFT) || (vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.RIGHT)) {
            return EnumFacing.NORTH;
        }
        return EnumFacing.NORTH;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(final EnumFacing facing) {
        this.facing = facing;
        markDirty();
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


    public boolean toggleFacing(final EnumFacing facing) {
        if (enabledFacings.contains(facing)) {
            enabledFacings.remove(facing);
            return false;
        } else {
            enabledFacings.add(facing);
            return true;
        }
    }

    public boolean disableFacing(final EnumFacing facing) {
        if (enabledFacings.contains(facing)) {
            enabledFacings.remove(facing);
            return false;
        } else {

            return true;
        }
    }

    public boolean activeFacing(final EnumFacing facing) {
        if (enabledFacings.contains(facing)) {
            return false;
        } else {
            enabledFacings.add(facing);
            return true;
        }
    }

    public boolean isFacingEnabled(final @Nullable EnumFacing facing) {
        return enabledFacings.contains(facing) || facing == null;
    }

    public Set<EnumFacing> getEnabledFacings() {
        return enabledFacings;
    }

    private void notifyBlockUpdate() {
        final IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        notifyBlockUpdate();
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        facing = EnumFacing.getFront(tag.getInteger("facing"));
        faceRotation = EnumFaceRotation.values()[tag.getInteger("faceRotation")];
        active = tag.getBoolean("active");

        enabledFacings.clear();

        final int[] enabledFacingIndices = tag.getIntArray("EnabledFacings");
        for (final int index : enabledFacingIndices) {
            enabledFacings.add(EnumFacing.getFront(index));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        final int[] enabledFacingIndices = enabledFacings.stream()
                .mapToInt(EnumFacing::getIndex)
                .toArray();
        tag.setInteger("facing", facing.getIndex());
        tag.setInteger("faceRotation", faceRotation.ordinal());
        tag.setIntArray("EnabledFacings", enabledFacingIndices);
        tag.setBoolean("active", this.active);

        return super.writeToNBT(tag);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return isFacingEnabled(facing);
        }

        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (isFacingEnabled(facing)) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
            }

            return null;
        }

        return super.getCapability(capability, facing);
    }

    public EnumFaceRotation getFaceRotation() {
        return faceRotation;
    }

    public void setFaceRotation(final EnumFaceRotation faceRotation) {
        this.faceRotation = faceRotation;
        markDirty();
    }
}