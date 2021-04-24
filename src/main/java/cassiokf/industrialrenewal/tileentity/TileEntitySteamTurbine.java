package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.handlers.SteamBoiler;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntitySteamTurbine extends TileEntityMultiBlockBase<TileEntitySteamTurbine> implements IDynamicSound
{
    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(100000, 10240, 10240)
    {
        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public boolean canReceive()
        {
            return false;
        }

        @Override
        public void onEnergyChange()
        {
            TileEntitySteamTurbine.this.sync();
        }
    };
    private static final float volume = IRConfig.Sounds.turbineVolume.get() * IRConfig.Sounds.masterVolumeMult.get();

    public CustomFluidTank waterTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean canFill(FluidStack stack)
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamTurbine.this.sync();
        }
    };
    private final FluidStack waterStack = new FluidStack(IRConfig.getWaterFromSteamFluid(), References.BUCKET_VOLUME);

    private int rotation;
    private int oldRotation;
    private static final int maxRotation = 16000;
    private static final int energyPerTick = IRConfig.Main.steamTurbineEnergyPerTick.get();
    private static final int steamPerTick = IRConfig.Main.steamTurbineSteamPerTick.get();
    public final CustomFluidTank steamTank = new CustomFluidTank(IRConfig.Main.steamTurbineSteamPerTick.get())
    {
        @Override
        public boolean canFill(FluidStack resource)
        {
            return fluid != null && fluid.getAmount() > 0 && fluid.getFluid().getRegistryName().equals("steam");
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        protected void onContentsChanged()
        {
            TileEntitySteamTurbine.this.markDirty();
        }
    };
    private float steamReceivedNorm = 0f;

    public TileEntitySteamTurbine(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
    {
        if (this.isMaster())
        {
            if (!this.world.isRemote)
            {
                steamToRotation();

                generateEnergyBasedOnRotation();

                extractEnergy();

                extractWater();

                if (oldRotation != rotation)
                {
                    oldRotation = rotation;
                    this.sync();
                }
            } else
            {
                updateSound();
            }
        }
    }

    public void steamToRotation()
    {
        FluidStack fluidStack = steamTank.drainInternal(steamPerTick, IFluidHandler.FluidAction.EXECUTE);
        if (fluidStack == null || fluidStack.getAmount() <= 0)
        {
            steamReceivedNorm = 0;
            if (rotation >= 4) rotation -= 4;
            return;
        }
        int amount = Math.min(fluidStack.getAmount(), steamPerTick);
        steamReceivedNorm = Utils.normalizeClamped(amount, 0, steamPerTick);
        if ((maxRotation * steamReceivedNorm) > rotation) rotation += (10 * steamReceivedNorm);
        else if (rotation >= 2) rotation -= 2;
        waterStack.setAmount(Math.round(((float) amount / (float) IRConfig.Main.steamBoilerConversionFactor.get()) * 0.98f));
        waterTank.fillInternal(waterStack, IFluidHandler.FluidAction.EXECUTE);
    }

    private void generateEnergyBasedOnRotation()
    {
        if (rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored())
        {
            int energy = getEnergyProduction();
            energyContainer.receiveInternally(energy, false);
            rotation -= 6;
        } else rotation -= 2;
        rotation = MathHelper.clamp(rotation, 0, maxRotation);
    }

    private void extractEnergy()
    {
        Direction facing = getMasterFacing();
        TileEntity eTE = world.getTileEntity(pos.offset(facing.getOpposite()).down().offset(facing.rotateYCCW(), 2));
        if (eTE != null && energyContainer.getEnergyStored() > 0)
        {
            IEnergyStorage upTank = eTE.getCapability(CapabilityEnergy.ENERGY, facing.rotateY()).orElse(null);
            if (upTank != null)
                energyContainer.extractEnergyInternally(upTank.receiveEnergy(energyContainer.extractEnergyInternally(10240, true), false), false);
        }
    }

    private void extractWater()
    {
        Direction facing = getMasterFacing();
        TileEntity wTE = world.getTileEntity(pos.offset(facing, 2).down());
        if (wTE != null && waterTank.getFluidAmount() > 0)
        {
            IFluidHandler wTank = wTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
            if (wTank != null)
                waterTank.tryPassFluid(2000, wTank);
        }
    }

    @Override
    public float getPitch()
    {
        return Math.max(getRotation(), 0.1F);
    }

    @Override
    public float getVolume()
    {
        return volume;
    }

    private void updateSound()
    {
        if (!world.isRemote) return;
        if (this.rotation > 0)
        {
            IRSoundHandler.playRepeatableSound(SoundsRegistration.MOTOR_ROTATION_RESOURCEL, volume, getPitch(), pos);
        } else
        {
            IRSoundHandler.stopTileSound(pos);
        }
    }

    @Override
    public void onMasterBreak()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntitySteamTurbine;
    }

    private int getEnergyProduction()
    {
        int energy = Math.round(energyPerTick * getRotation());
        float factor = this.waterTank.getFluidAmount() == 0 ? 1f : Math.max(0.5f, Math.min(1f, ((float) this.waterTank.getCapacity() / (float) this.waterTank.getFluidAmount()) - 0.5f));
        energy = Math.round(energy * factor);
        energy = MathHelper.clamp(energy, 0, energyPerTick);
        return energy;
    }

    public String getWaterText()
    {
        return FluidRegistry.WATER.getName();
    }

    public String getSteamText()
    {
        return SteamBoiler.steamName;
    }

    public String getGenerationText()
    {
        int energy = (rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored()) ? getEnergyProduction() : 0;
        return Utils.formatEnergyString(energy) + "/t";
    }

    public String getEnergyText()
    {
        int energy = this.energyContainer.getEnergyStored();
        return Utils.formatEnergyString(energy);
    }

    public String getRotationText()
    {
        return (rotation / 10) + " rpm";
    }

    public float getEnergyFill() //0 ~ 1
    {
        return Utils.normalizeClamped(energyContainer.getEnergyStored(), 0, energyContainer.getMaxEnergyStored());
    }

    private float getRotation()
    {
        return Utils.normalizeClamped(this.rotation, 0, maxRotation);
    }

    public float getGenerationFill() //0 ~ 180
    {
        float currentAmount = ((rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored()) ? getEnergyProduction() : 0);
        return Utils.normalizeClamped(currentAmount, 0, energyPerTick) * 90f;
    }

    public float getWaterFill() //0 ~ 180
    {
        return Utils.normalizeClamped(waterTank.getFluidAmount(), 0, waterTank.getCapacity()) * 180f;
    }

    public float getSteamFill() //0 ~ 180
    {
        return steamReceivedNorm * 180f;
    }

    public float getRotationFill() //0 ~ 180
    {
        return getRotation() * 140f;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT waterTag = new CompoundNBT();
        waterTank.writeToNBT(waterTag);
        compound.put("water", waterTag);
        compound.put("StoredIR", this.energyContainer.serializeNBT());
        compound.putInt("heat", rotation);
        compound.putFloat("steamOnTick", steamReceivedNorm);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT waterTag = compound.getCompound("water");
        waterTank.readFromNBT(waterTag);
        energyContainer.deserializeNBT(compound.getCompound("StoredIR"));
        rotation = compound.getInt("heat");
        steamReceivedNorm = compound.getFloat("steamOnTick");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        TileEntitySteamTurbine masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getPos();

        if (facing == Direction.UP && this.pos.equals(masterPos.up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.steamTank).cast();
        if (facing == face && this.pos.equals(masterPos.down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (facing == face.rotateYCCW() && this.pos.equals(masterPos.down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> masterTE.energyContainer).cast();

        return super.getCapability(capability, facing);
    }
}
