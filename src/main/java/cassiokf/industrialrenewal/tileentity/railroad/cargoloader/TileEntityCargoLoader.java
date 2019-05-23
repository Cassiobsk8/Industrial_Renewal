package cassiokf.industrialrenewal.tileentity.railroad.cargoloader;

import cassiokf.industrialrenewal.tileentity.railroad.TileEntityBaseLoader;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityCargoLoader extends TileEntityBaseLoader implements ITickable {

    public ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            TileEntityCargoLoader.this.Sync();
        }
    };

    private int intLoadActivity = 0;
    private int intUnloadActivity = 0;


    @Override
    public void update() {
        if (!world.isRemote) {

            if (this.world.getTotalWorldTime() % 8 == 0) {
                int itemsPerTick = 8;
                boolean activity = false;
                boolean hasCart = false;

                //Pull from up cart/te
                IItemHandler handlerPull = getInventoryUp();

                if (handlerPull != null) {
                    boolean needBreak = false;
                    for (int i = 0; i < handlerPull.getSlots(); i++) {
                        for (int j = 0; j < this.inventory.getSlots(); j++) {
                            ItemStack stack = handlerPull.extractItem(i, itemsPerTick, true);
                            ItemStack left = this.inventory.insertItem(j, stack, false);
                            if (left.isEmpty() || left.getCount() < stack.getCount()) {
                                activity = true;
                                intLoadActivity = 0;
                            }
                            if (!ItemStack.areItemStacksEqual(stack, left)) {
                                int toExtract = stack.getCount() - left.getCount();
                                handlerPull.extractItem(i, toExtract, false);
                                markDirty();
                                needBreak = true;
                                break;
                            }
                        }
                        if (needBreak) {
                            break;
                        }
                    }
                    if (isUnload()) {
                        hasCart = true;
                        if (getWaitEnum() == waitEnum.WAIT_EMPTY && Utils.IsInventoryEmpty(handlerPull)) {
                            letCartPass(true);
                        }
                    }
                    //Collect items in world above
                } else {
                    List<EntityItem> items = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.getX(), this.pos.getY() + 0.5, this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 2, this.pos.getZ() + 1));
                    if (!items.isEmpty()) {
                        boolean needBreak = false;
                        for (int i = 0; i < this.inventory.getSlots(); i++) {
                            for (EntityItem item : items) {
                                if (item != null && !item.isDead) {
                                    ItemStack left = this.inventory.insertItem(i, item.getItem(), false);
                                    item.setItem(left);
                                    markDirty();
                                    needBreak = true;
                                    break;
                                }
                            }
                            if (needBreak) {
                                break;
                            }
                        }
                    }
                }
                //push from output te or cart
                IItemHandler handlerPush = getInventoryForHopperTransfer();
                if (handlerPush != null && this.inventory != null) {
                    for (int i = 0; i < this.inventory.getSlots(); i++) {
                        for (int j = 0; j < handlerPush.getSlots(); j++) {
                            ItemStack stack = this.inventory.extractItem(i, itemsPerTick, true);
                            if (handlerPush.isItemValid(j, stack)) {
                                ItemStack left = handlerPush.insertItem(j, stack, false);
                                if (left.isEmpty() || left.getCount() < stack.getCount()) {
                                    activity = true;
                                    intUnloadActivity = 0;
                                }
                                if (!ItemStack.areItemStacksEqual(stack, left)) {
                                    int toExtract = stack.getCount() - left.getCount();
                                    this.inventory.extractItem(i, toExtract, false);
                                    markDirty();
                                    return;
                                }
                            }
                        }
                    }
                    if (!isUnload()) {
                        hasCart = true;
                        if (getWaitEnum() == waitEnum.WAIT_FULL && Utils.IsInventoryFull(handlerPush) && !activity) {
                            letCartPass(true);
                        }
                    }
                }
                //No Activity Check
                if (activity) {
                    this.markDirty();
                    letCartPass(false);
                } else {
                    if (hasCart && getWaitEnum() == waitEnum.NO_ACTIVITY) {
                        if (!isUnload()) {
                            intLoadActivity++;
                        } else {
                            intUnloadActivity++;
                        }
                    }
                }

                if (!activity && getWaitEnum() == waitEnum.NO_ACTIVITY) {
                    if (!isUnload() && intLoadActivity >= 2) {
                        intLoadActivity = 0;
                        letCartPass(true);
                    } else if (isUnload() && intUnloadActivity >= 2) {
                        intUnloadActivity = 0;
                        letCartPass(true);
                    }
                }
            }
        }

    }

    private IItemHandler getInventoryUp() {
        BlockPos handlerPos = pos.offset(EnumFacing.UP);
        return getInventoryAtPosition(this.getWorld(), handlerPos);
    }

    private IItemHandler getInventoryForHopperTransfer() {
        EnumFacing enumfacing = getOutput();
        BlockPos handlerPos = pos.offset(enumfacing);
        if (!isUnload()) handlerPos = handlerPos.down();
        return getInventoryAtPosition(this.getWorld(), handlerPos);
    }

    /**
     * Returns the IInventory (if applicable) of the TileEntity at the specified position
     */
    private IItemHandler getInventoryAtPosition(World worldIn, BlockPos pos) {
        IItemHandler iHandler = null;

        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if (block.hasTileEntity(state)) {
            TileEntity te = worldIn.getTileEntity(pos);

            if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getOutput().getOpposite())) {
                iHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getOutput().getOpposite());
            }
        }

        if (iHandler == null) {
            List<Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(pos.getX() - 0.5D, pos.getY() - 0.5D, pos.getZ() - 0.5D, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D), EntitySelectors.IS_ALIVE);

            if (!list.isEmpty()) {
                iHandler = list.get(worldIn.rand.nextInt(list.size())).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getOutput().getOpposite());
            }
        }

        return iHandler;
    }

    public EnumFacing getOutput() {
        if (!isUnload()) {
            return EnumFacing.DOWN;
        }
        IBlockState state = this.world.getBlockState(this.pos).getActualState(this.world, this.pos);
        return state.getValue(BlockCargoLoader.FACING).getOpposite();
    }

    @Override
    public boolean isUnload() {
        IBlockState state = this.world.getBlockState(this.pos).getActualState(this.world, this.pos);
        return state.getValue(BlockCargoLoader.UNLOAD);
    }

    public void readTankFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Empty")) {
            tag.removeTag("Empty");
        }
        this.inventory.deserializeNBT(tag.getCompoundTag("inventory"));
    }

    public void writeEntityTankToNBT(NBTTagCompound tag) {
        tag.setTag("inventory", inventory.serializeNBT());
    }

    public NBTTagCompound GetTag() {
        NBTTagCompound tag = new NBTTagCompound();
        writeEntityTankToNBT(tag);
        return tag;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
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
