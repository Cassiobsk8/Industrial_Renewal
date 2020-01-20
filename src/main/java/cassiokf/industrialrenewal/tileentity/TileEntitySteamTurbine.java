package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.FluidInit;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntitySteamTurbine extends TileEntity3x3MachineBase<TileEntitySteamTurbine> implements ITickable, IDynamicSound
{
    private final VoltsEnergyContainer energyContainer;
    private float volume = IRConfig.MainConfig.Sounds.TurbineVolume;

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
    public FluidTank steamTank = new FluidTank(320000)
    {
        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            return fluid != null && fluid.getFluid().getName().equals("steam");
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamTurbine.this.Sync();
        }
    };

    private int maxRotation = 16000;
    private int rotation;
    private int energyPerTick = IRConfig.MainConfig.Main.steamTurbineEnergyPerTick;
    private int oldRotation;
    private int steamPerTick = IRConfig.MainConfig.Main.steamTurbineSteamPerTick;

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
    public void update()
    {
        if (this.isMaster())
        {
            if (!this.world.isRemote)
            {
                if (this.steamTank.getFluidAmount() > 0)
                {
                    FluidStack stack = this.steamTank.drainInternal(steamPerTick, true);
                    float amount = stack != null ? stack.amount : 0f;
                    FluidStack waterStack = new FluidStack(FluidRegistry.WATER, Math.round(amount / (float) IRConfig.MainConfig.Main.steamBoilerConversionFactor));
                    this.waterTank.fillInternal(waterStack, true);
                    float factor = amount / (float) steamPerTick;
                    rotation += (10 * factor);
                } else rotation -= 4;

                if (rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored())
                {
                    int energy = Math.min(this.energyContainer.getMaxEnergyStored(), this.energyContainer.getEnergyStored() + getEnergyProduction());
                    this.energyContainer.setEnergyStored(energy);
                    rotation -= 4;
                }

                rotation -= 2;
                rotation = MathHelper.clamp(rotation, 0, maxRotation);


                EnumFacing facing = getMasterFacing();
                TileEntity eTE = this.world.getTileEntity(pos.offset(facing.getOpposite()).down().offset(facing.rotateYCCW(), 2));
                if (eTE != null && this.energyContainer.getEnergyStored() > 0 && eTE.hasCapability(CapabilityEnergy.ENERGY, facing.rotateY()))
                {
                    IEnergyStorage upTank = eTE.getCapability(CapabilityEnergy.ENERGY, facing.rotateY());
                    this.energyContainer.extractEnergy(upTank.receiveEnergy(this.energyContainer.extractEnergy(10240, true), false), false);
                }
                TileEntity wTE = this.world.getTileEntity(pos.offset(facing, 2).down());
                if (wTE != null && this.waterTank.getFluidAmount() > 0 && wTE.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
                {
                    IFluidHandler wTank = wTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                    this.waterTank.drain(wTank.fill(this.waterTank.drain(2000, false), true), true);
                }

                if (oldRotation != rotation)
                {
                    this.Sync();
                    oldRotation = rotation;
                }
            } else
            {
                updateSound(getPitch());
            }
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

    private void updateSound(float pitch)
    {
        if (!world.isRemote) return;
        if (this.rotation > 0)
        {
            IRSoundHandler.playRepeatableSound(IRSoundRegister.MOTOR_ROTATION_RESOURCEL, volume, pitch, pos);
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
        return FluidInit.STEAM.getName();
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
        return rotation / 10 + " rpm";
    }

    public float getEnergyFill() //0 ~ 1
    {
        float currentAmount = this.energyContainer.getEnergyStored() / 1000F;
        float totalCapacity = this.energyContainer.getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount;
    }

    private float getRotation()
    {
        return Utils.normalize(this.rotation, 0, this.maxRotation);
    }

    public float getGenerationFill() //0 ~ 180
    {
        float currentAmount = ((rotation >= 6000 && this.energyContainer.getEnergyStored() < this.energyContainer.getMaxEnergyStored()) ? getEnergyProduction() : 0) / 100f;
        float totalCapacity = energyPerTick / 100f;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 90f;
    }

    public float getWaterFill() //0 ~ 180
    {
        float currentAmount = this.waterTank.getFluidAmount() / 1000f;
        float totalCapacity = this.waterTank.getCapacity() / 1000f;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getSteamFill() //0 ~ 180
    {
        float currentAmount = this.steamTank.getFluidAmount() / 1000f;
        float totalCapacity = this.steamTank.getCapacity() / 1000f;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getRotationFill() //0 ~ 180
    {
        return getRotation() * 140f;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = new NBTTagCompound();
        NBTTagCompound steamTag = new NBTTagCompound();
        this.waterTank.writeToNBT(waterTag);
        this.steamTank.writeToNBT(steamTag);
        compound.setTag("water", waterTag);
        compound.setTag("steam", steamTag);
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        compound.setInteger("heat", rotation);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = compound.getCompoundTag("water");
        NBTTagCompound steamTag = compound.getCompoundTag("steam");
        this.waterTank.readFromNBT(waterTag);
        this.steamTank.readFromNBT(steamTag);
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        this.rotation = compound.getInteger("heat");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        TileEntitySteamTurbine masterTE = this.getMaster();
        if (masterTE == null) return false;
        EnumFacing face = masterTE.getMasterFacing();
        BlockPos masterPos = masterTE.getPos();

        return (facing == EnumFacing.UP && this.pos.equals(masterPos.up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == face && this.pos.equals(masterPos.down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == face.rotateYCCW() && this.pos.equals(masterPos.down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityEnergy.ENERGY);
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
