package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityTenderBase;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ItemCartTender extends ItemCartBase
{
    public ItemCartTender(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityTenderBase(world, x, y, z);
    }
}
