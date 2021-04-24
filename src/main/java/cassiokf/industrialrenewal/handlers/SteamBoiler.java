package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.FluidsRegistration;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.ISync;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class SteamBoiler
{
    private final ISync tiedTE;
    private final int maxHeat = 32000;
    public static final String steamName = "Steam";//I18n.format(FluidInit.STEAM.getUnlocalizedName());
    public final CustomFluidTank waterTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean canFill(FluidStack fluid)
        {
            return fluid != null && IRConfig.waterTypesContains(fluid.getFluid().getRegistryName());
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            SteamBoiler.this.tiedTE.sync();
        }
    };
    public final CustomFluidTank steamTank = new CustomFluidTank(320000)
    {
        @Override
        public boolean canFill(FluidStack stack)
        {
            return false;
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            SteamBoiler.this.tiedTE.sync();
        }
    };
    private boolean useSolid;
    private int amountPerTick;
    private int heat;
    private int oldHeat;
    private int waterPtick = IRConfig.Main.steamBoilerWaterPerTick.get();
    private int fuelTime;
    private String fuelName = "";
    private int maxFuelTime;
    private int steamGenerated;
    private final FluidStack steamStack = new FluidStack(FluidsRegistration.STEAM.get(), References.BUCKET_VOLUME);
    public final CustomFluidTank fuelTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean canFill(FluidStack fluid)
        {
            return fluid != null && IRConfig.Main.fluidFuel.get().containsKey(fluid.getFluid().getRegistryName().getPath());
        }

        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return SteamBoiler.this.updateLiquidFuel(resource, action);
        }
    };
    public final CustomItemStackHandler solidFuelInv = new CustomItemStackHandler(1)
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            if (stack.isEmpty()) return false;
            return FurnaceTileEntity.isFuel(stack);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            return SteamBoiler.this.updateSolidFuel(stack, simulate);
        }
    };
    private int oldFuelTime;

    public SteamBoiler(ISync tiedTE, BoilerType useSolid, int amountPerTick)
    {
        this.tiedTE = tiedTE;
        this.useSolid = useSolid == BoilerType.Solid;
        this.amountPerTick = amountPerTick;
    }

    public SteamBoiler setWaterTankCapacity(int amount)
    {
        waterTank.setCapacity(amount);
        return this;
    }

    public void onTick()
    {
        if (tiedTE.getThisWorld().isRemote) return;

        updateHeat();

        generateSteam();

        heat = MathHelper.clamp(heat, 2420, maxHeat);
        fuelTime = Math.max(0, fuelTime);

        outPutSteam();

        //IF no heat turn steam to water
        if (this.steamTank.getFluidAmount() > 0 && heat < 9000)
        {
            steamTank.drainInternal(10, IFluidHandler.FluidAction.EXECUTE);
        }

        if (oldHeat != heat || fuelTime != oldFuelTime)
        {
            oldHeat = heat;
            oldFuelTime = fuelTime;
            tiedTE.sync();
        }
    }

    private void generateSteam()
    {
        if (heat >= 10000 && waterTank.getFluidAmount() >= waterPtick && steamTank.getFluidAmount() < steamTank.getCapacity())
        {
            float factor = Utils.normalizeClamped(heat, 10000, maxHeat);
            int amount = Math.round(waterPtick * factor);
            waterTank.drainInternal(amount, IFluidHandler.FluidAction.EXECUTE);
            steamStack.setAmount(amount * IRConfig.Main.steamBoilerConversionFactor.get());
            steamGenerated = steamTank.fillInternal(steamStack, IFluidHandler.FluidAction.EXECUTE);
            heat -= 4;
        } else
        {
            steamGenerated = 0;
            heat -= 2;
        }
    }

    public void outPutSteam()
    {
        if (tiedTE.getThisWorld().isRemote || steamTank.getFluidAmount() <= 0) return;
        BlockPos pos = tiedTE.getThisPosition().up(2); //TODO this needs to change
        TileEntity tileEntity = tiedTE.getThisWorld().getTileEntity(pos);
        if (tileEntity != null)
        {
            IFluidHandler upTank = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
            if (upTank != null)
                steamTank.drainInternal(upTank.fill(steamTank.drainInternal(10000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        }

    }

    public void setType(BoilerType type, int amountPerTick)
    {
        switch (type)
        {
            case Solid:
                useSolid = true;
                break;
            case Liquid:
                useSolid = false;
                break;
        }
        this.amountPerTick = amountPerTick;
        resetFuelTime();
    }

    public int getFuelTime()
    {
        return fuelTime;
    }

    public int getMaxFuelTime()
    {
        return maxFuelTime;
    }

    public String getFuelName()
    {
        return fuelName;
    }

    public int getHeat()
    {
        return heat;
    }

    public int getMaxHeat()
    {
        return maxHeat;
    }

    public int getWaterTankAmount()
    {
        return waterTank.getFluidAmount();
    }

    public void dropItemsOnGround(BlockPos pos)
    {
        Utils.dropInventoryItems(tiedTE.getThisWorld(), pos, solidFuelInv);
    }

    private ItemStack updateSolidFuel(ItemStack stack, boolean simulate)
    {
        if (!useSolid || fuelTime > 0) return stack;
        int fuel = FurnaceTileEntity.getBurnTimes().get(stack);
        if (fuel > 0)
        {
            if (!simulate)
            {
                fuelTime = fuel;
                maxFuelTime = fuelTime;
                fuelName = stack.getDisplayName().getFormattedText();
                stack.shrink(1);
            }
            ItemStack stack1 = stack.copy();
            stack1.shrink(1);
            return simulate ? stack1 : stack;
        }
        return stack;
    }

    private void updateHeat()
    {
        if (fuelTime >= amountPerTick)
        {
            heat += 8;
            fuelTime -= amountPerTick;
        } else heat -= 2;
    }

    public void coolDown()
    {
        if (tiedTE.getThisWorld().isRemote) return;
        if (this.steamTank.getFluidAmount() > 0 && heat < 9000)
        {
            this.steamTank.drainInternal(10, IFluidHandler.FluidAction.EXECUTE);
        }
        if (heat > 2420)
        {
            heat -= 6;
            tiedTE.sync();
        }
    }

    private int updateLiquidFuel(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (useSolid) return 0;
        if (fuelTime > 0) return 0;
        int fuel = IRConfig.Main.fluidFuel.get().get(resource.getFluid().getRegistryName().getPath()) != null ? IRConfig.Main.fluidFuel.get().get(resource.getFluid().getRegistryName().getPath()) : 0;
        if (fuel > 0)
        {
            int amount = Math.min(References.BUCKET_VOLUME, resource.getAmount());
            float norm = Utils.normalizeClamped(amount, 0, References.BUCKET_VOLUME);
            if (action.execute())
            {
                fuelTime = (int) (fuel * norm);
                maxFuelTime = fuelTime;
                fuelName = resource.getDisplayName().getFormattedText();
            }
            return amount;
        }
        return 0;
    }

    public void resetFuelTime()
    {
        fuelTime = 0;
    }

    public void serialize(CompoundNBT compound)
    {
        CompoundNBT newTag = new CompoundNBT();
        newTag.putInt("fuelTime", fuelTime);
        newTag.putInt("maxFuelTime", maxFuelTime);
        newTag.putInt("amountPerTick", amountPerTick);
        newTag.putInt("heat", heat);
        newTag.putInt("steamGen", steamGenerated);
        newTag.putBoolean("useSolid", useSolid);
        newTag.putString("fuelName", fuelName);
        newTag.put("steam", steamTank.writeToNBT(new CompoundNBT()));
        newTag.put("water", waterTank.writeToNBT(new CompoundNBT()));
        compound.put("boiler", newTag);
    }

    public void deserialize(CompoundNBT compound)
    {
        CompoundNBT nbt = compound.getCompound("boiler");
        fuelTime = nbt.getInt("fuelTime");
        maxFuelTime = nbt.getInt("maxFuelTime");
        amountPerTick = nbt.getInt("amountPerTick");
        heat = nbt.getInt("heat");
        steamGenerated = nbt.getInt("steamGen");
        useSolid = nbt.getBoolean("useSolid");
        fuelName = nbt.getString("fuelName");
        steamTank.readFromNBT(nbt.getCompound("steam"));
        waterTank.readFromNBT(nbt.getCompound("water"));
    }

    public boolean isBurning()
    {
        return fuelTime > 0;
    }

    //FOR RENDER
    public String getWaterText()
    {
        return Fluids.WATER.getRegistryName().getPath().trim();
    }

    public String getSteamText()
    {
        return steamName;
    }

    public String getFuelText()
    {
        return fuelTime > 0 ? fuelName : "No Fuel";
    }

    public String getHeatText()
    {
        return Utils.getConvertedTemperatureString(heat / 100F);
    }

    public float getFuelFill() //0 ~ 180
    {
        return Utils.normalizeClamped(fuelTime, 0, maxFuelTime) * 180f;
    }

    public float GetWaterFill() //0 ~ 180
    {
        return Utils.normalizeClamped(waterTank.getFluidAmount(), 0, waterTank.getCapacity()) * 180f;
    }

    public float GetSteamFill() //0 ~ 180
    {
        int maxValue = IRConfig.Main.steamBoilerWaterPerTick.get() * IRConfig.Main.steamBoilerConversionFactor.get();
        return Utils.normalizeClamped(steamGenerated, 0, maxValue) * 180f;
    }

    public float getHeatFill() //0 ~ 140
    {
        return Utils.normalizeClamped(getHeat(), 0, getMaxHeat()) * 140f;
    }

    public enum BoilerType
    {
        Solid,
        Liquid
    }
}
