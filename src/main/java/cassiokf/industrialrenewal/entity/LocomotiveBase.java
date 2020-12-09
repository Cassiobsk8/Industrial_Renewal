package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemIronPlow;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import cassiokf.industrialrenewal.util.interfaces.ISync;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class LocomotiveBase extends RotatableBase implements ICoupleCart, ISync
{
    private static final DataParameter<Boolean> PLOW = EntityDataManager.createKey(EntitySteamLocomotive.class, DataSerializers.BOOLEAN);
    public boolean hasPlowItem;

    public ItemStackHandler inventory = new ItemStackHandler(1)
    {
        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return stack.getItem() instanceof ItemIronPlow;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            LocomotiveBase.this.sync();
        }
    };

    public EntityTenderBase tender;

    public LocomotiveBase(World worldIn)
    {
        super(worldIn);
    }

    public LocomotiveBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public void onLocomotiveUpdate()
    {
    }

    public void moveForward()
    {
        EnumFacing cartDir = getAdjustedHorizontalFacing();
        double acceleration = 0.04D;
        this.motionX += cartDir.getXOffset() * acceleration;
        this.motionZ += cartDir.getZOffset() * acceleration;
        this.motionX = MathHelper.clamp(this.motionX, -this.getMaxCartSpeedOnRail(), this.getMaxCartSpeedOnRail());
        this.motionZ = MathHelper.clamp(this.motionZ, -this.getMaxCartSpeedOnRail(), this.getMaxCartSpeedOnRail());
    }

    public void setTender(EntityTenderBase tender)
    {
        this.tender = tender;
    }

    private boolean hasPlowItem()
    {
        boolean temp = false;
        ItemStack stack = this.inventory.getStackInSlot(0);
        if (!stack.isEmpty())
        {
            temp = true;
        }
        hasPlowItem = temp;
        return hasPlowItem;
    }

    public void horn()
    {
        world.playSound(null, getPosition(), IRSoundRegister.TILEENTITY_TRAINHORN, SoundCategory.NEUTRAL, 2F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1F);
    }

    @Override
    protected double getMaxSpeed()
    {
        return super.getMaxSpeed();
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(ModItems.steamLocomotive, 1), 0.0F);
        }
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
        this.sync();
    }

    @Override
    public void sync()
    {
        if (!this.world.isRemote)
        {
            this.dataManager.set(PLOW, hasPlowItem());
        }
    }

    @Override
    public World getThisWorld()
    {
        return getEntityWorld();
    }

    @Override
    public BlockPos getThisPosition()
    {
        return getPosition();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory)
                : super.getCapability(capability, facing);
    }

    @Override
    public EntityMinecart.Type getType()
    {
        return Type.FURNACE;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(PLOW, false);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if (this.world.isRemote && key.equals(PLOW))
        {
            this.hasPlowItem = this.dataManager.get(PLOW);
        }
    }
}
