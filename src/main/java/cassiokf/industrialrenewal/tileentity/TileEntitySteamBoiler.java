package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSteamBoiler;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.SteamBoiler;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntitySteamBoiler extends TileEntityMultiBlockBase<TileEntitySteamBoiler>
{
    private int type;
    private static final int solidPerTick = IRConfig.Main.solidFuelPerFireboxTick.get();
    public final SteamBoiler boiler = new SteamBoiler(this, SteamBoiler.BoilerType.Solid, solidPerTick);
    private static final int fluidPerTick = 1;

    public TileEntitySteamBoiler(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
    {
        if (this.isMaster() && !this.world.isRemote)
        {
            if (this.type > 0)
            {
                boiler.onTick();
            } else
            {
                boiler.outPutSteam();
                boiler.coolDown();
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntitySteamBoiler;
    }

    public int getBoilerType()
    {
        if (!isMaster()) return getMaster().type;
        return this.type;
    }

    public void setType(int type)
    {
        if (!this.isMaster())
        {
            this.getMaster().setType(type);
            return;
        }
        boiler.dropItemsOnGround(pos);
        this.type = type;
        if (type > 0)
            boiler.setType(type == 1 ? SteamBoiler.BoilerType.Solid : SteamBoiler.BoilerType.Liquid, type == 1 ? solidPerTick : fluidPerTick);
        else boiler.resetFuelTime();
        BlockState state = getBlockState().with(BlockSteamBoiler.TYPE, type);
        world.setBlockState(pos, state, 3);
        world.notifyBlockUpdate(pos, state, state, 3);
        this.sync();
    }

    public String getFuelText()
    {
        if (getBoilerType() == 0) return "No Firebox";
        return boiler.getFuelText();
    }

    @Override
    public void onMasterBreak()
    {
        boiler.dropItemsOnGround(pos);
        Utils.spawnItemStack(world, pos, getFireBoxStack());
    }

    public ItemStack getFireBoxStack()
    {
        switch (type)
        {
            default:
            case 0:
                return ItemStack.EMPTY;
            case 1:
                return new ItemStack(ItemsRegistration.FIREBOXSOLID.get());
            case 2:
                return new ItemStack(ItemsRegistration.FIREBOXFLUID.get());
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        boiler.serialize(compound);
        compound.putInt("type", this.type);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        boiler.deserialize(compound);
        this.type = compound.getInt("type");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        TileEntitySteamBoiler masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();
        SteamBoiler masterBoiler = masterTE.boiler;

        boolean matchFuelFace = facing == face.rotateYCCW()
                && pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW()));
        if (facing == Direction.UP && this.pos.equals(masterTE.getPos().up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterBoiler.steamTank).cast();
        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterBoiler.waterTank).cast();
        if (masterTE.getBoilerType() == 1 && matchFuelFace && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterBoiler.solidFuelInv).cast();
        if (masterTE.getBoilerType() == 2 && matchFuelFace && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterBoiler.fuelTank).cast();
        return super.getCapability(capability, facing);
    }
}
