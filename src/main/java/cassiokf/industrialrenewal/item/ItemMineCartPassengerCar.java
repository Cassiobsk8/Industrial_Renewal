package cassiokf.industrialrenewal.item;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ItemMineCartPassengerCar extends ItemSpawnableCart
{
    public ItemMineCartPassengerCar(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public AbstractMinecartEntity getMinecartEntity(World world, double x, double y, double z)
    {
        return null;// new EntityHopperCart(world, x, y, z);
    }
}
