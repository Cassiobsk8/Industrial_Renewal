package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.pipes.BlockWireBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityWireBase extends TileEntitySyncable
{
    public TileEntityWireBase leftConnection, rightConnection;

    public void connectSide(EnumFacing side, TileEntityWireBase connector, EnumFacing connectorSide)
    {
        EnumFacing blockFront = getBlockFacing();
        if (side == blockFront.rotateYCCW())
        {
            leftConnection = connector;
            connector.connectFirst(connectorSide, this);
            Sync();
        } else if (side == blockFront.rotateY())
        {
            rightConnection = connector;
            connector.connectFirst(connectorSide, this);
            Sync();
        }
    }

    private void connectFirst(EnumFacing side, TileEntityWireBase connector)
    {
        EnumFacing blockFront = getBlockFacing();
        if (side == blockFront.rotateYCCW())
        {
            leftConnection = connector;
            Sync();
        } else if (side == blockFront.rotateY())
        {
            rightConnection = connector;
            Sync();
        }
    }

    public boolean canConnectToSide(EnumFacing side)
    {
        EnumFacing blockFront = getBlockFacing();
        return (side == blockFront.rotateYCCW() && leftConnection == null)
                || (side == blockFront.rotateY() && rightConnection == null);
    }

    private EnumFacing getBlockFacing()
    {
        return world.getBlockState(pos).getValue(BlockWireBase.FACING);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        if (compound.hasKey("right"))
            rightConnection = (TileEntityWireBase) world.getTileEntity(BlockPos.fromLong(compound.getLong("right")));
        if (compound.hasKey("left"))
            leftConnection = (TileEntityWireBase) world.getTileEntity(BlockPos.fromLong(compound.getLong("left")));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (rightConnection != null) compound.setLong("right", rightConnection.getPos().toLong());
        if (leftConnection != null) compound.setLong("left", leftConnection.getPos().toLong());
        return super.writeToNBT(compound);
    }
}
