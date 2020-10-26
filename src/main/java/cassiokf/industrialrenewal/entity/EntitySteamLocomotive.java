package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.handlers.SteamBoiler;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemCartLinkable;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public class EntitySteamLocomotive extends LocomotiveBase
{
    private final SteamBoiler boiler = new SteamBoiler(this, SteamBoiler.BoilerType.Solid, 1)
    {
        @Override
        public void outPutSteam()
        {
            EntitySteamLocomotive.this.onSteamGenerated();
        }
    }.setWaterTankCapacity(64000);

    public EntitySteamLocomotive(World worldIn)
    {
        super(worldIn);
        this.setSize(1F, 1.0F);
    }

    public EntitySteamLocomotive(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    public void onLocomotiveUpdate()
    {
        //if (!inventory.getStackInSlot(0).isEmpty()) moveForward();
        fillBoiler();
        boiler.onTick();
    }

    public void onSteamGenerated()
    {
        if (boiler.steamTank.getFluidAmount() > 0)
        {
            this.moveForward();
            boiler.steamTank.drain(20, true);
        }
    }

    private void fillBoiler()
    {
        if (tender == null) return;
        FluidTank tenderTank = tender.tank;
        tenderTank.drain(boiler.waterTank.fill(tenderTank.drain(Fluid.BUCKET_VOLUME, false) , true) , true);
        Utils.moveItemsBetweenInventories(tender.inventory, boiler.solidFuelInv);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        //if (!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof ItemCartLinkable)
        //{
        //    //send to server to rotate the cart
        //    return true;
        //}
        if (!player.isSneaking())
        {
            if (!this.world.isRemote)
                player.openGui(IndustrialRenewal.instance, GUIHandler.STEAMLOCOMOTIVE, this.world, this.getEntityId(), 0, 0);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.steamLocomotive);
    }

    @Override
    public float getMaxCouplingDistance(EntityMinecart cart)
    {
        return 2.0f;
    }

    @Override
    public float getFixedDistance(EntityMinecart cart)
    {
        return 1.6f;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        boiler.serialize(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        boiler.deserialize(compound);
    }
}
