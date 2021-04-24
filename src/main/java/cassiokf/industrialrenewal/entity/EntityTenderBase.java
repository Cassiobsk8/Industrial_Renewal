package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EntityTenderBase extends RotatableBase implements ICoupleCart, IInventory
{
    private static final DataParameter<CompoundNBT> TANK = EntityDataManager.createKey(EntityFluidBase.class, DataSerializers.COMPOUND_TAG);

    public final ItemStackHandler inventory = new ItemStackHandler(6)
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            if (stack.isEmpty()) return false;
            return FurnaceTileEntity.isFuel(stack);
        }
    };
    public final CustomFluidTank tank = new CustomFluidTank(64 * References.BUCKET_VOLUME)
    {
        @Override
        public boolean canFill(FluidStack resource)
        {
            return resource != null && IRConfig.waterTypesContains(resource.getFluid().getRegistryName());
        }

        @Override
        protected void onContentsChanged()
        {
            EntityTenderBase.this.Sync();
        }
    };

    public EntityTenderBase(World worldIn)
    {
        super(worldIn);
    }

    public EntityTenderBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand)
    {
        if (FluidUtil.interactWithFluidHandler(player, hand, tank))
        {
            if (world.isRemote) player.swingArm(hand);
            return ActionResultType.SUCCESS;
        }
        if (!player.isSneaking())
        {
            if (!this.world.isRemote)
                player.openGui(IndustrialRenewal.instance, GUIHandler.TENDER, this.world, this.getEntityId(), 0, 0);
            return ActionResultType.SUCCESS;
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        this.remove();
        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(ItemsRegistration.TENDER.get(), 1), 0.0F);
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
        return new ItemStack(ItemsRegistration.TENDER.get());
    }

    @Override
    public float getFixedDistance(MinecartEntity cart)
    {
        return 0.6f;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        return super.getCapability(capability, facing);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("tank", this.tank.writeToNBT(new CompoundNBT()));
        compound.put("tankD", this.dataManager.get(TANK));
    }

    @Override
    protected void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        this.inventory.deserializeNBT(compound.getCompound("inventory"));
        this.tank.readFromNBT(compound.getCompound("tank"));
        this.dataManager.set(TANK, compound.getCompound("tankD"));
    }

    public void Sync()
    {
        if (!this.world.isRemote)
        {
            this.dataManager.set(TANK, GetTag());
        }
    }

    public CompoundNBT GetTag()
    {
        CompoundNBT tankCompound = new CompoundNBT();
        tank.writeToNBT(tankCompound);
        return tankCompound;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(TANK, new CompoundNBT());
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if (this.world.isRemote && key.equals(TANK))
        {
            this.tank.readFromNBT(this.dataManager.get(TANK));
        }
    }

    //Because Railcraft
    @Override
    public int getSizeInventory()
    {
        return 6;
    }

    @Override
    public boolean isEmpty()
    {
        return Utils.IsInventoryEmpty(inventory);
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return inventory.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return inventory.extractItem(index, 64, false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        inventory.setStackInSlot(index, stack);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return !player.isSpectator();
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        player.openGui(IndustrialRenewal.instance, GUIHandler.TENDER, this.world, this.getEntityId(), 0, 0);
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        player.closeScreen();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return inventory.isItemValid(index, stack);
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }
}
