package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGauge;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import static cassiokf.industrialrenewal.init.TileRegistration.GAUGE_TILE;

public class TileEntityGauge extends TileEntity
{
    private Direction baseFacing = Direction.DOWN;
    private Direction indicatorHorizontalFacing;
    private IFluidHandler tankStorage;

    public TileEntityGauge()
    {
        super(GAUGE_TILE.get());
    }

    public Direction getBaseFacing()
    {
        return baseFacing;
    }

    public void setBaseFacing(Direction facing)
    {
        baseFacing = facing;
        markDirty();
    }

    public Direction getGaugeFacing()
    {
        if (indicatorHorizontalFacing != null) return indicatorHorizontalFacing;
        return forceIndicatorCheck();
    }

    public Direction forceIndicatorCheck()
    {
        indicatorHorizontalFacing = getBlockState().get(BlockGauge.FACING);
        return indicatorHorizontalFacing;
    }

    public String GetText()
    {
        if (getTankStorage() != null)
        {
            IFluidHandler properties = getTankStorage();
            if (properties != null)
            {
                FluidStack stack = properties.getFluidInTank(0);
                return stack != null ? stack.getDisplayName().getString() : "Empty";
            }
        }
        return "No Tank";
    }

    public float GetTankFill() //0 ~ 180
    {
        if (getTankStorage() != null)
        {
            IFluidHandler properties = getTankStorage();
            if (properties != null)
            {
                float currentAmount = properties.getFluidInTank(0).getAmount() / 1000f;
                float totalCapacity = properties.getTankCapacity(0) / 1000f;
                currentAmount = currentAmount / totalCapacity;
                return currentAmount * 180f;
            }
        }
        return 0;
    }

    private IFluidHandler getTankStorage()
    {
        if (tankStorage != null) return tankStorage;
        return forceCheck();
    }

    public IFluidHandler forceCheck()
    {
        TileEntity te = world.getTileEntity(pos.offset(baseFacing));
        if (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, baseFacing.getOpposite()).isPresent())
        {
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, baseFacing.getOpposite()).orElse(null);
            if (handler != null)
            {
                tankStorage = handler;
                return tankStorage;
            }
        }
        tankStorage = null;
        return null;
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        tag.putInt("baseFacing", baseFacing.getIndex());
        return super.write(tag);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        baseFacing = Direction.byIndex(tag.getInt("baseFacing"));
        super.read(tag);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return write(new CompoundNBT());
    }
}
