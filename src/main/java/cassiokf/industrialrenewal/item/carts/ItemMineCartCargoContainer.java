package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityCargoContainer;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;

public class ItemMineCartCargoContainer extends ItemCartBase
{

    public ItemMineCartCargoContainer(Properties properties)
    {
        super(properties);
    }

    @Override
    public AbstractMinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityCargoContainer(world, x, y, z);
    }
}
