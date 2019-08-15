package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockElectricPump;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nullable;

public class TileEntityElectricPump extends TileFluidHandler implements ICapabilityProvider, ITickable {
    private final VoltsEnergyContainer energyContainer;

    public FluidTank tank = new FluidTank(0)
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
    private int tick;
    private int energyPerTick = 10;
    private EnumFacing facing;

    public TileEntityElectricPump() {
        this.energyContainer = new VoltsEnergyContainer(1024, 120, 0)
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        if (!world.isRemote && getIdex() == 1)
        {
            if (tick >= 10)
            {
                tick = 0;
                GetFluidDown();
            }
            tick++;
        }
    }

    private int getIdex()
    {
        if (index != -1) return index;
        IBlockState state = world.getBlockState(pos);
        index = state.getBlock() instanceof BlockElectricPump ? state.getValue(BlockElectricPump.INDEX) : -1;
        return index;
    }

    private void GetFluidDown() {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null) {
            Block block = this.world.getBlockState(this.pos.down()).getBlock();
            IFluidHandler downFluid = Utils.wrapFluidBlock(block, this.world, this.pos.down());
            boolean consumeFluid = !(downFluid.getTankProperties()[0].getContents() != null && downFluid.getTankProperties()[0].getContents().getFluid().equals(FluidRegistry.WATER) && IRConfig.MainConfig.Main.pumpInfinityWater);
            VoltsEnergyContainer motorEnergyContainer = GetEnergyContainer();

            if (upTank.fill(downFluid.drain(Integer.MAX_VALUE, false), false) > 0 && motorEnergyContainer != null && motorEnergyContainer.getEnergyStored() >= (energyPerTick * 10))
            {
                upTank.fill(downFluid.drain(Integer.MAX_VALUE, consumeFluid), true);
                motorEnergyContainer.setEnergyStored(motorEnergyContainer.getEnergyStored() - (energyPerTick * 10));
            }
        }
    }

    private VoltsEnergyContainer GetEnergyContainer() {
        IBlockState state = this.world.getBlockState(this.pos);
        if (state.getBlock() instanceof BlockElectricPump) {
            EnumFacing facing = state.getValue(BlockElectricPump.FACING);
            TileEntityElectricPump te = (TileEntityElectricPump) this.world.getTileEntity(this.pos.offset(facing.getOpposite()));
            if (te != null) {
                return te.energyContainer;
            }
        }
        return null;
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
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = new NBTTagCompound();
        tank.writeToNBT(tag);
        compound.setTag("fluid", tag);
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        NBTTagCompound tag = compound.getCompoundTag("fluid");
        tank.readFromNBT(tag);
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
