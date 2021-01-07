package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockConcrete;
import cassiokf.industrialrenewal.tileentity.abstracts.TEHorizontalDirection;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class TileEntityDamIntake extends TEHorizontalDirection implements ITickable, ICompressedFluidCapability
{
    private static final Map<BlockPos, BlockPos> WALLS = new HashMap<>();
    private final List<BlockPos> connectedWalls = new CopyOnWriteArrayList<>();
    private final List<BlockPos> failBlocks = new CopyOnWriteArrayList<>();
    private final List<BlockPos> failWaters = new CopyOnWriteArrayList<>();
    private int waterAmount = -1;
    private BlockPos neighborPos = null;
    private int concreteAmount;
    private boolean firstTick = false;

    @Override
    public void update()
    {
        if (!firstTick)
        {
            firstTick = true;
            firstTick();
        }
        if (!world.isRemote && getWaterAmount() > 0)
        {
            TileEntity te = world.getTileEntity(getOutPutPos());
            if (te instanceof ICompressedFluidCapability
                    && ((ICompressedFluidCapability) te).canAccept(getBlockFacing().getOpposite(), getOutPutPos()))
            {
                ((ICompressedFluidCapability) te).passCompressedFluid(waterAmount, pos.getY(), false);
            }
        }
    }

    public void firstTick()
    {
        initializeMultiblockIfNecessary(true);
    }

    private BlockPos getOutPutPos()
    {
        if (neighborPos != null) return neighborPos;
        return neighborPos = pos.offset(getBlockFacing().getOpposite());
    }

    public boolean onBlockActivated(EntityPlayer player)
    {
        if (world.isRemote) return true;
        cleanWallCached();
        initializeMultiblockIfNecessary(true);
        int percentage = waterAmount / 10;
        boolean done = false;
        if (percentage < 100 && failWaters.size() > 0 && player.getHeldItemMainhand().getItem() == Items.WATER_BUCKET)
        {
            if (tryFillDam(player)) done = true;
        }
        Utils.sendChatMessage(player, "Efficiency: " + percentage + "%");
        if (done) return true;

        if (percentage < 100)
        {
            if (concreteAmount < 143)
            {
                sendFailBlockMessage(player);
            } else
            {
                sendFailWaterBlockMessage(player);
            }
        }
        return true;
    }

    private boolean tryFillDam(EntityPlayer player)
    {
        BlockPos bPos = getNextReplaceableBlockForWater();
        if (bPos != null)
        {
            IBlockState state = world.getBlockState(bPos);
            if (state.getBlock().isReplaceable(world, bPos))
            {
                world.setBlockState(bPos, Blocks.WATER.getDefaultState());
                failWaters.remove(bPos);

                if (!player.isCreative())
                {
                    player.getHeldItemMainhand().shrink(1);
                    player.addItemStackToInventory(new ItemStack(Items.BUCKET));
                }
                return true;
            }
        }
        return false;
    }

    private void sendFailBlockMessage(EntityPlayer player)
    {
        Utils.sendChatMessage(player, "Concrete Amount: " + concreteAmount + " / 143");
        for (BlockPos bPos : failBlocks)
        {
            Block block = world.getBlockState(bPos).getBlock();
            String msg;
            if (block instanceof BlockConcrete)
                msg = "Concrete at " + Utils.blockPosToString(bPos) + " already used by another Intake";
            else
                msg = block.getLocalizedName() + " at: " + Utils.blockPosToString(bPos) + " is not a valid dam block";
            Utils.sendChatMessage(player, msg);
            break;
        }
    }

    private void sendFailWaterBlockMessage(EntityPlayer player)
    {
        Utils.sendChatMessage(player, "Water Amount: " + waterAmount + " / 1000");
        for (BlockPos bPos : failWaters)
        {
            Utils.sendChatMessage(player, "Block at: " + Utils.blockPosToString(bPos) + " is not a water source");
        }
    }

    private BlockPos getNextReplaceableBlockForWater()
    {
        for (BlockPos bPos : failWaters)
        {
            IBlockState state = world.getBlockState(bPos);
            if (state.getBlock().isReplaceable(world, bPos))
                return bPos;
        }
        return null;
    }

    @Override
    public boolean canAccept(EnumFacing face, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean canPipeConnect(EnumFacing face, BlockPos pos)
    {
        return face.equals(getBlockFacing().getOpposite());
    }

    @Override
    public int passCompressedFluid(int amount, int y, boolean simulate)
    {
        return 0;
    }

    public int getWaterAmount()
    {
        initializeMultiblockIfNecessary(false);
        return waterAmount;
    }

    private void initializeMultiblockIfNecessary(boolean forced)
    {
        if (world.isRemote) return;
        if (waterAmount < 0 || forced)
        {
            cleanWallCached();

            EnumFacing facing = getBlockFacing();

            searchForConcrete(facing);
            searchForWater(facing);
        }
    }

    private void searchForWater(EnumFacing facing)
    {
        failWaters.clear();
        waterAmount = 0;
        for (BlockPos wall : connectedWalls)
        {
            int f = 1;
            while (world.getBlockState(wall.offset(facing, f)).getMaterial() == Material.WATER
                    && world.getBlockState(wall.offset(facing, f)).getValue(BlockFluidBase.LEVEL) == 0
                    && f <= 10)
            {
                f++;
            }
            if (f < 10) failWaters.add(wall.offset(getBlockFacing(), f));
            waterAmount += f - 1;
        }
        waterAmount = (int) (Utils.normalizeClamped(waterAmount, 0, 1430) * Fluid.BUCKET_VOLUME);
    }

    private void searchForConcrete(EnumFacing facing)
    {
        failBlocks.clear();
        connectedWalls.add(pos);
        for (int x = -6; x <= 6; x++)
        {
            for (int y = 0; y <= 10; y++)
            {
                BlockPos cPos = (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) ?
                        (new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ())) :
                        (new BlockPos(pos.getX(), pos.getY() + y, pos.getZ() + x));

                IBlockState state = world.getBlockState(cPos);
                if (state.getBlock() instanceof BlockConcrete && (!WALLS.containsKey(cPos) || WALLS.get(cPos).equals(this.pos)))
                {
                    if (!WALLS.containsKey(cPos)) WALLS.put(cPos, this.pos);
                    connectedWalls.add(cPos);
                } else
                {
                    failBlocks.add(cPos);
                }
            }
        }
        concreteAmount = connectedWalls.size();
    }

    @Override
    public void onBlockBreak()
    {
        cleanWallCached();
        super.onBlockBreak();
    }

    private void cleanWallCached()
    {
        for (BlockPos wall : connectedWalls)
        {
            WALLS.remove(wall);
        }
        connectedWalls.clear();
    }
}
