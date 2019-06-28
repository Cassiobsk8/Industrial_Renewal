package cassiokf.industrialrenewal.tileentity.tubes.fluidpipe;

import cassiokf.industrialrenewal.tileentity.TileEntitySyncable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityFluidPipe extends TileEntitySyncable implements ITickable, ICapabilityProvider
{
    public FluidTank tank = new FluidTank(1000)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityFluidPipe.this.Sync();
        }
    };
/*
    public void Sync() {
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
        this.world.notifyNeighborsOfStateChange(this.pos, state.getBlock(), true);
    }*/

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update()
    {
        if (!this.world.isRemote)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                if (this.tank.getFluid() == null || this.tank.getFluidAmount() <= 0) break;

                BlockPos pos = this.getPos().offset(facing);
                TileEntity tileEntity = this.getWorld().getTileEntity(pos);

                if (tileEntity == null) continue;

                IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                if (fluidHandler == null) continue;

                boolean isPipe = tileEntity instanceof TileEntityFluidPipe;
                if (isPipe && ((TileEntityFluidPipe) tileEntity).tank.getFluidAmount() > this.tank.getFluidAmount())
                    continue;

                this.tank.drain(fluidHandler.fill(this.tank.drain(1000, false), true), true);
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) this.tank;
        }
        return null;
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
