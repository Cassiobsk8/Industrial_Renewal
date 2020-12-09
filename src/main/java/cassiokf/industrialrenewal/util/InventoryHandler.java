package cassiokf.industrialrenewal.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class InventoryHandler extends ItemStackHandler
{
    public InventoryHandler(int slotsCount)
    {
        super(slotsCount);
    }

    public void setInvSize(int size)
    {
        NBTTagCompound oldTag = serializeNBT();
        setSize(size);
        NBTTagList tagList = oldTag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        List<ItemStack> toRemove = new ArrayList<>();
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < stacks.size())
            {
                stacks.set(slot, new ItemStack(itemTags));
            }
            else toRemove.add(new ItemStack(itemTags));
        }
        itemsToSpawn(toRemove);
    }

    public void itemsToSpawn(List<ItemStack> list)
    {
    }
}
