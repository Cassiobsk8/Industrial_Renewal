package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.item.ModItems;
import cassiokf.industrialrenewal.network.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketLogCart;
import cassiokf.industrialrenewal.network.PacketReturnLogCart;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityLogCart extends EntityMinecart {

    public int invItensCount = 0;
    private int tick = 0;
    public ItemStackHandler inventory = new ItemStackHandler(27) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (stack.isEmpty()) {
                return false;
            }
            return Utils.isWood(stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            GetInvNumber();
            if (!world.isRemote) {
                NetworkHandler.INSTANCE.sendToAllAround(new PacketLogCart(EntityLogCart.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 64));
            }
        }
    };

    public EntityLogCart(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityLogCart(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    private void getPacket() {
        tick++;
        tick %= 40;
        if (world.isRemote && tick == 0) {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnLogCart(this));
        }
    }

    @Override
    public void killMinecart(DamageSource source) {
        //super.killMinecart(source);
        this.setDead();

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(ModItems.logCart, 1), 0.0F);
        }
    }

    public void GetInvNumber() {

        int items = 0;
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                items = items + 1;
            }
        }
        if (world.isRemote) {
            getPacket();
        } else invItensCount = items / 3;
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.logCart);
    }

    public EntityMinecart.Type getType() {
        return Type.CHEST;
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
        GetInvNumber();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
    }
}
