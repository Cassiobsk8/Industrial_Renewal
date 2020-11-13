package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockSteamBoiler;
import cassiokf.industrialrenewal.handlers.SteamBoiler;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntitySteamBoiler extends TileEntityMultiBlockBase<TileEntitySteamBoiler>
{
    private int type;
    private final int solidPerTick = 2;
    public final SteamBoiler boiler = new SteamBoiler(this, SteamBoiler.BoilerType.Solid, solidPerTick);
    private final int fluidPerTick = 1;

    @Override
    public void tick()
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

    public int getType()
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
        IBlockState state = this.world.getBlockState(this.pos).withProperty(BlockSteamBoiler.TYPE, type);
        this.world.setBlockState(this.pos, state, 3);
        world.notifyBlockUpdate(pos, state, state, 3);
        this.sync();
    }

    public String getFuelText()
    {
        if (getType() == 0) return "No Firebox";
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
                return new ItemStack(ModItems.fireBoxSolid);
            case 2:
                return new ItemStack(ModItems.fireBoxFluid);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        boiler.serialize(compound);
        compound.setInteger("type", this.type);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        boiler.deserialize(compound);
        this.type = compound.getInteger("type");
        super.readFromNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        TileEntitySteamBoiler masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        EnumFacing face = masterTE.getMasterFacing();
        SteamBoiler masterBoiler = masterTE.boiler;

        boolean matchFuelFace = facing == face.rotateYCCW()
                && pos.equals(masterTE.getPos().down().offset(face.getOpposite()).offset(face.rotateYCCW()));
        if (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().up()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterBoiler.steamTank);
        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterBoiler.waterTank);
        if (masterTE.getType() == 1 && matchFuelFace && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(masterBoiler.solidFuelInv);
        if (masterTE.getType() == 2 && matchFuelFace && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterBoiler.fuelTank);
        return super.getCapability(capability, facing);
    }
}
