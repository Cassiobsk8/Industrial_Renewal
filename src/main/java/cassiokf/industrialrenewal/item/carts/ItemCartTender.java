package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityTenderBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;

public class ItemCartTender extends ItemCartBase
{
    public ItemCartTender(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public EntityMinecart getEntity(World world, double x, double y, double z)
    {
        return new EntityTenderBase(world, x, y, z);
    }
}
