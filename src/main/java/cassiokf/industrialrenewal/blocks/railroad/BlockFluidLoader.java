package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluidLoader extends BlockHorizontalFacing
{
    public static final IntegerProperty UNLOAD = IntegerProperty.create("unload", 0, 2);
    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    public BlockFluidLoader()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            if (state.get(MASTER))
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityFluidLoader)
                {
                    OpenGUI(worldIn, pos, player);
                }
            }
            else
            {
                TileEntity tileentity = worldIn.getTileEntity(pos.down());
                if (tileentity instanceof TileEntityFluidLoader)
                {
                    OpenGUI(worldIn, pos.down(), player);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    public void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        player.openGui(IndustrialRenewal.instance, GUIHandler.FLUIDLOADER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            worldIn.setBlockState(pos.up(), state.with(MASTER, false));
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        if (state.get(MASTER))
        {
            if (IsLoader(worldIn, pos.up())) worldIn.removeBlock(pos.up(), false);
        }
        else
        {
            if (IsLoader(worldIn, pos.down())) worldIn.removeBlock(pos.down(), false);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private boolean IsLoader(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockFluidLoader;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return isReplaceable(worldIn, pos) && isReplaceable(worldIn, pos.up());
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(UNLOAD, isUnload(worldIn, currentPos, stateIn));
    }

    private int isUnload(IBlockReader world, BlockPos pos, BlockState state)
    {
        if (!state.get(MASTER)) return 0;
        TileEntityFluidLoader te = (TileEntityFluidLoader) world.getTileEntity(pos);
        if (te != null && te.isUnload()) return 2;
        return 1;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(MASTER, true);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, UNLOAD, MASTER);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFluidLoader();
    }
}
