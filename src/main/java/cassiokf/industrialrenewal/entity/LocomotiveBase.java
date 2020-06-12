package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import cassiokf.industrialrenewal.util.interfaces.IDirectionCart;
import cassiokf.industrialrenewal.util.interfaces.ISync;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class LocomotiveBase extends TrainBase implements ICoupleCart, IDirectionCart, ISync
{
    private static final DataParameter<Boolean> PLOW = EntityDataManager.createKey(EntitySteamLocomotive.class, DataSerializers.BOOLEAN);
    public boolean hasPlowItem;
    public boolean cornerFlip;
    public ItemStackHandler inventory = new ItemStackHandler(7)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            LocomotiveBase.this.sync();
        }
    };
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;

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
        double acceleration = 0.03D;
        this.motionX += cartDir.getXOffset() * acceleration;
        this.motionZ += cartDir.getZOffset() * acceleration;
        this.motionX = MathHelper.clamp(this.motionX, -this.getMaxCartSpeedOnRail(), this.getMaxCartSpeedOnRail());
        this.motionZ = MathHelper.clamp(this.motionZ, -this.getMaxCartSpeedOnRail(), this.getMaxCartSpeedOnRail());
    }

    @Override
    public void moveMinecartOnRail(BlockPos pos)
    {
        super.moveMinecartOnRail(pos);
        IBlockState blockState = world.getBlockState(pos);
        BlockRailBase.EnumRailDirection railDirection = ((BlockRailBase) blockState.getBlock()).getRailDirection(world, pos, blockState, this);
        cornerFlip = ((railDirection == BlockRailBase.EnumRailDirection.SOUTH_EAST || railDirection == BlockRailBase.EnumRailDirection.SOUTH_WEST) && motionX < 0.0)
                || ((railDirection == BlockRailBase.EnumRailDirection.NORTH_EAST || railDirection == BlockRailBase.EnumRailDirection.NORTH_WEST) && motionX > 0.0);

    }

    public boolean getRenderFlippedYaw(float yaw)
    {
        yaw %= 360.0f;
        if (yaw < 0.0f)
        {
            yaw += 360.0f;
        }
        if (!oldRender || Math.abs(yaw - lastRenderYaw) < 90.0f || Math.abs(yaw - lastRenderYaw) > 270.0f || (motionX > 0.0 && lastMotionX < 0.0) || (motionZ > 0.0 && lastMotionZ < 0.0)
                || (motionX < 0.0 && lastMotionX > 0.0) || (motionZ < 0.0 && lastMotionZ > 0.0) || wrongRender >= 50)
        {
            lastMotionX = motionX;
            lastMotionZ = motionZ;
            lastRenderYaw = yaw;
            oldRender = true;
            wrongRender = 0;
            return false;
        }
        ++wrongRender;
        return true;
    }

    private boolean hasPlowItem()
    {
        boolean temp = false;
        ItemStack stack = this.inventory.getStackInSlot(6);
        if (!stack.isEmpty())
        {
            temp = true;
        }
        hasPlowItem = temp;
        return hasPlowItem;
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

    @Override
    public boolean isRotated()
    {
        return false;
    }

    @Override
    public void setRotation(float rotation)
    {
        //renderYaw = rotation;
    }
}
