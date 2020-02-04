package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSteamBoiler;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.FluidInit;
import cassiokf.industrialrenewal.item.ItemFireBox;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySteamBoiler extends TileEntity3x3MachineBase<TileEntitySteamBoiler> implements ITickable
{
    public FluidTank waterTank = new FluidTank(32000)
    {
        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            return fluid != null && fluid.getFluid().equals(FluidRegistry.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.Sync();
        }
    };
    public FluidTank steamTank = new FluidTank(320000)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.Sync();
        }
    };

    public FluidTank fuelTank = new FluidTank(32000)
    {
        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            return fluid != null && IRConfig.MainConfig.Main.fluidFuel.containsKey(fluid.getFluid().getName());
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.Sync();
        }
    };

    public ItemStackHandler fireBoxInv = new ItemStackHandler(1)
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            if (stack.isEmpty()) return false;
            return stack.getItem() instanceof ItemFireBox;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntitySteamBoiler.this.Sync();
        }
    };

    public ItemStackHandler solidFuelInv = new ItemStackHandler(1)
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            if (stack.isEmpty()) return false;
            return TileEntityFurnace.isItemFuel(stack);
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntitySteamBoiler.this.Sync();
        }
    };

    private int type;

    private int maxHeat = 32000;
    private int heat;
    private int oldHeat;
    private int waterPtick = IRConfig.MainConfig.Main.steamBoilerWaterPerTick;

    private int fuelTime;
    private int maxFuelTime;
    private int oldFuelTime;

    private int solidPerTick = 2;
    private int fluidPerTick = 1;

    @Override
    public void update()
    {
        if (this.isMaster() && !this.world.isRemote)
        {
            if (this.type > 0)
            {
                //Fuel to Heat
                switch (this.type)
                {
                    default:
                    case 1:
                        if (fuelTime >= solidPerTick || !this.solidFuelInv.getStackInSlot(0).isEmpty())
                        {
                            ItemStack fuel = this.solidFuelInv.getStackInSlot(0);
                            if (fuelTime <= 0)
                            {
                                fuelTime = TileEntityFurnace.getItemBurnTime(fuel);
                                maxFuelTime = fuelTime;
                                fuel.shrink(1);
                            }
                            heat += 8;
                            fuelTime -= solidPerTick;
                        } else heat -= 2;
                        break;
                    case 2:
                        if (fuelTime >= fluidPerTick || this.fuelTank.getFluidAmount() > 0)
                        {
                            FluidStack fuel = this.fuelTank.getFluid();
                            if (fuelTime <= 0)
                            {
                                fuelTime = IRConfig.MainConfig.Main.fluidFuel.get(fuel.getFluid().getName()) != null ? IRConfig.MainConfig.Main.fluidFuel.get(fuel.getFluid().getName()) : 0;
                                maxFuelTime = fuelTime;
                                fuel.amount -= Fluid.BUCKET_VOLUME;
                                this.markDirty();
                            }
                            heat += 8;
                            fuelTime -= fluidPerTick;
                        } else heat -= 2;
                        break;
                }

                //Water to Steam
                if (heat >= 10000 && this.waterTank.getFluidAmount() >= waterPtick && this.steamTank.getFluidAmount() < this.steamTank.getCapacity())
                {
                    int amount = waterPtick;
                    float factor = (heat / 100f) / (maxHeat / 100f);
                    amount = Math.round(amount * factor);
                    this.waterTank.drain(amount, true);
                    FluidStack steamStack = new FluidStack(FluidRegistry.getFluid("steam"), amount * IRConfig.MainConfig.Main.steamBoilerConversionFactor);
                    this.steamTank.fillInternal(steamStack, true);
                    heat -= 2;
                }

                heat -= 2;
                heat = MathHelper.clamp(heat, 2420, maxHeat);
                fuelTime = Math.max(0, fuelTime);

                //Auto output Steam
                TileEntity upTE = this.world.getTileEntity(pos.up(2));
                if (this.steamTank.getFluidAmount() > 0 && upTE != null && upTE.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
                {
                    IFluidHandler upTank = upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
                    this.steamTank.drain(upTank.fill(this.steamTank.drain(10000, false), true), true);
                }

                //Steam to Water if no Heat
                if (this.steamTank.getFluidAmount() > 0 && heat < 9000)
                {
                    FluidStack stack = this.steamTank.drain(10, true);
                    stack.amount = stack.amount / IRConfig.MainConfig.Main.steamBoilerConversionFactor;
                    this.waterTank.fill(stack, true);
                }
            } else if (heat > 2420) heat -= 6;
            //Sync with Client
            if (oldHeat != heat || fuelTime != oldFuelTime)
            {
                oldHeat = heat;
                oldFuelTime = fuelTime;
                this.Sync();
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntitySteamBoiler;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        if (!this.isMaster())
        {
            this.getMaster().setType(type);
            return;
        }
        dropItemsInGround(solidFuelInv);
        this.fuelTime = 0;
        this.type = type;
        IBlockState state = this.world.getBlockState(this.pos).withProperty(BlockSteamBoiler.TYPE, type);
        this.world.setBlockState(this.pos, state, 3);
        world.notifyBlockUpdate(pos, state, state, 3);
        this.Sync();
    }

    public void dropAllItems()
    {
        dropItemsInGround(solidFuelInv);
        dropItemsInGround(fireBoxInv);
    }

    private void dropItemsInGround(ItemStackHandler inventory)
    {
        ItemStack stack = inventory.getStackInSlot(0);
        if (!stack.isEmpty())
        {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            inventory.setStackInSlot(0, ItemStack.EMPTY);
            this.world.spawnEntity(item);
        }
    }

    public String getWaterText()
    {
        return FluidRegistry.WATER.getName();
    }

    public String getSteamText()
    {
        return FluidInit.STEAM.getName();
    }

    public String getFuelText()
    {
        switch (getType())
        {
            default:
            case 0:
                return "No Firebox";
            case 1:
                int energy = this.solidFuelInv.getStackInSlot(0).getCount();
                if (energy == 0) return "No Fuel";
                return energy + " Fuel";
            case 2:
                return this.fuelTank.getFluidAmount() > 0 ? this.fuelTank.getFluid().getLocalizedName() : "No Fuel";
        }
    }

    public String getHeatText()
    {
        String st;
        switch (IRConfig.MainConfig.Main.temperatureScale)
        {
            default:
            case 0:
                st = " " + I18n.format("render.industrialrenewal.c");
                break;
            case 1:
                st = " " + I18n.format("render.industrialrenewal.f");
                break;
            case 2:
                st = " " + I18n.format("render.industrialrenewal.k");
                break;
        }
        return (int) Utils.getConvertedTemperature(heat / 100F) + st;
    }

    public float getFuelFill() //0 ~ 180
    {
        switch (getType())
        {
            default:
            case 0:
                return 0;
            case 1:
                float currentAmount = (float) this.fuelTime;
                currentAmount = currentAmount / (float) maxFuelTime;
                return currentAmount * 180f;
            case 2:
                float amount = this.fuelTank.getFluidAmount() / 1000f;
                float totalCapacity = this.fuelTank.getCapacity() / 1000f;
                amount = amount / totalCapacity;
                return amount * 180f;
        }
    }

    public float GetWaterFill() //0 ~ 180
    {
        float currentAmount = this.waterTank.getFluidAmount() / 1000F;
        float totalCapacity = this.waterTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float GetSteamFill() //0 ~ 180
    {
        float currentAmount = this.steamTank.getFluidAmount() / 1000F;
        float totalCapacity = this.steamTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getHeatFill() //0 ~ 180
    {
        float currentAmount = heat;
        float totalCapacity = maxHeat;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 140f;
    }

    public int getFuelTime()
    {
        return fuelTime;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = new NBTTagCompound();
        NBTTagCompound steamTag = new NBTTagCompound();
        NBTTagCompound fuelTag = new NBTTagCompound();
        this.waterTank.writeToNBT(waterTag);
        this.steamTank.writeToNBT(steamTag);
        this.fuelTank.writeToNBT(fuelTag);
        compound.setTag("water", waterTag);
        compound.setTag("steam", steamTag);
        compound.setTag("fluidFuel", fuelTag);
        compound.setInteger("type", this.type);
        compound.setTag("firebox", this.fireBoxInv.serializeNBT());
        compound.setTag("solidfuel", this.solidFuelInv.serializeNBT());
        compound.setInteger("heat", heat);
        compound.setInteger("fueltime", fuelTime);
        compound.setInteger("maxtime", maxFuelTime);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = compound.getCompoundTag("water");
        NBTTagCompound steamTag = compound.getCompoundTag("steam");
        NBTTagCompound fluidFuel = compound.getCompoundTag("fluidFuel");
        this.waterTank.readFromNBT(waterTag);
        this.steamTank.readFromNBT(steamTag);
        this.fuelTank.readFromNBT(fluidFuel);
        this.type = compound.getInteger("type");
        this.fireBoxInv.deserializeNBT(compound.getCompoundTag("firebox"));
        this.solidFuelInv.deserializeNBT(compound.getCompoundTag("solidfuel"));
        this.heat = compound.getInteger("heat");
        this.fuelTime = compound.getInteger("fueltime");
        this.maxFuelTime = compound.getInteger("maxtime");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        TileEntitySteamBoiler masterTE = this.getMaster();
        if (masterTE == null) return false;
        EnumFacing face = masterTE.getMasterFacing();
        return (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (facing == face && this.pos.equals(masterTE.getPos().down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                || (this.getMaster().getType() == 1 && facing == face.rotateYCCW() && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                || (this.getMaster().getType() == 2 && facing == face.rotateYCCW() && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        TileEntitySteamBoiler masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        EnumFacing face = masterTE.getMasterFacing();

        if (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.steamTank);
        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.waterTank);
        if (masterTE.getType() == 1 && facing == face.rotateYCCW() && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(masterTE.solidFuelInv);
        if (masterTE.getType() == 2 && facing == face.rotateYCCW() && this.pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.fuelTank);
        return super.getCapability(capability, facing);
    }

    public IItemHandler getFireBoxHandler()
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.getMaster().fireBoxInv);
    }
}
