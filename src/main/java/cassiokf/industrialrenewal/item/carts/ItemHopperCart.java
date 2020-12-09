package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityHopperCart;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public class ItemHopperCart extends ItemCartBase
{

    public ItemHopperCart(Properties properties)
    {
        super(properties);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityHopperCart(world, x, y, z);
    }
}
