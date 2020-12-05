package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.MultiBlockHelper;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFluidPipe extends TileEntityMultiBlocksTube<TileEntityFluidPipe>
{
    public int averageFluid;

    public int maxOutput = IRConfig.Main.maxFluidPipeTransferAmount.get();
    private int oldFluid;
    private int tick;
    private boolean inUse = false;
    public CustomFluidTank tank = new CustomFluidTank(References.BUCKET_VOLUME)
    {
        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return TileEntityFluidPipe.this.onFluidReceived(resource, action);
        }
    };

    public TileEntityFluidPipe(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
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

    public int onFluidReceived(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (!isMaster() && !isMasterInvalid()) return getMaster().onFluidReceived(resource, action);

        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;
        if (resource == null || resource.getAmount() <= 0)
        {
            inUse = false;
            return 0;
        }
        List<Integer> out = MultiBlockHelper.outputFluid(this, resource, maxOutput, action, world);
        if (action.execute()) outPut += out.get(0);
        outPutCount = out.get(1);
        inUse = false;
        return out.get(0);
    }

    @Override
    public void checkForOutPuts()
    {
        if (world.isRemote) return;
        for (Direction face : Direction.values())
        {
            BlockPos currentPos = pos.offset(face);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = te != null && !(te instanceof TileEntityFluidPipe);
            IFluidHandler machineCap = null;
            if (hasMachine)
                machineCap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).orElse(null);
            if (hasMachine
                    && machineCap != null
                    && machineCap.getTanks() > 0)
            {
                addMachine(te, face);
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityFluidPipe || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getMaster() != null)
            return LazyOptional.of(() -> getMaster().tank).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound)
    {
        tagCompound.putInt("fluid_average", averageFluid);
        return super.write(tagCompound);
    }

    @Override
    public void read(CompoundNBT tagCompound)
    {
        averageFluid = tagCompound.getInt("fluid_average");
        super.read(tagCompound);
    }
}
