package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.MultiBlockHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFluidPipe extends TileEntityMultiBlocksTube<TileEntityFluidPipe>
{
    public int averageFluid;

    public int maxOutput = IRConfig.MainConfig.Main.maxFluidPipeTransferAmount;
    private int oldFluid;
    private int tick;
    private boolean inUse = false;
    public FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME)
    {
        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            return TileEntityFluidPipe.this.onFluidReceived(resource, doFill);
        }
    };

    @Override
    public void tick()
    {
        if (!world.isRemote && isMaster())
        {
            if (tick >= 10)
            {
                tick = 0;
                averageFluid = outPut / 10;
                outPut = 0;
                if (averageFluid != oldFluid)
                {
                    oldFluid = averageFluid;
                    sync();
                }
            }
            tick++;
        }
    }

    public int onFluidReceived(FluidStack resource, boolean doFill)
    {
        if (!isMaster() && !isMasterInvalid()) return getMaster().onFluidReceived(resource, doFill);

        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;
        if (resource == null || resource.amount <= 0)
        {
            inUse = false;
            return 0;
        }
        List<Integer> out = MultiBlockHelper.outputFluid(this, resource, maxOutput, doFill, world);
        if (doFill) outPut += out.get(0);
        outPutCount = out.get(1);
        inUse = false;
        return out.get(0);
    }

    @Override
    public void checkForOutPuts()
    {
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = te != null
                    && !(te instanceof TileEntityFluidPipe)
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
                addMachine(te, face);
            } else removeMachine(te);
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityFluidPipe || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
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
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getMaster() != null)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getMaster().tank);
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("fluid_average", averageFluid);
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        averageFluid = tagCompound.getInteger("fluid_average");
        super.readFromNBT(tagCompound);
    }
}
