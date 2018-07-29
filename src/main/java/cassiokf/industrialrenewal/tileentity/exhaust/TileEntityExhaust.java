package cassiokf.industrialrenewal.tileentity.exhaust;

import cassiokf.industrialrenewal.IndustrialRenewal;
import com.google.common.collect.ImmutableMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityExhaust extends TileEntity {

    @Nullable
    private final IAnimationStateMachine asm;
    private final TimeValues.VariableValue cycleLength = new TimeValues.VariableValue(4);
    private final TimeValues.VariableValue clickTime = new TimeValues.VariableValue(Float.NEGATIVE_INFINITY);
    //private final VariableValue offset = new VariableValue(0);

    public TileEntityExhaust() {
        asm = ModelLoaderRegistry.loadASM(new ResourceLocation(IndustrialRenewal.MODID.toLowerCase(), "asms/block/exhaust.json"), ImmutableMap.<String, ITimeValue>of(
                "cycle_length", cycleLength,
                "click_time", clickTime
                //"offset", offset
        ));
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side) {
        return capability == CapabilityAnimation.ANIMATION_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        }
        return super.getCapability(capability, side);
    }
}
