package cassiokf.industrialrenewal.item;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ItemLogCart extends ItemSpawnableCart
{


    public ItemLogCart(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public AbstractMinecartEntity getMinecartEntity(World world, double x, double y, double z)
    {
        return null;// new EntityLogCart(world, x, y, z);
    }

}
