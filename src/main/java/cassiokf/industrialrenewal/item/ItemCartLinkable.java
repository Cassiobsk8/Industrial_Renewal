package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.entity.EntityTenderBase;
import cassiokf.industrialrenewal.entity.LocomotiveBase;
import cassiokf.industrialrenewal.handlers.CouplingHandler;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.enums.EnumCouplingType;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import com.google.common.collect.MapMaker;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;

public class ItemCartLinkable extends ItemBase
{
    static final UUID NULL_UUID = new UUID(0, 0);
    private static final Map<PlayerEntity, MinecartEntity> linkMap = new MapMaker().weakKeys().weakValues().makeMap();

    public ItemCartLinkable(Properties properties)
    {
        super(properties);
    }


    public static void onPlayerUseLinkableItemOnCart(PlayerEntity player, MinecartEntity cart)
    {
        MinecartEntity last = linkMap.remove(player);
        if (last != null && last.isAlive())
        {
            if (CouplingHandler.isConnected(cart, last))
            {
                CouplingHandler.removeConnection(cart, last);
                Utils.sendChatMessage(player, "Carts Decoupled");
                return;
            } else
            {
                if (CoupleCarts(last, cart))
                {
                    Utils.sendChatMessage(player, "Carts Coupled");
                    Utils.sendConsoleMessage("player " + player.getName().getFormattedText() + " Connected " + last.getName() + " to " + cart.getName());
                    return;
                }
            }
            Utils.sendChatMessage(player, "Coupling Fail");
        } else
        {
            linkMap.put(player, cart);
            Utils.sendChatMessage(player, "Coupling Start");
        }
    }

    public static boolean CoupleCarts(MinecartEntity cart1, MinecartEntity cart2)
    {
        if (canCoupleCarts(cart1, cart2))
        {
            setConnection(cart1, cart2);
            setConnection(cart2, cart1);
            return true;
        }
        return false;
    }

    private static void setConnection(MinecartEntity from, MinecartEntity to)
    {
        for (EnumCouplingType link : EnumCouplingType.VALUES)
        {
            if (hasFreeConnectionIn(from, link))
            {
                if (from instanceof EntityTenderBase && to instanceof LocomotiveBase)
                {
                    ((LocomotiveBase) to).setTender((EntityTenderBase) from);
                }
                UUID id = to.getUniqueID();
                from.getPersistentData().putLong(link.tagMostSigBits, id.getMostSignificantBits());
                from.getPersistentData().putLong(link.tagLeastSigBits, id.getLeastSignificantBits());
                return;
            }
        }
    }

    private static boolean canCoupleCarts(MinecartEntity cart1, MinecartEntity cart2)
    {
        if (cart1 == cart2 || (thereIsNoConnectionLeft(cart1) && thereIsNoConnectionLeft(cart2)) || CouplingHandler.isConnected(cart1, cart2))
        {
            return false;
        }
        return cart1.getDistanceSq(cart2) <= getMaxCouplingDistance(cart1, cart2);
    }

    public static boolean thereIsNoConnectionLeft(MinecartEntity cart)
    {
        return !hasFreeConnectionIn(cart, EnumCouplingType.COUPLING_1)
                && !hasFreeConnectionIn(cart, EnumCouplingType.COUPLING_2);
    }

    public static boolean hasFreeConnectionIn(MinecartEntity cart, EnumCouplingType type)
    {
        UUID cartUUID = CouplingHandler.getConnection(cart, type);
        return cartUUID.equals(NULL_UUID);
    }

    private static float getMaxCouplingDistance(MinecartEntity cart1, MinecartEntity cart2)
    {
        float defaultDistance = 1.6f;
        float dist = 0;
        if (cart1 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart1).getMaxCouplingDistance(cart2);
        else
            dist += defaultDistance;
        if (cart2 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart2).getMaxCouplingDistance(cart1);
        else
            dist += defaultDistance;
        return dist * dist;
    }
}
