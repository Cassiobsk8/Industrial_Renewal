package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x2x2MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityLathe extends BlockEntity3x2x2MachineBase<BlockEntityLathe> implements MenuProvider {
    private static final int energyPTick = 10;//IRConfig.MainConfig.Main.energyPerTickLatheMachine;
    public final int MAX_ENERGY = 10000;
    public boolean inProcess = false;
    public float renderCutterProcess;
    public LazyOptional<CustomEnergyStorage> energyHandler;
    public LazyOptional<ItemStackHandler> inputItemHandler;
    public LazyOptional<ItemStackHandler> outputItemHandler;
    public LazyOptional<ItemStackHandler> holdHandler;
    public int currentEnergy;
    //private static final float volume = 0.2f * IRConfig.MainConfig.Sounds.masterVolumeMult;
    private final CustomEnergyStorage energyContainer;
    private final ItemStackHandler input;
    private final ItemStackHandler output;
    private final ItemStackHandler hold;
    private boolean oldInProcess;
    private int tick;
    private int processTime;
    private float oldProcessTime;
    private boolean stopping = false;
    
    public BlockEntityLathe(BlockPos pos, BlockState state) {
        super(ModBlockEntity.LATHE_TILE.get(), pos, state);
        this.energyContainer = new CustomEnergyStorage(MAX_ENERGY, 256, 256) {
            @Override
            public void onEnergyChange() {
                //super.onEnergyChange();
                currentEnergy = this.getEnergyStored();
                BlockEntityLathe.this.sync();
            }
            
            @Override
            public boolean canExtract() {
                return false;
            }
        };
        
        this.input = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                BlockEntityLathe.this.sync();
            }
            
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };
        
        this.output = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntityLathe.this.sync();
            }
            
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };
        
        this.hold = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntityLathe.this.sync();
            }
            
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };
        
        this.energyHandler = LazyOptional.of(() -> energyContainer);
        this.inputItemHandler = LazyOptional.of(() -> input);
        this.outputItemHandler = LazyOptional.of(() -> output);
        this.holdHandler = LazyOptional.of(() -> hold);
        
    }
    
    
    public void tick() {
        if (level == null) return;
        if (!level.isClientSide) {
            if (isMaster()) {
                //                Utils.debug("TICKING", inProcess);
                
                ItemStack inputStack = input.getStackInSlot(0);
                oldProcessTime = renderCutterProcess;
                if (!inProcess && !inputStack.isEmpty() && valid(output.getStackInSlot(0))) {
                    getProcessFromInputItem();
                } else if (inProcess) {
                    getProcessFromInputItem();
                    process();
                }
                if (inputStack.isEmpty() || !valid(output.getStackInSlot(0))) {
                    interrupt();
                }
                if (!inProcess && !oldInProcess) stopping = true;
                oldInProcess = inProcess;
                renderCutterProcess = processTime > 0 ? Utils.normalizeClamped(tick, 0, processTime) * 0.8f : 0;
                tryOutPutItem();
                sync();
            }
        }
    }
    
    public void interrupt() {
        inProcess = false;
        processTime = 0;
        tick = 0;
        stopping = true;
        hold.setStackInSlot(0, ItemStack.EMPTY);
    }
    
    private boolean valid(ItemStack outputSlot) {
        return false;
        //        if(level == null) return false;
        ////        Utils.debug("OUTPUT", outputSlot);
        //        if(outputSlot.isEmpty())
        //            return true;
        //
        ////        Inventory inv = new Inventory(1);
        //        SimpleContainer inv = new SimpleContainer(1);
        //        inv.setItem(0, input.getStackInSlot(0));
        //
        //        Optional<LatheRecipe> recipe = level.getRecipeManager().getRecipeFor(LatheRecipe.Type.INSTANCE, inv, level);
        //
        //        Recipe iRecipe = recipe.orElse(null);
        //        if(iRecipe == null)
        //            return false;
        //        ItemStack resultItem = iRecipe.getResultItem();
        //        return resultItem.is(outputSlot.getItem()) && outputSlot.isStackable() && outputSlot.getCount() <= outputSlot.getMaxStackSize() - hold.getStackInSlot(0).getCount();
    }
    
    private void process() {
        if (level == null) return;
        if (energyContainer.getEnergyStored() < energyPTick) return;
        energyContainer.extractEnergy(energyPTick, false);
        tick++;
        if (tick >= processTime) {
            tick = 0;
            processTime = 0;
            inProcess = false;
            
            {
                if (!level.isClientSide) {
                    input.extractItem(0, 1, false);
                    //                    ItemStack simulatedStack = hold.extractItem(0, 64, true);
                    //                    ItemStack remainderStack = output.insertItem(0, simulatedStack, true);
                    //                    Utils.debug("extracting from hold", simulatedStack);
                    //                    Utils.debug("remainder stack", remainderStack);
                    
                    output.insertItem(0, hold.extractItem(0, 64, false), false);
                }
            }
        }
    }
    
    private void getProcessFromInputItem() {
        //        if(level == null) return;
        ////        Utils.debug("GET PROCESS");
        ////        Inventory inv = new Inventory(1);
        //        SimpleContainer inv = new SimpleContainer(1);
        //        inv.setItem(0, input.getStackInSlot(0));
        //
        //        Optional<LatheRecipe> recipe = level.getRecipeManager().getRecipeFor(LatheRecipe.Type.INSTANCE, inv, level);
        //
        //        recipe.ifPresent(iRecipe -> {
        ////            Utils.debug("RECIPE PRESENT");
        //            processTime = iRecipe.getProcessTime();
        //            ItemStack resultItem = iRecipe.getResultItem();
        //            hold.setStackInSlot(0, resultItem);
        //            inProcess = true;
        //        });
        //
        //        sync();
    }
    
    private void tryOutPutItem() {
        if (level == null) return;
        if (!level.isClientSide && !output.getStackInSlot(0).isEmpty()) {
            Direction facing = getMasterFacing().getClockWise();
            BlockEntity te = level.getBlockEntity(worldPosition.relative(facing, 2));
            if (te != null) {
                IItemHandler outputCap = te.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).orElse(null);
                if (outputCap != null) {
                    Utils.moveItemsBetweenInventories(output, outputCap);
                }
            }
        }
    }
    
    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityLathe;
    }
    
    @Override
    public void onMasterBreak() {
        Utils.dropInventoryItems(level, worldPosition.relative(getMasterFacing().getCounterClockWise()), input);
        Utils.dropInventoryItems(level, worldPosition.relative(getMasterFacing().getClockWise()), output);
        super.onMasterBreak();
    }
    
    public IItemHandler getInputInv() {
        return input;
    }
    
    public IItemHandler getOutputInv() {
        return output;
    }
    
    public ItemStack getResultItem() {
        return hold.getStackInSlot(0);
    }
    
    public float getNormalizedProcess() {
        return renderCutterProcess;
    }
    
    public float getOldProcess() {
        return oldProcessTime;
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        BlockEntityLathe masterTE = getMaster();
        if (masterTE == null || facing == null) return super.getCapability(capability, facing);
        if (capability.equals(ForgeCapabilities.ENERGY) && facing.equals(getMasterFacing()) && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing()).relative(getMasterFacing().getCounterClockWise())))
            
            return getMaster().energyHandler.cast();
        if (capability.equals(ForgeCapabilities.ITEM_HANDLER)) {
            if (facing.equals(getMasterFacing().getCounterClockWise()) && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing().getCounterClockWise())))
                return getMaster().inputItemHandler.cast();
            if (facing.equals(getMasterFacing().getClockWise()) && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing().getClockWise())))
                return getMaster().outputItemHandler.cast();
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("current", this.currentEnergy);
        compoundTag.putFloat("progress", this.renderCutterProcess);
        compoundTag.putInt("tick", this.tick);
        compoundTag.putInt("processTime", this.processTime);
        compoundTag.putBoolean("processing", this.inProcess);
        
        inputItemHandler.ifPresent(in -> {
            CompoundTag tag = in.serializeNBT();
            compoundTag.put("input", tag);
        });
        
        outputItemHandler.ifPresent(out -> {
            CompoundTag tag = out.serializeNBT();
            compoundTag.put("output", tag);
        });
        
        holdHandler.ifPresent(handler -> {
            CompoundTag tag = handler.serializeNBT();
            compoundTag.put("hold", tag);
        });
        
        compoundTag.putInt("energy", energyContainer.getEnergyStored());
        
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        this.currentEnergy = compoundTag.getInt("current");
        this.renderCutterProcess = compoundTag.getFloat("progress");
        this.tick = compoundTag.getInt("tick");
        this.processTime = compoundTag.getInt("processTime");
        this.inProcess = compoundTag.getBoolean("processing");
        
        inputItemHandler.ifPresent(input -> input.deserializeNBT(compoundTag.getCompound("input")));
        outputItemHandler.ifPresent(output -> output.deserializeNBT(compoundTag.getCompound("output")));
        holdHandler.ifPresent(handler -> handler.deserializeNBT(compoundTag.getCompound("hold")));
        
        energyContainer.setEnergy(compoundTag.getInt("energy"));
        super.load(compoundTag);
    }
    
    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.literal("Lathe");
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return null;//new LatheMenu(id, inv, this);
    }
}
