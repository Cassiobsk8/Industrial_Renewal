package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockCargoLoader extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    public BlockCargoLoader()
    {
        super(Block.Properties.create(Material.IRON));
    }

    public static BlockPos getMasterPos(IBlockReader world, BlockPos pos, Direction facing)
    {
        for (int y = -2; y < 3; y++)
        {
            BlockPos newPos = pos.up(y);
            BlockPos newPosFront = pos.offset(facing).up(y);
            BlockPos newPosBack = pos.offset(facing.getOpposite()).up(y);
            if (world.getBlockState(newPos).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPos).get(MASTER)) return newPos;
            }
            if (world.getBlockState(newPosFront).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosFront).get(MASTER)) return newPosFront;
            }
            if (world.getBlockState(newPosBack).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosBack).get(MASTER)) return newPosBack;
            }
        }
        return null;
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        //player.openGui(IndustrialRenewal.instance, GUIHandler.CARGOLOADER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (!worldIn.isRemote && handIn.equals(Hand.MAIN_HAND))
        {
            if (state.get(MASTER))
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityCargoLoader)
                {
                    OpenGUI(worldIn, pos, player);
                }
            } else
            {
                BlockPos masterPos = getMasterPos(worldIn, pos, state.get(FACING));
                if (masterPos != null)
                {
                    TileEntity tileentity = worldIn.getTileEntity(masterPos);
                    if (tileentity instanceof TileEntityCargoLoader)
                    {
                        OpenGUI(worldIn, masterPos, player);
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        Direction facing = state.get(FACING);
        if (state.get(MASTER))
        {
            for (BlockPos pos1 : getBlocks(pos, facing))
            {
                worldIn.removeBlock(pos1, false);
            }
        } else
        {
            BlockPos masterPos = getMasterPos(worldIn, pos, facing);
            if (masterPos != null)
            {
                worldIn.removeBlock(masterPos, false);
                for (BlockPos pos1 : getBlocks(masterPos, facing))
                {
                    if (pos1 != pos) worldIn.removeBlock(pos1, false);
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private Set<BlockPos> getBlocks(BlockPos posMaster, Direction facing)
    {
        Set<BlockPos> positions = new HashSet<>();
        positions.add(posMaster.down());
        positions.add(posMaster.down().offset(facing));
        positions.add(posMaster.up());
        positions.add(posMaster.up().offset(facing));
        return positions;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(MASTER, false);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.up(), state.with(MASTER, true));
    }


    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            Direction facing = state.get(FACING);
            worldIn.setBlockState(pos.up(), state.with(MASTER, false));
            worldIn.setBlockState(pos.up().offset(facing), state.with(MASTER, false));
            worldIn.setBlockState(pos.down(), state.with(MASTER, false));
            worldIn.setBlockState(pos.down().offset(facing), state.with(MASTER, false));
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        PlayerEntity player = worldIn.getDimension().getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        if (!worldIn.getBlockState(pos).getMaterial().isReplaceable()) return false;
        if (!worldIn.getBlockState(pos.up()).getMaterial().isReplaceable()) return false;
        if (!worldIn.getBlockState(pos.up(2)).getMaterial().isReplaceable()) return false;
        if (!worldIn.getBlockState(pos.up(2).offset(player.getHorizontalFacing())).getMaterial().isReplaceable())
            return false;
        return worldIn.getBlockState(pos.offset(player.getHorizontalFacing())).getMaterial().isReplaceable();
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityCargoLoader createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCargoLoader();
    }
}
