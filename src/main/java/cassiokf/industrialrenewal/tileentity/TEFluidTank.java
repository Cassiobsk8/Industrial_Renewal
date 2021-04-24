package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
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

public class TEFluidTank extends TEMultiTankBase<TEFluidTank>
{
    public final CustomFluidTank inTank = new CustomFluidTank(capacity)
    {
        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return TEFluidTank.this.getBottomTE().tank.fillInternal(resource, action);
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }
    };
    public final CustomFluidTank tank = new CustomFluidTank(capacity)
    {
        @Override
        public boolean canFill(FluidStack stack)
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
    public static final int capacity = IRConfig.Main.fluidTankCapacity.get();

    public TEFluidTank(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

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
                    IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getMasterFacing()).orElse(null);
                    if (handler != null)
                    {
                        tank.tryPassFluidInternal(1000, handler);
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
            stack.setAmount(cap);
        }
        tank.setCapacity(cap);
        sync();
    }

    public String getFluidName()
    {
        String name = tank.getFluidAmount() > 0 ? tank.getFluid().getDisplayName().getFormattedText() : "Empty";
        return name + ": " + (tank.getFluidAmount() / References.BUCKET_VOLUME) + "B / " + (tank.getCapacity() / References.BUCKET_VOLUME) + "B";
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
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
        Direction downFace = getMasterFacing().getOpposite();
        TEFluidTank master = getMaster();
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (facing == downFace && this.pos.equals(master.pos.down().offset(downFace)))
                return LazyOptional.of(() -> master.tank).cast();
            if (facing == Direction.UP && this.pos.equals(master.pos.up()))
                return LazyOptional.of(() -> master.inTank).cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        tank.writeToNBT(compound);
        compound.putInt("tankCap", tank.getCapacity());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        tank.readFromNBT(compound);
        tank.setCapacity(compound.getInt("tankCap"));
        super.read(compound);
    }
}
