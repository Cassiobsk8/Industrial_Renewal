package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityFlatCart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.MinecartEntity;
import net.minecraft.world.World;

public class ItemMineCartFlat extends ItemCartBase
{
    public ItemMineCartFlat(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityFlatCart(world, x, y, z);
    }
}
