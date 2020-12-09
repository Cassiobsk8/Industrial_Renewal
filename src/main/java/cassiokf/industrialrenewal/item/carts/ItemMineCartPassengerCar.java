package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityPassengerCar;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public class ItemMineCartPassengerCar extends ItemCartBase
{


    public ItemMineCartPassengerCar(Properties properties)
    {
        super(properties);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityPassengerCar(world, x, y, z);
    }
}
