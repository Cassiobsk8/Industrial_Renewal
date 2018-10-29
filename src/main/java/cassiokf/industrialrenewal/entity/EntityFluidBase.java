package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class EntityFluidBase extends EntityMinecart implements IFluidHandler {

    public FluidTank tank = new FluidTank(32000);
    private boolean needsUpdate = false;
    private int updateTimer = 0;

    public EntityFluidBase(World worldIn) {
        super(worldIn);
        this.setSize(1.6F, 1.4F);
    }

    public EntityFluidBase(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        //super.killMinecart(source);
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(ModItems.cargoContainer, 1), 0.0F);
        }
    }

    @Override
    public Type getType() {
        return Type.CHEST;
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.cargoContainer);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        needsUpdate = true;
        return this.tank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        needsUpdate = true;
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tank.writeToNBT(tag);
    }



}
