package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.Utils;
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

public class TEFluidTank extends TEMultiTankBase<TEFluidTank>
{
    public static final int capacity = IRConfig.MainConfig.Main.fluidTankCapacity;
    public final FluidTank tank = new FluidTank(capacity)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        protected void onContentsChanged()
        {
            TEFluidTank.this.sync();
        }
    };
    public final FluidTank inTank = new FluidTank(capacity)
    {
        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            return TEFluidTank.this.getBottomTE().tank.fillInternal(resource, doFill);
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }
    };

    @Override
    public void onTick()
    {
        if (!world.isRemote && isMaster() && isBottom())
        {
            if (tank.getFluidAmount() > 0)
            {
                TileEntity te = world.getTileEntity(getOutPos());
                if (te != null)
                {
                    IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getMasterFacing());
                    if (handler != null)
                    {
                        tank.drainInternal(handler.fill(tank.drainInternal(1000, false), true), true);
                    }
                }
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TEFluidTank;
    }

    @Override
    public void setSize(int i)
    {
        int cap = i * capacity;
        if (cap < 0) cap = Integer.MAX_VALUE;
        if (cap < tank.getFluidAmount())
        {
            FluidStack stack = tank.getFluid();
            stack.amount = cap;
        }
        tank.setCapacity(cap);
        sync();
    }

    public String getFluidName()
    {
        String name = tank.getFluidAmount() > 0 ? tank.getFluid().getLocalizedName() : "Empty";
        return name + ": " + (tank.getFluidAmount() / Fluid.BUCKET_VOLUME) + "B / " + (tank.getCapacity() / Fluid.BUCKET_VOLUME) + "B";
    }

    public float getFluidAngle()
    {
        return Utils.normalizeClamped(tank.getFluidAmount(), 0, tank.getCapacity()) * 180f;
    }

    private BlockPos getOutPos()
    {
        return pos.down().offset(getMasterFacing().getOpposite(), 2);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        EnumFacing downFace = getMasterFacing().getOpposite();
        TEFluidTank master = getMaster();
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (facing == downFace && this.pos.equals(master.pos.down().offset(downFace)))
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(master.tank);
            if (facing == EnumFacing.UP && this.pos.equals(master.pos.up()))
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(master.inTank);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        tank.writeToNBT(compound);
        compound.setInteger("tankCap", tank.getCapacity());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        tank.readFromNBT(compound);
        tank.setCapacity(compound.getInteger("tankCap"));
        super.readFromNBT(compound);
    }
}
