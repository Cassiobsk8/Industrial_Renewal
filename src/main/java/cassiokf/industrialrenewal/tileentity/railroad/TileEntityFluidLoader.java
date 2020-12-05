package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.blocks.railroad.BlockFluidLoader;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityFluidLoader extends TileEntityBaseLoader implements ITickableTileEntity
{

    public final CustomFluidTank tank = new CustomFluidTank(16000)
    {
        @Override
        public boolean canFill(FluidStack resource)
        {
            return !TileEntityFluidLoader.this.isUnload();
        }

        @Override
        protected void onContentsChanged()
        {
            TileEntityFluidLoader.this.sync();
        }
    };
    private static final int maxFlowPerTick = 200;
    private boolean checked = false;
    private boolean master;
    private float ySlide = 0;

    private int cartFluidAmount;
    private int cartFluidCapacity;
    private int noActivity = 0;

    public TileEntityFluidLoader(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
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
                    if (te != null)
                    {
                        IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getBlockFacing()).orElse(null);
                        if (handler != null)
                            tank.drain(handler.fill(tank.drain(maxFlowPerTick, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
            master = world.getBlockState(pos).get(BlockChunkLoader.MASTER);
            checked = true;
        }
        return master;
    }

    public String getTankText()
    {
        if (tank.getFluid().isEmpty()) return I18n.format("gui.industrialrenewal.fluid.empty");
        return I18n.format("render.industrialrenewal.fluid") + ": " + tank.getFluid().getDisplayName().getFormattedText();
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
    public boolean onMinecartPass(MinecartEntity cart, TileEntityLoaderRail loaderRail)
    {
        if (!world.isRemote && isMaster())
        {
            cartName = cart.getName().getFormattedText();
            cartActivity = 10;
            IFluidHandler cartCapability = cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).orElse(null);
            if (cartCapability != null)
            {
                if (cartCapability.getTanks() <= 0) return false;

                FluidStack cartStack = cartCapability.getFluidInTank(0);
                cartFluidAmount = cartStack.getAmount();
                cartFluidCapacity = cartCapability.getTankCapacity(0);

                if (isUnload())
                {
                    if (cartStack.getAmount() > 0 && tank.getFluidAmount() < tank.getCapacity())
                    {
                        cartCapability.drain(tank.fillInternal(cartCapability.drain(maxFlowPerTick, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        loading = true;
                        noActivity = 0;
                        return true;
                    }
                    loading = false;
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return cartStack.getAmount() > 0;
                    } else if (waitE == waitEnum.WAIT_FULL)
                    {
                        return cartStack.getAmount() < cartFluidCapacity;
                    }
                } else
                {
                    if (tank.getFluidAmount() > 0 && cartStack.getAmount() < cartFluidCapacity)
                    {
                        tank.drain(cartCapability.fill(tank.drain(maxFlowPerTick, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        loading = true;
                        noActivity = 0;
                        return true;
                    }

                    loading = false;
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return cartStack.getAmount() < cartFluidCapacity;
                    } else if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return cartStack.getAmount() > 0;
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
    public Direction getBlockFacing()
    {
        if (blockFacing == null) blockFacing =getBlockState().get(BlockFluidLoader.FACING);
        return blockFacing;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        tank.writeToNBT(compound);
        compound.putInt("capacity", cartFluidCapacity);
        compound.putInt("cartAmount", cartFluidAmount);
        compound.putInt("activity", cartActivity);
        compound.putBoolean("loading", loading);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        tank.readFromNBT(compound);
        cartFluidCapacity = compound.getInt("capacity");
        cartFluidAmount = compound.getInt("cartAmount");
        cartActivity = compound.getInt("activity");
        loading = compound.getBoolean("loading");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        return (facing == getBlockFacing().getOpposite() && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                ? LazyOptional.of(() -> tank).cast()
                : super.getCapability(capability, facing);
    }
}
