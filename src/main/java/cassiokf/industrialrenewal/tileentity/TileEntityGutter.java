package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockGutter;
import cassiokf.industrialrenewal.blocks.BlockRoof;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityGutter extends TileEntityMultiBlocksTube<TileEntityGutter>
{
    Direction blockFacing = null;

    public final CustomFluidTank tank = new CustomFluidTank(1000)
    {
        @Override
        public boolean canFill(FluidStack stack)
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

    public TileEntityGutter(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
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
                        if (tileEntity != null && !tileEntity.isRemoved())
                        {
                            Direction facing = getMachineContainers().get(tileEntity).getOpposite();
                            final IFluidHandler consumer = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
                            if (consumer != null)
                            {
                                tank.tryPassFluid(quantity, consumer);
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
    public void checkForOutPuts()
    {
        if (world.isRemote) return;
        Direction face = Direction.DOWN;
        BlockPos currentPos = pos.offset(face);
        BlockState state = world.getBlockState(currentPos);
        TileEntity te = world.getTileEntity(currentPos);
        if (te == null) return;
        boolean hasMachine = !(state.getBlock() instanceof BlockGutter);
        IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).orElse(null);
        if (hasMachine && handler != null && handler.getTanks() > 0)
            addMachine(te, face);
        else removeMachine(te);
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
        if (blockFacing != null) return blockFacing;
        BlockState state = world.getBlockState(pos);
        return blockFacing = state.getBlock() instanceof BlockGutter ? state.get(BlockGutter.FACING) : Direction.NORTH;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == Direction.DOWN)
            return LazyOptional.of(() -> getMaster().tank).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(final CompoundNBT tag)
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
