package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityFlatCart;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public class ItemMineCartFlat extends ItemCartBase
{

    public ItemMineCartFlat(Properties properties)
    {
        super(properties);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityFlatCart(world, x, y, z);
    }
}
