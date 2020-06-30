package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.blocks.railroad.BlockFluidLoader;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class TileEntityFluidLoader extends TileEntityBaseLoader implements ITickable {

    public final FluidTank tank = new FluidTank(16000)
    {
        @Override
        public boolean canFill()
        {
            return !TileEntityFluidLoader.this.isUnload();
        }

        @Override
        protected void onContentsChanged()
        {
            TileEntityFluidLoader.this.sync();
        }
    };
    private int maxFlowPerTick = 200;
    private boolean checked = false;
    private boolean master;
    private float ySlide = 0;

    private int cartFluidAmount;
    private int cartFluidCapacity;
    private int noActivity = 0;

    @Override
    public void update()
    {
        if (isMaster())
        {
            if (!world.isRemote)
            {
                if (cartActivity > 0)
                {
                    cartActivity--;
                    sync();
                }
                if (isUnload() && tank.getFluidAmount() > 0)
                {
                    TileEntity te = world.getTileEntity(pos.offset(getBlockFacing().getOpposite()));
                    if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getBlockFacing()))
                    {
                        IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getBlockFacing());
                        if (handler != null)
                            tank.drain(handler.fill(tank.drain(maxFlowPerTick, false), true), true);
                    }
                }
            } else
            {
                if (loading)
                {
                    ySlide = Utils.lerp(ySlide, 0.5f, 0.08f);
                } else
                {
                    ySlide = Utils.lerp(ySlide, 0, 0.04f);
                }
            }
        }
    }

    public boolean isMaster()
    {
        if (!checked)
        {
            master = world.getBlockState(pos).getValue(BlockChunkLoader.MASTER);
            checked = true;
        }
        return master;
    }

    public String getTankText()
    {
        if (tank.getFluid() == null) return I18n.format("gui.industrialrenewal.fluid.empty");
        return I18n.format("render.industrialrenewal.fluid") + ": " + tank.getFluid().getLocalizedName();
    }

    public String getCartName()
    {
        if (cartActivity <= 0) return "No Cart";
        return cartName;
    }

    public float getSlide()
    {
        return ySlide;
    }

    public float getCartFluidAngle()
    {
        if (cartActivity <= 0) return 0;
        float currentAmount = cartFluidAmount;
        float totalCapacity = cartFluidCapacity;
        return Utils.normalize(currentAmount, 0, totalCapacity) * 180f;
    }

    public float getTankFluidAngle()
    {
        float currentAmount = tank.getFluidAmount();
        float totalCapacity = tank.getCapacity();
        return Utils.normalize(currentAmount, 0, totalCapacity) * 180f;
    }

    @Override
    public boolean onMinecartPass(EntityMinecart cart, TileEntityLoaderRail loaderRail)
    {
        if (!world.isRemote && isMaster())
        {
            cartName = cart.getName();
            cartActivity = 10;
            IFluidHandler cartCapability = cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
            if (cartCapability != null)
            {
                final IFluidTankProperties properties = cartCapability.getTankProperties()[0];
                if (properties == null) return false;

                cartFluidAmount = properties.getContents() != null
                        ? properties.getContents().amount
                        : 0;
                cartFluidCapacity = properties.getCapacity();
                FluidStack cartStack = properties.getContents();
                if (isUnload())
                {
                    if (cartStack != null && cartStack.amount > 0 && tank.getFluidAmount() < tank.getCapacity())
                    {
                        cartCapability.drain(tank.fillInternal(cartCapability.drain(maxFlowPerTick, false), true), true);
                        loading = true;
                        noActivity = 0;
                        return true;
                    }
                    loading = false;
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return cartStack != null && cartStack.amount > 0;
                    } else if (waitE == waitEnum.WAIT_FULL)
                    {
                        return cartStack == null || cartStack.amount < cartFluidCapacity;
                    }
                } else
                {
                    if (tank.getFluidAmount() > 0 && (cartStack == null || cartStack.amount < cartFluidCapacity))
                    {
                        tank.drain(cartCapability.fill(tank.drain(maxFlowPerTick, false), true), true);
                        loading = true;
                        noActivity = 0;
                        return true;
                    }

                    loading = false;
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return cartStack == null || cartStack.amount < cartFluidCapacity;
                    } else if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return cartStack != null && cartStack.amount > 0;
                    }
                }
                if (waitE == waitEnum.NO_ACTIVITY)
                {
                    noActivity++;
                    return noActivity < 10;
                }
            }
        }
        return waitE == waitEnum.NEVER; //false
    }

    @Override
    public boolean isUnload()
    {
        return unload;
    }

    @Override
    public EnumFacing getBlockFacing()
    {
        if (blockFacing == null) blockFacing = world.getBlockState(pos).getValue(BlockFluidLoader.FACING);
        return blockFacing;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        tank.writeToNBT(compound);
        compound.setInteger("capacity", cartFluidCapacity);
        compound.setInteger("cartAmount", cartFluidAmount);
        compound.setInteger("activity", cartActivity);
        compound.setBoolean("loading", loading);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        tank.readFromNBT(compound);
        cartFluidCapacity = compound.getInteger("capacity");
        cartFluidAmount = compound.getInteger("cartAmount");
        cartActivity = compound.getInteger("activity");
        loading = compound.getBoolean("loading");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        return (facing == getBlockFacing().getOpposite() && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        return (facing == getBlockFacing().getOpposite() && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank)
                : super.getCapability(capability, facing);
    }
}
