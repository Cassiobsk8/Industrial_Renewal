package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityLogCart;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public class ItemLogCart extends ItemCartBase
{

    public ItemLogCart(Properties properties)
    {
        super(properties);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityLogCart(world, x, y, z);
    }
}
