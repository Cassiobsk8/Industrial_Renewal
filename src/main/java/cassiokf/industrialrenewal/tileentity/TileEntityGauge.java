package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGauge;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityGauge extends TEBase
{

    private Direction baseFacing = Direction.DOWN;
    private Direction indicatorHorizontalFacing;
    private IFluidHandler tankStorage;

    public TileEntityGauge(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
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
        indicatorHorizontalFacing = this.world.getBlockState(this.pos).get(BlockGauge.FACING);
        return indicatorHorizontalFacing;
    }

    public String GetText()
    {
        if (getTankStorage() != null && getTankStorage().getTanks() > 0)
        {
            FluidStack stack = getTankStorage().getFluidInTank(0);
            return stack.getDisplayName().getFormattedText();
        }
        return "No Tank";
    }

    public float GetTankFill() //0 ~ 180
    {
        if (getTankStorage() != null && getTankStorage().getTanks() > 0 && !getTankStorage().getFluidInTank(0).isEmpty())
        {
            return Utils.normalize(getTankStorage().getFluidInTank(0).getAmount(), 0, getTankStorage().getTankCapacity(0)) * 180f;
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
        if (te != null)
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
    public void read(final CompoundNBT tag)
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
