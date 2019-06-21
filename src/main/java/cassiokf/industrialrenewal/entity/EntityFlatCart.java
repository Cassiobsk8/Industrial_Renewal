package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFlatCart extends EntityMinecart
{

    public EntityFlatCart(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 0.4F);
    }

    public EntityFlatCart(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        //super.killMinecart(source);
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(ModItems.mineCartFlat, 1), 0.0F);
        }
    }

    @Override
    public Type getType()
    {
        return Type.CHEST;
    }

    @Override
    public ItemStack getCartItem()
    {
        return new ItemStack(ModItems.mineCartFlat);
    }

}
