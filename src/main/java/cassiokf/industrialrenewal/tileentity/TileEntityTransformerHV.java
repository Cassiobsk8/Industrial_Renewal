package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

import static cassiokf.industrialrenewal.init.TileRegistration.TRANSFORMERHV_TILE;

public class TileEntityTransformerHV extends TileEntity3x3MachineBase<TileEntityTransformerHV> implements ITickableTileEntity, IConnectorHV
{
    public boolean isOutPut;
    IConnectorHV otherSideTransformer;
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);
    private BlockPos cableConnectionPos;
    private int energyTransfer;
    private int oldEnergyTransfer;
    private boolean oldOutPut;
    private boolean isConnected;

    public TileEntityTransformerHV()
    {
        super(TRANSFORMERHV_TILE.get());
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(IRConfig.Main.maxHVTransformerTransferAmount.get(), IRConfig.Main.maxHVTransformerTransferAmount.get(), IRConfig.Main.maxHVTransformerTransferAmount.get())
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
    }

    private IEnergyStorage createEnergyDummy()
    {
        return new CustomEnergyStorage(0, 0, 0)
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
    public void tick()
    {
        if (this.isMaster())
        {
            isOutPut();
            if (this.hasWorld() && !world.isRemote)
            {
                IEnergyStorage thisEnergy = energyStorage.orElse(null);
                if (!isOutPut)
                {
                    if (otherSideTransformer != null && thisEnergy.getEnergyStored() > 0)
                    {
                        int energy = thisEnergy.extractEnergy(IRConfig.Main.maxHVTransformerTransferAmount.get(), true);
                        int energy1 = otherSideTransformer.receiveEnergy(energy, false);
                        energyTransfer = thisEnergy.extractEnergy(energy1, false);
                    } else energyTransfer = 0;

                } else
                {
                    //OUTPUT ENERGY
                    if (thisEnergy.getEnergyStored() > 0)
                    {
                        BlockPos outPutPos = pos.offset(getMasterFacing().getOpposite(), 2).down();
                        TileEntity outTileEntity = world.getTileEntity(outPutPos);
                        if (outTileEntity != null)
                        {
                            IEnergyStorage outPutStorage = outTileEntity.getCapability(CapabilityEnergy.ENERGY, getMasterFacing()).orElse(null);
                            if (outPutStorage != null)
                            {
                                int energy = thisEnergy.extractEnergy(IRConfig.Main.maxHVTransformerTransferAmount.get(), true);
                                int energy1 = outPutStorage.receiveEnergy(energy, false);
                                energyTransfer = thisEnergy.extractEnergy(energy1, false);
                            } else energyTransfer = 0;
                        } else energyTransfer = 0;
                    } else energyTransfer = 0;
                }

                if (energyTransfer != oldEnergyTransfer)
                {
                    oldEnergyTransfer = energyTransfer;
                    this.Sync();
                }
            }
        }
    }

    private void isOutPut()
    {
        Direction facing = getMasterFacing();
        BlockPos posPort = pos.offset(facing.rotateY()).offset(facing.getOpposite()).down();
        isOutPut = (world.isBlockPowered(posPort) || world.isBlockPowered(posPort.offset(facing.getOpposite())));
        if (isOutPut != oldOutPut)
        {
            oldOutPut = isOutPut;
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 2);
            this.Sync();
            this.checkIfNeedsNetworkRefresh();
        }
    }

    private void checkIfNeedsNetworkRefresh()
    {
        if (isConnected() && isMaster())
        {
            TileEntity te = world.getTileEntity(cableConnectionPos);
            if (te instanceof IConnectorHV)
            {
                if ((isOutPut && !((IConnectorHV) te).isOutput()) || (!isOutPut && ((IConnectorHV) te).isOutput()))
                {
                    setOtherSideTransformer((IConnectorHV) te);
                    ((IConnectorHV) te).setOtherSideTransformer(this);
                } else
                {
                    setOtherSideTransformer(null);
                    ((IConnectorHV) te).setOtherSideTransformer(null);
                }
            } else if (te instanceof TileEntityWireIsolator)
            {
                ((TileEntityWireIsolator) te).forceRecheck();
            }
        }
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
        int energy = energyTransfer;
        return Utils.formatEnergyString(energy) + "/t";
    }

    public float getGenerationFill() //0 ~ 90
    {
        float currentAmount = energyTransfer;
        float totalCapacity = IRConfig.Main.maxHVTransformerTransferAmount.get();
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 90f;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        if (cableConnectionPos != null) compound.putLong("conP", cableConnectionPos.toLong());
        else compound.putLong("conP", 0L);

        compound.putBoolean("connected", isConnected);
        compound.putBoolean("isOutPut", isOutPut);
        compound.putInt("transfer", energyTransfer);
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        long conL = compound.getLong("conP");
        if (conL == 0L) cableConnectionPos = null;
        else cableConnectionPos = BlockPos.fromLong(conL);

        isConnected = compound.getBoolean("connected");
        isOutPut = compound.getBoolean("isOutPut");
        energyTransfer = compound.getInt("transfer");
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        TileEntityTransformerHV masterTE = this.getMaster();
        Direction face = masterTE.getMasterFacing();
        if (masterTE == null) return super.getCapability(capability, facing);

        if (facing == face.getOpposite()
                && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()))
                && capability == CapabilityEnergy.ENERGY)
            return masterTE.isOutPut ? dummyEnergy.cast()
                    : masterTE.energyStorage.cast();

        return super.getCapability(capability, facing);
    }

    @Override
    public void onMasterBreak()
    {
        if (isConnected())
        {
            removeConnectionAndSpawn();
        }
    }

    private void disableConnectedCables(BlockPos connectedPos)
    {
        TileEntity te = world.getTileEntity(connectedPos);
        if (te instanceof TileEntityWireIsolator)
        {
            ((TileEntityWireIsolator) te).removeConnection(getConnectorPos());
        } else if (te instanceof IConnectorHV)
        {
            ((IConnectorHV) te).removeConnection();
        }
    }

    public void removeConnectionAndSpawn()
    {
        BlockPos connectorPos = getConnectorPos();
        if (!world.isRemote)
            Utils.spawnItemStack(world, connectorPos, new ItemStack(ItemsRegistration.COILHV.get()));
        disableConnectedCables(cableConnectionPos);
        removeConnection();
    }

    @Override
    public boolean isOutput()
    {
        return getMaster().isOutPut;
    }

    @Override
    public BlockPos getConnectionPos()
    {
        return getMaster().cableConnectionPos;
    }

    @Override
    public void setConnectionPos(BlockPos endPos)
    {
        getMaster().cableConnectionPos = endPos;
        getMaster().isConnected = true;
        if (world.getTileEntity(endPos) instanceof IConnectorHV)
        {
            getMaster().checkIfNeedsNetworkRefresh();
        }
        getMaster().Sync();
    }

    @Override
    public BlockPos getConnectorPos()
    {
        return getMaster() != null ? getMaster().getPos().up() : pos;
    }

    @Override
    public void setOtherSideTransformer(IConnectorHV transformer)
    {
        getMaster().otherSideTransformer = transformer;
        getMaster().Sync();
    }

    @Override
    public boolean isConnected()
    {
        return getMaster().isConnected;
    }

    @Override
    public int receiveEnergy(int quantity, boolean simulate)
    {
        if (isRemoved()) return 0;
        return getMaster().energyStorage.orElse(null).receiveEnergy(quantity, simulate);
    }

    @Override
    public void removeConnection()
    {
        if (isConnected())
        {
            getMaster().isConnected = false;
            getMaster().cableConnectionPos = null;
            otherSideTransformer = null;
            getMaster().Sync();
        }
    }
}
