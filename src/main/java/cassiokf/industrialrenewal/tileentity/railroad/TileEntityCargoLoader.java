package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.blocks.railroad.BlockCargoLoader;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileEntityCargoLoader extends TileEntityBaseLoader implements ITickableTileEntity
{

    public final CustomItemStackHandler inventory = new CustomItemStackHandler(5)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityCargoLoader.this.sync();
        }
    };
    private static final int itemsPerTick = IRConfig.Railroad.maxLoaderItemPerTick.get();

    private int intUnloadActivity = 0;
    private boolean checked = false;
    private boolean master;
    private int noActivity = 0;

    public TileEntityCargoLoader(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
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
                if (te != null)
                {
                    IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing()).orElse(null);
                    if (handler != null)
                    {
                        Utils.moveItemsBetweenInventories(inventory, handler);
                    }
                }
            }
        }
    }

    @Override
    public boolean onMinecartPass(MinecartEntity cart, TileEntityLoaderRail loaderRail)
    {
        if (!world.isRemote)
        {
            cartActivity = 10;
            IItemHandler cartCapability = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
            if (cartCapability != null)
            {
                if (isUnload()) //From cart to inventory
                {
                    if (Utils.moveItemsBetweenInventories(cartCapability, inventory, itemsPerTick))
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
                    if (Utils.moveItemsBetweenInventories(inventory, cartCapability, itemsPerTick))
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
            master = world.getBlockState(pos).get(BlockChunkLoader.MASTER);
            checked = true;
        }
        return master;
    }

    private BlockPos getMasterPos()
    {
        if (!isRemoved()
                && (getBlockState().getBlock() instanceof BlockCargoLoader)
                && getBlockState().get(BlockCargoLoader.MASTER))
            return pos;
        return BlockCargoLoader.getMasterPos(world, pos, getBlockFacing());
    }

    @Override
    public boolean isUnload()
    {
        return unload;
    }

    @Override
    public Direction getBlockFacing()
    {
        if (blockFacing == null) blockFacing = getBlockState().get(BlockCargoLoader.FACING);
        return blockFacing;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inventory", inventory.serializeNBT());
        compound.putInt("activity", cartActivity);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.inventory.deserializeNBT(compound.getCompound("inventory"));
        cartActivity = compound.getInt("activity");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        BlockPos masterPos = getMasterPos();
        if (masterPos == null) return super.getCapability(capability, facing);
        TileEntityCargoLoader te = (TileEntityCargoLoader) world.getTileEntity(masterPos);
        if (te != null && pos.equals(masterPos.up()) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> te.inventory).cast();
        return super.getCapability(capability, facing);
    }
}
