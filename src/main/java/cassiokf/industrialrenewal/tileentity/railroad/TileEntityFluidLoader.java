package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.blocks.railroad.BlockFluidLoader;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

import static cassiokf.industrialrenewal.init.TileRegistration.FLUIDLOADER_TILE;

public class TileEntityFluidLoader extends TileEntityBaseLoader implements ITickableTileEntity
{

    public CustomFluidTank tank = new CustomFluidTank(16000)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return !TileEntityFluidLoader.this.isUnload();
        }

        @Override
        protected void onContentsChanged()
        {
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

    public TileEntityFluidLoader()
    {
        super(FLUIDLOADER_TILE.get());
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
                    Sync();
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
        return I18n.format("render.industrialrenewal.fluid") + ": " + tank.getFluid().getDisplayName().getString();
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
        float currentAmount = tank.getFluidAmount() / 1000F;
        float totalCapacity = tank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    @Override
    public boolean onMinecartPass(AbstractMinecartEntity cart, TileEntityLoaderRail loaderRail)
    {
        if (!world.isRemote && isMaster())
        {
            cartName = cart.getName().getString();
            cartActivity = 10;
            IFluidHandler cartCapability = cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).orElse(null);
            if (cartCapability != null)
            {
                cartFluidAmount = cartCapability.getFluidInTank(0).getAmount();
                cartFluidCapacity = cartCapability.getTankCapacity(0);
                if (isUnload())
                {
                    if (cartFluidAmount > 0 && tank.getFluidAmount() < tank.getCapacity())
                    {
                        cartCapability.drain(tank.fill(cartCapability.drain(maxFlowPerTick, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
                        return cartFluidAmount > 0;
                    }
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return cartFluidAmount < cartCapability.getTankCapacity(0);
                    }
                    if (waitE == waitEnum.NO_ACTIVITY) return false;
                } else
                {
                    FluidStack cartStack = cartCapability.getFluidInTank(0);
                    if (tank.getFluidAmount() > 0 && (cartFluidAmount < cartCapability.getTankCapacity(0)))
                    {
                        tank.drain(cartCapability.fill(tank.drain(maxFlowPerTick, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
                        return cartFluidAmount < cartCapability.getTankCapacity(0);
                    }
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return cartFluidAmount > 0;
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
    public Direction getBlockFacing()
    {
        if (blockFacing == null) blockFacing = world.getBlockState(pos).get(BlockFluidLoader.FACING);
        return blockFacing;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        tank.writeToNBT(compound);
        compound.putInt("capacity", cartFluidCapacity);
        compound.putInt("cartAmount", cartFluidAmount);
        compound.putInt("activity", cartActivity);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        tank.readFromNBT(compound);
        cartFluidCapacity = compound.getInt("capacity");
        cartFluidAmount = compound.getInt("cartAmount");
        cartActivity = compound.getInt("activity");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (facing == getBlockFacing().getOpposite() && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return LazyOptional.of(() -> tank).cast();
        }
        return super.getCapability(capability, facing);
    }
}
