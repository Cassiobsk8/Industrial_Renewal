package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.tileentity.TileEntitySyncable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public abstract class TileEntityMultiBlocksTube<TE extends TileEntityMultiBlocksTube> extends TileEntitySyncable implements ITickable
{
    private TE master;
    private boolean isMaster;
    private Map<BlockPos, EnumFacing> posSet = new HashMap<>();
    int outPut;
    int oldOutPut = -1;
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

    public void initializeMultiblockIfNecessary()
    {
        if (master == null || master.isInvalid()) //TODO Run only in Server
        {
            List<TE> connectedCables = new ArrayList<TE>();
            Stack<TE> traversingCables = new Stack<TE>();
            TE master = (TE) this;
            traversingCables.add((TE) this);
            while (!traversingCables.isEmpty())
            {
                TE storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = storage;
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
            for (TE storage : connectedCables)
            {
                storage.setMaster(master);
                storage.checkForOutPuts(storage.getPos());
                storage.markDirty();
            }
        }
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

    void setMaster(TE master)
    {
        this.master = master;
        isMaster = master == this;
        if (!isMaster) posSet.clear();
        markDirty();
    }

    public Map<BlockPos, EnumFacing> getPosSet()
    {
        return posSet;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        for (EnumFacing d : EnumFacing.VALUES)
        {
            TileEntity te = world.getTileEntity(pos.offset(d));
            if (instanceOf(te))
            {
                ((TileEntityMultiBlocksTube) te).master = null;
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
