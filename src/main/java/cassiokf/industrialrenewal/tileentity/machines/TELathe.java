package cassiokf.industrialrenewal.tileentity.machines;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.recipes.LatheRecipe;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TELathe extends TileEntityMultiBlockBase<TELathe>
{
    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(10240, 256, 256)
    {
        @Override
        public void onEnergyChange()
        {
            if (!world.isRemote)
            {
                TELathe.this.sync();
            }
        }
    };
    private final CustomItemStackHandler input = new CustomItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TELathe.this.sync();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return LatheRecipe.CACHED_RECIPES.containsKey(stack.getItem());
        }
    };
    private final CustomItemStackHandler outPut = new CustomItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TELathe.this.sync();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return false;
        }
    };
    private ItemStack hold = ItemStack.EMPTY;
    public boolean inProcess = false;
    private boolean oldInProcess;
    private int tick;
    private int processTime;
    private float renderCutterProcess;
    private float oldProcessTime;
    private ItemStack processingItem;
    private static final int energyPTick = IRConfig.Main.energyPerTickLatheMachine.get();
    private static final float volume = 0.2f * IRConfig.Sounds.masterVolumeMult.get();
    private boolean stopping = false;
    private boolean stopped = true;
    private boolean oldStopping = false;

    public TELathe(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
    {
        if (isMaster())
        {
            ItemStack inputStack = input.getStackInSlot(0);
            oldProcessTime = renderCutterProcess;
            if (!inProcess
                    && !inputStack.isEmpty()
                    && LatheRecipe.CACHED_RECIPES.containsKey(inputStack.getItem()))
            {
                getProcessFromInputItem(inputStack);
            } else if (inProcess)
            {
                process();
            }
            if (!inProcess && !oldInProcess) stopping = true;
            oldInProcess = inProcess;
            renderCutterProcess = processTime > 0 ? Utils.normalize(tick, 0, processTime) * 0.8f : 0;
            tryOutPutItem();
            handleSound();
        }
    }

    private void handleSound()
    {
        if (!inProcess && stopping && !stopped)
        {
            if (!world.isRemote)
                world.playSound(null, pos, SoundsRegistration.LATHE_STOP, SoundCategory.BLOCKS, volume, 1.0F);
            stopping = false;
            stopped = true;
            oldStopping = false;
            sync();
        } else if (inProcess)
        {
            stopped = false;
            if (world.isRemote)
                IRSoundHandler.playRepeatableSound(SoundsRegistration.LATHE_RESOURCEL, volume, 1.0F, pos);
        } else
        {
            if (world.isRemote) IRSoundHandler.stopTileSound(pos);
            stopping = false;
            if (oldStopping)
            {
                oldStopping = false;
                sync();
            }
        }
    }

    private void process()
    {
        energyContainer.extractEnergy(energyPTick, false);
        tick++;
        if (tick >= processTime)
        {
            tick = 0;
            processTime = 0;
            inProcess = false;
            processingItem = null;

            if (!world.isRemote) outPut.insertItem(0, hold, false);
        }
    }

    private void getProcessFromInputItem(ItemStack inputStack)
    {
        LatheRecipe recipe = LatheRecipe.CACHED_RECIPES.get(inputStack.getItem());
        if (recipe != null)
        {
            ItemStack result = recipe.getRecipeOutput();
            if (result != null
                    && !result.isEmpty()
                    && outPut.insertItem(0, result, true).isEmpty()
                    && energyContainer.getEnergyStored() >= (energyPTick * recipe.getProcessTime()))
            {
                processTime = recipe.getProcessTime();
                inProcess = true;
                processingItem = inputStack;
                if (!world.isRemote)
                {
                    inputStack.shrink(recipe.getInput().get(0).getCount());
                }
                hold = result;
            }
        }
    }

    private void tryOutPutItem()
    {
        if (!world.isRemote && !outPut.getStackInSlot(0).isEmpty())
        {
            Direction facing = getMasterFacing().rotateY();
            TileEntity te = world.getTileEntity(pos.offset(facing, 2));
            if (te != null)
            {
                IItemHandler outputCap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if (outputCap != null)
                {
                    Utils.moveItemsBetweenInventories(outPut, outputCap);
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
        return hold;
    }

    public ItemStack getProcessingItem()
    {
        return processingItem;
    }

    public float getNormalizedProcess()
    {
        return renderCutterProcess;
    }

    public float getOldProcess()
    {
        return oldProcessTime;
    }

    @Override
    public void onMasterBreak()
    {
        Utils.dropInventoryItems(world, pos.offset(getMasterFacing().rotateYCCW()), input);
        Utils.dropInventoryItems(world, pos.offset(getMasterFacing().rotateY()), outPut);
        super.onMasterBreak();
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        TELathe masterTE = getMaster();
        if (masterTE == null || facing == null) return super.getCapability(capability, facing);
        if (capability.equals(CapabilityEnergy.ENERGY)
                && facing.equals(getMasterFacing())
                && pos.equals(masterTE.getPos().offset(getMasterFacing()).offset(getMasterFacing().rotateYCCW())))
            return LazyOptional.of(() -> masterTE.energyContainer).cast();
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            if (facing.equals(getMasterFacing().rotateYCCW())
                    && pos.equals(masterTE.getPos().offset(getMasterFacing().rotateYCCW())))
                return LazyOptional.of(() -> masterTE.input).cast();
            if (facing.equals(getMasterFacing().rotateY())
                    && pos.equals(masterTE.getPos().offset(getMasterFacing().rotateY())))
                return LazyOptional.of(() -> masterTE.outPut).cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.put("input", this.input.serializeNBT());
        compound.put("output", this.outPut.serializeNBT());
        compound.put("hold", this.hold.serializeNBT());
        compound.put("StoredIR", this.energyContainer.serializeNBT());
        compound.putBoolean("processing", inProcess);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        this.input.deserializeNBT(compound.getCompound("input"));
        this.outPut.deserializeNBT(compound.getCompound("output"));
        this.hold.deserializeNBT(compound.getCompound("hold"));
        this.energyContainer.deserializeNBT(compound.getCompound("StoredIR"));
        inProcess = compound.getBoolean("processing");
        super.read(compound);
    }
}
