package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.blocks.railroad.BlockCargoLoader;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityCargoLoader extends TileEntityBaseLoader implements ITickable {

    public ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityCargoLoader.this.Sync();
        }
    };
    private int itemsPerTick = 16;

    private int intUnloadActivity = 0;
    private boolean checked = false;
    private boolean master;

    @Override
    public void update()
    {
        if (!world.isRemote && isMaster())
        {
            if (isUnload())
            {
                if (cartActivity > 0)
                {
                    cartActivity--;
                    Sync();
                }
                TileEntity te = world.getTileEntity(pos.down().offset(getBlockFacing().getOpposite()));
                if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing()))
                {
                    IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing());
                    if (handler != null)
                    {
                        for (int i = 0; i < inventory.getSlots(); i++)
                        {
                            if (!inventory.getStackInSlot(i).isEmpty())
                            {
                                for (int j = 0; j < handler.getSlots(); j++)
                                {
                                    ItemStack stack = this.inventory.extractItem(i, itemsPerTick, true);
                                    if (!stack.isEmpty() && handler.isItemValid(j, stack))
                                    {
                                        ItemStack left = handler.insertItem(j, stack, false);
                                        if (!ItemStack.areItemStacksEqual(stack, left))
                                        {
                                            int toExtract = stack.getCount() - left.getCount();
                                            this.inventory.extractItem(i, toExtract, false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onMinecartPass(EntityMinecart cart, TileEntityLoaderRail loaderRail)
    {
        if (!world.isRemote)
        {
            cartActivity = 10;
            IItemHandler cartCapability = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            if (cartCapability != null)
            {
                if (isUnload()) //From cart to inventory
                {
                    boolean needBreak = false;
                    boolean activity = false;
                    for (int i = 0; i < cartCapability.getSlots(); i++)
                    {
                        for (int j = 0; j < this.inventory.getSlots(); j++)
                        {
                            ItemStack stack = cartCapability.extractItem(i, itemsPerTick, true);
                            if (!stack.isEmpty())
                            {
                                ItemStack left = this.inventory.insertItem(j, stack, false);
                                if (left.isEmpty() || left.getCount() < stack.getCount())
                                {
                                    activity = true;
                                    intUnloadActivity = 0;
                                }
                                if (!ItemStack.areItemStacksEqual(stack, left))
                                {
                                    int toExtract = stack.getCount() - left.getCount();
                                    cartCapability.extractItem(i, toExtract, false);
                                    needBreak = true;
                                    break;
                                }
                            }
                        }
                        if (needBreak)
                        {
                            break;
                        }
                    }
                    if (activity)
                    {
                        loading = true;
                        return true;
                    } else intUnloadActivity++;
                    loading = false;
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return !Utils.IsInventoryEmpty(cartCapability);
                    }
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return intUnloadActivity < 2 || !Utils.IsInventoryFull(cartCapability);
                    }
                    if (waitE == waitEnum.NO_ACTIVITY) return false;
                } else //From inventory to cart
                {
                    boolean needBreak = false;
                    boolean activity = false;

                    for (int i = 0; i < this.inventory.getSlots(); i++) {
                        for (int j = 0; j < cartCapability.getSlots(); j++)
                        {
                            ItemStack stack = this.inventory.extractItem(i, itemsPerTick, true);
                            if (!stack.isEmpty() && cartCapability.isItemValid(j, stack))
                            {
                                ItemStack left = cartCapability.insertItem(j, stack, false);
                                if (left.isEmpty() || left.getCount() < stack.getCount()) {
                                    activity = true;
                                    intUnloadActivity = 0;
                                }
                                if (!ItemStack.areItemStacksEqual(stack, left)) {
                                    int toExtract = stack.getCount() - left.getCount();
                                    this.inventory.extractItem(i, toExtract, false);
                                    needBreak = true;
                                    break;
                                }
                            }
                        }
                        if (needBreak)
                        {
                            break;
                        }
                    }

                    if (activity)
                    {
                        loading = true;
                        return true;
                    } else intUnloadActivity++;
                    loading = false;
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return intUnloadActivity < 2 || !Utils.IsInventoryFull(cartCapability);
                    }
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return !Utils.IsInventoryEmpty(cartCapability);
                    }
                    if (waitE == waitEnum.NO_ACTIVITY) return false;
                }
            }
        }
        return waitE == waitEnum.NEVER; //false
    }

    public String getModeText()
    {
        String mode = I18n.format("tesr.ir.mode") + ": ";
        if (isUnload()) return mode + I18n.format("gui.industrialrenewal.button.unloader_mode");
        return mode + I18n.format("gui.industrialrenewal.button.loader_mode");
    }

    public String getTankText()
    {
        return I18n.format("tesr.ir.inventory");
    }

    public float getCartFluidAngle()
    {
        if (cartActivity <= 0) return 0;
        float currentAmount = Utils.getInvNorm(inventory);
        float totalCapacity = inventory.getSlots();
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public boolean isMaster()
    {
        if (!checked)
        {
            master = world.getBlockState(pos).getValue(BlockChunkLoader.MASTER);
            checked = true;
        }
        return master;
    }

    private BlockPos getMasterPos()
    {
        if (!isInvalid()
                && world.getBlockState(pos).getBlock() instanceof BlockCargoLoader
                && world.getBlockState(pos).getValue(BlockCargoLoader.MASTER))
            return pos;
        return BlockCargoLoader.getMasterPos(world, pos, getBlockFacing());
    }

    @Override
    public boolean isUnload()
    {
        return unload;
    }

    @Override
    public EnumFacing getBlockFacing()
    {
        if (blockFacing == null) blockFacing = world.getBlockState(pos).getValue(BlockCargoLoader.FACING);
        return blockFacing;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("activity", cartActivity);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        cartActivity = compound.getInteger("activity");
        super.readFromNBT(compound);
    }

    public <T> T getInternalCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        BlockPos masterPos = getMasterPos();
        if (masterPos == null) return false;
        return (pos.equals(masterPos.up()) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        BlockPos masterPos = getMasterPos();
        if (masterPos == null) return super.getCapability(capability, facing);
        TileEntityCargoLoader te = (TileEntityCargoLoader) world.getTileEntity(masterPos);
        if (te != null && pos.equals(masterPos.up()) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(te.inventory);
        return super.getCapability(capability, facing);
    }
}
