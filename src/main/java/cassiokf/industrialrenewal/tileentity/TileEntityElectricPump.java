package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.BlockElectricPump;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityElectricPump extends TileEntitySync implements ITickableTileEntity
{
    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(energyPerTick * 2,
            energyPerTick,
            energyPerTick * 2)
    {
        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public void onEnergyChange()
        {
            TileEntityElectricPump.this.markDirty();
        }
    };

    public CustomFluidTank tank = new CustomFluidTank(References.BUCKET_VOLUME)
    {
        @Override
        public boolean canFill(FluidStack stack)
        {
            return false;
        }

        @Override
        protected void onContentsChanged()
        {
            TileEntityElectricPump.this.markDirty();
        }
    };

    private int index = -1;
    private final int everyXtick = 10;
    private int tick;
    public static int energyPerTick = IRConfig.Main.pumpEnergyPerTick.get();
    private Direction facing;
    private final float volume = IRConfig.Sounds.pumpVolume.get() * IRConfig.Sounds.masterVolumeMult.get();

    private final List<BlockPos> fluidSet = new ArrayList<>();
    private final int maxRadius = IRConfig.Main.maxPumpRadius.get();

    private boolean isRunning = false;
    private boolean oldIsRunning = false;
    private boolean starting = false;
    private boolean oldStarting = false;

    public TileEntityElectricPump(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (getIndex() == 1)
        {
            if (!world.isRemote)
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
            handleSound();
        }
    }

    private int getIndex()
    {
        if (index != -1) return index;
        BlockState state = world.getBlockState(pos);
        index = state.getBlock() instanceof BlockElectricPump ? state.get(BlockElectricPump.INDEX) : -1;
        return index;
    }

    private void handleSound()
    {
        if (isRunning && !starting)
        {
            if (!world.isRemote)
                world.playSound(null, pos, IRSoundRegister.PUMP_START, SoundCategory.BLOCKS, volume + 0.5f, 1.0F);
            starting = true;
            oldStarting = true;
            sync();
        } else if (isRunning)
        {
            if (world.isRemote)
                IRSoundHandler.playRepeatableSound(IRSoundRegister.PUMP_ROTATION_RESOURCEL, volume, 1.0F, pos);
        } else
        {
            if (world.isRemote) IRSoundHandler.stopTileSound(pos);
            starting = false;
            if (oldStarting)
            {
                oldStarting = false;
                sync();
            }
        }
    }

    private void consumeEnergy()
    {
        if (energyContainer.getEnergyStored() >= energyPerTick)
        {
            energyContainer.extractEnergyInternally(energyPerTick, false);
            isRunning = true;
        } else
        {
            isRunning = false;
            starting = false;
        }

        if (oldIsRunning != isRunning || oldStarting != starting)
        {
            oldIsRunning = isRunning;
            oldStarting = starting;
            sync();
        }
    }

    private void GetFluidDown()
    {
        if (isRunning && tank.getFluidAmount() <= 0)
        {
            if (IRConfig.Main.pumpInfinityWater.get()
                    && (world.getBlockState(pos.down()).getBlock().equals(Blocks.WATER)
                    || world.getBlockState(pos.down()).getBlock().equals(Blocks.FLOWING_WATER)))
            {
                tank.fillInternal(new FluidStack(FluidRegistry.WATER, References.BUCKET_VOLUME), true);
                return;
            }
            if (getFluidSet() != null && !getFluidSet().isEmpty())
            {
                BlockPos fluidPos = getFluidSet().get(0);

                while (!instanceOf(fluidPos, true, null))
                {
                    getFluidSet().remove(fluidPos);
                    if (getFluidSet() == null || getFluidSet().isEmpty()) return;
                    fluidPos = getFluidSet().get(0);
                }

                BlockState blockState = world.getBlockState(fluidPos);
                IFluidHandler downFluid = Utils.wrapFluidBlock(blockState, world, fluidPos);

                boolean consumeFluid = !(downFluid.getTanks() > 0
                        && !downFluid.getFluidInTank(0).isEmpty()
                        && downFluid.getFluidInTank(0).getFluid().equals(FluidRegistry.WATER)
                        && IRConfig.Main.pumpInfinityWater.get());

                if (tank.fillInternal(downFluid.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0)
                {
                    FluidStack stack = downFluid.drain(Integer.MAX_VALUE, consumeFluid ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE);
                    if (IRConfig.Main.replaceLavaWithCobble.get() && stack != null && stack.getFluid().equals(FluidRegistry.LAVA))
                        world.setBlockState(fluidPos, Blocks.COBBLESTONE.getDefaultState());
                    tank.fillInternal(stack, IFluidHandler.FluidAction.EXECUTE);
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
        Block block = world.getBlockState(pos.down()).getBlock();
        if (block instanceof IFluidBlock)
        {
            Stack<BlockPos> traversingFluids = new Stack<>();
            List<BlockPos> flowingPos = new ArrayList<>();
            traversingFluids.add(pos.down());
            IFluidBlock filter = (IFluidBlock) block;
            while (!traversingFluids.isEmpty())
            {
                BlockPos fluidPos = traversingFluids.pop();
                if (instanceOf(fluidPos, true, filter)) fluidSet.add(fluidPos);
                else flowingPos.add(fluidPos);

                for (Direction d : Direction.values())
                {
                    BlockPos newPos = fluidPos.offset(d);
                    if (instanceOf(newPos, false, filter) && !fluidSet.contains(newPos) && !flowingPos.contains(newPos))
                    {
                        traversingFluids.add(newPos);
                    }
                }
            }
        }
    }

    private boolean instanceOf(BlockPos pos, boolean checkLevel, IFluidBlock filter)
    {
        if (pos == null) return false;
        BlockState state = getBlockState();
        return state.getBlock() instanceof IFluidBlock
                && (filter == null || state.getBlock() == filter)
                && (!checkLevel || state.get(BlockLiquid.LEVEL) == 0)
                && this.pos.distanceSq(pos.getX(), pos.getY(), pos.getZ(), true) <= maxRadius * maxRadius;
    }

    private void passFluidUp()
    {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null && tank.getFluidAmount() > 0)
        {
            tank.tryPassFluid(tank.getCapacity()/ everyXtick, upTank);
        }
    }

    @Override
    public void remove()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
        starting = false;
        super.remove();
    }

    private IFluidHandler GetTankUp() {
        TileEntity upTE = world.getTileEntity(pos.up());
        if (upTE != null)
        {
            IFluidHandler cap = upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
            if (cap != null) return cap;
        }
        return null;
    }

    private Direction getBlockFacing()
    {
        if (facing != null) return facing;
        BlockState state = getBlockState();
        if (state.getBlock() instanceof BlockElectricPump) facing = state.get(BlockElectricPump.FACING);
        else return Direction.NORTH;
        return facing;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT tag = new CompoundNBT();
        tank.writeToNBT(tag);
        compound.put("fluid", tag);
        compound.putInt("index", getIndex());
        compound.putBoolean("isRunning", isRunning);
        compound.putBoolean("starting", starting);
        compound.put("StoredIR", this.energyContainer.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT tag = compound.getCompound("fluid");
        tank.readFromNBT(tag);
        index = compound.getInt("index");
        isRunning = compound.getBoolean("isRunning");
        starting = compound.getBoolean("starting");
        this.energyContainer.deserializeNBT(compound.getCompound("StoredIR"));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
        int index = getIndex();
        if (index == 1 && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == Direction.UP)
            return LazyOptional.of(() -> tank).cast();
        Direction face = getBlockFacing();
        if (index == 0 && capability == CapabilityEnergy.ENERGY && facing == face.getOpposite())
        {
            TileEntityElectricPump te = (TileEntityElectricPump) world.getTileEntity(pos.offset(face));
            if (te != null)
                return LazyOptional.of(() -> te.energyContainer).cast();
        }
        return super.getCapability(capability, facing);
    }
}
