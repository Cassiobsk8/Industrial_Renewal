package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityTransformerHV extends TileEntityMultiBlockBase<TileEntityTransformerHV> implements IConnectorHV
{
    private final VoltsEnergyContainer energyContainer;

    private final VoltsEnergyContainer dummyEnergyContainer;
    IConnectorHV otherSideTransformer;
    public boolean isOutPut;
    private BlockPos cableConnectionPos;
    private int energyTransfer;
    private boolean oldOutPut;
    private boolean isConnected;

    public int averageEnergy;
    private int oldEnergy;
    private int tick;
    private boolean inUse = false;

    public TileEntityTransformerHV()
    {
        this.energyContainer = new VoltsEnergyContainer(IRConfig.MainConfig.Main.maxHVTransformerTransferAmount, IRConfig.MainConfig.Main.maxHVTransformerTransferAmount, IRConfig.MainConfig.Main.maxHVTransformerTransferAmount)
        {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate)
            {
                return TileEntityTransformerHV.this.onEnergyReceived(maxReceive, simulate);
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

    public int onEnergyReceived(int maxReceive, boolean simulate)
    {
        if (maxReceive <= 0) return 0;
        if (inUse) return 0; //to prevent stack overflow (IE)
        inUse = true;
        int out = 0;
        if (!isOutPut)
        {
            if (otherSideTransformer != null)
            {
                out = otherSideTransformer.receiveEnergy(Math.min(maxReceive, energyContainer.getMaxOutput()), simulate);
            }

        } else
        {
            //OUTPUT ENERGY
            BlockPos outPutPos = pos.offset(getMasterFacing().getOpposite(), 2).down();
            TileEntity outTileEntity = world.getTileEntity(outPutPos);
            if (outTileEntity != null && outTileEntity.hasCapability(CapabilityEnergy.ENERGY, getMasterFacing()))
            {
                IEnergyStorage outPutStorage = outTileEntity.getCapability(CapabilityEnergy.ENERGY, getMasterFacing());
                if (outPutStorage != null && outPutStorage.canReceive())
                {
                    out = outPutStorage.receiveEnergy(maxReceive, simulate);
                }
            }
        }
        if (!simulate) energyTransfer += out;
        inUse = false;
        return out;
    }

    @Override
    public void tick()
    {
        if (!world.isRemote && isMaster())
        {
            if (tick >= 10)
            {
                tick = 0;
                isOutPut();
                averageEnergy = energyTransfer / 10;
                energyTransfer = 0;
                if (averageEnergy != oldEnergy)
                {
                    oldEnergy = averageEnergy;
                    sync();
                }
            }
            tick++;
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
            this.sync();
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
            } else if (te instanceof TileEntityHVConnectorBase)
            {
                ((TileEntityHVConnectorBase) te).forceRecheck();
            }
        }
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x2x3CenteredPlus1OnTop(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityTransformerHV;
    }

    public String getGenerationText()
    {
        int energy = averageEnergy;
        return Utils.formatEnergyString(energy) + "/t";
    }

    public float getGenerationFill() //0 ~ 90
    {
        float currentAmount = averageEnergy;
        float totalCapacity = energyContainer.getMaxOutput();
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 90f;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (cableConnectionPos != null) compound.setLong("conP", cableConnectionPos.toLong());
        else compound.setLong("conP", 0L);

        compound.setBoolean("connected", isConnected);
        compound.setBoolean("isOutPut", isOutPut);
        compound.setInteger("energy_average", averageEnergy);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        long conL = compound.getLong("conP");
        if (conL == 0L) cableConnectionPos = null;
        else cableConnectionPos = BlockPos.fromLong(conL);

        isConnected = compound.getBoolean("connected");
        isOutPut = compound.getBoolean("isOutPut");
        averageEnergy = compound.getInteger("energy_average");
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
        if (masterTE == null) return super.getCapability(capability, facing);
        EnumFacing face = masterTE.getMasterFacing();
        if (facing == face.getOpposite()
                && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()))
                && capability == CapabilityEnergy.ENERGY)
            return masterTE.isOutPut ? CapabilityEnergy.ENERGY.cast(dummyEnergyContainer)
                    : CapabilityEnergy.ENERGY.cast(masterTE.energyContainer);

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
        if (te instanceof TileEntityHVConnectorBase)
        {
            ((TileEntityHVConnectorBase) te).removeConnection(getConnectorPos());
        } else if (te instanceof IConnectorHV)
        {
            ((IConnectorHV) te).removeConnection();
        }
    }

    public void removeConnectionAndSpawn()
    {
        BlockPos connectorPos = getConnectorPos();
        if (!world.isRemote)
            Utils.spawnItemStack(world, connectorPos, new ItemStack(ModItems.coilHV));
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
        getMaster().sync();
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
        getMaster().sync();
    }

    @Override
    public boolean isConnected()
    {
        return getMaster().isConnected;
    }

    @Override
    public int receiveEnergy(int quantity, boolean simulate)
    {
        if (isInvalid()) return 0;
        return getMaster().energyContainer.receiveEnergy(quantity, simulate);
    }

    @Override
    public void removeConnection()
    {
        if (isConnected())
        {
            getMaster().isConnected = false;
            getMaster().cableConnectionPos = null;
            getMaster().otherSideTransformer = null;
            getMaster().sync();
        }
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }
}
