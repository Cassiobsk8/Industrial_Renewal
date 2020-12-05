package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.init.ItemsRegistration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityCargoContainer extends ContainerMinecartEntity
{
    public static final IInventory inventory = new Inventory(9 * 4);

    public EntityCargoContainer(World world)
    {
        super(EntityType.CHEST_MINECART, world);
    }

    public EntityCargoContainer(World worldIn, double x, double y, double z)
    {
        super(EntityType.CHEST_MINECART, x, y, z, worldIn);
    }

    public EntityCargoContainer(EntityType<? extends EntityCargoContainer> type, World world)
    {
        super(type, world);
    }

    public Type getMinecartType()
    {
        return Type.CHEST;
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        this.remove();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            this.entityDropItem(new ItemStack(ItemsRegistration.CARGOCONTAINER.get()), 0.0F);
        }
    }

    @Override
    protected Container createContainer(int id, PlayerInventory playerInventoryIn)
    {
        return new ChestContainer(ContainerType.GENERIC_9X4, id, playerInventoryIn, inventory, 4);
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ItemsRegistration.CARGOCONTAINER.get());
    }

    @Override
    public int getSizeInventory()
    {
        return 32;
    }
}
