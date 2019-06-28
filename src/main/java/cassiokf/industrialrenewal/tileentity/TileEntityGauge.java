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

    private EnumFacing blockFacing = EnumFacing.DOWN;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

    public String GetText()
    {
        TileEntity te = this.world.getTileEntity(pos.offset(blockFacing));
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, blockFacing.getOpposite()))
        {
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            FluidStack stack = handler == null ? null : handler.drain(1, false);
            return stack != null ? stack.getLocalizedName() : "Empty";
        } else
        {
            return "No Tank";
        }
    }

    public EnumFacing getBlockFacing()
    {
        return blockFacing;
    }

    public void setBlockFacing(EnumFacing facing)
    {
        blockFacing = facing;
        markDirty();
    }

    public EnumFacing getGaugeFacing()
    {
        EnumFacing value = this.world.getBlockState(this.pos).getValue(BlockGauge.FACING);
        return value;
    }

    public float GetTankFill() //0 ~ 180
    {
        TileEntity te = this.world.getTileEntity(pos.offset(blockFacing));
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, blockFacing.getOpposite()))
        {
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            IFluidTankProperties properties = handler == null ? null : handler.getTankProperties()[0];
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

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tag.setInteger("baseFacing", blockFacing.getIndex());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        blockFacing = EnumFacing.getFront(tag.getInteger("baseFacing"));
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }
}
