package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEntitySolarPanel extends BlockEntity {

    private boolean DECORATIVE = false;
    private int tick = 0;
    private int energyCanGenerate;

    private final IEnergyStorage energyStorage = new EnergyStorage(0);
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(()->energyStorage);
    public BlockEntitySolarPanel(BlockPos pos, BlockState state){
        super(ModBlockEntity.SOLAR_PANEL.get(), pos, state);
    }

    public void tick(){
        if(level == null) return;
        if(level.isClientSide || DECORATIVE){
            return;
        }

        if(tick >= 20){
            tick = 0;
            getEnergyFromSun();
        }
        tick++;
        moveEnergyOut(energyCanGenerate, false);
    }

    private void moveEnergyOut(int energy, boolean simulate)
    {
        if(level == null) return;
        for(Direction direction : Direction.values()){
            BlockEntity te = level.getBlockEntity(worldPosition.relative(direction));
            if(te == null)
                continue;

            te.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(iEnergyStorage -> {
                if(iEnergyStorage.canReceive() && iEnergyStorage.getEnergyStored() < iEnergyStorage.getMaxEnergyStored()){
                    iEnergyStorage.receiveEnergy(energy, simulate);
                }
            });
        }
    }

    public static int getGeneration(Level world, BlockPos pos)
    {
        int i = world.getBrightness(LightLayer.SKY, pos);
        float f = world.getSunAngle(1.0F);
        if (i > 0)
        {
            float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
            f = f + (f1 - f) * 0.2F;
            i = (int)(Math.round((float) i * Math.cos(f)));
        }
        i = Mth.clamp(i, 0, 15);
        float normalize = i / 15f;
        if (world.isRaining()) normalize = normalize / 2;
        return Math.round(normalize * 15);
        //return 15;
    }

    public void getEnergyFromSun()
    {
        if(level == null) return;
        energyCanGenerate = getGeneration(this.level, this.worldPosition);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyHandler.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (side == null)
            return super.getCapability(cap, side);
        return cap == ForgeCapabilities.ENERGY && side == Direction.DOWN ? this.energyHandler.cast() : super.getCapability(cap, side);
    }
}
