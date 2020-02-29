package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.blocks.BlockElectricPump;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySyncable;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static cassiokf.industrialrenewal.init.TileRegistration.ELECTRICPUMP_TILE;

public class TileEntityElectricPump extends TileEntitySyncable implements ICapabilityProvider, ITickableTileEntity
{
    public CustomFluidTank tank = new CustomFluidTank(1000)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityElectricPump.this.markDirty();
        }
    };
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private int index = -1;
    private int everyXtick = 10;
    private int tick;
    private int energyPerTick = 10;
    private Direction facing;

    private List<BlockPos> fluidSet = new ArrayList<>();
    private int maxRadius = IRConfig.Main.maxPumpRadius.get();

    IEnergyStorage motorEnergy = null;

    private boolean isRunning = false;
    private boolean oldIsRunning = false;
    private boolean starting = false;
    private boolean oldStarting = false;

    public TileEntityElectricPump()
    {
        super(ELECTRICPUMP_TILE.get());
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(200, 200, 0)
        {
            @Override
            public boolean canExtract()
            {
                return false;
            }

            @Override
            public void onEnergyChange()
            {
                TileEntityElectricPump.this.Sync();
            }
        };
    }

    @Override
    public void tick()
    {
        if (!world.isRemote)
        {
            if (getIdex() == 1)
            {
                consumeEnergy();
                if (tick >= everyXtick)
                {
                    tick = 0;
                    GetFluidDown();
                }
                tick++;
                passFluidUp();
            }
        } else
        {
            if (getIdex() == 1)
            {
                handleSound();
            }
        }
    }

    private int getIdex()
    {
        if (index != -1) return index;
        BlockState state = world.getBlockState(pos);
        index = state.getBlock() instanceof BlockElectricPump ? state.get(BlockElectricPump.INDEX) : -1;
        return index;
    }

    private void handleSound()
    {
        if (!world.isRemote) return;
        if (isRunning && !starting)
        {
            IRSoundHandler.playSound(world, SoundsRegistration.PUMP_START.get(), IRConfig.Main.pumpVolume.get().floatValue() + 0.5f, 1.0F, pos);
            starting = true;
            oldStarting = true;
            Sync();
        } else if (isRunning)
        {
            IRSoundHandler.playRepeatableSound(this, SoundsRegistration.PUMP_ROTATION.get(), IRConfig.MAIN.pumpVolume.get().floatValue(), 1.0F);
        } else
        {
            IRSoundHandler.stopTileSound(pos);
            starting = false;
            if (oldStarting)
            {
                oldStarting = false;
                Sync();
            }
        }
    }

    private void consumeEnergy()
    {
        IEnergyStorage motorEnergyContainer = GetEnergyContainer();
        if (motorEnergyContainer != null && motorEnergyContainer.getEnergyStored() >= energyPerTick)
        {
            motorEnergyContainer.extractEnergy(energyPerTick, false);
            isRunning = true;
        } else
        {
            isRunning = false;
            starting = false;
            Sync();
        }
        if (oldIsRunning != isRunning || oldStarting != starting)
        {
            oldIsRunning = isRunning;
            oldStarting = starting;
            Sync();
        }
    }

    private void GetFluidDown()
    {
        if (tank.getFluidAmount() <= 0 && isRunning)
        {
            if (IRConfig.Main.pumpInfinityWater.get()
                    && world.getBlockState(pos.down()).getBlock().equals(Blocks.WATER))
            {
                tank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
            }
            if (getFluidSet() != null && !getFluidSet().isEmpty())
            {
                BlockPos fluidPos = getFluidSet().get(0);

                while (!instanceOf(fluidPos, true))
                {
                    getFluidSet().remove(fluidPos);
                    if (getFluidSet() == null || getFluidSet().isEmpty()) return;
                    fluidPos = getFluidSet().get(0);
                }

                BlockState state = world.getBlockState(fluidPos);
                IFluidHandler downFluid = Utils.wrapFluidBlock(state, world, fluidPos);

                boolean consumeFluid = !(downFluid.getFluidInTank(0).getFluid().equals(Fluids.WATER)
                        && IRConfig.Main.pumpInfinityWater.get());

                if (tank.fill(downFluid.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE) > 0 && GetEnergyContainer().getEnergyStored() >= (energyPerTick * everyXtick))
                {
                    FluidStack stack = downFluid.drain(Integer.MAX_VALUE, consumeFluid ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE);
                    if (IRConfig.Main.repleceLavaWithCobble.get() && stack != null && stack.getFluid().equals(Fluids.LAVA))
                        world.setBlockState(fluidPos, Blocks.COBBLESTONE.getDefaultState());
                    tank.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    isRunning = true;
                }
                getFluidSet().remove(fluidPos);
            }
        }
    }

    private List<BlockPos> getFluidSet()
    {
        if (fluidSet.isEmpty()) getAllFluids();
        return fluidSet;
    }

    private void getAllFluids()
    {
        if (world.getBlockState(pos.down()).getBlock() instanceof FlowingFluidBlock)
        {
            Stack<BlockPos> traversingFluids = new Stack<>();
            List<BlockPos> flowingPos = new ArrayList<>();
            traversingFluids.add(pos.down());
            while (!traversingFluids.isEmpty())
            {
                BlockPos fluidPos = traversingFluids.pop();
                if (instanceOf(fluidPos, true)) fluidSet.add(fluidPos);
                else flowingPos.add(fluidPos);

                for (Direction d : Direction.values())
                {
                    BlockPos newPos = fluidPos.offset(d);
                    if (instanceOf(newPos, false) && !fluidSet.contains(newPos) && !flowingPos.contains(newPos))
                    {
                        traversingFluids.add(newPos);
                    }
                }
            }
        }
    }

    private boolean instanceOf(BlockPos pos, boolean checkLevel)
    {
        if (pos == null) return false;
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof FlowingFluidBlock
                && (!checkLevel || state.get(FlowingFluidBlock.LEVEL) == 0)
                && getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= maxRadius * maxRadius;
    }

    private void passFluidUp()
    {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null)
        {
            if (upTank.fill(tank.drain(tank.getCapacity() / everyXtick, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0)
            {
                upTank.fill(tank.drain(tank.getCapacity() / everyXtick, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                isRunning = true;
            }
        }
    }

    @Override
    public void remove()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
        starting = false;
        super.remove();
    }

    private IEnergyStorage GetEnergyContainer()
    {
        if (getIdex() == 0) return energyStorage.orElse(null);
        if (motorEnergy != null) return motorEnergy;
        BlockState state = getBlockState();
        if (state.getBlock() instanceof BlockElectricPump)
        {
            Direction facing = state.get(BlockElectricPump.FACING);
            TileEntityElectricPump te = (TileEntityElectricPump) world.getTileEntity(this.pos.offset(facing.getOpposite()));
            if (te != null)
            {
                motorEnergy = te.energyStorage.orElse(null);
            }
        }
        return motorEnergy;
    }

    private IFluidHandler GetTankUp()
    {
        TileEntity upTE = world.getTileEntity(this.pos.up());
        if (upTE != null)
        {
            return upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
        }
        return null;
    }

    private Direction getBlockFacing()
    {
        if (facing != null) return facing;
        BlockState state = world.getBlockState(pos);
        facing = state.get(BlockElectricPump.FACING);
        return facing;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT tag = new CompoundNBT();
        tank.writeToNBT(tag);
        compound.put("fluid", tag);
        compound.putBoolean("isRunning", isRunning);
        compound.putBoolean("starting", starting);
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag2 = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag2);
        });
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT tag = compound.getCompound("fluid");
        tank.readFromNBT(tag);
        isRunning = compound.getBoolean("isRunning");
        starting = compound.getBoolean("starting");
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        int index = getIdex();
        if (index == 1 && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == Direction.UP)
            return LazyOptional.of(() -> tank).cast();
        Direction face = getBlockFacing();
        if (index == 0 && capability == CapabilityEnergy.ENERGY && facing == face.getOpposite())
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }
}
