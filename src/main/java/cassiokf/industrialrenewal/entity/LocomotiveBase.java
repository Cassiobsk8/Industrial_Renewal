package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.item.ItemIronPlow;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import cassiokf.industrialrenewal.util.interfaces.ISync;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

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
        Direction cartDir = getAdjustedHorizontalFacing();
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
        world.playSound(null, getPosition(), SoundsRegistration.TILEENTITY_TRAINHORN, SoundCategory.NEUTRAL, 2F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1F);
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (!source.isExplosion() && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
        {
            this.entityDropItem(new ItemStack(ItemsRegistration.STEAMLOCOMOTIVE.get(), 1), 0.0F);
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.put("inventory", this.inventory.serializeNBT());
    }

    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        this.inventory.deserializeNBT(compound.getCompound("inventory"));
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
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction facing)
    {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory)
                : super.getCapability(cap, facing);
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
