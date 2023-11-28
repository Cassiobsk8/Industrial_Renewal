package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.block.BlockSteamBoiler;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.obj.SteamBoiler;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntitySteamBoiler extends BlockEntity3x3x3MachineBase<BlockEntitySteamBoiler> {
    private int type;
    
    private static final int solidPerTick = 2;
    public final SteamBoiler boiler = new SteamBoiler(this, SteamBoiler.BoilerType.Solid, solidPerTick);
    private static final int fluidPerTick = 1;
    
    public BlockEntitySteamBoiler(BlockPos pos, BlockState state) {
        super(ModBlockEntity.STEAM_BOILER_TILE.get(), pos, state);
    }
    
    public void tick()
    {
        if (this.isMaster() && !level.isClientSide())
        {
            if (this.type > 0)
            {
                boiler.onTick();
            }
            else
            {
                boiler.outPutSteam();
                boiler.coolDown();
            }
        }
    }
    
    @Override
    public boolean instanceOf(BlockEntity tileEntity)
    {
        return tileEntity instanceof BlockEntitySteamBoiler;
    }
    
    public int getIntType()
    {
        if (!isMaster()) return getMaster().type;
        return this.type;
    }
    
    public void setType(int type)
    {
        if (!this.isMaster())
        {
            this.getMaster().setType(type);
            return;
        }
        boiler.dropItemsOnGround(worldPosition);
        this.type = type;
        if (type > 0)
            boiler.setType(type == 1 ? SteamBoiler.BoilerType.Solid : SteamBoiler.BoilerType.Liquid, type == 1 ? solidPerTick : fluidPerTick);
        else boiler.resetFuelTime();
        BlockState state = getBlockState().setValue(BlockSteamBoiler.TYPE, type);
        level.setBlockAndUpdate(worldPosition, state);
        this.sync();
    }
    
    public String getFuelText()
    {
        if (getIntType() == 0) return "No Firebox";
        return boiler.getFuelText();
    }
    
    @Override
    public void onMasterBreak()
    {
        boiler.dropItemsOnGround(worldPosition);
        Utils.spawnItemStack(level, worldPosition, getFireBoxStack());
    }
    
    public ItemStack getFireBoxStack()
    {
        switch (type)
        {
            default:
            case 0:
                return ItemStack.EMPTY;
            case 1:
                return new ItemStack(ModItems.FIREBOX_SOLID.get());
            case 2:
                return new ItemStack(ModItems.FIREBOX_FLUID.get());
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        boiler.saveBoiler(compoundTag);
        compoundTag.putInt("type", this.type);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        boiler.loadBoiler(compoundTag);
        this.type = compoundTag.getInt("type");
        super.load(compoundTag);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        BlockEntitySteamBoiler masterTE = getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();
        SteamBoiler b = masterTE.boiler;
        
        if (facing == null) return super.getCapability(capability, facing);
        
        if (facing == Direction.UP && worldPosition.equals(masterTE.getBlockPos().above()) && capability == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(b::getSteamTank).cast();
        if (facing == face && worldPosition.equals(masterTE.getBlockPos().below().relative(face)) && capability == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(b::getWaterTank).cast();
        if (masterTE.getIntType() == 1 && facing == face.getCounterClockWise() && worldPosition.equals(masterTE.getBlockPos().below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == ForgeCapabilities.ITEM_HANDLER)
            return b.getSolidFuelInvHandler().cast();
        if (masterTE.getIntType() == 2 && facing == face.getCounterClockWise() && worldPosition.equals(masterTE.getBlockPos().below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(b::getFuelTank).cast();
        return super.getCapability(capability, facing);
    }
    
    public String getSteamText() {
        return "Steam";
    }
    
    public SteamBoiler getBoiler() {
        return boiler;
    }
    
    public ItemStack getDrop(){
        return switch (type) {
            case 1 -> new ItemStack(ModItems.FIREBOX_SOLID.get(), 1);
            case 2 -> new ItemStack(ModItems.FIREBOX_FLUID.get(), 1);
            default -> null;
        };
    }
}
