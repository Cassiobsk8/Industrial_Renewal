package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
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

public class BlockFluidLoader extends BlockTileEntity<TileEntityFluidLoader>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty UNLOAD = IntegerProperty.create("unload", 0, 2);
    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    public BlockFluidLoader()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
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
            } else
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
        //player.openGui(IndustrialRenewal.instance, GUIHandler.FLUIDLOADER, world, pos.getX(), pos.getY(), pos.getZ());
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
        } else
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
        return worldIn.getBlockState(pos).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.up()).getMaterial().isReplaceable();
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


    @Nullable
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

    @Nullable
    @Override
    public TileEntityFluidLoader createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFluidLoader();
    }
}
