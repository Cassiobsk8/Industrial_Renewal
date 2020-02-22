package cassiokf.industrialrenewal.fluids;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.init.FluidsRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

import java.awt.*;

public abstract class FluidSteam extends FlowingFluid
{
    @Override
    public Fluid getFlowingFluid()
    {
        return FluidsRegistration.STEAM_FLOWING.get();
    }

    @Override
    public Fluid getStillFluid()
    {
        return FluidsRegistration.STEAM.get();
    }

    @Override
    protected boolean canSourcesMultiply()
    {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state)
    {

    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn)
    {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn)
    {
        return 2;
    }

    @Override
    public Item getFilledBucket()
    {
        return FluidsRegistration.STEAM_BUCKET.get();
    }

    @Override
    protected boolean canDisplace(IFluidState state, IBlockReader world, BlockPos pos, Fluid fluid, Direction direction)
    {
        return false;
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_)
    {
        return 5;
    }

    @Override
    protected float getExplosionResistance()
    {
        return 100F;
    }

    @Override
    protected BlockState getBlockState(IFluidState state)
    {
        return FluidsRegistration.STEAM_BLOCK.get().getDefaultState().with(BlockSteam.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn)
    {
        return fluidIn == FluidsRegistration.STEAM.get() || fluidIn == FluidsRegistration.STEAM_FLOWING.get();
    }

    @Override
    protected FluidAttributes createAttributes()
    {
        return FluidAttributes.builder(
                new ResourceLocation(References.MODID, "blocks/steam_still"),
                new ResourceLocation(References.MODID, "blocks/steam_flow"))
                .translationKey("block.industrialrenewal.steam").gaseous()
                .density(-1000).temperature(380).viscosity(500).color(Color.WHITE.getRGB())
                .build(this);
    }

    public static class Flowing extends FluidSteam
    {

        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder)
        {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public boolean isSource(IFluidState state)
        {
            return false;
        }

        @Override
        public int getLevel(IFluidState state)
        {
            return state.get(FluidSteam.LEVEL_1_8);
        }

    }

    public static class Source extends FluidSteam
    {

        @Override
        public boolean isSource(IFluidState state)
        {
            return true;
        }

        @Override
        public int getLevel(IFluidState fluidState)
        {
            return 8;
        }

    }
}
