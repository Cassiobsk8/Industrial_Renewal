package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.BlockFluidPipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityFluidPipe extends TileEntityMultiBlocksTube<TileEntityFluidPipe> implements ITickable, ICapabilityProvider
{
    public FluidTank tank = new FluidTank(1000);

    private int maxOutput = 600;

    @Override
    public void update()
    {
        super.update();
        if (!world.isRemote && isMaster())
        {
            int quantity = getPosSet().size();
            this.tank.setCapacity(maxOutput * quantity);
            for (BlockPos posM : getPosSet().keySet())
            {
                TileEntity te = world.getTileEntity(posM);
                if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getPosSet().get(posM).getOpposite()))
                {
                    IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getPosSet().get(posM).getOpposite());
                    if (tankStorage != null && tankStorage.getTankProperties()[0].canFill())
                    {
                        this.tank.drain(tankStorage.fill(this.tank.drain(maxOutput, false), true), true);
                    }
                }
            }
        }
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos currentPos = pos.offset(face);
            IBlockState state = world.getBlockState(currentPos);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = !(state.getBlock() instanceof BlockFluidPipe)
                    && te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite());
            if (hasMachine && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).getTankProperties()[0].canFill())
                getMaster().addMachine(currentPos, face);
            else getMaster().removeMachine(pos, currentPos);
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityFluidPipe;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getMaster().tank);
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tank.writeToNBT(tag);
        tagCompound.setTag("fluid", tag);
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        NBTTagCompound tag = tagCompound.getCompoundTag("fluid");
        tank.readFromNBT(tag);
        super.readFromNBT(tagCompound);
    }
}
