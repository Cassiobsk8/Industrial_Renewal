package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockPortableGenerator;
import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.FluidGenerator;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySaveContent;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityPortableGenerator extends TileEntitySaveContent implements ITickableTileEntity
{
    private final FluidGenerator generator = new FluidGenerator(this);
    private Direction blockFacing;
    private boolean soundStarted = false;
    private final float volume = IRConfig.Sounds.genVolume.get() * IRConfig.Sounds.masterVolumeMult.get();

    public TileEntityPortableGenerator(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (!world.isRemote)
        {
            generator.onTick();
            passEnergy();
        } else
        {
            handleSound();
        }
    }

    private void handleSound()
    {
        if (generator.isGenerating() && !soundStarted)
        {
            IRSoundHandler.playRepeatableSound(IRSoundRegister.GENERATOR_RESOURCEL, volume, 1.0F, pos);
            soundStarted = true;
        } else if (!generator.isGenerating() && soundStarted)
        {
            IRSoundHandler.stopTileSound(pos);
            soundStarted = false;
        }
    }

    public boolean isWorking()
    {
        return generator.isGenerating();
    }

    @Override
    public void remove()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
        super.remove();
    }

    public void changeGenerator()
    {
        generator.changeCanGenerate();
    }

    private void passEnergy()
    {
        if (generator.energyStorage.getEnergyStored() >= 0)
        {
            TileEntity te = world.getTileEntity(pos.offset(getBlockFacing()));
            if (te != null)
            {
                IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, getBlockFacing()).orElse(null);
                if (handler != null && handler.canReceive())
                    generator.energyStorage.extractEnergyInternally(handler.receiveEnergy(generator.energyStorage.extractEnergyInternally(128, true), false), false);
            }
        }
    }

    @Override
    public CustomFluidTank getTank()
    {
        return generator.tank;
    }

    public Direction getBlockFacing()
    {
        return getBlockFacing(false);
    }
    public Direction getBlockFacing(boolean force)
    {
        if (!force && blockFacing != null) return blockFacing;
        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof BlockPortableGenerator)) return Direction.NORTH;
        blockFacing = state.get(BlockHorizontalFacing.FACING);
        return blockFacing;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        generator.saveGenerator(compound);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        generator.loadGenerator(compound);
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == getBlockFacing())
        {
            return LazyOptional.of(() -> generator.energyStorage).cast();
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return LazyOptional.of(() -> generator.tank).cast();
        }
        return super.getCapability(capability, facing);
    }

    public float getTankFill()
    {
        return Utils.normalize(getTank().getFluidAmount(), 0, getTank().getCapacity()) * 180f;
    }

    public String getTankText()
    {
        return getTank().getFluidAmount() > 0 ? getTank().getFluid().getDisplayName().getFormattedText() : I18n.format("gui.industrialrenewal.fluid.empty");
    }

    public float getEnergyFill()
    {
        return Utils.normalize(generator.isGenerating() ? FluidGenerator.energyPerTick : 0, 0, 128) * 90;
    }

    public String getEnergyText()
    {
        return Utils.formatEnergyString(generator.isGenerating() ? FluidGenerator.energyPerTick : 0) + "/t";
    }
}
