package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityHopperCart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;

public class ItemHopperCart extends ItemCartBase
{
    public ItemHopperCart(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public EntityMinecart getEntity(World world, double x, double y, double z)
    {
        return new EntityHopperCart(world, x, y, z);
    }
}
