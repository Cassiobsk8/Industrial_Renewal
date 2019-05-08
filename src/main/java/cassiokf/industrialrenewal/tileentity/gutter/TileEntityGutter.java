package cassiokf.industrialrenewal.tileentity.gutter;

import cassiokf.industrialrenewal.blocks.BlockRoof;
import cassiokf.industrialrenewal.util.FluidTankUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;

import javax.annotation.Nullable;

public class TileEntityGutter extends TileFluidHandler implements IFluidHandler, ITickable
{

    private final long PERIOD = 400L;
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public TileEntityGutter()
    {
        tank = new FluidTankUtils(this, 1000);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public void update()
    {
        if (this.hasWorld())
        {
            long thisTime = System.currentTimeMillis();
            if (this.getWorld().isRaining())
            {
                if ((thisTime - lastTime) >= PERIOD)
                {
                    lastTime = thisTime;
                    World world = this.getWorld();
                    IBlockState state = world.getBlockState(this.getPos());
                    BlockPos pos = this.getPos();
                    IBlockState actualstate = state.getActualState(world, pos);
                    EnumFacing facing = EnumFacing.DOWN;
                    Integer l = 1;
                    Integer r = 1;
                    Integer f = 1;
                    Integer fillamount = 0;

                    while (world.getBlockState(pos.offset(state.getValue(BlockGutter.FACING), f)).getBlock() instanceof BlockRoof)
                    {
                        if (world.isRainingAt(pos.offset(state.getValue(BlockGutter.FACING), f).up(2)))
                        {
                            fillamount++;
                            f++;
                        } else
                        {
                            f++;
                        }
                    }
                    tank.fillInternal(new FluidStack(FluidRegistry.WATER, fillamount), true);

                    if (actualstate.getValue(BlockGutter.ACTIVE_DOWN))
                    {
                        final TileEntity tileEntityD = world.getTileEntity(this.getPos().offset(EnumFacing.DOWN));
                        if (tileEntityD != null && !tileEntityD.isInvalid())
                        {
                            if (tileEntityD.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
                            {
                                IFluidHandler consumer = tileEntityD.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                                if (consumer != null)
                                {
                                    tank.drain(consumer.fill(tank.drain(tank.getCapacity(), false), true), true);
                                }
                            }
                        }
                    } else
                    {
                        while (world.getBlockState(pos.offset(state.getValue(BlockGutter.FACING).rotateY(), r)).getBlock() instanceof BlockGutter)
                        {
                            if (world.getBlockState(pos.offset(state.getValue(BlockGutter.FACING).rotateY(), r)).getActualState(world, pos.offset(state.getValue(BlockGutter.FACING).rotateY(), r)).getValue(BlockGutter.ACTIVE_DOWN))
                            {
                                r++;
                                break;
                            }
                            if (world.getBlockState(pos.offset(state.getValue(BlockGutter.FACING).rotateY(), r)).getActualState(world, pos.offset(state.getValue(BlockGutter.FACING).rotateY(), r)).getValue(BlockGutter.ACTIVE_RIGHT))
                            {
                                r = r + 100;
                                break;
                            }
                            r++;
                        }
                        while (world.getBlockState(pos.offset(state.getValue(BlockGutter.FACING).rotateY().getOpposite(), l)).getBlock() instanceof BlockGutter)
                        {
                            if (world.getBlockState(pos.offset(state.getValue(BlockGutter.FACING).rotateY().getOpposite(), l)).getActualState(world, pos.offset(state.getValue(BlockGutter.FACING).rotateY().getOpposite(), l)).getValue(BlockGutter.ACTIVE_DOWN))
                            {
                                l++;
                                break;
                            }
                            if (world.getBlockState(pos.offset(state.getValue(BlockGutter.FACING).rotateY().getOpposite(), l)).getActualState(world, pos.offset(state.getValue(BlockGutter.FACING).rotateY().getOpposite(), l)).getValue(BlockGutter.ACTIVE_LEFT))
                            {
                                l = l + 100;
                                break;
                            }
                            l++;
                        }
                        if (r < l)
                        {
                            facing = state.getValue(BlockGutter.FACING).rotateY();
                        }
                        if (r >= l)
                        {
                            facing = state.getValue(BlockGutter.FACING).rotateY().getOpposite();
                        }
                        final TileEntity tileEntityToFill = world.getTileEntity(pos.offset(facing));
                        if (tileEntityToFill != null && !tileEntityToFill.isInvalid())
                        {
                            if (tileEntityToFill.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
                            {
                                IFluidHandler consumer = tileEntityToFill.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                                if (consumer != null)
                                {
                                    tank.drain(consumer.fill(tank.drain(tank.getCapacity(), false), true), true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
        notifyBlockUpdate();
    }

    private void notifyBlockUpdate()
    {
        final IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return new IFluidTankProperties[]{new FluidTankProperties(tank.getInfo().fluid, tank.getInfo().capacity)};
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return facing == EnumFacing.DOWN
                    || facing == this.getWorld().getBlockState(this.getPos()).getValue(BlockGutter.FACING).rotateY()
                    || facing == this.getWorld().getBlockState(this.getPos()).getValue(BlockGutter.FACING).rotateY().getOpposite();
        }

        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (facing == this.getWorld().getBlockState(this.getPos()).getValue(BlockGutter.FACING).rotateY()
                    || facing == this.getWorld().getBlockState(this.getPos()).getValue(BlockGutter.FACING).rotateY().getOpposite()
                    || facing == EnumFacing.DOWN)
            {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
            }
            return super.getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        return super.writeToNBT(tag);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (resource == null)
            return 0;
        int canAccept = resource.amount;
        if (canAccept <= 0)
            return 0;

        return this.tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        //needsUpdate = true;
        return this.tank.drain(maxDrain, doDrain);
    }
}
