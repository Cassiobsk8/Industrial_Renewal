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

    @Override
    protected Container createContainer(int id, PlayerInventory playerInventoryIn)
    {
        return new ChestContainer(ContainerType.GENERIC_9X4, id, playerInventoryIn, inventory, 4);
    }

    @Override
    public Type getMinecartType()
    {
        return Type.CHEST;
    }

    @Override
    public ItemStack getCartItem()
    {
        return new ItemStack(ItemsRegistration.CARGOCONTAINER.get());
    }

    @Override
    public int getSizeInventory()
    {
        return 36;
    }

}
