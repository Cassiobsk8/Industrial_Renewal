package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGauge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityGauge extends TileEntity
{

    private EnumFacing baseFacing = EnumFacing.DOWN;
    private EnumFacing indicatorHorizontalFacing;
    private IFluidHandler tankStorage;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

    public EnumFacing getBaseFacing()
    {
        return baseFacing;
    }

    public void setBaseFacing(EnumFacing facing)
    {
        baseFacing = facing;
        markDirty();
    }

    public EnumFacing getGaugeFacing()
    {
        if (indicatorHorizontalFacing != null) return indicatorHorizontalFacing;
        return forceIndicatorCheck();
    }

    public EnumFacing forceIndicatorCheck()
    {
        indicatorHorizontalFacing = this.world.getBlockState(this.pos).getValue(BlockGauge.FACING);
        return indicatorHorizontalFacing;
    }

    public String GetText()
    {
        if (getTankStorage() != null && getTankStorage().getTankProperties().length > 0)
        {
            IFluidTankProperties properties = getTankStorage().getTankProperties()[0];
            if (properties != null)
            {
                FluidStack stack = properties.getContents();
                return stack != null ? stack.getLocalizedName() : "Empty";
            }
        }
        return "No Tank";
    }

    public float GetTankFill() //0 ~ 180
    {
        if (getTankStorage() != null && getTankStorage().getTankProperties().length > 0)
        {
            IFluidTankProperties properties = getTankStorage().getTankProperties()[0];
            if (properties != null && properties.getContents() != null)
            {
                float currentAmount = properties.getContents().amount / 1000f;
                float totalCapacity = properties.getCapacity() / 1000f;
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
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, baseFacing.getOpposite()))
        {
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, baseFacing.getOpposite());
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
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tag.setInteger("baseFacing", baseFacing.getIndex());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        baseFacing = EnumFacing.byIndex(tag.getInteger("baseFacing"));
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }
}
