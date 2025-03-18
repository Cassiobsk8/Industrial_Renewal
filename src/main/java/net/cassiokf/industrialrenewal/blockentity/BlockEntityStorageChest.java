package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x2MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityStorageChest extends BlockEntity3x3x2MachineBase<BlockEntityStorageChest> implements MenuProvider {
    
    public int maxPage = 4;
    //public int currentPage = 0;
    public int slotsPerPage = 66;
    public String search = "";
    public boolean searchActive = true;
    public int maxNumSlots = slotsPerPage * maxPage;
    //public IIntArray data;
    
    //public final CustomItemStackHandler[] inventory = createItemStackHandler(maxPage, slotsPerPage);
    //private static final int slots = 99;
    public final CustomItemStackHandler inventory = new CustomItemStackHandler(maxNumSlots) {
        @Override
        protected void onContentsChanged(int slot) {
            BlockEntityStorageChest.this.sync();
            //super.onContentsChanged(slot);
        }
    };
    
    public LazyOptional<CustomItemStackHandler> inventoryHandler = LazyOptional.of(() -> inventory);
    
    
    public BlockEntityStorageChest(BlockPos pos, BlockState state) {
        super(ModBlockEntity.STORAGE_CHEST_TILE.get(), pos, state);
    }
    
    //    private INamedContainerProvider createContainerProvider(Level world, BlockPos pos) {
    //        return  new INamedContainerProvider() {
    //            @Override
    //            public ITextComponent getDisplayName() {
    //                return new TranslationTextComponent("");
    //            }
    //
    //            @Nullable
    //            @Override
    //            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    //                TileEntity te = world.getBlockEntity(pos);
    //                TileEntityStorageChest teMaster = te instanceof TileEntityStorageChest? ((TileEntityStorageChest) te).getMaster() : null;
    //                return new StorageChestContainer(i, playerInventory, teMaster);
    //            }
    //        };
    //    }
    
    @Override
    public void onMasterBreak() {
        super.onMasterBreak();
        dropResources();
    }
    
    public void dropResources() {
        Utils.dropInventoryItems(level, worldPosition, inventory);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockEntityStorageChest masterTE = getMaster();
        if (masterTE == null) return super.getCapability(cap, side);
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return masterTE.inventoryHandler.cast();
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityStorageChest;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.put("inv", inventory.serializeNBT());
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        inventory.deserializeNBT(compoundTag.getCompound("inv"));
        super.load(compoundTag);
    }
    
    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.literal("Storage Rack");
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return null;//new StorageChestMenu(id, inv, this);
    }
}
