package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;

public interface IConnectorHV
{

    boolean isStorage();

    BlockPos getConnectorPos();

    void onConnectionChange();

    IConnectorHV getLeftOrCentralConnection();

    void setLeftOrCentralConnection(IConnectorHV connector);

    IConnectorHV getRightConnection();

    void setRightConnection(IConnectorHV connector);

    default boolean canConnectToSide(BlockPos pos, EnumFacing facing)
    {
        if (isStorage()) return pos.equals(getConnectorPos()) && getLeftOrCentralConnection() == null;

        EnumFacing blockFacing = getBlockFacing();

        return ((facing.equals(blockFacing.rotateYCCW()) && getLeftOrCentralConnection() == null))
                || (facing.equals(blockFacing.rotateY()) && getRightConnection() == null);
    }

    default void removeAllConnections()
    {
        if (getLeftOrCentralConnection() != null) getLeftOrCentralConnection().removeConnection(this);
        if (getRightConnection() != null) getRightConnection().removeConnection(this);
    }

    default void removeConnection(IConnectorHV toRemove)
    {
        if (getLeftOrCentralConnection() == toRemove) setLeftOrCentralConnection(null);
        else if (getRightConnection() == toRemove) setRightConnection(null);
        onConnectionChange();
    }

    default void connectSide(EnumFacing side, IConnectorHV connector, EnumFacing connectorSide)
    {
        if (isStorage())
        {
            setLeftOrCentralConnection(connector);
            connector.connectFirst(connectorSide, this);
            onConnectionChange();
            return;
        }
        EnumFacing blockFacing = getBlockFacing();

        if (side == blockFacing.rotateYCCW())
        {
            setLeftOrCentralConnection(connector);
            connector.connectFirst(connectorSide, this);
            onConnectionChange();
        } else if (side == blockFacing.rotateY())
        {
            setRightConnection(connector);
            connector.connectFirst(connectorSide, this);
            onConnectionChange();
        }
    }

    default void connectFirst(EnumFacing side, IConnectorHV connector)
    {
        if (isStorage())
        {
            setLeftOrCentralConnection(connector);
            onConnectionChange();
            return;
        }

        EnumFacing blockFront = getBlockFacing();
        if (side == blockFront.rotateYCCW())
        {
            setLeftOrCentralConnection(connector);
            onConnectionChange();
        } else if (side == blockFront.rotateY())
        {
            setRightConnection(connector);
            onConnectionChange();
        }
    }

    default EnumFacing getBlockFacing()
    {
        return EnumFacing.NORTH;
    }

    default IEnergyStorage getEnergyStorage()
    {
        return null;
    }

    default IConnectorHV getOtterSideEnergyStorage()
    {
        if (getLeftOrCentralConnection() == null) return null;
        IConnectorHV energyStorage = null;
        IConnectorHV currentConnector;
        IConnectorHV oldConnector = this;
        IConnectorHV nextConnection = getLeftOrCentralConnection();
        while (energyStorage == null)
        {
            currentConnector = nextConnection;
            if (!nextConnection.isStorage()) nextConnection = nextConnection.getOtherConnector(oldConnector);
            oldConnector = currentConnector;

            if (nextConnection == null) return null;

            if (nextConnection.isStorage())
            {
                energyStorage = nextConnection;
                return energyStorage;
            }
        }
        return energyStorage;
    }

    default IConnectorHV getOtherConnector(IConnectorHV fromConnector)
    {
        if (getLeftOrCentralConnection() == fromConnector)
        {
            return getRightConnection();
        } else if (getRightConnection() == fromConnector)
        {
            return getLeftOrCentralConnection();
        }
        return fromConnector == getLeftOrCentralConnection() ? getRightConnection()
                : (fromConnector == getRightConnection() ? getLeftOrCentralConnection() : null);
    }
}
