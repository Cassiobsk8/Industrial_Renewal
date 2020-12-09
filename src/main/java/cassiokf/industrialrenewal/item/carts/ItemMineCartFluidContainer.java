package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityFluidContainer;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public class ItemMineCartFluidContainer extends ItemCartBase
{

    public ItemMineCartFluidContainer(Properties properties)
    {
        super(properties);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityFluidContainer(world, x, y, z);
    }
}
