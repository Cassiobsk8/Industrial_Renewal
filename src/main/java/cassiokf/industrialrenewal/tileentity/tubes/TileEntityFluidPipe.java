package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityFluidPipe extends TileEntityMultiBlocksTube<TileEntityFluidPipe> implements ICapabilityProvider
{
    public FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityFluidPipe.this.markDirty();
        }
    };

    public int maxOutput = IRConfig.MainConfig.Main.maxFluidPipeTransferAmount;

    @Override
    public void update()
    {
        if (!world.isRemote && isMaster())
        {
            int quantity = getPosSet().size();
            this.tank.setCapacity(Math.max(maxOutput * quantity, this.tank.getFluidAmount()));

            int canAccept = moveFluid(true, 1);
            outPut = canAccept > 0 ? moveFluid(false, canAccept) : 0;

            outPutCount = getPosSet().size();
            if ((oldOutPut != outPut) || (oldOutPutCount != outPutCount))
            {
                oldOutPut = outPut;
                oldOutPutCount = outPutCount;
                this.Sync();
            }
        }
    }

    public int moveFluid(boolean simulate, int validOutputs)
    {
        int canAccept = 0;
        int out = 0;
        int realMaxOutput = Math.min(tank.getFluidAmount() / validOutputs, maxOutput);
        for (BlockPos posM : getPosSet().keySet())
        {
            TileEntity te = world.getTileEntity(posM);
            EnumFacing face = getPosSet().get(posM).getOpposite();
            if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face))
            {
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
                if (tankStorage != null
                        && tankStorage.getTankProperties() != null
                        && tankStorage.getTankProperties().length > 0
                        && tankStorage.getTankProperties()[0].canFill()
                        && this.tank.drain(maxOutput, false) != null)
                {
                    int fluid = tankStorage.fill(this.tank.drain(realMaxOutput, false), !simulate);
                    if (simulate)
                    {
                        if (fluid > 0) canAccept++;
                    } else
                    {
                        out += fluid;
                        this.tank.drain(fluid, true);
                    }
                }
            }
        }
        return simulate ? canAccept : out;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = te != null
                    && !instanceOf(te)
                    && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite());
            IFluidHandler machineCap = null;
            if (hasMachine)
                machineCap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite());
            if (hasMachine
                    && machineCap != null
                    && machineCap.getTankProperties() != null
                    && machineCap.getTankProperties().length > 0
                    && machineCap.getTankProperties()[0].canFill())
            {
                getMaster().addMachine(currentPos, face);
            }
            else getMaster().removeMachine(pos, currentPos);
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityFluidPipe || te instanceof TileEntityCableTray;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getMaster() != null)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getMaster().tank);
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tank.writeToNBT(tag);
        tagCompound.setTag("fluid", tag);
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        NBTTagCompound tag = tagCompound.getCompoundTag("fluid");
        tank.readFromNBT(tag);
        super.readFromNBT(tagCompound);
    }
}
