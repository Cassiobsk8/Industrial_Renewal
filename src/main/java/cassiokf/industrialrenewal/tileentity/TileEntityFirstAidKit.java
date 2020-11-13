package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockFirstAidKit;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityFirstAidKit extends TileEntitySync
{
    private EnumFacing facing;
    public ItemStackHandler inventory;

    public TileEntityFirstAidKit()
    {
        this.inventory = new ItemStackHandler(8)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityFirstAidKit.this.sync();
            }
        };
    }

    public EnumFacing getFaceDirection()
    {
        if (facing != null) return facing;
        return facing = BlockFirstAidKit.getFaceDirection(world.getBlockState(pos));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", this.inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory)
                : super.getCapability(capability, facing);
    }
}
