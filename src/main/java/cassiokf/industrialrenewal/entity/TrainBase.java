package cassiokf.industrialrenewal.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public abstract class TrainBase extends MinecartEntity
{
    public TrainBase(World worldIn)
    {
        super(EntityType.MINECART, worldIn);
    }

    public TrainBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
}
