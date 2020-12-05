package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityLogCart extends EntityInventoryCartBase
{

    public int invItensCount = 0;
    private static final DataParameter<Integer> COUNT = EntityDataManager.createKey(EntityLogCart.class, DataSerializers.VARINT);

    public EntityLogCart(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityLogCart(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, EnumHand hand)
    {
        if (!player.isSneaking())
        {
            if (!this.world.isRemote)
                player.openGui(IndustrialRenewal.instance, GUIHandler.LOGCART, this.world, this.getEntityId(), 0, 0);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return Utils.isWood(stack);
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(ModItems.logCart, 1), 0.0F);
        }
    }

    private int GetInvNumber()
    {
        int items = 0;
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                items = items + 1;
            }
        }
        this.invItensCount = items / 3;
        return this.invItensCount;
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.logCart);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("inventory", this.inventory.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
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
    public int getSizeInventory()
    {
        return 27;
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
            this.invItensCount = this.dataManager.get(COUNT);
        }
    }
}
