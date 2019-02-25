package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPassengerCar extends EntityMinecartEmpty {

    public EntityPassengerCar(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityPassengerCar(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        //super.killMinecart(source);
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(ModItems.passengerCar, 1), 0.0F);
        }
    }

    @Override
    public Type getType() {
        return Type.RIDEABLE;
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.passengerCar);
    }

}
