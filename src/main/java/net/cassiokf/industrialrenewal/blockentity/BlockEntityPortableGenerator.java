package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.blockentity.abstracts.FluidGenerator;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockEntityPortableGenerator extends BlockEntitySyncable {

    private final FluidGenerator generator = new FluidGenerator(this);
    private boolean soundStarted = false;
    private final float volume = 0.5f;
    private BlockState state;

    public BlockEntityPortableGenerator(BlockPos pos, BlockState state) {
        super(ModBlockEntity.PORTABLE_GENERATOR_TILE.get(), pos, state);
        this.state = state;
    }

    public FluidTank getTank() {
        return generator.tank;
    }

    public void tick() {
        if(level == null) return;
        if (!level.isClientSide())
        {
            generator.onTick();
            passEnergy();
        }
        else
        {
            handleSound();
        }
    }

    private void passEnergy()
    {
        if(level == null) return;
        if (generator.energyStorage.getEnergyStored() >= 0)
        {
            BlockEntity te = level.getBlockEntity(worldPosition.relative(getBlockFacing()));
            if (te != null)
            {
                te.getCapability(ForgeCapabilities.ENERGY, getBlockFacing()).ifPresent(cap->{
                    if(cap.canReceive()){
                        int amount = cap.receiveEnergy(generator.energyStorage.extractEnergy(128, true), true);
                        generator.energyStorage.extractEnergy(cap.receiveEnergy(amount, false), false);
                    }
                });
            }
        }
    }
    private void handleSound()
    {

    }

    public boolean isGenerating(){
        return generator.isGenerating();
    }

    public String getTankContent(){
        return generator.getTankContent();
    }

    public int getTankAmount(){
        return generator.getTankAmount();
    }

    public int getGenerateAmount(){
        return generator.getGenerateAmount();
    }

//    public String getChatQuantity(){
//        String message = "";
//        if(!generator.isGenerating()){
//            message += "NOT RUNNING: Out of fuel or no signal.\n";
//        }
//
//        message += String.format("%s: %d mB, %d FE/t", I18n.get(generator.getTankContent()), generator.getTankAmount(), generator.getGenerateAmount());
//        return message;
//    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void invalidateCaps() {
        generator.invalidate();
    }

    public void changeGenerator()
    {
        generator.changeCanGenerate();
    }

    public boolean isWorking()
    {
        return generator.isGenerating();
    }

    public Direction getBlockFacing()
    {
//        return level.getBlockState(worldPosition).getValue(HorizontalDirectionalBlock.FACING);
        return state.getValue(HorizontalDirectionalBlock.FACING);
    }

    public float getTankFill()
    {
        return Utils.normalizeClamped(getTank().getFluidAmount(), 0, getTank().getCapacity()) * 180f;
    }

    public String getTankText()
    {
        //return getTank().getFluidAmount() > 0 ? getTank().getFluid().getTranslationKey() : "Empty";//I18n.get(getTank().getFluid().getTranslationKey()) : "Empty";
        return getTank().getFluidAmount() > 0 ? I18n.get(getTank().getFluid().getTranslationKey()) : "Empty";
    }

    public float getEnergyFill()
    {
        return Utils.normalizeClamped(generator.isGenerating() ? FluidGenerator.energyPerTick : 0, 0, 128) * 90;
    }

    public String getEnergyText()
    {
        return Utils.formatEnergyString(generator.isGenerating() ? FluidGenerator.energyPerTick : 0) + "/t";
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, @Nullable final Direction facing)
    {
        if (facing == null)
            return super.getCapability(capability, facing);

        if (capability == ForgeCapabilities.ENERGY && facing == getBlockFacing())
        {
            return generator.energyHandler.cast();
        }
        if (capability == ForgeCapabilities.FLUID_HANDLER)
        {
            return generator.tankHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    public void setCanGenerate(boolean value) {
        generator.setCanGenerate(value);
    }


    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        generator.saveGenerator(compoundTag);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        generator.loadGenerator(compoundTag);
        super.load(compoundTag);
    }
}
