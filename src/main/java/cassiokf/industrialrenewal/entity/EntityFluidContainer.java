package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFluidContainer extends EntityFluidBase {

    public EntityFluidContainer(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityFluidContainer(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(ModItems.fluidContainer, 1), 0.0F);
        }
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.fluidContainer);
    }

    @Override
    public Type getType() {
        return Type.CHEST;
    }
}
