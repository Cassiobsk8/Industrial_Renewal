package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlockEntityBarrel extends BlockEntitySyncable {
    private final int MAX_CAPACITY = 64000;

    public final CustomFluidTank tank = new CustomFluidTank(MAX_CAPACITY).setBlockEntity(this);
    protected LazyOptional<CustomFluidTank> handler = LazyOptional.of(()->this.tank);

    public BlockEntityBarrel(BlockPos pos, BlockState state){
        super(ModBlockEntity.BARREL_TILE.get(), pos, state);
    }

    public String getFluid(){
        return this.tank.getFluid().getTranslationKey();
    }

    public int getFluidAmount(){
        return this.tank.getFluidAmount();
    }

    public int getMAX_CAPACITY(){
        return MAX_CAPACITY;
    }

    public String getChatQuantity()
    {
        if (this.tank.getFluidAmount() > 0)
            return String.format("%s: %d/%d mB", I18n.get(this.tank.getFluid().getTranslationKey()), this.tank.getFluidAmount(), MAX_CAPACITY);
        return "Empty";
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
//        setChanged();
//        handler.invalidate();
    }

    @Override
    public void saveAdditional (CompoundTag compoundNBT) {
        CompoundTag nbt = new CompoundTag();
        tank.writeToNBT(nbt);
        compoundNBT.put("fluid", nbt);
        super.saveAdditional(compoundNBT);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        CompoundTag nbt = compoundNBT.getCompound("fluid");
        tank.readFromNBT(nbt);

        super.load(compoundNBT);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == ForgeCapabilities.FLUID_HANDLER){
            return this.handler.cast();
        }
        return super.getCapability(cap);
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER){
            return this.handler.cast();
        }
        return super.getCapability(cap, side);
    }
}
