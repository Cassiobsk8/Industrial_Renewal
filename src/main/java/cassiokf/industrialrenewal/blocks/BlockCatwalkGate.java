package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCatwalkGate extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final AxisAlignedBB RNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB RSOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB RWEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB REAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.5D, 0.03125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 1.5D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 1.5D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D);
    protected static final AxisAlignedBB FRONT_LEFT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.5D, 0.125D);
    protected static final AxisAlignedBB FRONT_RIGHT_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.5D, 0.125D);
    protected static final AxisAlignedBB BACK_LEFT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 0.125D, 1.5D, 1.0D);
    protected static final AxisAlignedBB BACK_RIGHT_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.875D, 1.0D, 1.5D, 1.0D);

    public BlockCatwalkGate(Block.Properties property)
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
            Random r = new Random();
            float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
            if (state.get(ACTIVE))
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F, pitch);
            } else
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F, pitch);
            }

            state = state.cycle(ACTIVE);
            worldIn.setBlockState(pos, state, 3);
            return ActionResultType.SUCCESS;
        }
    }

    public boolean isPassable(IBlockReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).get(ACTIVE);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            Direction face = state.get(FACING);
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
            return RNORTH_AABB;
        }


        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            BlockState actualState = getActualState(state, worldIn, pos);
            Boolean active = actualState.get(ACTIVE);
            if (!active) {
                Direction face = state.get(FACING);
                if (face == Direction.NORTH) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                } else if (face == Direction.SOUTH) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                } else if (face == Direction.WEST) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
                } else if (face == Direction.EAST) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                }
            } else if (active) {
                Direction face = state.get(FACING);
                if (face == Direction.NORTH) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, FRONT_LEFT_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, FRONT_RIGHT_AABB);
                } else if (face == Direction.SOUTH) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, BACK_LEFT_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, BACK_RIGHT_AABB);
                } else if (face == Direction.WEST) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, FRONT_LEFT_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, BACK_LEFT_AABB);
                } else if (face == Direction.EAST) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, FRONT_RIGHT_AABB);
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, BACK_RIGHT_AABB);
                }
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
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing().getOpposite()).with(ACTIVE, false);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
