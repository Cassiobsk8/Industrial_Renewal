package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractNotFullCube;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockElectricGate extends BlockAbstractNotFullCube
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");

    protected static final VoxelShape RNORTH_AABB = Block.makeCuboidShape(0, 0, 6, 16, 16, 10);
    protected static final VoxelShape RWEST_AABB = Block.makeCuboidShape(6, 0, 0, 10, 16, 16);

    protected static final VoxelShape CNORTH_AABB = Block.makeCuboidShape(0, 0, 6, 16, 24, 10);
    protected static final VoxelShape CWEST_AABB = Block.makeCuboidShape(6, 0, 0, 10, 24, 16);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(-14, 0, 6, 2, 24, 10);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(14, 0, 6, 30, 24, 10);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(6, 0, 14, 10, 24, 30);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(6, 0, -14, 10, 24, 2);
    protected static final VoxelShape INORTH_AABB = Block.makeCuboidShape(14, 0, 6, 30, 24, 10);
    protected static final VoxelShape ISOUTH_AABB = Block.makeCuboidShape(-14, 0, 6, 2, 24, 10);
    protected static final VoxelShape IWEST_AABB = Block.makeCuboidShape(6, 0, -14, 10, 24, 2);
    protected static final VoxelShape IEAST_AABB = Block.makeCuboidShape(6, 0, 14, 10, 24, 30);

    public BlockElectricGate()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        }

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
            ((BlockElectricGate) rightState.getBlock()).OpenUpAndDown(worldIn, rightState, rightPos, active);
        } else if (inverted && leftState.getBlock() instanceof BlockElectricGate && !leftState.get(INVERTED))
        {
            ((BlockElectricGate) leftState.getBlock()).OpenUpAndDown(worldIn, leftState, leftPos, active);
        }

        //Sound
        Random r = new Random();
        float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
        if (active)
        {
            worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_OPEN.get(), SoundCategory.BLOCKS, 1.0F, pitch);
        } else
        {
            worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_CLOSE.get(), SoundCategory.BLOCKS, 1.0F, pitch);
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
        world.setBlockState(pos, state);
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
            world.setBlockState(pos.up(n), thisState);
            n++;
        }
    }

    public void OpenDown(World world, BlockPos pos, boolean active)
    {
        int n = 1;
        while (world.getBlockState(pos.down(n)).getBlock() instanceof BlockElectricGate)
        {
            BlockState thisState = world.getBlockState(pos.down(n)).with(ACTIVE, active);
            world.setBlockState(pos.down(n), thisState);
            n++;
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return getFullState(stateIn, worldIn, currentPos);
    }

    private BlockState getFullState(BlockState stateIn, IWorld worldIn, BlockPos currentPos)
    {
        Direction face = stateIn.get(FACING);
        BlockState rightState = worldIn.getBlockState(currentPos.offset(face.rotateY()));
        BlockState leftState = worldIn.getBlockState(currentPos.offset(face.rotateYCCW()));

        boolean leftIsGate = (leftState.getBlock() instanceof BlockElectricGate);
        boolean rightIsGate = (rightState.getBlock() instanceof BlockElectricGate);
        boolean inverted = (leftIsGate && !rightIsGate);

        Block dnb = worldIn.getBlockState(currentPos.down()).getBlock();
        Block upb = worldIn.getBlockState(currentPos.up()).getBlock();

        boolean isTop = (dnb instanceof BlockElectricGate) && !(upb instanceof BlockElectricGate);

        stateIn = stateIn.with(UP, isTop).with(INVERTED, inverted)
                .with(LEFT, !inverted)
                .with(RIGHT, !rightIsGate);

        return stateIn;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return worldIn.getBlockState(pos).get(ACTIVE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, false);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, true);
    }

    private VoxelShape getVoxelShape(BlockState state, boolean collision)
    {
        Direction face = state.get(FACING);

        if (!collision)
        {
            if (face == Direction.NORTH || face == Direction.SOUTH)
            {
                return RNORTH_AABB;
            }
            return RWEST_AABB;
        }

        boolean active = state.get(ACTIVE);
        boolean inverted = state.get(INVERTED);
        VoxelShape FINAL_SHAPE = NULL_SHAPE;

        if (active)
        {
            if (face == Direction.NORTH)
            {
                if (inverted)
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, INORTH_AABB);
                } else
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                }
            } else if (face == Direction.SOUTH)
            {
                if (inverted)
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, ISOUTH_AABB);
                } else
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                }
            } else if (face == Direction.WEST)
            {
                if (inverted)
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, IWEST_AABB);
                } else
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
                }
            } else if (face == Direction.EAST)
            {
                if (inverted)
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, IEAST_AABB);
                } else
                {
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
                }
            }
        } else
        {
            if (face == Direction.NORTH || face == Direction.SOUTH)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, CNORTH_AABB);
            } else if (face == Direction.WEST || face == Direction.EAST)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, CWEST_AABB);
            }
        }
        return FINAL_SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, UP, LEFT, RIGHT, INVERTED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = getFullState(getDefaultState(), context.getWorld(), context.getPos());
        return state.with(FACING, context.getPlayer().getHorizontalFacing()).with(ACTIVE, false);
    }
}
