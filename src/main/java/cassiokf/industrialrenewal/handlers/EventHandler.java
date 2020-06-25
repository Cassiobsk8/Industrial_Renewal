package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.LocomotiveBase;
import cassiokf.industrialrenewal.item.ItemCartLinkable;
import cassiokf.industrialrenewal.recipes.LatheRecipe;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOW;

@Mod.EventBusSubscriber(modid = References.MODID)
public class EventHandler
{
    @SubscribeEvent
    public static void onMinecartUpdate(MinecartUpdateEvent event)
    {
        EntityMinecart cart = event.getMinecart();
        CouplingHandler.onMinecartTick(cart);
        if (cart instanceof LocomotiveBase) ((LocomotiveBase) cart).onLocomotiveUpdate();
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

    @SubscribeEvent(priority = LOW)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> ev)
    {
        LatheRecipe.populateLatheRecipes();
    }
}
