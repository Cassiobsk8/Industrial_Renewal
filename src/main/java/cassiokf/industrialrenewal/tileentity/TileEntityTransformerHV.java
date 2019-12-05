package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityTransformerHV extends TileEntity3x3MachineBase<TileEntityTransformerHV> implements ITickable, IConnectorHV
{
    private final VoltsEnergyContainer energyContainer;

    private final VoltsEnergyContainer dummyEnergyContainer;
    public boolean isOutPut;
    private BlockPos cableConnectionPos;
    private boolean initialized = false;
    private IConnectorHV cableConnection;
    private int energyTransfer;
    private int oldEnergyTransfer;
    private boolean oldOutPut;

    public TileEntityTransformerHV()
    {
        this.energyContainer = new VoltsEnergyContainer(10240, 10240, 10240)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityTransformerHV.this.Sync();
            }

            @Override
            public boolean canExtract()
            {
                return true;
            }

            @Override
            public boolean canReceive()
            {
                return true;
            }
        };

        this.dummyEnergyContainer = new VoltsEnergyContainer(0, 0, 0)
        {
            @Override
            public boolean canExtract()
            {
                return false;
            }

            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        if (cableConnectionPos != null) cableConnection = (IConnectorHV) world.getTileEntity(cableConnectionPos);
        else cableConnection = null;
        initialized = true;
        this.Sync();
    }

    @Override
    public void update()
    {
        if (this.isMaster())
        {
            isOutPut();
            if (!this.world.isRemote)
            {
                if (!isOutPut)
                {
                    IConnectorHV connectorHV = getOtterSideEnergyStorage();
                    IEnergyStorage inputStorage = null;
                    if (connectorHV != null) inputStorage = connectorHV.getEnergyStorage();

                    if (inputStorage != null && energyContainer.getEnergyStored() > 0)
                    {
                        int energy = energyContainer.extractEnergy(energyContainer.getMaxInput(), true);
                        energyTransfer = inputStorage.receiveEnergy(energy, false);
                        energyContainer.extractEnergy(energyTransfer, false);
                    } else energyTransfer = 0;

                    if (energyTransfer != oldEnergyTransfer)
                    {
                        oldEnergyTransfer = energyTransfer;
                        this.Sync();
                    }
                } else
                {
                    //OUTPUT ENERGY
                    if (energyContainer.getEnergyStored() > 0)
                    {
                        BlockPos outPutPos = pos.offset(getMasterFacing().getOpposite(), 2).down();
                        TileEntity outTileEntity = world.getTileEntity(outPutPos);
                        if (outTileEntity != null && outTileEntity.hasCapability(CapabilityEnergy.ENERGY, getMasterFacing().getOpposite()))
                        {
                            IEnergyStorage outPutStorage = outTileEntity.getCapability(CapabilityEnergy.ENERGY, getMasterFacing().getOpposite());
                            energyTransfer = outPutStorage.receiveEnergy(
                                    energyContainer.extractEnergy(energyContainer.getMaxOutput(), true), false);

                            energyContainer.extractEnergy(energyTransfer, false);
                        } else energyTransfer = 0;
                    } else energyTransfer = 0;

                    if (energyTransfer != oldEnergyTransfer)
                    {
                        oldEnergyTransfer = energyTransfer;
                        this.Sync();
                    }
                }
            }
        }
    }

    private void isOutPut()
    {
        EnumFacing facing = getMasterFacing();
        BlockPos posPort = pos.offset(facing.rotateY()).offset(facing.getOpposite()).down();
        isOutPut = (world.isBlockPowered(posPort) || world.isBlockPowered(posPort.offset(facing.getOpposite())));
        if (isOutPut != oldOutPut)
        {
            oldOutPut = isOutPut;
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 2);
            getMaster().Sync();
        }
    }

    @Override
    public void breakMultiBlocks()
    {
        if (!this.isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().breakMultiBlocks();
            }
            return;
        }
        if (cableConnection != null) removeConnection(cableConnection);

        super.breakMultiBlocks();
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x2x3CenteredPlus1OnTop(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityTransformerHV;
    }

    public String getGenerationText()
    {
        int energy = (this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored()) ? energyTransfer : 0;
        return Utils.formatEnergyString(energy) + " FE/t";
    }

    public float getGenerationFill() //0 ~ 90
    {
        float currentAmount = energyTransfer;
        float totalCapacity = energyContainer.getMaxOutput();
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 90f;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (cableConnection != null) compound.setLong("leftCon", cableConnection.getConnectorPos().toLong());
        else compound.setLong("leftCon", 0L);

        compound.setBoolean("isOutPut", isOutPut);
        compound.setInteger("transfer", energyTransfer);
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        long cPos = compound.getLong("leftCon");
        cableConnectionPos = cPos != 0L ? BlockPos.fromLong(cPos) : null;

        if (initialized)
            cableConnection = cableConnectionPos == null ? null : (IConnectorHV) world.getTileEntity(cableConnectionPos);

        isOutPut = compound.getBoolean("isOutPut");
        energyTransfer = compound.getInteger("transfer");
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        TileEntityTransformerHV masterTE = this.getMaster();
        if (masterTE == null) return false;
        EnumFacing face = masterTE.getMasterFacing();

        return facing == face.getOpposite()
                && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()))
                && capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        TileEntityTransformerHV masterTE = this.getMaster();
        EnumFacing face = masterTE.getMasterFacing();
        if (masterTE == null) return super.getCapability(capability, facing);

        if (facing == face.getOpposite()
                && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()))
                && capability == CapabilityEnergy.ENERGY)
            return masterTE.isOutPut ? CapabilityEnergy.ENERGY.cast(dummyEnergyContainer)
                    : CapabilityEnergy.ENERGY.cast(masterTE.energyContainer);

        return super.getCapability(capability, facing);
    }

    @Override
    public EnumFacing getBlockFacing()
    {
        return getMasterFacing();
    }

    @Override
    public IEnergyStorage getEnergyStorage()
    {
        return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
    }

    @Override
    public boolean isStorage()
    {
        return true;
    }

    @Override
    public BlockPos getConnectorPos()
    {
        return getMaster() != null ? getMaster().getPos().up() : pos;
    }

    @Override
    public void onConnectionChange()
    {
        getMaster().Sync();
    }

    @Override
    public IConnectorHV getLeftOrCentralConnection()
    {
        return getMaster().cableConnection;
    }

    @Override
    public void setLeftOrCentralConnection(IConnectorHV connector)
    {
        getMaster().cableConnection = connector;
    }

    @Override
    public IConnectorHV getRightConnection()
    {
        return null;
    }

    @Override
    public void setRightConnection(IConnectorHV connector)
    {

    }
}
