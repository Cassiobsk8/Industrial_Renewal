package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockElectricGate extends BlockBase
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");

    protected static final AxisAlignedBB RNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB RWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);

    protected static final AxisAlignedBB CNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
    protected static final AxisAlignedBB CWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(-0.875D, 0.0D, 0.375D, 0.125D, 1.5D, 0.625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.875, 0.0D, 0.375D, 1.875D, 1.5D, 0.625D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.875D, 0.625D, 1.5D, 1.875D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.375D, 0.0D, -0.875, 0.625D, 1.5D, 0.125D);

    protected static final AxisAlignedBB INORTH_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.375D, 1.875D, 1.5D, 0.625D);
    protected static final AxisAlignedBB ISOUTH_AABB = new AxisAlignedBB(-0.875D, 0.0D, 0.375D, 0.125D, 1.5D, 0.625D);
    protected static final AxisAlignedBB IWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, -0.875, 0.625D, 1.5D, 0.125D);
    protected static final AxisAlignedBB IEAST_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.875D, 0.625D, 1.5D, 1.875D);

    public BlockElectricGate(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        } else
        {
            boolean active = !state.get(ACTIVE);

            OpenUpAndDown(worldIn, state, pos, active);

            Direction facing = state.get(FACING);
            BlockPos rightPos = pos.offset(facing.rotateY());
            BlockPos leftPos = pos.offset(facing.rotateYCCW());
            BlockState rightState = worldIn.getBlockState(rightPos);
            BlockState leftState = worldIn.getBlockState(leftPos);
            boolean inverted = state.get(INVERTED);

            if (!inverted && rightState.getBlock() instanceof BlockElectricGate && rightState.get(INVERTED))
            {
                ((BlockElectricGate) rightState.getBlock()).OpenUpAndDown(worldIn, state, rightPos, active);
            } else if (inverted && leftState.getBlock() instanceof BlockElectricGate && !leftState.get(INVERTED))
            {
                ((BlockElectricGate) leftState.getBlock()).OpenUpAndDown(worldIn, state, leftPos, active);
            }

            //Sound
            Random r = new Random();
            float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
            if (active)
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F, pitch);
            } else
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F, pitch);
            }
        }
        return ActionResultType.SUCCESS;
    }

    public void OpenUpAndDown(World world, BlockState state, BlockPos pos, boolean active)
    {

        BlockState upstate = world.getBlockState(pos.up());
        BlockState dnstate = world.getBlockState(pos.down());
        Block upb = upstate.getBlock();
        Block dnb = dnstate.getBlock();

        state = state.with(ACTIVE, active);
        world.setBlockState(pos, state, 3);
        if (upb instanceof BlockElectricGate)
        {
            OpenUp(world, pos, active);
        }
        if (dnb instanceof BlockElectricGate)
        {
            OpenDown(world, pos, active);
        }
    }

    public void OpenUp(World world, BlockPos pos, boolean active)
    {
        int n = 1;
        while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockElectricGate)
        {
            BlockState thisState = world.getBlockState(pos.up(n)).with(ACTIVE, active);
            world.setBlockState(pos.up(n), thisState, 3);
            n++;
        }
    }

    public void OpenDown(World world, BlockPos pos, boolean active)
    {
        int n = 1;
        while (world.getBlockState(pos.down(n)).getBlock() instanceof BlockElectricGate)
        {
            BlockState thisState = world.getBlockState(pos.down(n)).with(ACTIVE, active);
            world.setBlockState(pos.down(n), thisState, 3);
            n++;
        }
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        Direction face = stateIn.get(FACING);
        BlockState rightState = worldIn.getBlockState(currentPos.offset(face.rotateY()));
        BlockState leftState = worldIn.getBlockState(currentPos.offset(face.rotateYCCW()));
        Block leftBlock = leftState.getBlock();
        Block rightBlock = rightState.getBlock();
        boolean leftIsGate = (leftBlock instanceof BlockElectricGate);
        boolean rightIsGate = (rightBlock instanceof BlockElectricGate);
        boolean inverted = (leftIsGate && !rightIsGate);
        boolean rightInverted = rightIsGate;
        Block dnb = worldIn.getBlockState(currentPos.down()).getBlock();
        Block upb = worldIn.getBlockState(currentPos.up()).getBlock();
        boolean isTop = (dnb instanceof BlockElectricGate) && !(upb instanceof BlockElectricGate);

        stateIn = stateIn.with(UP, isTop).with(INVERTED, inverted)
                .with(LEFT, !inverted)
                .with(RIGHT, !rightInverted);

        return stateIn;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return worldIn.getBlockState(pos).get(ACTIVE);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
        {
            Direction face = state.get(FACING);
            if (face == Direction.NORTH || face == Direction.SOUTH)
            {
                return RNORTH_AABB;
            } else
            {
                return RWEST_AABB;
            }
        }


        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
        {
            BlockState actualState = getActualState(state, worldIn, pos);
            boolean active = actualState.get(ACTIVE);
            Direction face = state.get(FACING);
            boolean inverted = actualState.get(INVERTED);
            if (active)
            {
                if (face == Direction.NORTH)
                {
                    if (inverted)
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, INORTH_AABB);
                    } else
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                    }
                } else if (face == Direction.SOUTH)
                {
                    if (inverted)
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, ISOUTH_AABB);
                    } else
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                    }
                } else if (face == Direction.WEST)
                {
                    if (inverted)
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, IWEST_AABB);
                    } else
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
                    }
                } else if (face == Direction.EAST)
                {
                    if (inverted)
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, IEAST_AABB);
                    } else
                    {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                    }
                }
            } else
            {
                if (face == Direction.NORTH || face == Direction.SOUTH)
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, CNORTH_AABB);
                } else if (face == Direction.WEST || face == Direction.EAST)
                {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, CWEST_AABB);
                }
            }

        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, UP, LEFT, RIGHT, INVERTED);
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
