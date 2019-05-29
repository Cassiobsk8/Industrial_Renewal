package cassiokf.industrialrenewal.tileentity.machines.pump;

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
    public FluidTank tank = new FluidTank(0);

    public TileEntityElectricPump() {
        this.energyContainer = new VoltsEnergyContainer(960, 60, 0);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        if (!world.isRemote && this.world.getTotalWorldTime() % 8 == 0) {
            int index = this.world.getBlockState(this.pos).getBlock() instanceof BlockElectricPump ? this.world.getBlockState(this.pos).getValue(BlockElectricPump.INDEX) : 0;
            if (index == 1) {
                GetFluidDown();
            }
        }
    }

    private void GetFluidDown() {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null) {
            Block block = this.world.getBlockState(this.pos.down()).getBlock();
            IFluidHandler downFluid = Utils.wrapFluidBlock(block, this.world, this.pos.down());
            boolean consumeFluid = !(downFluid.getTankProperties()[0].getContents() != null && downFluid.getTankProperties()[0].getContents().getFluid().equals(FluidRegistry.WATER) && IRConfig.pumpInfinityWater);
            VoltsEnergyContainer motorContainer = GetEnergyContainer();

            if (upTank.fill(downFluid.drain(Integer.MAX_VALUE, false), false) > 0 && motorContainer != null && motorContainer.getEnergyStored() >= 15) {
                upTank.fill(downFluid.drain(Integer.MAX_VALUE, consumeFluid), true);
                motorContainer.setEnergyStored(motorContainer.getEnergyStored() - 15);
                //FluidUtil.tryFluidTransfer(GetTankUp(), downFluid, Integer.MAX_VALUE, true);
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
        int index = this.world.getBlockState(this.pos).getValue(BlockElectricPump.INDEX);
        return (index == 1 && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP)
                || (index == 0 && (capability == CapabilityEnergy.ENERGY));
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        if (capability == CapabilityEnergy.ENERGY)
            return (T) this.energyContainer;
        return super.getCapability(capability, facing);
    }
}
