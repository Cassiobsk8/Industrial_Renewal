package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGutter;
import cassiokf.industrialrenewal.blocks.BlockRoof;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityGutter extends TileEntityMultiBlocksTube<TileEntityGutter>
{
    EnumFacing blockFacing = null;

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
    public void tick()
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
                    int quantity = getMachineContainers().size() > 0 ? (this.tank.getFluidAmount() / getMachineContainers().size()) : 0;
                    for (TileEntity tileEntity : getMachineContainers().keySet())
                    {
                        if (tileEntity != null && !tileEntity.isInvalid())
                        {
                            EnumFacing facing = getMachineContainers().get(tileEntity).getOpposite();
                            if (tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
                            {
                                final IFluidHandler consumer = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
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
        IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite());
        if (hasMachine && handler != null && handler.getTankProperties().length > 0 && handler.getTankProperties()[0].canFill())
            addMachine(te, face);
        else removeMachine(te);
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
        if (blockFacing != null) return blockFacing;
        IBlockState state = world.getBlockState(pos);
        return blockFacing = state.getBlock() instanceof BlockGutter ? state.getValue(BlockGutter.FACING) : EnumFacing.NORTH;
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
