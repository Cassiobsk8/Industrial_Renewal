package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalActivate;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockCatwalkGate extends BlockAbstractHorizontalActivate
{
    protected static final VoxelShape RNORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape RSOUTH_AABB = Block.makeCuboidShape(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.makeCuboidShape(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.makeCuboidShape(15, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 24, 1);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0, 0, 15, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 0, 0, 15, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(15, 0, 0, 16, 24, 16);

    public BlockCatwalkGate()
    {
        super(Block.Properties.create(Material.IRON));
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
                worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_CLOSE.get(), SoundCategory.NEUTRAL, 1.0F, pitch);
            } else
            {
                worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_OPEN.get(), SoundCategory.NEUTRAL, 1.0F, pitch);
            }

            state = state.cycle(ACTIVE);
            worldIn.setBlockState(pos, state, 3);
            return ActionResultType.SUCCESS;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing().getOpposite()).with(ACTIVE, false);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        Direction face = state.get(FACING);
        if (face == Direction.NORTH)
        {
            return RNORTH_AABB;
        }
        if (face == Direction.SOUTH)
        {
            return RSOUTH_AABB;
        }
        if (face == Direction.WEST)
        {
            return RWEST_AABB;
        }
        if (face == Direction.EAST)
        {
            return REAST_AABB;
        }
        return RNORTH_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        Boolean open = state.get(ACTIVE);
        VoxelShape FINAL_SHAPE = NONE_AABB;
        Direction face = state.get(FACING);
        if (!open)
        {
            if (face == Direction.NORTH)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
            } else if (face == Direction.SOUTH)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
            } else if (face == Direction.WEST)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            } else
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
            }
        }
        return FINAL_SHAPE;
    }
}
