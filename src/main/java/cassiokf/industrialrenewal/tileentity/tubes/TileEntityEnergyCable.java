package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.BlockEnergyCable;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityEnergyCable extends TileEntityMultiBlocksTube<TileEntityEnergyCable> implements ICapabilityProvider, ITickable
{

    private final VoltsEnergyContainer energyContainer;


    public TileEntityEnergyCable() {
        this.energyContainer = new VoltsEnergyContainer(10240, 1024, 1024);
    }

    //IEnergyStorage

    @Override
    public void update() {
        super.update();
        if (!world.isRemote && isMaster())
        {
            int quantity = getPosSet().size();
            this.energyContainer.setMaxEnergyStored(this.energyContainer.getMaxOutput() * quantity);
            for (BlockPos posM : getPosSet().keySet())
            {
                TileEntity te = world.getTileEntity(posM);
                if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, getPosSet().get(posM)))
                {
                    IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, getPosSet().get(posM).getOpposite());
                    if (energyStorage != null && energyStorage.canReceive())
                    {
                        this.energyContainer.extractEnergy(energyStorage.receiveEnergy(this.energyContainer.extractEnergy(this.energyContainer.getMaxOutput(), true), false), false);
                    }
                }
            }
        }
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (world.isRemote) return;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos currentPos = pos.offset(face);
            IBlockState state = world.getBlockState(currentPos);
            TileEntity te = world.getTileEntity(currentPos);
            boolean hasMachine = !(state.getBlock() instanceof BlockEnergyCable) && te != null && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite());
            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).canReceive())
                getMaster().addMachine(currentPos, face);
            else getMaster().removeMachine(pos, currentPos);
        }
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityEnergyCable;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }
}
