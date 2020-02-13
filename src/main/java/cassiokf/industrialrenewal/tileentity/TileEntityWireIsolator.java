package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.pipes.BlockWireBase;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityWireIsolator extends TileEntitySyncable
{
    public BlockPos leftConnectionPos = null;
    public BlockPos rightConnectionPos = null;
    private boolean leftConnected;
    private boolean rightConnected;

    private TileEntityWireIsolator master;
    private boolean isMaster;

    public TileEntityWireIsolator()
    {
        super(TileEntityRegister.WIRE_BASE);
    }

    @Override
    public void onLoad()
    {
        initializeNetworkIfNecessary();
    }

    private void initializeNetworkIfNecessary()
    {
        if (master == null || master.isRemoved())
        {
            List<TileEntityWireIsolator> connectedCables = new ArrayList<TileEntityWireIsolator>();
            Stack<TileEntityWireIsolator> traversingCables = new Stack<TileEntityWireIsolator>();
            IConnectorHV inTransformerT = null;
            IConnectorHV outTransformerT = null;
            TileEntityWireIsolator master = (TileEntityWireIsolator) this;
            traversingCables.add((TileEntityWireIsolator) this);
            while (!traversingCables.isEmpty())
            {
                TileEntityWireIsolator storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = storage;
                }
                connectedCables.add(storage);
                if (storage.isLeftConnected())
                {
                    TileEntity te = world.getTileEntity(storage.leftConnectionPos);
                    if (te instanceof TileEntityWireIsolator && !connectedCables.contains(te))
                    {
                        traversingCables.add((TileEntityWireIsolator) te);
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
                    if (te instanceof TileEntityWireIsolator && !connectedCables.contains(te))
                    {
                        traversingCables.add((TileEntityWireIsolator) te);
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
            for (TileEntityWireIsolator storage : connectedCables)
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
        if (te instanceof TileEntityWireIsolator)
        {
            ((TileEntityWireIsolator) te).removeConnection(this.pos);
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

    public Direction getBlockFacing()
    {
        return world.getBlockState(pos).get(BlockWireBase.FACING);
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

    public TileEntityWireIsolator getMaster()
    {
        initializeNetworkIfNecessary();
        return master;
    }

    public void setMaster(TileEntityWireIsolator master)
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
    public void remove()
    {
        super.remove();
        for (Direction d : Direction.values())
        {
            TileEntity te = world.getTileEntity(pos.offset(d));
            if (te instanceof TileEntityWireIsolator)
            {
                ((TileEntityWireIsolator) te).master = null;
                ((TileEntityWireIsolator) te).initializeNetworkIfNecessary();
            }
        }
    }

    @Override
    public void read(CompoundNBT compound)
    {
        rightConnectionPos = BlockPos.fromLong(compound.getLong("rightP"));
        leftConnectionPos = BlockPos.fromLong(compound.getLong("leftP"));
        rightConnected = compound.getBoolean("rightCon");
        leftConnected = compound.getBoolean("leftCon");
        isMaster = compound.getBoolean("isMaster");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        if (rightConnectionPos != null) compound.putLong("rightP", rightConnectionPos.toLong());
        if (leftConnectionPos != null) compound.putLong("leftP", leftConnectionPos.toLong());
        compound.putBoolean("rightCon", rightConnected);
        compound.putBoolean("leftCon", leftConnected);
        compound.putBoolean("isMaster", isMaster);
        return super.write(compound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        // This, combined with isGlobalRenderer in the TileEntityRenderer makes it so that the
        // render does not disappear if the player can't see the block
        // This is useful for rendering larger models or dynamically sized models
        return INFINITE_EXTENT_AABB;
    }
}
