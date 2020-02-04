package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipe;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityCableTray extends TileEntityMultiBlocksTube<TileEntityCableTray>
{
    private EnumCableIn energyCable = EnumCableIn.NONE;
    private boolean fluidPipe = false;
    private boolean dataCable = false;

    @Override
    public void onLoad()
    {
        refreshConnections();
    }

    @Override
    public void update()
    {
    }

    public boolean onBlockActivated(EntityPlayer player, ItemStack stack)
    {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (block instanceof BlockFluidPipe && !fluidPipe)
        {
            fluidPipe = true;
            if (!world.isRemote && !player.isCreative()) stack.shrink(1);
            refreshConnections();
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                Sync();
            }
            return true;
        }
        if (block instanceof BlockEnergyCable && energyCable.equals(EnumCableIn.NONE))
        {
            switch (((BlockEnergyCable) block).type)
            {
                default:
                case LV:
                    energyCable = EnumCableIn.LV;
                    break;
                case MV:
                    energyCable = EnumCableIn.MV;
                    break;
                case HV:
                    energyCable = EnumCableIn.HV;
                    break;
            }
            if (!world.isRemote && !player.isCreative()) stack.shrink(1);
            refreshConnections();
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                Sync();
            }
            return true;
        }
        if (stack.getItem() instanceof ItemPowerScrewDrive && (fluidPipe || dataCable || energyCable != EnumCableIn.NONE))
        {
            if (!player.isCreative()) spawnBlocks(player);
            fluidPipe = false;
            dataCable = false;
            energyCable = EnumCableIn.NONE;
            refreshConnections();
            if (!world.isRemote)
            {
                ItemPowerScrewDrive.playDrillSound(world, pos);
                Sync();
            }
            return true;
        }
        return false;
    }

    private void spawnBlocks(EntityPlayer player)
    {
        if (world.isRemote) return;
        if (fluidPipe)
        {
            ItemStack stack = new ItemStack(Item.getItemFromBlock(ModBlocks.fluidPipe), 1);
            if (player != null) player.inventory.addItemStackToInventory(stack);
            else Utils.spawnItemStack(world, pos, stack);
        }
        if (dataCable) ;
        if (energyCable != EnumCableIn.NONE)
        {
            switch (energyCable)
            {
                case LV:
                    ItemStack stack = new ItemStack(Item.getItemFromBlock(ModBlocks.energyCableLV), 1);
                    if (player != null) player.inventory.addItemStackToInventory(stack);
                    else Utils.spawnItemStack(world, pos, stack);
                    break;
                case MV:
                    ItemStack stack2 = new ItemStack(Item.getItemFromBlock(ModBlocks.energyCableMV), 1);
                    if (player != null) player.inventory.addItemStackToInventory(stack2);
                    else Utils.spawnItemStack(world, pos, stack2);
                    break;
                case HV:
                    ItemStack stack3 = new ItemStack(Item.getItemFromBlock(ModBlocks.energyCableHV), 1);
                    if (player != null) player.inventory.addItemStackToInventory(stack3);
                    else Utils.spawnItemStack(world, pos, stack3);
                    break;
            }
        }
    }

    public boolean hasPipe()
    {
        return fluidPipe;
    }

    public boolean hasData()
    {
        return dataCable;
    }

    public EnumCableIn getCableIn()
    {
        return energyCable;
    }

    @Override
    public boolean isMaster()
    {
        return false;
    }

    @Override
    public void setMaster(TileEntityCableTray master)
    {
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityCableTray;
    }

    @Override
    public boolean isTray()
    {
        return true;
    }

    public void refreshConnections()
    {
        List<TileEntityMultiBlocksTube> connectedCables = new ArrayList<>();
        List<TileEntityCableTray> cableTrayList = new ArrayList<>();
        Stack<TileEntityCableTray> traversingCables = new Stack<>();
        traversingCables.add(this);
        while (!traversingCables.isEmpty())
        {
            TileEntityCableTray storage = traversingCables.pop();
            cableTrayList.add(storage);
            for (EnumFacing d : getFacesToCheck())
            {
                TileEntity te = world.getTileEntity(storage.getPos().offset(d));
                if (instanceOf(te) && !cableTrayList.contains(te))
                {
                    traversingCables.add((TileEntityCableTray) te);
                } else if (!instanceOf(te) && te instanceof TileEntityMultiBlocksTube && !connectedCables.contains(te))
                {
                    connectedCables.add((TileEntityMultiBlocksTube) te);
                }
            }
        }
        for (TileEntityMultiBlocksTube cables : connectedCables)
        {
            cables.setMaster(null);
            cables.getMaster();
        }
    }

    @Override
    public void invalidate()
    {
        spawnBlocks(null);
        super.invalidate();
        refreshConnections();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        fluidPipe = compound.getBoolean("fluidPipe");
        dataCable = compound.getBoolean("dataCable");
        energyCable = EnumCableIn.byIndex(compound.getInteger("energyCableIn"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("fluidPipe", fluidPipe);
        compound.setBoolean("dataCable", dataCable);
        compound.setInteger("energyCableIn", energyCable.getIndex());
        return super.writeToNBT(compound);
    }
}