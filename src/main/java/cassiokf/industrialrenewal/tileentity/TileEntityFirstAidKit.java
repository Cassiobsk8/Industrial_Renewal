package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockFirstAidKit;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityFirstAidKit extends TileEntitySync
{
    private Direction facing;
    public final CustomItemStackHandler inventory = new CustomItemStackHandler(8)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityFirstAidKit.this.sync();
        }
    };

    public TileEntityFirstAidKit(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public Direction getFaceDirection()
    {
        if (facing != null) return facing;
        return facing = BlockFirstAidKit.getFaceDirection(getBlockState());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inventory", inventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        inventory.deserializeNBT(compound.getCompound("inventory"));
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                ? LazyOptional.of(() -> inventory).cast()
                : super.getCapability(capability, facing);
    }
}
