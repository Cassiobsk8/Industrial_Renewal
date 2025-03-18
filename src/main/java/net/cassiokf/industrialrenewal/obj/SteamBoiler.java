package net.cassiokf.industrialrenewal.obj;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModFluids;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.cassiokf.industrialrenewal.util.capability.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class SteamBoiler {
    public static final String steamName = "Steam";//I18n.format(FluidInit.STEAM.getUnlocalizedName());
    public static final FluidStack steamStack = new FluidStack(ModFluids.STEAM.get(), Utils.BUCKET_VOLUME);
    private static final int maxHeat = 32000;
    private static final int waterPtick = 76;//IRConfig.MainConfig.Main.steamBoilerWaterPerTick;
    private final BlockEntitySyncable tiedTE;
    private final CustomFluidTank waterTank = new CustomFluidTank(32000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack != null;// && IRConfig.waterTypesContains(fluid.getFluid().getName());
        }
        
        @Override
        public boolean canDrain() {
            return false;
        }
        
        @Override
        public void onContentsChanged() {
            SteamBoiler.this.tiedTE.sync();
        }
    };
    private final CustomFluidTank steamTank = new CustomFluidTank(320000) {
        @Override
        public boolean canFill() {
            return false;
        }
        
        @Override
        public boolean canDrain() {
            return false;
        }
        
        @Override
        public void onContentsChanged() {
            SteamBoiler.this.tiedTE.sync();
        }
    };
    private boolean useSolid;
    private int amountPerTick;
    private int heat;
    private int oldHeat;
    private int fuelTime;
    private String fuelName = "";
    private int maxFuelTime;
    private final CustomFluidTank fuelTank = new CustomFluidTank(32000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack != null;// && IRConfig.MainConfig.Main.fluidFuel.containsKey(fluid.getFluid().getName());
        }
        
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return SteamBoiler.this.updateLiquidFuel(resource, action);
        }
    };
    //    private final CustomItemStackHandler solidFuelInv = new CustomItemStackHandler(1)
    //    {
    //        @Override
    //        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    //            if (stack.isEmpty()) return false;
    //            return FurnaceBlockEntity.isFuel(stack);
    //        }
    //
    //        @Override
    //        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    //            return SteamBoiler.this.updateSolidFuel(stack, simulate);
    //        }
    //    };
    public LazyOptional<IItemHandler> solidFuelInv = LazyOptional.of(this::createFuelInv);
    private int steamGenerated;
    private int oldFuelTime;
    public SteamBoiler(BlockEntitySyncable tiedTE, BoilerType useSolid, int amountPerTick) {
        this.tiedTE = tiedTE;
        this.useSolid = useSolid == BoilerType.Solid;
        this.amountPerTick = amountPerTick;
    }
    
    private IItemHandler createFuelInv() {
        return new CustomItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (stack.isEmpty()) return false;
                return FurnaceBlockEntity.isFuel(stack);
            }
            
            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return SteamBoiler.this.updateSolidFuel(stack, simulate);
            }
        }.setBlockEntity(tiedTE);
    }
    
    public boolean canRun() {
        return (fuelTime >= amountPerTick && !waterTank.isEmpty());
    }
    
    public SteamBoiler setWaterTankCapacity(int amount) {
        waterTank.setCapacity(amount);
        return this;
    }
    
    public void onTick() {
        if (tiedTE.getLevel().isClientSide()) return;
        
        updateHeat();
        
        generateSteam();
        
        heat = Mth.clamp(heat, 2420, maxHeat);
        fuelTime = Math.max(0, fuelTime);
        
        outPutSteam();
        
        //IF no heat turn steam to water
        if (this.steamTank.getFluidAmount() > 0 && heat < 9000) {
            steamTank.drainInternal(10, IFluidHandler.FluidAction.EXECUTE);
        }
        
        if (oldHeat != heat || fuelTime != oldFuelTime) {
            oldHeat = heat;
            oldFuelTime = fuelTime;
            tiedTE.sync();
        }
    }
    
    public CustomFluidTank getFuelTank() {
        return fuelTank;
    }
    
    public CustomFluidTank getSteamTank() {
        return steamTank;
    }
    
    public CustomFluidTank getWaterTank() {
        return waterTank;
    }
    
    public LazyOptional<IItemHandler> getSolidFuelInvHandler() {
        return solidFuelInv;
    }
    
    public CustomItemStackHandler getSolidFuelInv() {
        return (CustomItemStackHandler) solidFuelInv.orElse(null);
    }
    
    private void generateSteam() {
        if (heat >= 10000 && waterTank.getFluidAmount() >= waterPtick && steamTank.getFluidAmount() < steamTank.getCapacity()) {
            float factor = Utils.normalizeClamped(heat, 10000, maxHeat);
            int amount = Math.round(waterPtick * factor);
            waterTank.drainInternal(amount, IFluidHandler.FluidAction.EXECUTE);
            steamStack.setAmount(amount * 5);
            steamGenerated = steamTank.fillInternal(steamStack, IFluidHandler.FluidAction.EXECUTE);
            heat -= 4;
        } else {
            steamGenerated = 0;
            heat -= 2;
        }
    }
    
    public void outPutSteam() {
        if (tiedTE.getLevel().isClientSide() || steamTank.getFluidAmount() <= 0) return;
        BlockPos pos = tiedTE.getBlockPos().above(2); //TODO this needs to change
        BlockEntity tileEntity = tiedTE.getLevel().getBlockEntity(pos);
        if (tileEntity != null && tileEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN).isPresent()) {
            IFluidHandler upTank = tileEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN).orElse(null);
            if (upTank != null)
                steamTank.drainInternal(upTank.fill(steamTank.drainInternal(10000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        }
        
    }
    
    public void setType(BoilerType type, int amountPerTick) {
        switch (type) {
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
    
    public int getFuelTime() {
        return fuelTime;
    }
    
    public int getMaxFuelTime() {
        return maxFuelTime;
    }
    
    public String getFuelName() {
        return fuelName;
    }
    
    public int getHeat() {
        return heat;
    }
    
    public int getMaxHeat() {
        return maxHeat;
    }
    
    public int getWaterTankAmount() {
        return waterTank.getFluidAmount();
    }
    
    public void dropItemsOnGround(BlockPos pos) {
        Utils.dropInventoryItems(tiedTE.getLevel(), pos, getSolidFuelInv());
    }
    
    private ItemStack updateSolidFuel(ItemStack stack, boolean simulate) {
        if (!useSolid || fuelTime >= amountPerTick) return stack;
        int fuel = net.minecraftforge.common.ForgeHooks.getBurnTime(stack, RecipeType.BLASTING);
        if (fuel > 0) {
            if (!simulate) {
                fuelTime = fuel;
                maxFuelTime = fuelTime;
                fuelName = stack.getDisplayName().getString();
                stack.shrink(1);
            }
            ItemStack stack1 = stack.copy();
            stack1.shrink(1);
            return simulate ? stack1 : stack;
        }
        return stack;
    }
    
    private void updateHeat() {
        if (fuelTime >= amountPerTick) {
            heat += 8;
            fuelTime -= amountPerTick;
        } else if (fuelTime > 0) {
            resetFuelTime();
        } else heat -= 2;
    }
    
    public void coolDown() {
        if (tiedTE.getLevel().isClientSide()) return;
        if (this.steamTank.getFluidAmount() > 0 && heat < 9000) {
            this.steamTank.drainInternal(10, IFluidHandler.FluidAction.EXECUTE);
        }
        if (heat > 2420) {
            heat -= 6;
            tiedTE.sync();
        }
    }
    
    private int updateLiquidFuel(FluidStack resource, IFluidHandler.FluidAction action) {
        if (useSolid) return 0;
        if (fuelTime > 0) return 0;
        int fuel = 1000;//IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getFluidType().getDescription()) != null ? IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getFluidType().getDescription()) : 0;
        if (fuel > 0) {
            int amount = Math.min(Utils.BUCKET_VOLUME, resource.getAmount());
            float norm = Utils.normalizeClamped(amount, 0, Utils.BUCKET_VOLUME);
            if (action.equals(IFluidHandler.FluidAction.EXECUTE)) {
                fuelTime = (int) (fuel * norm);
                maxFuelTime = fuelTime;
                fuelName = resource.getDisplayName().getString();
            }
            return amount;
        }
        return 0;
    }
    
    public void resetFuelTime() {
        fuelTime = 0;
    }
    
    
    public void saveBoiler(CompoundTag compound) {
        CompoundTag newTag = new CompoundTag();
        newTag.putInt("fuelTime", fuelTime);
        newTag.putInt("maxFuelTime", maxFuelTime);
        newTag.putInt("amountPerTick", amountPerTick);
        newTag.putInt("heat", heat);
        newTag.putInt("steamGen", steamGenerated);
        newTag.putBoolean("useSolid", useSolid);
        newTag.putString("fuelName", fuelName);
        newTag.put("steam", steamTank.writeToNBT(new CompoundTag()));
        newTag.put("water", waterTank.writeToNBT(new CompoundTag()));
        compound.put("boiler", newTag);
    }
    
    public void loadBoiler(CompoundTag compound) {
        CompoundTag nbt = compound.getCompound("boiler");
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
    
    public boolean isBurning() {
        return fuelTime > 0;
    }
    
    //FOR RENDER
    public String getWaterText() {
        return Utils.WATER_NAME;
    }
    
    public String getSteamText() {
        return steamName;
    }
    
    public String getFuelText() {
        //return fuelTime > 0 ? fuelName : "No Fuel";
        if (useSolid) {
            IItemHandler handler = solidFuelInv.orElse(null);
            if (solidFuelInv.isPresent()) {
                int energy = handler.getStackInSlot(0).getCount();
                return energy == 0 ? "No Fuel" : energy + " Fuel";
            }
        }
        return fuelTank.getFluidAmount() > 0 ? fuelTank.getFluid().getDisplayName().getString() : "No Fuel";
        //return "NULL Fuel";
    }
    
    public String getHeatText() {
        return (int) Utils.getConvertedTemperature(heat / 100F) + " " + Utils.getTemperatureUnit();
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
        if (steamGenerated == 0 && steamTank.getFluidAmount() == 0)
            return 0f; //When the boiler generates no steam, "no pressure".
        else
            return 45f + Utils.normalizeClamped(steamTank.getFluidAmount(), 0, steamTank.getCapacity()) * 135f; //Yellow zone when the boiler at least generates some steam, "steam pressure" goes up when the steam tank fills up.
    }
    
    public float getHeatFill() //0 ~ 140
    {
        return Utils.normalizeClamped(getHeat(), 0, getMaxHeat()) * 140f;
    }
    
    public enum BoilerType {
        Solid, Liquid
    }
}
