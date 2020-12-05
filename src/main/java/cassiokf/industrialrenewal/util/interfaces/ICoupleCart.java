package cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.entity.item.minecart.MinecartEntity;

public interface ICoupleCart
{
    default float getMaxCouplingDistance(MinecartEntity cart)
    {
        return 1.3f;
    }

    default float getFixedDistance(MinecartEntity cart)
    {
        return 0.78f;
    }
}
