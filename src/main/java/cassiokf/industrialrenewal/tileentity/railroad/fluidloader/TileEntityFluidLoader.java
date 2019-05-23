package cassiokf.industrialrenewal.tileentity.railroad.fluidloader;

import cassiokf.industrialrenewal.tileentity.railroad.TileEntityBaseLoader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFluidLoader extends TileEntityBaseLoader implements ITickable {

    private int intLoadActivity = 0;
    private int intUnloadActivity = 0;
    public FluidTank tank = new FluidTank(16000) {
        @Override
        protected void onContentsChanged() {
            TileEntityFluidLoader.this.Sync();
        }
    };


    @Override
    public void update() {
        if (!world.isRemote) {
            if (this.world.getTotalWorldTime() % 8 == 0) {
                boolean activity = false;

//			    up cart -> tank
                int maxFlowPerTick = 320;
                boolean hasCart = false;

                IFluidHandler upHandler = getInventoryUp();
                if (upHandler != null) {
                    FluidStack drainedStack = upHandler.drain(maxFlowPerTick, false);
                    if (tank.getFluidAmount() < tank.getCapacity()) {
                        if (drainedStack != null && drainedStack.amount > 0) {
                            tank.fill(upHandler.drain(maxFlowPerTick, true), true);
                            activity = true;
                            intLoadActivity = 0;
                        }
                    }
                    if (isUnload()) {
                        hasCart = true;
                        if (getWaitEnum() == waitEnum.WAIT_EMPTY && (drainedStack == null || drainedStack.amount <= 0)) {
                            letCartPass(true);
                        }
                    }
                }

//			tank -> front/down cart
                IFluidHandler outHandler = getInventoryForHopperTransfer();
                if (outHandler != null) {
                    int fillAmount = outHandler.fill(tank.drain(maxFlowPerTick, false), false);
                    if (tank.getFluidAmount() > 0 && fillAmount > 0) {
                        tank.drain(outHandler.fill(tank.drain(maxFlowPerTick, false), true), true);
                        activity = true;
                        intUnloadActivity = 0;
                    }
                    if (!isUnload()) {
                        hasCart = true;
                        if (getWaitEnum() == waitEnum.WAIT_FULL && fillAmount <= 0) {
                            letCartPass(true);
                        }
                    }
                }

                if (activity) {
                    this.markDirty();
                    letCartPass(false);
                } else {
                    if (hasCart && getWaitEnum() == waitEnum.NO_ACTIVITY) {
                        if (!isUnload()) {
                            intLoadActivity++;
                        } else {
                            intUnloadActivity++;
                        }
                    }
                }

                if (!activity && getWaitEnum() == waitEnum.NO_ACTIVITY) {
                    if (!isUnload() && intLoadActivity >= 2) {
                        intLoadActivity = 0;
                        letCartPass(true);
                    } else if (isUnload() && intUnloadActivity >= 2) {
                        intUnloadActivity = 0;
                        letCartPass(true);
                    }
                }
            }
        }
    }

    private IFluidHandler getInventoryUp() {
        BlockPos handlerPos = pos.offset(EnumFacing.UP);
        return getInventoryAtPosition(this.world, handlerPos);
    }

    private IFluidHandler getInventoryForHopperTransfer() {
        EnumFacing enumfacing = getOutput();
        BlockPos handlerPos = pos.offset(enumfacing);
        if (!isUnload()) handlerPos = handlerPos.down();
        return getInventoryAtPosition(this.getWorld(), handlerPos);
    }

    /**
     * Returns the IInventory (if applicable) of the TileEntity at the specified position
     */
    private IFluidHandler getInventoryAtPosition(World worldIn, BlockPos pos) {
        IFluidHandler ifluid = null;

        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if (block.hasTileEntity(state)) {
            TileEntity te = worldIn.getTileEntity(pos);

            if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getOutput().getOpposite())) {
                ifluid = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getOutput().getOpposite());
            }
        }

        if (ifluid == null) {
            List<Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(pos.getX() - 0.5D, pos.getY() - 0.5D, pos.getZ() - 0.5D, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D), EntitySelectors.IS_ALIVE);

            if (!list.isEmpty()) {
                ifluid = list.get(worldIn.rand.nextInt(list.size())).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getOutput().getOpposite());
            }
        }

        return ifluid;
    }

    public EnumFacing getOutput() {
        if (!isUnload()) {
            return EnumFacing.DOWN;
        }
        IBlockState state = this.world.getBlockState(this.pos).getActualState(this.world, this.pos);
        return state.getValue(BlockFluidLoader.FACING).getOpposite();
    }

    @Override
    public boolean isUnload() {
        IBlockState state = this.world.getBlockState(this.pos).getActualState(this.world, this.pos);
        return state.getValue(BlockFluidLoader.UNLOAD);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        tank.writeToNBT(compound);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        tank.readFromNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }
}
