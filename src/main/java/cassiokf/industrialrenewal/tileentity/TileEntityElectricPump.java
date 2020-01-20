package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.blocks.BlockElectricPump;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityElectricPump extends TileEntitySyncable implements ICapabilityProvider, ITickable
{
    private final VoltsEnergyContainer energyContainer;

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
            TileEntityElectricPump.this.markDirty();
        }
    };

    private int index = -1;
    private int everyXtick = 10;
    private int tick;
    private int energyPerTick = 10;
    private EnumFacing facing;
    private VoltsEnergyContainer energyStorage;

    private List<BlockPos> fluidSet = new ArrayList<>();
    private int maxRadius = IRConfig.MainConfig.Main.maxPumpRadius;

    private boolean isRunning = false;
    private boolean oldIsRunning = false;
    private boolean starting = false;
    private boolean oldStarting = false;

    public TileEntityElectricPump()
    {
        this.energyContainer = new VoltsEnergyContainer(200, 200, 0)
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update()
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
        IBlockState state = world.getBlockState(pos);
        index = state.getBlock() instanceof BlockElectricPump ? state.getValue(BlockElectricPump.INDEX) : -1;
        return index;
    }

    private void handleSound()
    {
        if (!world.isRemote) return;
        if (isRunning && !starting)
        {
            IRSoundHandler.playSound(world, IRSoundRegister.PUMP_START, IRConfig.MainConfig.Sounds.pumpVolume + 0.5f, 1.0F, pos);
            starting = true;
            oldStarting = true;
            Sync();
        } else if (isRunning)
        {
            IRSoundHandler.playRepeatableSound(IRSoundRegister.PUMP_ROTATION_RESOURCEL, IRConfig.MainConfig.Sounds.pumpVolume, 1.0F, pos);
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
        VoltsEnergyContainer motorEnergyContainer = GetEnergyContainer();
        if (motorEnergyContainer != null && motorEnergyContainer.getEnergyStored() >= energyPerTick)
        {
            motorEnergyContainer.setEnergyStored(Math.max(motorEnergyContainer.getEnergyStored() - energyPerTick, 0));
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
            if (IRConfig.MainConfig.Main.pumpInfinityWater
                    && (world.getBlockState(pos.down()).getBlock().equals(Blocks.WATER)
                    || world.getBlockState(pos.down()).getBlock().equals(Blocks.FLOWING_WATER)))
            {
                tank.fillInternal(new FluidStack(FluidRegistry.WATER, 1000), true);
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

                Block block = world.getBlockState(fluidPos).getBlock();
                IFluidHandler downFluid = Utils.wrapFluidBlock(block, world, fluidPos);

                boolean consumeFluid = !(downFluid.getTankProperties()[0].getContents() != null
                        && downFluid.getTankProperties()[0].getContents().getFluid().equals(FluidRegistry.WATER)
                        && IRConfig.MainConfig.Main.pumpInfinityWater);

                VoltsEnergyContainer motorEnergyContainer = GetEnergyContainer();

                if (tank.fillInternal(downFluid.drain(Integer.MAX_VALUE, false), false) > 0 && motorEnergyContainer != null && motorEnergyContainer.getEnergyStored() >= (energyPerTick * everyXtick))
                {
                    FluidStack stack = downFluid.drain(Integer.MAX_VALUE, consumeFluid);
                    if (IRConfig.MainConfig.Main.repleceLavaWithCobble && stack != null && stack.getFluid().equals(FluidRegistry.LAVA))
                        world.setBlockState(fluidPos, Blocks.COBBLESTONE.getDefaultState());
                    tank.fillInternal(stack, true);
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
        if (world.getBlockState(pos.down()).getBlock() instanceof BlockLiquid)
        {
            Stack<BlockPos> traversingFluids = new Stack<>();
            List<BlockPos> flowingPos = new ArrayList<>();
            traversingFluids.add(pos.down());
            while (!traversingFluids.isEmpty())
            {
                BlockPos fluidPos = traversingFluids.pop();
                if (instanceOf(fluidPos, true)) fluidSet.add(fluidPos);
                else flowingPos.add(fluidPos);

                for (EnumFacing d : EnumFacing.VALUES)
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
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockLiquid
                && (!checkLevel || state.getValue(BlockLiquid.LEVEL) == 0)
                && this.pos.distanceSq(pos.getX(), pos.getY(), pos.getZ()) <= maxRadius * maxRadius;
    }

    private void passFluidUp()
    {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null)
        {
            if (upTank.fill(tank.drain(tank.getCapacity() / everyXtick, false), false) > 0)
            {
                upTank.fill(tank.drain(tank.getCapacity() / everyXtick, true), true);
                isRunning = true;
            }
        }
    }

    @Override
    public void invalidate()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
        starting = false;
        super.invalidate();
    }

    private VoltsEnergyContainer GetEnergyContainer()
    {
        if (energyStorage != null) return energyStorage;
        IBlockState state = this.world.getBlockState(this.pos);
        if (state.getBlock() instanceof BlockElectricPump)
        {
            EnumFacing facing = state.getValue(BlockElectricPump.FACING);
            TileEntityElectricPump te = (TileEntityElectricPump) this.world.getTileEntity(this.pos.offset(facing.getOpposite()));
            if (te != null)
            {
                energyStorage = te.energyContainer;
            }
        }
        return energyStorage;
    }

    private IFluidHandler GetTankUp() {
        TileEntity upTE = this.world.getTileEntity(this.pos.up());
        if (upTE != null && upTE.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
            return upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
        }
        return null;
    }

    private EnumFacing getBlockFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        facing = state.getValue(BlockElectricPump.FACING);
        return facing;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tank.writeToNBT(tag);
        compound.setTag("fluid", tag);
        compound.setBoolean("isRunning", isRunning);
        compound.setBoolean("starting", starting);
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = compound.getCompoundTag("fluid");
        tank.readFromNBT(tag);
        isRunning = compound.getBoolean("isRunning");
        starting = compound.getBoolean("starting");
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockElectricPump)
        {
            int index = getIdex();
            EnumFacing face = getBlockFacing();
            return (index == 1 && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP)
                    || (index == 0 && capability == CapabilityEnergy.ENERGY && facing == face.getOpposite());
        }
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        int index = getIdex();
        if (index == 1 && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        EnumFacing face = getBlockFacing();
        if (index == 0 && capability == CapabilityEnergy.ENERGY && facing == face.getOpposite())
            return CapabilityEnergy.ENERGY.cast(this.energyContainer);
        return super.getCapability(capability, facing);
    }
}
