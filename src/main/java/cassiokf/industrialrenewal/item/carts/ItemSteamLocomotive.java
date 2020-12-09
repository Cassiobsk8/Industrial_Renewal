package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public class ItemSteamLocomotive extends ItemCartBase
{

    public ItemSteamLocomotive(Properties properties)
    {
        super(properties);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntitySteamLocomotive(world, x, y, z);
    }
}
