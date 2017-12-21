package cassiokf.industrialrenewal.tileentity.carts;

import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class CartTransportContainer extends EntityMinecartContainer implements ISidedInventory {

    protected static final DataParameter<Integer> CARTITEMS = EntityDataManager.<Integer>createKey(CartTransportContainer.class, DataSerializers.VARINT);


    public CartTransportContainer(World world, double x, double y, double z, int cartType, int color) {
        super(world, x, y, z);
    }
    @Override
    public void killMinecart(DamageSource par1DamageSource) {
        this.setDead();

        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = getCartItem();
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getName());
            }
            this.entityDropItem(itemstack, 0.0F);
        }
    }
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return 36;
    }
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CARTITEMS, 0);
    }

    @Override
    public Type getType() {
        return null;
    }


    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[36];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        this.addLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }
}