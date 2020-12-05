package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.blocks.BlockConcrete;
import cassiokf.industrialrenewal.tileentity.abstracts.TEHorizontalDirection;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class TileEntityDamIntake extends TEHorizontalDirection implements ITickableTileEntity, ICompressedFluidCapability
{
    private static final Map<BlockPos, BlockPos> WALLS = new HashMap<>();
    private final List<BlockPos> connectedWalls = new CopyOnWriteArrayList<>();
    private final List<BlockPos> failBlocks = new CopyOnWriteArrayList<>();
    private final List<BlockPos> failWaters = new CopyOnWriteArrayList<>();
    private int waterAmount = -1;
    private BlockPos neighborPos = null;
    private int concreteAmount;
    private boolean firstTick = false;

    public TileEntityDamIntake(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
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

    public boolean onBlockActivated(PlayerEntity player)
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

    private boolean tryFillDam(PlayerEntity player)
    {
        BlockPos bPos = getNextReplaceableBlockForWater();
        if (bPos != null)
        {
            BlockState state = world.getBlockState(bPos);
            if (state.isReplaceable(world, bPos))
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

    private void sendFailBlockMessage(PlayerEntity player)
    {
        Utils.sendChatMessage(player, "Concrete Amount: " + concreteAmount + " / 143");
        for (BlockPos bPos : failBlocks)
        {
            Block block = world.getBlockState(bPos).getBlock();
            String msg;
            if (block instanceof BlockConcrete)
                msg = "Concrete at " + Utils.blockPosToString(bPos) + " already used by another Intake";
            else
                msg = block.getNameTextComponent().getFormattedText() + " at: " + Utils.blockPosToString(bPos) + " is not a valid dam block";
            Utils.sendChatMessage(player, msg);
            break;
        }
    }

    private void sendFailWaterBlockMessage(PlayerEntity player)
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
            BlockState state = world.getBlockState(bPos);
            if (state.getBlock().isReplaceable(world, bPos))
                return bPos;
        }
        return null;
    }

    @Override
    public boolean canAccept(Direction face, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean canPipeConnect(Direction face, BlockPos pos)
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

            Direction facing = getBlockFacing();

            searchForConcrete(facing);
            searchForWater(facing);
        }
    }

    private void searchForWater(Direction facing)
    {
        failWaters.clear();
        waterAmount = 0;
        for (BlockPos wall : connectedWalls)
        {
            int f = 1;
            while (world.getBlockState(wall.offset(facing, f)).getMaterial() == Material.WATER
                    && world.getBlockState(wall.offset(facing, f)).get(FlowingFluidBlock.LEVEL) == 0
                    && f <= 10)
            {
                f++;
            }
            if (f < 10) failWaters.add(wall.offset(getBlockFacing(), f));
            waterAmount += f - 1;
        }
        waterAmount = (int) (Utils.normalize(waterAmount, 0, 1430) * References.BUCKET_VOLUME);
    }

    private void searchForConcrete(Direction facing)
    {
        failBlocks.clear();
        connectedWalls.add(pos);
        for (int x = -6; x <= 6; x++)
        {
            for (int y = 0; y <= 10; y++)
            {
                BlockPos cPos = (facing == Direction.NORTH || facing == Direction.SOUTH) ?
                        (new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ())) :
                        (new BlockPos(pos.getX(), pos.getY() + y, pos.getZ() + x));

                BlockState state = world.getBlockState(cPos);
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
