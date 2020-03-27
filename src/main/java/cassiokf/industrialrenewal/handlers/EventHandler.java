package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.item.ItemCartLinkable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = References.MODID)
public class EventHandler
{
    @SubscribeEvent
    public static void onMinecartUpdate(MinecartUpdateEvent event)
    {
        CouplingHandler.onMinecartTick(event.getMinecart());
    }

    @SubscribeEvent
    public static void onPlayerInteractWithMineCarts(MinecartInteractEvent event)
    {
        if (event.getItem().getItem() instanceof ItemCartLinkable)
            event.setCanceled(true);
        else return;
        EntityPlayer thePlayer = event.getPlayer();
        if (thePlayer.getEntityWorld().isRemote || !event.getHand().equals(EnumHand.MAIN_HAND)) return;

        ItemCartLinkable.onPlayerUseLinkableItemOnCart(thePlayer, event.getMinecart());
    }
}
