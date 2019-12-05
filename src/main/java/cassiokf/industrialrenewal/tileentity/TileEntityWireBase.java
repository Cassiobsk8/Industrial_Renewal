package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.pipes.BlockWireBase;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityWireBase extends TileEntitySyncable implements IConnectorHV
{
    public IConnectorHV leftConnection, rightConnection;
    private BlockPos leftConnectionPos, rightConnectionPos;
    private boolean initialized = false;

    @Override
    public void onLoad()
    {
        super.onLoad();
        if (leftConnectionPos != null) leftConnection = (IConnectorHV) world.getTileEntity(leftConnectionPos);
        else leftConnection = null;
        if (rightConnectionPos != null) rightConnection = (IConnectorHV) world.getTileEntity(rightConnectionPos);
        else rightConnection = null;
        initialized = true;
        this.Sync();
    }

    @Override
    public boolean isStorage()
    {
        return false;
    }

    @Override
    public BlockPos getConnectorPos()
    {
        return this.pos;
    }

    @Override
    public void onConnectionChange()
    {
        this.Sync();
    }

    @Override
    public IConnectorHV getLeftOrCentralConnection()
    {
        return leftConnection;
    }

    @Override
    public void setLeftOrCentralConnection(IConnectorHV connector)
    {
        leftConnection = connector;
    }

    @Override
    public IConnectorHV getRightConnection()
    {
        return rightConnection;
    }

    @Override
    public void setRightConnection(IConnectorHV connector)
    {
        rightConnection = connector;
    }

    @Override
    public EnumFacing getBlockFacing()
    {
        return world.getBlockState(pos).getValue(BlockWireBase.FACING);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        long lPos = compound.getLong("leftCon");
        long rPos = compound.getLong("rightCon");
        leftConnectionPos = lPos != 0L ? BlockPos.fromLong(lPos) : null;
        rightConnectionPos = rPos != 0L ? BlockPos.fromLong(rPos) : null;
        if (initialized)
            leftConnection = leftConnectionPos == null ? null : (IConnectorHV) world.getTileEntity(leftConnectionPos);
        if (initialized)
            rightConnection = rightConnectionPos == null ? null : (IConnectorHV) world.getTileEntity(rightConnectionPos);

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (leftConnection != null) compound.setLong("leftCon", leftConnection.getConnectorPos().toLong());
        else compound.setLong("leftCon", 0L);
        if (rightConnection != null) compound.setLong("rightCon", rightConnection.getConnectorPos().toLong());
        else compound.setLong("rightCon", 0L);

        return super.writeToNBT(compound);
    }
}
