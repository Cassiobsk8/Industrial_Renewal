package net.cassiokf.industrialrenewal.util.interfaces;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface ICoupleCart
{
    default float getMaxCouplingDistance(AbstractMinecart cart)
    {
        return 1.0f;
    }

    default float getFixedDistance(AbstractMinecart cart)
    {
        return 0.85f;
    }
}
