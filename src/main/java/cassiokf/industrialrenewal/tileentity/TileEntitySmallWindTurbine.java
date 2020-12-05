package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSmallWindTurbine;
import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ItemWindBlade;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class TileEntitySmallWindTurbine extends TileEntitySync implements ITickableTileEntity
{
    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(32000, 1024, 1024)
    {
        @Override
        public boolean canReceive()
        {
            return false;
        }
    };
    public ItemStackHandler bladeInv = new ItemStackHandler(1)
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            if (stack.isEmpty()) return false;
            return stack.getItem() instanceof ItemWindBlade;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntitySmallWindTurbine.this.sync();
        }
    };
    private float rotation;
    private float oldRotation;
    private int tickToDamage;
    private Direction blockFacing;

    private static final Random random = new Random();

    public TileEntitySmallWindTurbine(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(world, pos, bladeInv);
    }

    public static int getMaxGeneration()
    {
        return IRConfig.Main.maxEnergySWindTurbine.get();
    }

    @Override
    public void tick()
    {
        if (!world.isRemote)
        {
            int energyGen = 0;
            //Generate Energy
            if (hasBlade())
            {
                energyGen = Math.round(getMaxGeneration() * getEfficiency());
                //damage blade
                if (tickToDamage >= 1200 && energyGen > 0)
                {
                    tickToDamage = 0;
                    if (bladeInv.getStackInSlot(0).attemptDamageItem(1, random, null))
                        bladeInv.setStackInSlot(0, ItemStack.EMPTY);
                }
                if (tickToDamage < 1201) tickToDamage++;
            }
            //OutPut Energy
            if (energyGen > 0)
            {
                TileEntity te = world.getTileEntity(pos.down());
                if (te != null)
                {
                    IEnergyStorage downE = te.getCapability(CapabilityEnergy.ENERGY, Direction.UP).orElse(null);
                    if (downE != null && downE.canReceive())
                    {
                        downE.receiveEnergy(energyGen, false);
                    }
                }
            }
        } else
        {
            if (hasBlade())
            {
                oldRotation = rotation;
                rotation += 6f * getEfficiency();
                if (rotation >= 360f)
                {
                    rotation -= 360f;
                    oldRotation -= 360f;
                }
            }
        }
    }

    public IItemHandler getBladeHandler()
    {
        return this.bladeInv;
    }

    public float getRotation()
    {
        //float inverted = Utils.normalize(partialTicks, 1, 0);
        //rotation = rotation + (4f * inverted) * getEfficiency();
        //if (rotation >= 360) rotation -= 360;
        return rotation;
    }

    public float getOldRotation()
    {
        return oldRotation;
    }

    @Override
    public double getMaxRenderDistanceSquared()
    {
        return super.getMaxRenderDistanceSquared() * IRConfig.Render.windBladesRenderDistanceMult.get();
    }

    public boolean hasBlade()
    {
        return !this.bladeInv.getStackInSlot(0).isEmpty();
    }

    private float getEfficiency()
    {
        float weatherModifier;
        if (world.isThundering())
        {
            weatherModifier = 1f;
        } else if (world.isRaining())
        {
            weatherModifier = 0.9f;
        } else
        {
            weatherModifier = 0.8f;
        }

        float heightModifier;
        float posMin = -2040f;
        if (pos.getY() - 62 <= 0) heightModifier = 0;
        else heightModifier = (pos.getY() - posMin) / (255 - posMin);
        heightModifier = MathHelper.clamp(heightModifier, 0, 1);

        return weatherModifier * heightModifier;
    }

    public Direction getBlockFacing()
    {
        if (blockFacing != null) return blockFacing;
        BlockState state = getBlockState();
        if (state.getBlock() instanceof BlockSmallWindTurbine)
        {
            return blockFacing = state.get(BlockHorizontalFacing.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> energyContainer).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        this.bladeInv.deserializeNBT(compound.getCompound("bladeInv"));
        this.tickToDamage = compound.getInt("damageTick");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.put("bladeInv", this.bladeInv.serializeNBT());
        compound.putInt("damageTick", tickToDamage);
        return super.write(compound);
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(pos.add(-4D, -4D, -4D), pos.add(5D, 5D, 5D));
    }
}
