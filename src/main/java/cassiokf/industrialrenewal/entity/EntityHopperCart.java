package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityHopperCart extends EntityInventoryCartBase implements ICoupleCart, IInventory
{

    private static final DataParameter<Integer> COUNT = EntityDataManager.createKey(EntityHopperCart.class, DataSerializers.VARINT);
    public int invItemsCount = 0;

    public EntityHopperCart(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityHopperCart(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if (!player.isSneaking())
        {
            if (!this.world.isRemote)
                player.openGui(IndustrialRenewal.instance, GUIHandler.HOPPERCART, this.world, this.getEntityId(), 0, 0);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    public int GetInvNumber()
    {
        int items = 0;
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (!itemstack.isEmpty())
            {
                items = items + 1;
            }
        }
        this.invItemsCount = items;
        return this.invItemsCount;
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(ModItems.hopperCart, 1), 0.0F);
        }
    }

    @Override
    public ItemStack getCartItem()
    {
        return new ItemStack(ModItems.hopperCart);
    }

    @Override
    public int getSizeInventory()
    {
        return 27;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setTag("inventory", this.inventory.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.Sync();
    }

    @Override
    public void Sync()
    {
        if (!this.world.isRemote)
        {
            this.dataManager.set(COUNT, GetInvNumber());
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(COUNT, 0);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if (this.world.isRemote && key.equals(COUNT))
        {
            this.invItemsCount = this.dataManager.get(COUNT);
        }
    }

    @Override
    public float getFixedDistance(EntityMinecart cart)
    {
        return 0.86F;
    }
}
