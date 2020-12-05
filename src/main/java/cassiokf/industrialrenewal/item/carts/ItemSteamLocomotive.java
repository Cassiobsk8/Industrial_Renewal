package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.MinecartEntity;
import net.minecraft.world.World;

public class ItemSteamLocomotive extends ItemCartBase
{
    public ItemSteamLocomotive(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntitySteamLocomotive(world, x, y, z);
    }
}
