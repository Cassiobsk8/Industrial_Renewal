package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.BlockChunkLoader;
import cassiokf.industrialrenewal.blocks.railroad.BlockCargoLoader;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static cassiokf.industrialrenewal.init.TileRegistration.CARGOLOADER_TILE;

public class TileEntityCargoLoader extends TileEntityBaseLoader implements ITickableTileEntity
{

    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
    private int itemsPerTick = 16;

    private int intUnloadActivity = 0;
    private boolean checked = false;
    private boolean master;

    public TileEntityCargoLoader()
    {
        super(CARGOLOADER_TILE.get());
    }

    private IItemHandler createHandler()
    {
        return new ItemStackHandler(5)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityCargoLoader.this.Sync();
            }
        };
    }

    @Override
    public void tick()
    {
        if (hasWorld() && !world.isRemote && isMaster())
        {
            if (isUnload())
            {
                if (cartActivity > 0)
                {
                    cartActivity--;
                    Sync();
                }
                TileEntity te = world.getTileEntity(pos.down().offset(getBlockFacing().getOpposite()));
                if (te != null)
                {
                    IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing()).orElse(null);
                    if (handler != null)
                    {
                        for (int i = 0; i < inventory.orElse(null).getSlots(); i++)
                        {
                            if (!inventory.orElse(null).getStackInSlot(i).isEmpty())
                            {
                                for (int j = 0; j < handler.getSlots(); j++)
                                {
                                    ItemStack stack = inventory.orElse(null).extractItem(i, itemsPerTick, true);
                                    if (!stack.isEmpty() && handler.isItemValid(j, stack))
                                    {
                                        ItemStack left = handler.insertItem(j, stack, false);
                                        if (!ItemStack.areItemStacksEqual(stack, left))
                                        {
                                            int toExtract = stack.getCount() - left.getCount();
                                            inventory.orElse(null).extractItem(i, toExtract, false);
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
    public boolean onMinecartPass(AbstractMinecartEntity entityMinecart, TileEntityLoaderRail loaderRail)
    {
        if (hasWorld() && !world.isRemote)
        {
            cartActivity = 10;
            IItemHandler cartCapability = entityMinecart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
            if (cartCapability != null)
            {
                if (isUnload()) //From cart to inventory
                {
                    boolean needBreak = false;
                    boolean activity = false;
                    for (int i = 0; i < cartCapability.getSlots(); i++)
                    {
                        for (int j = 0; j < inventory.orElse(null).getSlots(); j++)
                        {
                            ItemStack stack = cartCapability.extractItem(i, itemsPerTick, true);
                            if (!stack.isEmpty())
                            {
                                ItemStack left = inventory.orElse(null).insertItem(j, stack, false);
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

                    for (int i = 0; i < inventory.orElse(null).getSlots(); i++)
                    {
                        for (int j = 0; j < cartCapability.getSlots(); j++)
                        {
                            ItemStack stack = inventory.orElse(null).extractItem(i, itemsPerTick, true);
                            if (!stack.isEmpty() && cartCapability.isItemValid(j, stack))
                            {
                                ItemStack left = cartCapability.insertItem(j, stack, false);
                                if (left.isEmpty() || left.getCount() < stack.getCount())
                                {
                                    activity = true;
                                    intUnloadActivity = 0;
                                }
                                if (!ItemStack.areItemStacksEqual(stack, left))
                                {
                                    int toExtract = stack.getCount() - left.getCount();
                                    inventory.orElse(null).extractItem(i, toExtract, false);
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
        float currentAmount = Utils.getInvNorm(inventory.orElse(null));
        float totalCapacity = inventory.orElse(null).getSlots();
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
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
        if (world.getBlockState(pos).get(BlockCargoLoader.MASTER)) return pos;
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
        if (blockFacing == null) blockFacing = world.getBlockState(pos).get(BlockCargoLoader.FACING);
        return blockFacing;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        inventory.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        compound.putInt("activity", cartActivity);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        cartActivity = compound.getInt("activity");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        BlockPos masterPos = getMasterPos();
        if (masterPos == null) return super.getCapability(capability, facing);
        TileEntityCargoLoader te = (TileEntityCargoLoader) world.getTileEntity(masterPos);
        if (te != null && pos.equals(masterPos.up()) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }
}
