package cassiokf.industrialrenewal.entity;

import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.world.World;

public class EntityCargoContainer extends EntityMinecartChest {

    public EntityCargoContainer(World worldIn) {
        super(worldIn);
    }

    public EntityCargoContainer(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

}
