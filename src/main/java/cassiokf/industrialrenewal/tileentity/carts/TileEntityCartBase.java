package cassiokf.industrialrenewal.tileentity.carts;

import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileEntityCartBase extends EntityMinecartContainer implements ISidedInventory {

    protected static final DataParameter<Integer> CARTTYPE = EntityDataManager.<Integer>createKey(TileEntityCartBase.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> CARTCOLOR = EntityDataManager.<Integer>createKey(TileEntityCartBase.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> CARTITEMS = EntityDataManager.<Integer>createKey(TileEntityCartBase.class, DataSerializers.VARINT);

    private static final int[] SLOTS36 = buildSlotArray(0, 36);
    public int colour = 0;
    protected int cart;


    public TileEntityCartBase(World world) {
        super(world);
    }

    public TileEntityCartBase(World world, double x, double y, double z, int cartType, int color) {
        super(world, x, y, z);
        this.setCustomCartType(cartType);
        this.cart = cartType;
        if (cartType == 5) {
            this.setColor(color);
            this.colour = color;
        }
    }

    public static int[] buildSlotArray(int start, int size) {
        int[] slots = new int[size];
        for (int i = 0; i < size; i++) {
            slots[i] = start + i;
        }
        return slots;
    }

    public int countItems() {
        int items = 0;
        if (this != null) {
            for (int i1 = 0; i1 < this.getSizeInventory(); ++i1) {
                ItemStack itemstack = this.getStackInSlot(i1);
                if (itemstack != null) {
                    items = items + 1;
                }
            }
        }
        return items;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CARTTYPE, cart);
        this.dataManager.register(CARTCOLOR, Integer.valueOf(colour));
        this.dataManager.register(CARTITEMS, 0);
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
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.cargoContainer);
    }

    protected void readEntityFromNBT(NBTTagCompound tag) {
        int type = tag.getInteger("CustomType");
        this.setCustomCartType(type);
        this.cart = type;
        this.dataManager.set(CARTITEMS, this.countItems());
        if ((type == 5 || type == 0) && tag.hasKey("Colour")) {
            this.setColor(tag.getInteger("Colour"));
        }
        super.readEntityFromNBT(tag);
        if (tag.getBoolean("CustomDisplayTile")) {
            Block block;

            if (tag.hasKey("DisplayTile", 8)) {
                block = Block.getBlockFromName(tag.getString("DisplayTile"));
            } else {
                block = Block.getBlockById(tag.getInteger("DisplayTile"));
            }

            int i = tag.getInteger("DisplayData");
            this.setDisplayTile(block == null ? Blocks.AIR.getDefaultState() : block.getStateFromMeta(i));
            this.setDisplayTileOffset(tag.getInteger("DisplayOffset"));
        }
        if (tag.hasKey("CustomName", 8) && tag.getString("CustomName").length() > 0) {
            this.setCustomNameTag(tag.getString("CustomName"));
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        if (this.hasDisplayTile()) {
            tag.setBoolean("CustomDisplayTile", true);
            IBlockState iblockstate = this.getDisplayTile();
            ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(iblockstate.getBlock());
            tag.setString("DisplayTile", resourcelocation == null ? "" : resourcelocation.toString());
            tag.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
            tag.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }
        if (hasCustomName()) {
            tag.setString("CustomName", getCustomNameTag());
        }
        int type = getCustomCartType();
        tag.setInteger("CustomType", type);
        if (type == 5 || type == 0) {
            tag.setInteger("Colour", getColor());
        }
    }

    public int getColor() {
        return this.dataManager.get(CARTCOLOR);
    }

    public void setColor(int color) {
        this.dataManager.set(CARTCOLOR, Integer.valueOf(color));
    }

    @Override
    public int getSizeInventory() {
        return 36;
    }


    public int getCustomCartType() {
        return this.dataManager.get(CARTTYPE);
    }

    public void setCustomCartType(int cartType) {
        this.dataManager.set(CARTTYPE, Integer.valueOf(cartType));
    }

    public int getDefaultDisplayTileOffset() {
        return 8;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return true;
    }

    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            this.dataManager.set(CARTITEMS, this.countItems());
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return SLOTS36;
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
    public String getGuiID() {
        return this.hasCustomName() ? getCustomNameTag() : getName();
    }

    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }


    @Override
    public Type getType() {
        // TODO Auto-generated method stub
        return null;
    }

}
