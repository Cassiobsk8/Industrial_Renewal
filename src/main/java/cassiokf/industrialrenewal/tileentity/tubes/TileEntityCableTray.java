package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.pipes.BlockCableTray;
import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipe;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static cassiokf.industrialrenewal.init.TileRegistration.CABLETRAY_TILE;

public class TileEntityCableTray extends TileEntityMultiBlocksTube<TileEntityCableTray>
{
    private EnumCableIn energyCable = EnumCableIn.NONE;
    private boolean fluidPipe = false;
    private boolean dataCable = false;

    public static final ModelProperty<Boolean> PIPE_CORE = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE_DOWN = new ModelProperty<>();

    public static final ModelProperty<Boolean> PIPE2_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE2_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE2_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE2_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE2_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> PIPE2_DOWN = new ModelProperty<>();

    public static final ModelProperty<Boolean> HV_CORE = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV_DOWN = new ModelProperty<>();

    public static final ModelProperty<Boolean> HV2_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV2_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV2_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV2_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV2_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> HV2_DOWN = new ModelProperty<>();

    public static final ModelProperty<Boolean> MV_CORE = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV_DOWN = new ModelProperty<>();

    public static final ModelProperty<Boolean> MV2_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV2_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV2_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV2_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV2_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> MV2_DOWN = new ModelProperty<>();

    public static final ModelProperty<Boolean> LV_CORE = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV_DOWN = new ModelProperty<>();

    public static final ModelProperty<Boolean> LV2_NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV2_SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV2_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV2_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV2_UP = new ModelProperty<>();
    public static final ModelProperty<Boolean> LV2_DOWN = new ModelProperty<>();

    public TileEntityCableTray()
    {
        super(CABLETRAY_TILE.get());
    }

    @Override
    public void tick()
    {
        if (!firstTick)
        {
            firstTick = true;
            onFirstLoad();
        }
    }

    @Override
    public void onFirstLoad()
    {
        refreshConnections();
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        boolean isHvPresent = getCableIn().equals(EnumCableIn.HV);
        boolean isMvPresent = getCableIn().equals(EnumCableIn.MV);
        boolean isLvPresent = getCableIn().equals(EnumCableIn.LV);
        boolean isPipePresent = hasPipe();
        ModelDataMap.Builder builder = new ModelDataMap.Builder()
                .withInitial(SOUTH, canConnectToPipe(Direction.SOUTH))
                .withInitial(NORTH, canConnectToPipe(Direction.NORTH))
                .withInitial(EAST, canConnectToPipe(Direction.EAST))
                .withInitial(WEST, canConnectToPipe(Direction.WEST))
                .withInitial(UP, canConnectToPipe(Direction.UP))
                .withInitial(DOWN, canConnectToPipe(Direction.DOWN));
        if (isPipePresent)
        {
            builder.withInitial(PIPE_CORE, true)
                    .withInitial(PIPE_NORTH, canConnectFluidPipeTrayToTray(Direction.NORTH))
                    .withInitial(PIPE_SOUTH, canConnectFluidPipeTrayToTray(Direction.SOUTH))
                    .withInitial(PIPE_EAST, canConnectFluidPipeTrayToTray(Direction.EAST))
                    .withInitial(PIPE_WEST, canConnectFluidPipeTrayToTray(Direction.WEST))
                    .withInitial(PIPE_UP, canConnectFluidPipeTrayToTray(Direction.UP))
                    .withInitial(PIPE_DOWN, canConnectFluidPipeTrayToTray(Direction.DOWN))
                    .withInitial(PIPE2_NORTH, canConnectToCapability(Direction.NORTH))
                    .withInitial(PIPE2_SOUTH, canConnectToCapability(Direction.SOUTH))
                    .withInitial(PIPE2_EAST, canConnectToCapability(Direction.EAST))
                    .withInitial(PIPE2_WEST, canConnectToCapability(Direction.WEST))
                    .withInitial(PIPE2_UP, canConnectToCapability(Direction.UP))
                    .withInitial(PIPE2_DOWN, canConnectToCapability(Direction.DOWN));
        }
        if (isHvPresent)
        {
            builder.withInitial(HV_CORE, true)
                    .withInitial(HV_NORTH, canConnectCableTrayToTray(Direction.NORTH))
                    .withInitial(HV_SOUTH, canConnectCableTrayToTray(Direction.SOUTH))
                    .withInitial(HV_EAST, canConnectCableTrayToTray(Direction.EAST))
                    .withInitial(HV_WEST, canConnectCableTrayToTray(Direction.WEST))
                    .withInitial(HV_UP, canConnectCableTrayToTray(Direction.UP))
                    .withInitial(HV_DOWN, canConnectCableTrayToTray(Direction.DOWN))
                    .withInitial(HV2_NORTH, canConnectToEnergyCapability(Direction.NORTH, EnumCableIn.HV))
                    .withInitial(HV2_SOUTH, canConnectToEnergyCapability(Direction.SOUTH, EnumCableIn.HV))
                    .withInitial(HV2_EAST, canConnectToEnergyCapability(Direction.EAST, EnumCableIn.HV))
                    .withInitial(HV2_WEST, canConnectToEnergyCapability(Direction.WEST, EnumCableIn.HV))
                    .withInitial(HV2_UP, canConnectToEnergyCapability(Direction.UP, EnumCableIn.HV))
                    .withInitial(HV2_DOWN, canConnectToEnergyCapability(Direction.DOWN, EnumCableIn.HV));
        } else if (isMvPresent)
        {
            builder.withInitial(MV_CORE, true)
                    .withInitial(MV_NORTH, canConnectCableTrayToTray(Direction.NORTH))
                    .withInitial(MV_SOUTH, canConnectCableTrayToTray(Direction.SOUTH))
                    .withInitial(MV_EAST, canConnectCableTrayToTray(Direction.EAST))
                    .withInitial(MV_WEST, canConnectCableTrayToTray(Direction.WEST))
                    .withInitial(MV_UP, canConnectCableTrayToTray(Direction.UP))
                    .withInitial(MV_DOWN, canConnectCableTrayToTray(Direction.DOWN))
                    .withInitial(MV2_NORTH, canConnectToEnergyCapability(Direction.NORTH, EnumCableIn.MV))
                    .withInitial(MV2_SOUTH, canConnectToEnergyCapability(Direction.SOUTH, EnumCableIn.MV))
                    .withInitial(MV2_EAST, canConnectToEnergyCapability(Direction.EAST, EnumCableIn.MV))
                    .withInitial(MV2_WEST, canConnectToEnergyCapability(Direction.WEST, EnumCableIn.MV))
                    .withInitial(MV2_UP, canConnectToEnergyCapability(Direction.UP, EnumCableIn.MV))
                    .withInitial(MV2_DOWN, canConnectToEnergyCapability(Direction.DOWN, EnumCableIn.MV));
        } else if (isLvPresent)
        {
            builder.withInitial(LV_CORE, true)
                    .withInitial(LV_NORTH, canConnectCableTrayToTray(Direction.NORTH))
                    .withInitial(LV_SOUTH, canConnectCableTrayToTray(Direction.SOUTH))
                    .withInitial(LV_EAST, canConnectCableTrayToTray(Direction.EAST))
                    .withInitial(LV_WEST, canConnectCableTrayToTray(Direction.WEST))
                    .withInitial(LV_UP, canConnectCableTrayToTray(Direction.UP))
                    .withInitial(LV_DOWN, canConnectCableTrayToTray(Direction.DOWN))
                    .withInitial(LV2_NORTH, canConnectToEnergyCapability(Direction.NORTH, EnumCableIn.LV))
                    .withInitial(LV2_SOUTH, canConnectToEnergyCapability(Direction.SOUTH, EnumCableIn.LV))
                    .withInitial(LV2_EAST, canConnectToEnergyCapability(Direction.EAST, EnumCableIn.LV))
                    .withInitial(LV2_WEST, canConnectToEnergyCapability(Direction.WEST, EnumCableIn.LV))
                    .withInitial(LV2_UP, canConnectToEnergyCapability(Direction.UP, EnumCableIn.LV))
                    .withInitial(LV2_DOWN, canConnectToEnergyCapability(Direction.DOWN, EnumCableIn.LV));
        }
        return builder.build();
    }

    public boolean canConnectToPipe(Direction neighborDirection)
    {
        BlockState state = world.getBlockState(pos.offset(neighborDirection));
        return state.getBlock() instanceof BlockCableTray;
    }

    public boolean canConnectToCapability(Direction neighborDirection)
    {
        BlockPos offsetPos = pos.offset(neighborDirection);
        BlockState state = world.getBlockState(offsetPos);
        return state.getBlock() instanceof BlockFluidPipe;
    }

    private boolean canConnectToEnergyCapability(Direction neighborDirection, EnumCableIn type)
    {
        BlockPos offset = pos.offset(neighborDirection);
        BlockState state = world.getBlockState(offset);
        Block block = state.getBlock();
        return block instanceof BlockEnergyCable
                && type.equals(BlockEnergyCable.convertFromType(((BlockEnergyCable) block).type));
    }

    private boolean canConnectFluidPipeTrayToTray(Direction neighborDirection)
    {
        TileEntity otherTE = world.getTileEntity(pos.offset(neighborDirection));

        return otherTE instanceof TileEntityCableTray && ((TileEntityCableTray) otherTE).hasPipe();
    }

    private boolean canConnectCableTrayToTray(Direction neighborDirection)
    {
        TileEntity otherTE = world.getTileEntity(pos.offset(neighborDirection));

        return otherTE instanceof TileEntityCableTray && getCableIn().equals(((TileEntityCableTray) otherTE).getCableIn());
    }

    public boolean onBlockActivated(PlayerEntity player, ItemStack stack)
    {
        if (!hasWorld()) return false;
        Block block = Block.getBlockFromItem(stack.getItem());
        if (block instanceof BlockFluidPipe && !fluidPipe)
        {
            fluidPipe = true;
            if (!world.isRemote && !player.isCreative()) stack.shrink(1);
            refreshConnections();
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            }
            Sync();
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
                world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            }
            Sync();
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
            }
            Sync();
            return true;
        }
        return false;
    }

    private void spawnBlocks(PlayerEntity player)
    {
        if (world.isRemote) return;
        if (fluidPipe)
        {
            ItemStack stack = new ItemStack(BlocksRegistration.FLUIDPIPE_ITEM.get());
            if (player != null) player.inventory.addItemStackToInventory(stack);
            else Utils.spawnItemStack(world, pos, stack);
        }
        if (dataCable) ;
        if (energyCable != EnumCableIn.NONE)
        {
            switch (energyCable)
            {
                case LV:
                    ItemStack stack = new ItemStack(BlocksRegistration.ENERGYCABLELV_ITEM.get());
                    if (player != null) player.inventory.addItemStackToInventory(stack);
                    else Utils.spawnItemStack(world, pos, stack);
                    break;
                case MV:
                    ItemStack stack2 = new ItemStack(BlocksRegistration.ENERGYCABLEMV_ITEM.get());
                    if (player != null) player.inventory.addItemStackToInventory(stack2);
                    else Utils.spawnItemStack(world, pos, stack2);
                    break;
                case HV:
                    ItemStack stack3 = new ItemStack(BlocksRegistration.ENERGYCABLEHV_ITEM.get());
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
            for (Direction d : getFacesToCheck())
            {
                TileEntity te = world.getTileEntity(storage.getPos().offset(d));
                if (instanceOf(te) && !cableTrayList.contains(te))
                {
                    traversingCables.add((TileEntityCableTray) te);
                } else if (!instanceOf(te)
                        && te instanceof TileEntityMultiBlocksTube
                        && !connectedCables.contains(te))
                {
                    connectedCables.add((TileEntityMultiBlocksTube) te);
                }
            }
        }
        for (TileEntityMultiBlocksTube cables : connectedCables)
        {
            cables.setMaster(null);
            cables.getMaster();
            cables.requestModelRefresh();
        }
        requestModelRefresh();
    }

    @Override
    public void remove()
    {
        spawnBlocks(null);
        super.remove();
        refreshConnections();
    }

    @Override
    public void read(CompoundNBT compound)
    {
        fluidPipe = compound.getBoolean("fluidPipe");
        dataCable = compound.getBoolean("dataCable");
        energyCable = EnumCableIn.byIndex(compound.getInt("energyCableIn"));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("fluidPipe", fluidPipe);
        compound.putBoolean("dataCable", dataCable);
        compound.putInt("energyCableIn", energyCable.getIndex());
        return super.write(compound);
    }
}