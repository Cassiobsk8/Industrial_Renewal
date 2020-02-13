package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityBunkerHatch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBunkerHatch extends BlockTileEntity<TileEntityBunkerHatch>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    protected static final AxisAlignedBB RENDER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockBunkerHatch(Block.Properties properties)
    {
        super(properties);
    }


    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        TileEntityBunkerHatch tile = getTileEntity(worldIn, pos);
        tile.changeOpen();
        return ActionResultType.SUCCESS;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        if (state.get(OPEN))
        {
            return 0;
        } else
        {
            return 250;
        }
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state;
    }


    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return worldIn.getBlockState(pos).get(OPEN);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.offset(state.get(FACING)), state.with(MASTER, true));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    if (z != 0 || x != 0)
                        worldIn.setBlockState(currentPos, state.with(MASTER, false));
                }
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntityBunkerHatch te = (TileEntityBunkerHatch) worldIn.getTileEntity(pos);
        if (te != null)
        {
            te.breakMultiBlocks();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }


    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        for (int z = 0; z < 3; z++)
        {
            for (int x = -1; x < 2; x++)
            {
                Direction facing = state.get(FACING);
                BlockPos currentPos = new BlockPos(pos.offset(facing, z).offset(facing.rotateY(), x));
                BlockState currentState = worldIn.getBlockState(currentPos);
                if (!currentState.getBlock().canBeReplacedByLogs(state, worldIn, pos)) return false;
            }
        }
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, OPEN);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing())
                .with(MASTER, false).with(OPEN, false);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
        {
            return RENDER_AABB;
        }


        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
        {
            BlockState actualState = getActualState(state, worldIn, pos);
            Boolean active = actualState.get(OPEN);
            if (active)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NULL_AABB);
            } else
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, RENDER_AABB);
            }
        }
    */
    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntityBunkerHatch createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBunkerHatch();
    }
}
