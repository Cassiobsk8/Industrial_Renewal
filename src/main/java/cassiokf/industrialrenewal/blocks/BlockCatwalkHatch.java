package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalkHatch extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final AxisAlignedBB RNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB RSOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB RWEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
    protected static final AxisAlignedBB REAST_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected static final AxisAlignedBB RDOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 0.0625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 0.125D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    protected static final AxisAlignedBB OPEN_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB OPEN_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB OPEN_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB OPEN_EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockCatwalkHatch(Block.Properties property)
    {
        super(property);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        } else
        {
            if (state.get(ACTIVE))
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            } else
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            }

            state = state.cycle(ACTIVE);
            worldIn.setBlockState(pos, state, 3);
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        if (state.get(ACTIVE))
        {
            return 0;
        } else
        {
            return 250;
        }
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return worldIn.getBlockState(pos).get(ACTIVE);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return world.getBlockState(pos).get(ACTIVE);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            Direction face = state.get(FACING);
            Boolean active = state.get(ACTIVE);
            if (active) {
                if (face == Direction.NORTH) {
                    return RNORTH_AABB;
                }
                if (face == Direction.SOUTH) {
                    return RSOUTH_AABB;
                }
                if (face == Direction.WEST) {
                    return RWEST_AABB;
                }
                if (face == Direction.EAST) {
                    return REAST_AABB;
                }
            } else {
                return RDOWN_AABB;
            }
            return RNORTH_AABB;
        }


        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            BlockState actualState = getActualState(state, worldIn, pos);
            Boolean active = actualState.get(ACTIVE);
            if (active) {
                Direction face = state.get(FACING);
                if (face == Direction.NORTH) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_NORTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
                } else if (face == Direction.SOUTH) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_SOUTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
                } else if (face == Direction.WEST) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_WEST_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                } else if (face == Direction.EAST) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, OPEN_EAST_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
                }
            } else {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, RDOWN_AABB);
            }
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(ACTIVE, false);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
