package cassiokf.industrialrenewal.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class CustomItemStackHandler extends ItemStackHandler
{
    public CustomItemStackHandler(int size)
    {
        super(size);
    }

    public void setInvSize(int size) {
        CompoundNBT oldTag = serializeNBT();
        setSize(size);
        ListNBT tagList = oldTag.getList("Items", Constants.NBT.TAG_COMPOUND);
        List<ItemStack> toRemove = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                stacks.set(slot, ItemStack.read(itemTags));
            } else toRemove.add(ItemStack.read(itemTags));
        }
        itemsToSpawn(toRemove);
    }

    public void itemsToSpawn(List<ItemStack> list) {
    }
}
