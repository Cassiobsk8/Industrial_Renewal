package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalkLadder extends BlockBase
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.03125D, 1.0D);
    protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.03125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockCatwalkLadder(Block.Properties property)
    {
        super(property);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        Item playerItem = player.inventory.getCurrentItem().getItem();
        if (playerItem.equals(ModItems.screwDrive))
        {
            worldIn.playSound(null, pos, IRSoundRegister.ITEM_DRILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            worldIn.setBlockState(pos, state.with(ACTIVE, !state.get(ACTIVE)), 3);
            return ActionResultType.SUCCESS;
        }
        if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.iladder)) || playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.sladder)))
        {
            BlockPos posOffset = pos.up();
            BlockState stateOffset = worldIn.getBlockState(posOffset);
            if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset) || stateOffset.getMaterial().isReplaceable())
            {
                Direction direction = state.get(FACING);
                worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().with(FACING, direction).with(ACTIVE, !OpenIf(worldIn, posOffset)), 3);
                if (!player.isCreative())
                {

                    player.inventory.getCurrentItem().shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean downConnection(BlockPos pos, IBlockReader world)
    {
        Block downB = world.getBlockState(pos.down()).getBlock();
        return !(downB instanceof LadderBlock || downB instanceof BlockCatwalkLadder || downB instanceof BlockCatwalkHatch
                || downB instanceof BlockCatwalkStair || downB instanceof StairsBlock || downB instanceof TrapDoorBlock);
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        stateIn = stateIn.with(DOWN, downConnection(currentPos, worldIn));
        return stateIn;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, DOWN);
    }

    protected boolean OpenIf(final IBlockReader WorldIn, BlockPos ownPos)
    {
        final BlockPos downpos = ownPos.down();
        final BlockPos twoDownPos = downpos.down();
        final BlockState downState = WorldIn.getBlockState(downpos);
        final BlockState twoDownState = WorldIn.getBlockState(twoDownPos);
        return downState.isSolid() || (downState.getBlock() instanceof BlockCatwalkLadder && !downState.get(ACTIVE) && twoDownState.isSolid());
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(ACTIVE, !OpenIf(context.getWorld(), context.getPos()));
    }
/*
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        switch (state.get(FACING)) {
            case NORTH:
                return LADDER_SOUTH_AABB;
            case SOUTH:
                return LADDER_NORTH_AABB;
            case WEST:
                return LADDER_EAST_AABB;
            case EAST:
            default:
                return LADDER_WEST_AABB;
        }
    }


    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        BlockState actualState = state.getActualState(worldIn, pos);
        Direction face = actualState.get(FACING);
        boolean active = actualState.get(ACTIVE);
        boolean down = actualState.get(DOWN);

        if (face == Direction.NORTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_SOUTH_AABB);
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
        if (face == Direction.SOUTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_NORTH_AABB);
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
        if (face == Direction.WEST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_EAST_AABB);
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }
        }
        if (face == Direction.EAST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_WEST_AABB);
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
        if (down) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
        }
    }
*/

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
