package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockFirstAidKit;
import cassiokf.industrialrenewal.init.TileEntityRegister;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileEntityFirstAidKit extends TileEntitySyncable implements ICapabilityProvider
{

    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

    public TileEntityFirstAidKit()
    {
        super(TileEntityRegister.FIRST_AID_KIT);
    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(8)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityFirstAidKit.this.Sync();
            }
        };
    }

    public Direction getFaceDirection()
    {

        return BlockFirstAidKit.getFaceDirection(getBlockState());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        inventory.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.write(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? inventory.cast() : super.getCapability(capability, facing);
    }
}
