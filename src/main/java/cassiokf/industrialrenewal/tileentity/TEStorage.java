package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.MultiStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

public class TEStorage extends TEMultiTankBase<TEStorage> {
    private final MultiStackHandler inventory = new MultiStackHandler(capacity, true, this);
    private static final int capacity = 4096;

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition) {
        return MachinesUtils.getBlocksIn3x3x2Centered(centerPosition, getMasterFacing());
    }

    @Override
    public void setSize(int i) {
        int newCapacity = capacity * i;
        if (inventory.getCount() > newCapacity) {
            spawnItems(inventory.getCount() - newCapacity);
        }
        inventory.setSlotLimit(capacity * i);
    }

    @Override
    public void onMasterBreak() {
        super.onMasterBreak();
        if (isBottom() && inventory.getCount() > 0) spawnItems(inventory.getCount());
    }

    private void spawnItems(int quantity) {
        if (world.isRemote) return;
        int temQ = quantity;
        ItemStack stack = inventory.getStackInSlot(0).copy();

        while (temQ >= stack.getMaxStackSize()) {
            ItemStack sStack = stack.copy();
            sStack.setCount(stack.getMaxStackSize());
            Utils.spawnItemStack(world, pos.offset(getMasterFacing().getOpposite(), 2), sStack);
            temQ -= stack.getMaxStackSize();
        }
        if (temQ > 0) {
            ItemStack sStack = stack.copy();
            sStack.setCount(temQ);
            Utils.spawnItemStack(world, pos.offset(getMasterFacing().getOpposite(), 2), sStack);
        }
        inventory.removeFromCount(quantity);
    }

    public ItemStack getStack() {
        return inventory.getStackInSlot(0);
    }

    public int getCount() {
        return inventory.getCount();
    }

    public int getCapacity() {
        return inventory.getSlotLimit(0);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TEStorage;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getMaster().getBottomTE().inventory);
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inv", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inv"));
        super.readFromNBT(compound);
    }
}
