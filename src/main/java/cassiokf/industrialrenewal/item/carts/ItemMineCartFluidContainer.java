package cassiokf.industrialrenewal.item.carts;

import cassiokf.industrialrenewal.entity.EntityFluidContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.MinecartEntity;
import net.minecraft.world.World;

public class ItemMineCartFluidContainer extends ItemCartBase
{
    public ItemMineCartFluidContainer(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public MinecartEntity getEntity(World world, double x, double y, double z)
    {
        return new EntityFluidContainer(world, x, y, z);
    }
}
