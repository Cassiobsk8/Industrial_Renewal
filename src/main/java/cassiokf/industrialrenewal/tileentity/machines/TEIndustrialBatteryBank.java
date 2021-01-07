package cassiokf.industrialrenewal.tileentity.machines;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TEIndustrialBatteryBank extends TEMultiTankBase<TEIndustrialBatteryBank>
{
    public static final int capacity = IRConfig.MainConfig.Main.lithiumBatteryCapacity;
    private static final int maxTransfer = IRConfig.MainConfig.Main.industrialBatteryBankMaxTransfer;
    private static final int maxBatteries = 24;
    private final VoltsEnergyContainer energyContainer = new VoltsEnergyContainer(1, maxTransfer, maxTransfer)
    {
        @Override
        public boolean canReceive()
        {
            return false;
        }

        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public void onEnergyChange()
        {
            TEIndustrialBatteryBank.this.sync();
        }
    };
    private int input;
    private int avrIn;
    private int oldIn;
    private int outPut;
    private int avrOut;
    private int oldOutPut;
    private int batteries = 0;
    private final VoltsEnergyContainer inEnergy = new VoltsEnergyContainer()
    {
        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return TEIndustrialBatteryBank.this.getBottomTE().onEnergyIn(maxReceive, simulate);
        }
    };
    private int tick;

    public int onEnergyIn(int maxReceive, boolean simulate)
    {
        maxReceive = Math.min(maxReceive, maxTransfer);
        int in;
        if (batteries > 0)
            in = energyContainer.receiveInternally(maxReceive, simulate);
        else
        {
            in = outPutEnergy(null, maxReceive, simulate);
            if (!simulate && !world.isRemote) outPut += in;
        }
        if (!simulate && !world.isRemote) input += in;
        return in;
    }

    public boolean placeBattery(EntityPlayer player, ItemStack batteryStack)
    {
        if (batteries >= maxBatteries) return false;
        batteries++;
        if (!world.isRemote)
        {
            if (!player.isCreative()) batteryStack.shrink(1);
            reachTop();
        }
        return true;
    }

    @Override
    public void onTick()
    {
        if (!world.isRemote && isMaster() && isBottom())
        {
            if (batteries > 0 && energyContainer.getEnergyStored() > 0)
            {
                outPut += outPutEnergy(energyContainer, maxTransfer, false);
            }

            if (tick >= 10)
            {
                tick = 0;
                avrIn = input / 10;
                avrOut = outPut / 10;
                input = 0;
                outPut = 0;

                if (avrOut != oldOutPut || avrIn != oldIn)
                {
                    oldOutPut = avrOut;
                    oldIn = avrIn;
                    sync();
                }
            }
            tick++;
        }
    }

    private int outPutEnergy(VoltsEnergyContainer container, int maxReceive, boolean simulate)
    {
        TileEntity te = world.getTileEntity(getOutPutPos());
        int out = 0;
        if (te != null)
        {
            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
            if (energyStorage != null)
            {
                if (container == null) out = energyStorage.receiveEnergy(maxReceive, simulate);
                else
                    out = container.extractEnergyInternally(energyStorage.receiveEnergy(container.extractEnergyInternally(maxReceive, true), simulate), simulate);
            }
        }
        return out;
    }

    @Override
    public void onMasterBreak()
    {
        super.onMasterBreak();
        if (batteries <= 0) return;
        ItemStack stack = new ItemStack(ModItems.battery_lithium, batteries);
        for (int i = 0; i <= batteries; i++)
        {
            Utils.spawnItemStack(world, pos, stack);
        }
    }

    private BlockPos getOutPutPos()
    {
        return getTopTE().pos.up(2).offset(getMasterFacing().rotateY());
    }

    @Override
    public void setSize(int i)
    {
        int realCapacity = 0;
        for (TEIndustrialBatteryBank te : topTE.machines)
        {
            realCapacity += te.getRealCapacity();
        }
        if (realCapacity <= -1) realCapacity = Integer.MAX_VALUE;
        energyContainer.setMaxEnergyStored(MathHelper.clamp(realCapacity, 0, Integer.MAX_VALUE));
        sync();
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TEIndustrialBatteryBank;
    }

    public int getRealCapacity()
    {
        return capacity * batteries;
    }

    public int getBatteries()
    {
        return batteries;
    }

    public String getEnergyText()
    {
        return Utils.formatEnergyString(energyContainer.getEnergyStored()).replace(" FE", "") + " / " + Utils.formatEnergyString(energyContainer.getMaxEnergyStored());
    }

    public float getBatteryFill()
    {
        return Utils.normalizeClamped(energyContainer.getEnergyStored(), 0, energyContainer.getMaxEnergyStored());
    }

    public float getOutPutAngle()
    {
        return Utils.normalizeClamped(avrOut, 0, 10240) * 90;
    }

    public String getOutPutText()
    {
        return Utils.formatPreciseEnergyString(avrOut) + "/t";
    }

    public float getInPutAngle()
    {
        return Utils.normalizeClamped(avrIn, 0, 10240) * 90;
    }

    public String getInPutText()
    {
        return Utils.formatPreciseEnergyString(avrIn) + "/t";
    }

    public String getInPutIndicatorText()
    {
        return TextFormatting.BLACK + I18n.format("tesr.indr.in");
    }

    public String getOutPutIndicatorText()
    {
        return TextFormatting.BLACK + I18n.format("tesr.indr.out");
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.UP)
        {
            EnumFacing face = getMasterFacing();
            if (this.pos.equals(getMaster().pos.up().offset(face.rotateY()))) //out
                return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
            if (this.pos.equals(getMaster().pos.up().offset(face.rotateYCCW()))) //In
                return CapabilityEnergy.ENERGY.cast(getMaster().inEnergy);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        energyContainer.serializeNBT(compound);
        compound.setInteger("out", avrOut);
        compound.setInteger("in", avrIn);
        compound.setInteger("battery", batteries);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        energyContainer.deserializeNBT(compound);
        avrOut = compound.getInteger("out");
        avrIn = compound.getInteger("in");
        batteries = compound.getInteger("battery");
        super.readFromNBT(compound);
    }
}
