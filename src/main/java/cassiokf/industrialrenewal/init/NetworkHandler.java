package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.network.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler
{

    public static SimpleNetworkWrapper INSTANCE;

    public static void init()
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.NETWORKCHANNEL);
        NetworkRegistry.INSTANCE.registerGuiHandler(IndustrialRenewal.instance, new GUIHandler());

        int dis = 0;

        INSTANCE.registerMessage(new PacketReturnRecordPlayer.Handler(), PacketReturnRecordPlayer.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketReturnEntityDetector.Handler(), PacketReturnEntityDetector.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketReturnFuseBox.Handler(), PacketReturnFuseBox.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketReturnFluidLoader.Handler(), PacketReturnFluidLoader.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketReturnCargoLoader.Handler(), PacketReturnCargoLoader.class, dis++, Side.SERVER);

        INSTANCE.registerMessage(new PacketReturnTEStorageChest.Handler(), PacketReturnTEStorageChest.class, dis++, Side.SERVER);
        INSTANCE.registerMessage(new PacketStorageChest.Handler(), PacketStorageChest.class, dis++, Side.CLIENT);
    }
}
