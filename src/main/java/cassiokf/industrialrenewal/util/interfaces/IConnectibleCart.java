package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.entity.item.EntityMinecart;

public interface IConnectibleCart
{
    default float getMaxCouplingDistance(EntityMinecart cart)
    {
        return 1.3f;
    }

    default float getFixedDistance(EntityMinecart cart)
    {
        return 0.78f;
    }
}
