package cassiokf.industrialrenewal.tileentity.machines;

import cassiokf.industrialrenewal.recipes.LatheRecipe;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TELathe extends TileEntityMultiBlockBase<TELathe>
{
    private final VoltsEnergyContainer energyContainer;
    private final ItemStackHandler input;
    private final ItemStackHandler outPut;
    private final ItemStackHandler hold;
    public boolean inProcess;
    private int tick;
    private int processTime;
    private ItemStack processingItem;
    private int energyPTick = 128;

    public TELathe()
    {
        this.energyContainer = new VoltsEnergyContainer(10240, 256, 256)
        {
            @Override
            public void onEnergyChange()
            {
                if (!world.isRemote)
                {
                    TELathe.this.Sync();
                }
            }
        };
        this.input = new ItemStackHandler(1)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TELathe.this.Sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return LatheRecipe.LATHE_RECIPES.containsKey(stack.getItem());
            }
        };
        this.outPut = new ItemStackHandler(1)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TELathe.this.Sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return false;
            }
        };
        this.hold = new ItemStackHandler(1)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TELathe.this.Sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return false;
            }
        };
    }

    @Override
    public void tick()
    {
        if (isMaster())
        {
            ItemStack inputStack = input.getStackInSlot(0);
            if (!inProcess
                    && !inputStack.isEmpty()
                    && LatheRecipe.LATHE_RECIPES.containsKey(inputStack.getItem()))
            {
                LatheRecipe recipe = LatheRecipe.LATHE_RECIPES.get(inputStack.getItem());
                if (recipe != null)
                {
                    ItemStack result = recipe.getRecipeOutput();
                    if (result != null
                            && !result.isEmpty()
                            && hold.insertItem(0, result, true).isEmpty()
                            && outPut.insertItem(0, result, true).isEmpty()
                            && energyContainer.getEnergyStored() >= (energyPTick * recipe.getProcessTime()))
                    {
                        processTime = recipe.getProcessTime();
                        inProcess = true;
                        processingItem = inputStack;
                        if (!world.isRemote)
                        {
                            inputStack.shrink(1);
                            hold.insertItem(0, result, false);
                        }
                    }
                }
            } else if (inProcess)
            {
                energyContainer.extractEnergy(energyPTick, false);
                tick++;
                if (tick >= processTime)
                {
                    tick = 0;
                    processTime = 0;
                    inProcess = false;
                    processingItem = null;
                    if (!world.isRemote) outPut.insertItem(0, hold.extractItem(0, Integer.MAX_VALUE, false), false);
                }
            }

            if (!world.isRemote && !outPut.getStackInSlot(0).isEmpty())
            {
                EnumFacing facing = getMasterFacing().rotateY();
                TileEntity te = world.getTileEntity(pos.offset(facing, 2));
                if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
                {
                    IItemHandler outputCap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
                    if (outputCap != null)
                    {
                        for (int j = 0; j < outputCap.getSlots(); j++)
                        {
                            ItemStack stack = outPut.extractItem(0, Integer.MAX_VALUE, true);
                            if (!stack.isEmpty() && outputCap.isItemValid(j, stack))
                            {
                                ItemStack left = outputCap.insertItem(j, stack, false);
                                if (!ItemStack.areItemStacksEqual(stack, left))
                                {
                                    int toExtract = stack.getCount() - left.getCount();
                                    outPut.extractItem(0, toExtract, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TELathe;
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x2x2Centered(centerPosition, getMasterFacing());
    }

    public IItemHandler getInputInv()
    {
        return input;
    }

    public IItemHandler getOutputInv()
    {
        return outPut;
    }

    public IEnergyStorage getEnergyStorage()
    {
        return energyContainer;
    }

    public ItemStack getResultItem()
    {
        return hold.getStackInSlot(0);
    }

    public ItemStack getProcessingItem()
    {
        return processingItem;
    }

    public float getNormalizedProcess()
    {
        return processTime > 0 ? Utils.normalize(tick, 0, processTime) : 0;
    }

    @Override
    public void onMasterBreak()
    {
        Utils.dropInventoryItems(world, pos.offset(getMasterFacing().rotateYCCW()), input);
        Utils.dropInventoryItems(world, pos.offset(getMasterFacing().rotateY()), outPut);
        super.onMasterBreak();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        TELathe masterTE = getMaster();
        if (capability.equals(CapabilityEnergy.ENERGY)
                && facing.equals(getMasterFacing())
                && pos.equals(masterTE.getPos().offset(getMasterFacing()).offset(getMasterFacing().rotateYCCW())))
            return true;
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            if (facing.equals(getMasterFacing().rotateYCCW())
                    && pos.equals(masterTE.getPos().offset(getMasterFacing().rotateYCCW())))
                return true;
            if (facing.equals(getMasterFacing().rotateY())
                    && pos.equals(masterTE.getPos().offset(getMasterFacing().rotateY())))
                return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        TELathe masterTE = getMaster();
        if (capability.equals(CapabilityEnergy.ENERGY)
                && facing.equals(getMasterFacing())
                && pos.equals(masterTE.getPos().offset(getMasterFacing()).offset(getMasterFacing().rotateYCCW())))
            return CapabilityEnergy.ENERGY.cast(masterTE.energyContainer);
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            if (facing.equals(getMasterFacing().rotateYCCW())
                    && pos.equals(masterTE.getPos().offset(getMasterFacing().rotateYCCW())))
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(masterTE.input);
            if (facing.equals(getMasterFacing().rotateY())
                    && pos.equals(masterTE.getPos().offset(getMasterFacing().rotateY())))
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(masterTE.outPut);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("input", this.input.serializeNBT());
        compound.setTag("output", this.outPut.serializeNBT());
        compound.setTag("hold", this.hold.serializeNBT());
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        compound.setBoolean("processing", inProcess);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.input.deserializeNBT(compound.getCompoundTag("input"));
        this.outPut.deserializeNBT(compound.getCompoundTag("output"));
        this.hold.deserializeNBT(compound.getCompoundTag("hold"));
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        inProcess = compound.getBoolean("processing");
        super.readFromNBT(compound);
    }
}
