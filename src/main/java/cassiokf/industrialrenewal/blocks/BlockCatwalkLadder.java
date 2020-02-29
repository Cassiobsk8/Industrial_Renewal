package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacingWithActivating;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalkLadder extends BlockAbstractHorizontalFacingWithActivating
{
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0, 0, 0, 16, 0.5, 16);
    protected static final VoxelShape LADDER_EAST_AABB = Block.makeCuboidShape(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape LADDER_WEST_AABB = Block.makeCuboidShape(15, 0, 0, 16, 16, 16);
    protected static final VoxelShape LADDER_SOUTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape LADDER_NORTH_AABB = Block.makeCuboidShape(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(15.5, 0, 0, 16, 16, 16);

    public BlockCatwalkLadder()
    {
        super(Block.Properties.create(Material.IRON));
        setDefaultState(getDefaultState().with(DOWN, false).with(ACTIVE, true));
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (handIn.equals(Hand.MAIN_HAND))
        {
            Item playerItem = player.inventory.getCurrentItem().getItem();
            if (playerItem.equals(ItemsRegistration.SCREWDRIVE.get()))
            {
                ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                worldIn.setBlockState(pos, state.with(ACTIVE, !state.get(ACTIVE)));
                return ActionResultType.SUCCESS;
            }
            if (playerItem.equals(BlocksRegistration.ILADDER_ITEM.get()) || playerItem.equals(BlocksRegistration.SLADDER_ITEM.get()))
            {
                BlockPos posOffset = pos.up();
                BlockState stateOffset = worldIn.getBlockState(posOffset);
                if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset) || stateOffset.getMaterial().isReplaceable())
                {
                    Direction direction = state.get(FACING);
                    worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().with(FACING, direction).with(ACTIVE, OpenCageIf(worldIn, posOffset)));
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!player.isCreative())
                    {
                        player.inventory.getCurrentItem().shrink(1);
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    private boolean downConnection(BlockPos pos, IBlockReader world)
    {
        Block downB = world.getBlockState(pos.down()).getBlock();
        return !(downB instanceof LadderBlock
                || downB instanceof BlockCatwalkLadder
                || downB instanceof BlockCatwalkHatch
                || downB instanceof BlockCatwalkStair
                || downB instanceof StairsBlock
                || downB instanceof TrapDoorBlock);
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing == Direction.DOWN) stateIn = stateIn.with(DOWN, downConnection(currentPos, worldIn));
        return stateIn;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, DOWN);
    }

    protected boolean OpenCageIf(final IBlockReader worldIn, BlockPos ownPos)
    {
        final BlockPos downpos = ownPos.down();
        final BlockPos twoDownPos = downpos.down();
        final BlockState downState = worldIn.getBlockState(downpos);
        final BlockState twoDownState = worldIn.getBlockState(twoDownPos);
        return !downState.isSolidSide(worldIn, downpos, Direction.UP)
                && (!(downState.getBlock() instanceof BlockCatwalkLadder)
                || downState.get(ACTIVE)
                || !twoDownState.isSolidSide(worldIn, twoDownPos, Direction.UP));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState()
                .with(FACING, context.getPlayer().getHorizontalFacing())
                .with(ACTIVE, OpenCageIf(context.getWorld(), context.getPos()))
                .with(DOWN, downConnection(context.getPos(), context.getWorld()));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        Direction face = state.get(FACING);
        boolean active = state.get(ACTIVE);
        boolean down = state.get(DOWN);

        VoxelShape FINAL_SHAPE = NULL_SHAPE;

        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_SOUTH_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        if (face == Direction.SOUTH)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_NORTH_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        if (face == Direction.WEST)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_EAST_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
            }
        } else
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_WEST_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        if (down)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, DOWN_AABB);
        }
        return FINAL_SHAPE;
    }
}
