package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGutter;
import cassiokf.industrialrenewal.blocks.BlockRoof;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityGutter extends TileEntityMultiBlocksTube<TileEntityGutter> implements ICapabilityProvider, ITickableTileEntity
{

    public CustomFluidTank tank = new CustomFluidTank(1000)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return stack.getFluid().equals(Fluids.WATER);
        }

        @Override
        protected void onContentsChanged()
        {
            TileEntityGutter.this.markDirty();
        }
    };
    private int tick;
    private int fillAmount;

    public TileEntityGutter()
    {
        super(TileEntityRegister.GUTTER);
    }

    @Override
    public void tick()
    {
        if (this.hasWorld() && !world.isRemote)
        {
            if (isMaster())
            {
                if (world.isRaining())
                {
                    tank.fill(new FluidStack(Fluids.WATER, fillAmount), IFluidHandler.FluidAction.EXECUTE);
                }
                if (tank.getFluidAmount() > 0)
                {
                    int quantity = getPosSet().size() > 0 ? (tank.getFluidAmount() / getPosSet().size()) : 0;
                    for (BlockPos outPutPos : getPosSet().keySet())
                    {
                        final TileEntity tileEntity = world.getTileEntity(outPutPos);
                        if (tileEntity != null && !tileEntity.isRemoved())
                        {
                            Direction facing = getPosSet().get(outPutPos);
                            final IFluidHandler consumer = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                            if (consumer != null)
                            {
                                tank.drain(consumer.fill(tank.drain(quantity, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
        Direction face = Direction.DOWN;
        BlockPos currentPos = pos.offset(face);
        BlockState state = world.getBlockState(currentPos);
        TileEntity te = world.getTileEntity(currentPos);
        boolean hasMachine = !(state.getBlock() instanceof BlockGutter) && te != null;
        if (hasMachine && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).isPresent())
            if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
            else if (!isMasterInvalid()) getMaster().removeMachine(pos, currentPos);
    }

    public void checkIfIsReady()
    {
        int f = 1;
        fillAmount = 0;

        Direction face = getBlockFacing();
        while (world.getBlockState(pos.offset(face, f)).getBlock() instanceof BlockRoof)
        {
            getMaster().fillAmount++;
            f++;
        }
    }

    @Override
    public Direction[] getFacesToCheck()
    {
        Direction facing = getBlockFacing();
        return new Direction[]{facing.rotateY(), facing.rotateYCCW()};
    }

    public Direction getBlockFacing()
    {
        return world.getBlockState(pos).get(BlockGutter.FACING);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == Direction.DOWN)
            return LazyOptional.of(() -> tank).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        tank.readFromNBT(tag.getCompound("tank"));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        tag.put("tank", tank.writeToNBT(new CompoundNBT()));
        return super.write(tag);
    }
}
