package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.tileentity.TileEntitySyncable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class TileEntityMultiBlocksTube<TE extends TileEntityMultiBlocksTube> extends TileEntitySyncable implements ITickable
{
    private TE master;
    private boolean isMaster;
    private Map<BlockPos, EnumFacing> posSet = new ConcurrentHashMap<>();
    public int outPut;
    public int oldOutPut = -1;
    int outPutCount;
    int oldOutPutCount = -1;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void onLoad()
    {
        initializeMultiblockIfNecessary();
    }

    public int getOutPut()
    {
        return outPut;
    }

    public int getOutPutCount()
    {
        return outPutCount;
    }

    private void initializeMultiblockIfNecessary()
    {
        if (isMasterInvalid())
        {
            if (isTray()) return;
            List<TileEntityMultiBlocksTube> connectedCables = new CopyOnWriteArrayList<>();
            Stack<TileEntityMultiBlocksTube> traversingCables = new Stack<>();
            TE master = (TE) this;
            traversingCables.add(this);
            while (!traversingCables.isEmpty())
            {
                TileEntityMultiBlocksTube storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = (TE) storage;
                }
                connectedCables.add(storage);
                for (EnumFacing d : getFacesToCheck())
                {
                    TileEntity te = world.getTileEntity(storage.getPos().offset(d));
                    if (instanceOf(te) && !connectedCables.contains(te))
                    {
                        traversingCables.add((TE) te);
                    }
                }
            }
            master.getPosSet().clear();
            if (canBeMaster(master))
            {
                for (TileEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.setMaster((TE) master);
                    storage.checkForOutPuts(storage.getPos());
                    storage.markDirty();
                }
            } else
            {
                for (TileEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.getPosSet().clear();
                    storage.setMaster(null);
                }
            }
            markDirty();
        }
    }

    public boolean isTray()
    {
        return false;
    }

    private boolean canBeMaster(TileEntity te)
    {
        return te != null && !(te instanceof TileEntityCableTray);
    }

    public boolean isMasterInvalid()
    {
        return master == null || master.isInvalid();
    }

    public EnumFacing[] getFacesToCheck()
    {
        return EnumFacing.values();
    }

    public abstract boolean instanceOf(TileEntity te);

    public abstract void checkForOutPuts(BlockPos bPos);

    public boolean isMaster()
    {
        return isMaster;
    }

    public TE getMaster()
    {
        initializeMultiblockIfNecessary();
        return master;
    }

    public void setMaster(TE master)
    {
        this.master = master;
        isMaster = master == this;
        if (!isMaster) posSet.clear();
    }

    public Map<BlockPos, EnumFacing> getPosSet()
    {
        return posSet;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();

        if (master != null)
        {
            master.setMaster(null);
            if (master != null) master.getMaster();
            else getMaster();
        }

        for (EnumFacing d : EnumFacing.VALUES)
        {
            TileEntity te = world.getTileEntity(pos.offset(d));
            if (instanceOf(te))
            {
                ((TileEntityMultiBlocksTube) te).master = null;

                if (te instanceof TileEntityCableTray)
                    ((TileEntityCableTray) te).refreshConnections();
                else
                    ((TileEntityMultiBlocksTube) te).initializeMultiblockIfNecessary();
            }
        }
    }

    public void addMachine(BlockPos pos, EnumFacing face)
    {
        posSet.put(pos, face);
    }

    public void removeMachine(BlockPos ownPos, BlockPos machinePos)
    {
        posSet.remove(machinePos);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        isMaster = compound.getBoolean("isMaster");
        outPut = compound.getInteger("out");
        outPutCount = compound.getInteger("count");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("isMaster", isMaster);
        compound.setInteger("out", outPut);
        compound.setInteger("count", outPutCount);
        return super.writeToNBT(compound);
    }
}
