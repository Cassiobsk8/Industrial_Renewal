package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockElectricPump;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityElectricPump extends TileEntitySync implements ITickable
{
    private final VoltsEnergyContainer energyContainer;

    public FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME)
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
    private final int everyXtick = 10;
    private int tick;
    public static int energyPerTick = IRConfig.MainConfig.Main.pumpEnergyPerTick;
    private EnumFacing facing;
    private final float volume = IRConfig.MainConfig.Sounds.pumpVolume * IRConfig.MainConfig.Sounds.masterVolumeMult;

    private final List<BlockPos> fluidSet = new ArrayList<>();
    private final int maxRadius = IRConfig.MainConfig.Main.maxPumpRadius;

    private boolean isRunning = false;
    private boolean oldIsRunning = false;
    private boolean starting = false;
    private boolean oldStarting = false;

    public TileEntityElectricPump()
    {
        this.energyContainer = new VoltsEnergyContainer(energyPerTick * 2,
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
    }

    @Override
    public void update()
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
        IBlockState state = world.getBlockState(pos);
        index = state.getBlock() instanceof BlockElectricPump ? state.getValue(BlockElectricPump.INDEX) : -1;
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
            if (IRConfig.MainConfig.Main.pumpInfinityWater
                    && (world.getBlockState(pos.down()).getBlock().equals(Blocks.WATER)
                    || world.getBlockState(pos.down()).getBlock().equals(Blocks.FLOWING_WATER)))
            {
                tank.fillInternal(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
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

                Block block = world.getBlockState(fluidPos).getBlock();
                IFluidHandler downFluid = Utils.wrapFluidBlock(block, world, fluidPos);

                boolean consumeFluid = !(downFluid.getTankProperties().length > 0
                        && downFluid.getTankProperties()[0].getContents() != null
                        && downFluid.getTankProperties()[0].getContents().getFluid().equals(FluidRegistry.WATER)
                        && IRConfig.MainConfig.Main.pumpInfinityWater);

                if (tank.fillInternal(downFluid.drain(Integer.MAX_VALUE, false), false) > 0)
                {
                    FluidStack stack = downFluid.drain(Integer.MAX_VALUE, consumeFluid);
                    if (IRConfig.MainConfig.Main.replaceLavaWithCobble && stack != null && stack.getFluid().equals(FluidRegistry.LAVA))
                        world.setBlockState(fluidPos, Blocks.COBBLESTONE.getDefaultState());
                    tank.fillInternal(stack, true);
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
        if (block instanceof BlockLiquid)
        {
            Stack<BlockPos> traversingFluids = new Stack<>();
            List<BlockPos> flowingPos = new ArrayList<>();
            traversingFluids.add(pos.down());
            BlockLiquid filter = (BlockLiquid) block;
            while (!traversingFluids.isEmpty())
            {
                BlockPos fluidPos = traversingFluids.pop();
                if (instanceOf(fluidPos, true, filter)) fluidSet.add(fluidPos);
                else flowingPos.add(fluidPos);

                for (EnumFacing d : EnumFacing.VALUES)
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

    private boolean instanceOf(BlockPos pos, boolean checkLevel, BlockLiquid filter)
    {
        if (pos == null) return false;
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockLiquid
                && (filter == null || state.getBlock() == filter)
                && (!checkLevel || state.getValue(BlockLiquid.LEVEL) == 0)
                && this.pos.distanceSq(pos.getX(), pos.getY(), pos.getZ()) <= maxRadius * maxRadius;
    }

    private void passFluidUp()
    {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null && tank.getFluidAmount() > 0)
        {
            if (upTank.fill(tank.drain(tank.getCapacity() / everyXtick, false), false) > 0)
            {
                upTank.fill(tank.drain(tank.getCapacity() / everyXtick, true), true);
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

    private IFluidHandler GetTankUp() {
        TileEntity upTE = world.getTileEntity(pos.up());
        if (upTE != null && upTE.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
            return upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
        }
        return null;
    }

    private EnumFacing getBlockFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockElectricPump) facing = state.getValue(BlockElectricPump.FACING);
        else return EnumFacing.NORTH;
        return facing;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tank.writeToNBT(tag);
        compound.setTag("fluid", tag);
        compound.setInteger("index", getIndex());
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
        index = compound.getInteger("index");
        isRunning = compound.getBoolean("isRunning");
        starting = compound.getBoolean("starting");
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        super.readFromNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        int index = getIndex();
        if (index == 1 && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        EnumFacing face = getBlockFacing();
        if (index == 0 && capability == CapabilityEnergy.ENERGY && facing == face.getOpposite())
        {
            TileEntityElectricPump te = (TileEntityElectricPump) world.getTileEntity(pos.offset(face));
            if (te != null)
                return CapabilityEnergy.ENERGY.cast(te.energyContainer);
        }
        return super.getCapability(capability, facing);
    }
}
