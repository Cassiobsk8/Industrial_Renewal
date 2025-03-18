package net.cassiokf.industrialrenewal.util.capability;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class CustomItemStackHandler extends ItemStackHandler {
    private BlockEntity blockEntity;
    
    public CustomItemStackHandler(int size) {
        super(size);
    }
    
    public CustomItemStackHandler setBlockEntity(BlockEntity entity) {
        this.blockEntity = entity;
        return this;
    }
    
    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (blockEntity != null) blockEntity.setChanged();
    }
}
