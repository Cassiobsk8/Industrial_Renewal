package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.Registry.ModItems;
import cassiokf.industrialrenewal.Registry.NetworkHandler;
import cassiokf.industrialrenewal.network.PacketReturnSteamLocomotive;
import cassiokf.industrialrenewal.network.PacketSteamLocomotive;
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

import javax.annotation.Nullable;

public class EntitySteamLocomotive extends EntityMinecart {

    public ItemStackHandler inventory = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            if (!world.isRemote) {
                NetworkHandler.INSTANCE.sendToAllAround(new PacketSteamLocomotive(EntitySteamLocomotive.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 64));
            }
        }
    };

    private int tick = 0;
    public boolean cornerFlip;
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;

    public EntitySteamLocomotive(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F); //TODO aumentar isso sem dar problema com os blocos adjacentes
    }

    public boolean hasPlowItem() {
        getPacket();
        ItemStack stack = this.inventory.getStackInSlot(6);
        if (!stack.isEmpty()) {
            return true;
        }
        return false;
    }

    private void getPacket() {
        tick++;
        tick %= 40;
        if (world.isRemote && tick == 0) {
            NetworkHandler.INSTANCE.sendToServer(new PacketReturnSteamLocomotive(this));
        }
    }

    public EntitySteamLocomotive(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        super.killMinecart(source);

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(ModItems.steamLocomotive, 1), 0.0F);
        }
    }

    public EntityMinecart.Type getType() {
        return Type.FURNACE;
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

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.steamLocomotive);
    }
}
