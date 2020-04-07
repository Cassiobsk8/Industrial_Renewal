package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public abstract class EntityFluidBase extends TrainBase implements IFluidHandler
{
    private static final DataParameter<NBTTagCompound> TANK = EntityDataManager.createKey(EntityFluidBase.class, DataSerializers.COMPOUND_TAG);
    public FluidTank tank = new FluidTank(IRConfig.MainConfig.Main.fluidCartCapacity)
    {
        @Override
        protected void onContentsChanged()
        {
            EntityFluidBase.this.Sync();
        }
    };

    public EntityFluidBase(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityFluidBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source)
    {
        this.setDead();
        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(ModItems.cargoContainer, 1), 0.0F);
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
        return new ItemStack(ModItems.cargoContainer);
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return this.tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        return this.tank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }

    //SYNC
    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);
        tank.readFromNBT(tag);
        this.dataManager.set(TANK, tag.getCompoundTag("tank"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        tank.writeToNBT(tag);
        tag.setTag("tank", this.dataManager.get(TANK));
    }

    public void Sync()
    {
        if (!this.world.isRemote)
        {
            this.dataManager.set(TANK, GetTag());
        }
    }

    public NBTTagCompound GetTag()
    {
        NBTTagCompound tankCompound = new NBTTagCompound();
        tank.writeToNBT(tankCompound);
        return tankCompound;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(TANK, new NBTTagCompound());
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
}
