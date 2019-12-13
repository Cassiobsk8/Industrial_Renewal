package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.util.math.BlockPos;

public interface IConnectorHV
{
    boolean isOutput();

    BlockPos getConnectionPos();

    void setConnectionPos(BlockPos endPos);

    BlockPos getConnectorPos();

    void setOtherSideTransformer(IConnectorHV transformer);

    boolean isConnected();

    int receiveEnergy(int quantity, boolean simulate);

    default boolean canConnect(BlockPos pos)
    {
        return !isConnected() && pos.equals(getConnectorPos());
    }

    default void connect(BlockPos endPos)
    {
        setConnectionPos(endPos);
    }

    void removeConnection();
}
