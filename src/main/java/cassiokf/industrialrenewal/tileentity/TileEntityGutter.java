package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGutter;
import cassiokf.industrialrenewal.blocks.BlockRoof;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityGutter extends TileEntityMultiBlocksTube<TileEntityGutter> implements ICapabilityProvider, ITickable
{

    public FluidTank tank = new FluidTank(1000)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        protected void onContentsChanged()
        {
            TileEntityGutter.this.markDirty();
        }
    };
    private int tick;
    private int fillAmount;

    @Override
    public void update()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            if (isMaster())
            {
                if (world.isRaining())
                {
                    this.tank.fillInternal(new FluidStack(FluidRegistry.WATER, fillAmount), true);
                }
                if (this.tank.getFluidAmount() > 0)
                {
                    int quantity = getPosSet().size() > 0 ? (this.tank.getFluidAmount() / getPosSet().size()) : 0;
                    for (BlockPos outPutPos : getPosSet().keySet())
                    {
                        final TileEntity tileEntity = world.getTileEntity(outPutPos);
                        if (tileEntity != null && !tileEntity.isInvalid())
                        {
                            EnumFacing facing = getPosSet().get(outPutPos);
                            if (tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
                            {
                                final IFluidHandler consumer = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                                if (consumer != null)
                                {
                                    this.tank.drain(consumer.fill(this.tank.drain(quantity, false), true), true);
                                }
                            }
                        }
                    }
                }
            }
            if (tick >= 100)
            {
                tick = 0;
                if (world.isRaining()) checkIfIsReady();
            }
            tick++;
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityGutter;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        EnumFacing face = EnumFacing.DOWN;
        BlockPos currentPos = pos.offset(face);
        IBlockState state = world.getBlockState(currentPos);
        TileEntity te = world.getTileEntity(currentPos);
        boolean hasMachine = !(state.getBlock() instanceof BlockGutter) && te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite());
        if (hasMachine && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).getTankProperties()[0].canFill())
            if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
            else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
    }

    public void checkIfIsReady()
    {
        int f = 1;
        fillAmount = 0;

        EnumFacing face = getBlockFacing();
        while (world.getBlockState(pos.offset(face, f)).getBlock() instanceof BlockRoof)
        {
            getMaster().fillAmount++;
            f++;
        }
    }

    @Override
    public EnumFacing[] getFacesToCheck()
    {
        EnumFacing facing = getBlockFacing();
        return new EnumFacing[]{facing.rotateY(), facing.rotateYCCW()};
    }

    public EnumFacing getBlockFacing()
    {
        return world.getBlockState(pos).getValue(BlockGutter.FACING);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.DOWN;
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.DOWN)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getMaster().tank);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        this.tank.readFromNBT(tag.getCompoundTag("tank"));
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tag.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(tag);
    }
}
