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

import javax.annotation.Nullable;

public class TileEntityFluidLoader extends TileEntityBaseLoader implements ITickable {

    public FluidTank tank = new FluidTank(16000) {
        @Override
        public boolean canFill()
        {
            return !TileEntityFluidLoader.this.isUnload();
        }

        @Override
        protected void onContentsChanged() {
            TileEntityFluidLoader.this.Sync();
        }
    };
    private int maxFlowPerTick = 200;
    private boolean checked = false;
    private boolean master;
    private boolean oldLoading;
    private float ySlide = 0;

    private int cartFluidAmount;
    private int cartFluidCapacity;

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
                    Sync();
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
        float currentAmount = cartFluidAmount / 1000F;
        float totalCapacity = cartFluidCapacity / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getTankFluidAngle()
    {
        float currentAmount = this.tank.getFluidAmount() / 1000F;
        float totalCapacity = this.tank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
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
                cartFluidAmount = cartCapability.getTankProperties()[0].getContents() != null
                        ? cartCapability.getTankProperties()[0].getContents().amount
                        : 0;
                cartFluidCapacity = cartCapability.getTankProperties()[0].getCapacity();
                if (isUnload())
                {
                    FluidStack cartStack = cartCapability.getTankProperties()[0].getContents();
                    if (cartStack != null && cartStack.amount > 0 && tank.getFluidAmount() < tank.getCapacity())
                    {
                        cartCapability.drain(tank.fillInternal(cartCapability.drain(maxFlowPerTick, false), true), true);
                        loading = true;
                        if (loading != oldLoading)
                        {
                            oldLoading = loading;
                            Sync();
                        }
                        return true;
                    }
                    loading = false;
                    if (loading != oldLoading)
                    {
                        oldLoading = loading;
                        Sync();
                    }
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return cartStack != null && cartStack.amount > 0;
                    }
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return cartStack == null || cartStack.amount < cartCapability.getTankProperties()[0].getCapacity();
                    }
                    if (waitE == waitEnum.NO_ACTIVITY) return false;
                } else
                {
                    FluidStack cartStack = cartCapability.getTankProperties()[0].getContents();
                    if (tank.getFluidAmount() > 0 && (cartStack == null || cartStack.amount < cartCapability.getTankProperties()[0].getCapacity()))
                    {
                        tank.drain(cartCapability.fill(tank.drain(maxFlowPerTick, false), true), true);
                        loading = true;
                        if (loading != oldLoading)
                        {
                            oldLoading = loading;
                            Sync();
                        }
                        return true;
                    }
                    loading = false;
                    if (loading != oldLoading)
                    {
                        oldLoading = loading;
                        Sync();
                    }
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return cartStack == null || cartStack.amount < cartCapability.getTankProperties()[0].getCapacity();
                    }
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return cartStack != null && cartStack.amount > 0;
                    }
                    if (waitE == waitEnum.NO_ACTIVITY) return false;
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
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        tank.readFromNBT(compound);
        cartFluidCapacity = compound.getInteger("capacity");
        cartFluidAmount = compound.getInteger("cartAmount");
        cartActivity = compound.getInteger("activity");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        return (facing == getBlockFacing().getOpposite() && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (facing == getBlockFacing().getOpposite() && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }
}
