package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockPortableGenerator;
import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.FluidGenerator;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySaveContent;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityPortableGenerator extends TileEntitySaveContent implements ITickable
{
    private final FluidGenerator generator = new FluidGenerator(this);
    private EnumFacing blockFacing;
    private boolean soundStarted = false;
    private float volume = IRConfig.MainConfig.Sounds.genVolume * IRConfig.MainConfig.Sounds.masterVolumeMult;

    @Override
    public void update()
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

    @Override
    public void invalidate()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
        super.invalidate();
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
                IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, getBlockFacing());
                if (handler != null && handler.canReceive())
                    generator.energyStorage.extractEnergyInternally(handler.receiveEnergy(generator.energyStorage.extractEnergyInternally(128, true), false), false);
            }
        }
    }

    @Override
    public FluidTank getTank()
    {
        return generator.tank;
    }

    public EnumFacing getBlockFacing()
    {
        if (blockFacing != null) return blockFacing;
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockPortableGenerator)) return EnumFacing.NORTH;
        blockFacing = world.getBlockState(pos).getValue(BlockHorizontalFacing.FACING);
        return blockFacing;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        generator.saveGenerator(compound);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        generator.loadGenerator(compound);
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && facing == getBlockFacing())
                || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == getBlockFacing())
        {
            return CapabilityEnergy.ENERGY.cast(generator.energyStorage);
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(generator.tank);
        }
        return super.getCapability(capability, facing);
    }

    public float getTankFill()
    {
        return Utils.normalize(getTank().getFluidAmount(), 0, getTank().getCapacity()) * 180f;
    }

    public String getTankText()
    {
        return getTank().getFluidAmount() > 0 ? getTank().getFluid().getLocalizedName() : I18n.format("gui.industrialrenewal.fluid.empty");
    }

    public float getEnergyFill()
    {
        return Utils.normalize(generator.isGenerating() ? generator.energyPerTick : 0, 0, 128) * 90;
    }

    public String getEnergyText()
    {
        return Utils.formatEnergyString(generator.isGenerating() ? generator.energyPerTick : 0) + "/t";
    }
}
