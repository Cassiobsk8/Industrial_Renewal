package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.handlers.SteamBoiler;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntitySteamTurbine extends TileEntityMultiBlockBase<TileEntitySteamTurbine> implements IDynamicSound
{
    private final VoltsEnergyContainer energyContainer;
    private float volume = IRConfig.MainConfig.Sounds.TurbineVolume * IRConfig.MainConfig.Sounds.masterVolumeMult;

    public FluidTank waterTank = new FluidTank(32000)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamTurbine.this.Sync();
        }
    };
    private final FluidStack waterStack = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);

    private int maxRotation = 16000;
    private int rotation;
    private int energyPerTick = IRConfig.MainConfig.Main.steamTurbineEnergyPerTick;
    private int oldRotation;
    private int steamPerTick = IRConfig.MainConfig.Main.steamTurbineSteamPerTick;
    public FluidTank steamTank = new FluidTank(IRConfig.MainConfig.Main.steamTurbineSteamPerTick)
    {
        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            return fluid != null && fluid.amount > 0 && fluid.getFluid().getName().equals("steam");
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

    public TileEntitySteamTurbine()
    {
        this.energyContainer = new VoltsEnergyContainer(100000, 0, 10240)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySteamTurbine.this.Sync();
            }
        };
    }

    @Override
    public void tick()
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
                    this.Sync();
                }
            } else
            {
                updateSound();
            }
        }
    }

    public void steamToRotation()
    {
        FluidStack fluidStack = steamTank.drainInternal(steamPerTick, true);
        if (fluidStack == null || fluidStack.amount <= 0)
        {
            steamReceivedNorm = 0;
            if (rotation >= 4) rotation -= 4;
            return;
        }
        int amount = Math.min(fluidStack.amount, steamPerTick);
        steamReceivedNorm = Utils.normalize(amount, 0, steamPerTick);
        if ((maxRotation * steamReceivedNorm) > rotation) rotation += (10 * steamReceivedNorm);
        else if (rotation >= 2) rotation -= 2;
        waterStack.amount = Math.round(((float) amount / (float) IRConfig.MainConfig.Main.steamBoilerConversionFactor) * 0.98f);
        waterTank.fillInternal(waterStack, true);
    }

    private void generateEnergyBasedOnRotation()
    {
        if (rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored())
        {
            int energy = getEnergyProduction();
            this.energyContainer.receiveInternally(energy, false);
            rotation -= 6;
        } else rotation -= 2;
        rotation = MathHelper.clamp(rotation, 0, maxRotation);
    }

    private void extractEnergy()
    {
        EnumFacing facing = getMasterFacing();
        TileEntity eTE = world.getTileEntity(pos.offset(facing.getOpposite()).down().offset(facing.rotateYCCW(), 2));
        if (eTE != null && energyContainer.getEnergyStored() > 0 && eTE.hasCapability(CapabilityEnergy.ENERGY, facing.rotateY()))
        {
            IEnergyStorage upTank = eTE.getCapability(CapabilityEnergy.ENERGY, facing.rotateY());
            if (upTank != null)
                energyContainer.extractEnergy(upTank.receiveEnergy(energyContainer.extractEnergy(10240, true), false), false);
        }
    }

    private void extractWater()
    {
        EnumFacing facing = getMasterFacing();
        TileEntity wTE = world.getTileEntity(pos.offset(facing, 2).down());
        if (wTE != null && waterTank.getFluidAmount() > 0 && wTE.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
        {
            IFluidHandler wTank = wTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
            if (wTank != null) waterTank.drain(wTank.fill(waterTank.drain(2000, false), true), true);
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
            IRSoundHandler.playRepeatableSound(IRSoundRegister.MOTOR_ROTATION_RESOURCEL, volume, getPitch(), pos);
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
        return Utils.normalize(energyContainer.getEnergyStored(), 0, energyContainer.getMaxEnergyStored());
    }

    private float getRotation()
    {
        return Utils.normalize(this.rotation, 0, this.maxRotation);
    }

    public float getGenerationFill() //0 ~ 180
    {
        float currentAmount = ((rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored()) ? getEnergyProduction() : 0);
        return Utils.normalize(currentAmount, 0, energyPerTick) * 90f;
    }

    public float getWaterFill() //0 ~ 180
    {
        return Utils.normalize(waterTank.getFluidAmount(), 0, waterTank.getCapacity()) * 180f;
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
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = new NBTTagCompound();
        this.waterTank.writeToNBT(waterTag);
        compound.setTag("water", waterTag);
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        compound.setInteger("heat", rotation);
        compound.setFloat("steamOnTick", steamReceivedNorm);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = compound.getCompoundTag("water");
        this.waterTank.readFromNBT(waterTag);
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        this.rotation = compound.getInteger("heat");
        this.steamReceivedNorm = compound.getFloat("steamOnTick");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        TileEntitySteamTurbine masterTE = this.getMaster();
        if (masterTE == null) return false;
        EnumFacing face = masterTE.getMasterFacing();
        BlockPos masterPos = masterTE.getPos();

        return (facing == EnumFacing.UP && pos.equals(masterPos.up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == face && pos.equals(masterPos.down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == face.rotateYCCW() && pos.equals(masterPos.down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityEnergy.ENERGY);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        TileEntitySteamTurbine masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        EnumFacing face = getMasterFacing();
        BlockPos masterPos = masterTE.getPos();

        if (facing == EnumFacing.UP && this.pos.equals(masterPos.up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.steamTank);
        if (facing == face && this.pos.equals(masterPos.down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.waterTank);
        if (facing == face.rotateYCCW() && this.pos.equals(masterPos.down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(masterTE.energyContainer);

        return super.getCapability(capability, facing);
    }
}
