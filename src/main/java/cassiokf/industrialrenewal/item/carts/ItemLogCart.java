package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityLogCart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.MinecartEntity;
import net.minecraft.world.World;

public class ItemLogCart extends ItemCartBase
{
    public ItemLogCart(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityLogCart(world, x, y, z);
    }
}
