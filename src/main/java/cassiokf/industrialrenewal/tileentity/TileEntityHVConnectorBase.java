package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.pipes.BlockHVConnectorBase;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySyncable;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityHVConnectorBase extends TileEntitySyncable
{
    public BlockPos leftConnectionPos = null;
    public BlockPos rightConnectionPos = null;
    private boolean leftConnected;
    private boolean rightConnected;

    private TileEntityHVConnectorBase master;
    private boolean isMaster;

    private void initializeNetworkIfNecessary()
    {
        if (master == null || master.isInvalid())
        {
            List<TileEntityHVConnectorBase> connectedCables = new ArrayList<TileEntityHVConnectorBase>();
            Stack<TileEntityHVConnectorBase> traversingCables = new Stack<TileEntityHVConnectorBase>();
            IConnectorHV inTransformerT = null;
            IConnectorHV outTransformerT = null;
            TileEntityHVConnectorBase master = this;
            traversingCables.add(this);
            while (!traversingCables.isEmpty())
            {
                TileEntityHVConnectorBase storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = storage;
                }
                connectedCables.add(storage);
                if (storage.isLeftConnected())
                {
                    TileEntity te = world.getTileEntity(storage.leftConnectionPos);
                    if (te instanceof TileEntityHVConnectorBase && !connectedCables.contains(te))
                    {
                        traversingCables.add((TileEntityHVConnectorBase) te);
                    }
                    if (te instanceof IConnectorHV)
                    {
                        if (((IConnectorHV) te).isOutput())
                        {
                            outTransformerT = (IConnectorHV) te;
                        } else
                        {
                            inTransformerT = (IConnectorHV) te;
                        }
                    }
                }
                if (storage.isRightConnected())
                {
                    TileEntity te = world.getTileEntity(storage.rightConnectionPos);
                    if (te instanceof TileEntityHVConnectorBase && !connectedCables.contains(te))
                    {
                        traversingCables.add((TileEntityHVConnectorBase) te);
                    }
                    if (te instanceof IConnectorHV)
                    {
                        ((IConnectorHV) te).setOtherSideTransformer(null);
                        if (((IConnectorHV) te).isOutput())
                        {
                            outTransformerT = (IConnectorHV) te;
                        } else
                        {
                            inTransformerT = (IConnectorHV) te;
                        }
                    }
                }
            }
            for (TileEntityHVConnectorBase storage : connectedCables)
            {
                storage.setMaster(master);
                storage.markDirty();
            }
            if (inTransformerT != null) inTransformerT.setOtherSideTransformer(outTransformerT);
            if (outTransformerT != null) outTransformerT.setOtherSideTransformer(inTransformerT);
            master.markDirty();
            markDirty();
        }
    }

    public void forceRecheck()
    {
        this.master = null;
        initializeNetworkIfNecessary();
    }

    public void removeCableAndSpawn(BlockPos connectionPos)
    {
        disableConnectedCables(connectionPos);
        removeConnection(connectionPos);
        if (!world.isRemote)
            Utils.spawnItemStack(world, pos, new ItemStack(ModItems.coilHV));
    }

    private void disableConnectedCables(BlockPos connectedPos)
    {
        TileEntity te = world.getTileEntity(connectedPos);
        if (te instanceof TileEntityHVConnectorBase)
        {
            ((TileEntityHVConnectorBase) te).removeConnection(this.pos);
        } else if (te instanceof IConnectorHV)
        {
            ((IConnectorHV) te).removeConnection();
        }
    }

    public void removeConnection(BlockPos sidePos)
    {
        if (sidePos.equals(leftConnectionPos))
        {
            leftConnected = false;
            leftConnectionPos = null;
            master = null;
            initializeNetworkIfNecessary();
            this.Sync();
        } else if (sidePos.equals(rightConnectionPos))
        {
            rightConnected = false;
            rightConnectionPos = null;
            master = null;
            initializeNetworkIfNecessary();
            this.Sync();
        }
    }

    public void removeAllConnections()
    {
        if (isRightConnected()) removeConnection(rightConnectionPos);
        if (isLeftConnected()) removeConnection(leftConnectionPos);
    }

    public boolean canConnect()
    {
        return !isLeftConnected() || !isRightConnected();
    }

    public void setConnection(BlockPos otherConnectorPos)
    {
        boolean madeConnection = false;
        if (!isLeftConnected())
        {
            setLeftConnectionPos(otherConnectorPos);
            madeConnection = true;
        } else if (!isRightConnected())
        {
            setRightConnectionPos(otherConnectorPos);
            madeConnection = true;
        }
        if (madeConnection)
        {
            master = null;
            initializeNetworkIfNecessary();
        }
        this.Sync();
    }

    public EnumFacing getBlockFacing()
    {
        return world.getBlockState(pos).getValue(BlockHVConnectorBase.FACING);
    }

    private void setLeftConnectionPos(BlockPos pos)
    {
        leftConnectionPos = pos;
        leftConnected = true;
    }

    private void setRightConnectionPos(BlockPos pos)
    {
        rightConnectionPos = pos;
        rightConnected = true;
    }

    public boolean isMaster()
    {
        return isMaster;
    }

    public TileEntityHVConnectorBase getMaster()
    {
        initializeNetworkIfNecessary();
        return master;
    }

    public void setMaster(TileEntityHVConnectorBase master)
    {
        this.master = master;
        isMaster = master == this;
        markDirty();
    }

    public boolean isLeftConnected()
    {
        return leftConnected;
    }

    public boolean isRightConnected()
    {
        return rightConnected;
    }

    @Override
    public void onBlockBreak()
    {
        if (isLeftConnected())
        {
            removeCableAndSpawn(leftConnectionPos);
        }
        if (isRightConnected())
        {
            removeCableAndSpawn(rightConnectionPos);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        rightConnectionPos = BlockPos.fromLong(compound.getLong("rightP"));
        leftConnectionPos = BlockPos.fromLong(compound.getLong("leftP"));
        rightConnected = compound.getBoolean("rightCon");
        leftConnected = compound.getBoolean("leftCon");
        isMaster = compound.getBoolean("isMaster");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (rightConnectionPos != null) compound.setLong("rightP", rightConnectionPos.toLong());
        if (leftConnectionPos != null) compound.setLong("leftP", leftConnectionPos.toLong());
        compound.setBoolean("rightCon", rightConnected);
        compound.setBoolean("leftCon", leftConnected);
        compound.setBoolean("isMaster", isMaster);
        return super.writeToNBT(compound);
    }
}
