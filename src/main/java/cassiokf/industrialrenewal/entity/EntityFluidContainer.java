package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFluidContainer extends EntityFluidBase implements ICoupleCart
{

    public EntityFluidContainer(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityFluidContainer(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public float GetTankFill() //0 ~ 180
    {
        if (this.tank != null && this.tank.getFluidAmount() > 0)
        {
            float currentAmount = this.tank.getFluidAmount() / 1000f;
            float totalCapacity = this.tank.getCapacity() / 1000f;
            currentAmount = currentAmount / totalCapacity;
            return currentAmount * 180f;
        }
        return 0;
    }

    public String GetText()
    {
        return this.tank.getFluidAmount() > 0 ? this.tank.getFluid().getLocalizedName() : "Empty";
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(ModItems.fluidContainer, 1), 0.0F);
        }
    }

    @Override
    public ItemStack getCartItem()
    {
        return new ItemStack(ModItems.fluidContainer);
    }

    @Override
    public Type getType()
    {
        return Type.CHEST;
    }

    @Override
    public float getFixedDistance(EntityMinecart cart)
    {
        return 0.86F;
    }
}
