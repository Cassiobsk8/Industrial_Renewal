package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.blocks.railroad.BlockCargoLoader;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityMinecart;
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

    public final ItemStackHandler inventory = new ItemStackHandler(5)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityCargoLoader.this.sync();
        }
    };
    private int itemsPerTick = 16;

    private int intUnloadActivity = 0;
    private boolean checked = false;
    private boolean master;
    private int noActivity = 0;

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
                    sync();
                }
                TileEntity te = world.getTileEntity(pos.down().offset(getBlockFacing().getOpposite()));
                if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing()))
                {
                    IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing());
                    if (handler != null)
                    {
                        Utils.moveItemsBetweenInventories(inventory, handler);
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
                    if (Utils.moveItemsBetweenInventories(cartCapability, inventory, true))
                    {
                        noActivity = 0;
                        intUnloadActivity = 0;
                        loading = true;
                        return true;
                    }

                    intUnloadActivity++;
                    loading = false;
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return !Utils.IsInventoryEmpty(cartCapability);
                    } else if (waitE == waitEnum.WAIT_FULL)
                    {
                        return intUnloadActivity < 10 || !Utils.IsInventoryFull(cartCapability);
                    }
                } else //From inventory to cart
                {
                    if (Utils.moveItemsBetweenInventories(inventory, cartCapability, true))
                    {
                        noActivity = 0;
                        intUnloadActivity = 0;
                        loading = true;
                        return true;
                    }

                    intUnloadActivity++;
                    loading = false;
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return intUnloadActivity < 10 || !Utils.IsInventoryFull(cartCapability);
                    } else if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return !Utils.IsInventoryEmpty(cartCapability);
                    }
                }
                if (waitE == waitEnum.NO_ACTIVITY)
                {
                    noActivity++;
                    return noActivity < 10;
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
        return Utils.getInvNorm(inventory) * 180f;
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
