package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSteamBoiler;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.FluidsRegistration;
import cassiokf.industrialrenewal.item.ItemFireBox;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static cassiokf.industrialrenewal.init.TileRegistration.STEAMBOILER_TILE;

public class TileEntitySteamBoiler extends TileEntity3x3MachineBase<TileEntitySteamBoiler> implements ITickableTileEntity
{
    public CustomFluidTank waterTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return stack != null && stack.getFluid().equals(Fluids.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.Sync();
        }
    };
    public CustomFluidTank steamTank = new CustomFluidTank(320000)
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

    public CustomFluidTank fuelTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            //TODO FLUIDSET CONFIG
            //return stack != null && IRConfig.Main.fluidFuel.containsKey(stack.getFluid().getDefaultState().getBlockState().getBlock().getNameTextComponent().getString());
            return true;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.Sync();
        }
    };

    public LazyOptional<IItemHandler> fireBoxInv = LazyOptional.of(this::createFireboxInv);

    public LazyOptional<IItemHandler> solidFuelInv = LazyOptional.of(this::createFuelInv);

    private int type;

    private int maxHeat = 32000;
    private int heat;
    private int oldHeat;
    private int waterPtick = IRConfig.Main.steamBoilerWaterPerTick.get();

    private int fuelTime;
    private int maxFuelTime;
    private int oldFuelTime;

    private int solidPerTick = 2;
    private int fluidPerTick = 1;

    public TileEntitySteamBoiler()
    {
        super(STEAMBOILER_TILE.get());
    }

    private IItemHandler createFireboxInv()
    {
        return new CustomItemStackHandler(1)
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
    }

    private IItemHandler createFuelInv()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return FurnaceTileEntity.isFuel(stack);
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntitySteamBoiler.this.Sync();
            }
        };
    }

    @Override
    public void tick()
    {
        if (this.isMaster() && !world.isRemote)
        {
            if (this.type > 0)
            {
                //Fuel to Heat
                switch (this.type)
                {
                    default:
                    case 1:
                        if (fuelTime >= solidPerTick || !solidFuelInv.orElse(null).getStackInSlot(0).isEmpty())
                        {
                            ItemStack fuel = solidFuelInv.orElse(null).getStackInSlot(0);
                            if (fuelTime <= 0)
                            {
                                fuelTime = ForgeHooks.getBurnTime(fuel);
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
                                //TODO Fluid Set Config
                                fuelTime = 50;//IRConfig.Main.fluidFuel.get(fuel.getFluid().getRegistryName().toString()) != null ? IRConfig.Main.fluidFuel.get(fuel.getFluid().getRegistryName().toString()) : 0;
                                maxFuelTime = fuelTime;
                                fuel.setAmount(fuel.getAmount() - FluidAttributes.BUCKET_VOLUME);
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
                    waterTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                    FluidStack steamStack = new FluidStack(FluidsRegistration.STEAM.get(), amount * IRConfig.Main.steamBoilerConversionFactor.get());
                    steamTank.fillInternal(steamStack, IFluidHandler.FluidAction.EXECUTE);
                    heat -= 2;
                }

                heat -= 2;
                heat = MathHelper.clamp(heat, 2420, maxHeat);
                fuelTime = Math.max(0, fuelTime);

                //Auto output Steam
                TileEntity upTE = world.getTileEntity(pos.up(2));
                if (this.steamTank.getFluidAmount() > 0 && upTE != null && upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).isPresent())
                {
                    IFluidHandler upTank = upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                    this.steamTank.drain(upTank.fill(this.steamTank.drain(10000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                }

                //Steam to Water if no Heat
                if (this.steamTank.getFluidAmount() > 0 && heat < 9000)
                {
                    FluidStack stack = this.steamTank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                    stack.setAmount(stack.getAmount() / IRConfig.Main.steamBoilerConversionFactor.get());
                    waterTank.fill(stack, IFluidHandler.FluidAction.EXECUTE);
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

    public int getIntType()
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
        BlockState state = getBlockState().with(BlockSteamBoiler.TYPE, type);
        world.setBlockState(this.pos, state, 3);
        world.notifyBlockUpdate(pos, state, state, 3);
        this.Sync();
    }

    public void dropAllItems()
    {
        dropItemsInGround(solidFuelInv);
        dropItemsInGround(fireBoxInv);
    }

    private void dropItemsInGround(LazyOptional<IItemHandler> inventory)
    {
        ItemStack stack = inventory.orElse(null).getStackInSlot(0);
        if (!stack.isEmpty())
        {
            ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(0, ItemStack.EMPTY));
            world.addEntity(item);
        }
    }

    public String getWaterText()
    {
        return Blocks.WATER.getNameTextComponent().getFormattedText();
    }

    public String getSteamText()
    {
        return FluidsRegistration.STEAM_BLOCK.get().getNameTextComponent().getFormattedText();
    }

    public String getFuelText()
    {
        switch (getIntType())
        {
            default:
            case 0:
                return "No Firebox";
            case 1:
                int energy = solidFuelInv.orElse(null).getStackInSlot(0).getCount();
                if (energy == 0) return "No Fuel";
                return energy + " Fuel";
            case 2:
                return fuelTank.getFluidAmount() > 0 ? fuelTank.getFluid().getDisplayName().getString() : "No Fuel";
        }
    }

    public String getHeatText()
    {
        return (int) Utils.getConvertedTemperature(heat / 100F) + Utils.getTemperatureUnit();
    }

    public float getFuelFill() //0 ~ 180
    {
        switch (getIntType())
        {
            default:
            case 0:
                return 0;
            case 1:
                float currentAmount = (float) fuelTime;
                currentAmount = currentAmount / (float) maxFuelTime;
                return currentAmount * 180f;
            case 2:
                float amount = fuelTank.getFluidAmount() / 1000f;
                float totalCapacity = fuelTank.getCapacity() / 1000f;
                amount = amount / totalCapacity;
                return amount * 180f;
        }
    }

    public float GetWaterFill() //0 ~ 180
    {
        float currentAmount = waterTank.getFluidAmount() / 1000F;
        float totalCapacity = waterTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float GetSteamFill() //0 ~ 180
    {
        float currentAmount = steamTank.getFluidAmount() / 1000F;
        float totalCapacity = steamTank.getCapacity() / 1000F;
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
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT waterTag = new CompoundNBT();
        CompoundNBT steamTag = new CompoundNBT();
        CompoundNBT fuelTag = new CompoundNBT();
        waterTank.writeToNBT(waterTag);
        steamTank.writeToNBT(steamTag);
        fuelTank.writeToNBT(fuelTag);
        compound.put("water", waterTag);
        compound.put("steam", steamTag);
        compound.put("fluidFuel", fuelTag);
        compound.putInt("type", type);
        fireBoxInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        solidFuelInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        compound.putInt("heat", heat);
        compound.putInt("fueltime", fuelTime);
        compound.putInt("maxtime", maxFuelTime);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        CompoundNBT waterTag = compound.getCompound("water");
        CompoundNBT steamTag = compound.getCompound("steam");
        CompoundNBT fluidFuel = compound.getCompound("fluidFuel");
        waterTank.readFromNBT(waterTag);
        steamTank.readFromNBT(steamTag);
        fuelTank.readFromNBT(fluidFuel);
        type = compound.getInt("type");
        CompoundNBT invTag = compound.getCompound("inv");
        fireBoxInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        CompoundNBT invTag2 = compound.getCompound("inv");
        solidFuelInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag2));
        heat = compound.getInt("heat");
        fuelTime = compound.getInt("fueltime");
        maxFuelTime = compound.getInt("maxtime");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        TileEntitySteamBoiler masterTE = getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();

        if (facing == Direction.UP && pos.equals(masterTE.getPos().up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> steamTank).cast();
        if (facing == face && pos.equals(masterTE.getPos().down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> waterTank).cast();
        if (masterTE.getIntType() == 1 && facing == face.rotateYCCW() && pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return solidFuelInv.cast();
        if (masterTE.getIntType() == 2 && facing == face.rotateYCCW() && pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fuelTank).cast();
        return super.getCapability(capability, facing);
    }

    public IItemHandler getFireBoxHandler()
    {
        return getMaster().fireBoxInv.orElse(null);
    }
}
