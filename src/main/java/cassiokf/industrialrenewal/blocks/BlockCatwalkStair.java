package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityCatWalkStair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
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
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalkStair extends BlockTileEntity<TileEntityCatWalkStair>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE_LEFT = BooleanProperty.create("active_left");
    public static final BooleanProperty ACTIVE_RIGHT = BooleanProperty.create("active_right");
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 0.03125D);
    protected static final AxisAlignedBB SC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 2.0D, 1.0D);
    protected static final AxisAlignedBB WC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 2.0D, 1.0D);
    protected static final AxisAlignedBB EC_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);

    public BlockCatwalkStair(Block.Properties property)
    {
        super(property);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (handIn.equals(Hand.MAIN_HAND))
        {
            Item playerItem = player.inventory.getCurrentItem().getItem();
            if (playerItem.equals(ModItems.screwDrive))
            {
                TileEntityCatWalkStair te = (TileEntityCatWalkStair) worldIn.getTileEntity(pos);
                if (te != null)
                {
                    te.toggleFacing(p_225533_6_.getFace());
                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    worldIn.notifyBlockUpdate(pos, state, state, 2);
                }
            }
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catwalkStair)) || playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catwalkStairSteel)))
            {
                BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
                BlockState stateOffset = worldIn.getBlockState(posOffset);
                if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset) || stateOffset.getMaterial().isReplaceable())
                {
                    worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().with(BlockCatwalkStair.FACING, player.getHorizontalFacing()), 3);
                    if (!player.isCreative())
                    {
                        player.inventory.getCurrentItem().shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catWalk)) || playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catWalkSteel)))
            {
                BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
                BlockState stateOffset = worldIn.getBlockState(posOffset);
                if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset) || stateOffset.getMaterial().isReplaceable())
                {
                    worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState(), 3);
                    if (!player.isCreative())
                    {
                        player.inventory.getCurrentItem().shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
        }
        return ActionResultType.PASS;
    }

    private Boolean leftConnected(BlockState state, IBlockReader world, BlockPos pos)
    {
        Direction face = state.get(FACING);
        TileEntityCatWalkStair te = (TileEntityCatWalkStair) world.getTileEntity(pos);
        if (te != null && te.isFacingBlackListed(face.rotateYCCW())) return false;

        BlockPos posOffset = pos.offset(face.rotateY().getOpposite());
        BlockState stateOffset = world.getBlockState(posOffset);

        if (stateOffset.getBlock() instanceof BlockCatwalkStair)
        {
            Direction leftFace = stateOffset.get(FACING);
            return !(leftFace == face);
        }
        return true;
    }

    private Boolean rightConnected(BlockState state, IBlockReader world, BlockPos pos)
    {
        Direction face = state.get(FACING);
        TileEntityCatWalkStair te = (TileEntityCatWalkStair) world.getTileEntity(pos);
        if (te != null && te.isFacingBlackListed(face.rotateY())) return false;

        BlockPos posOffset = pos.offset(face.rotateY());
        BlockState stateOffset = world.getBlockState(posOffset);

        if (stateOffset.getBlock() instanceof BlockCatwalkStair)
        {
            Direction rightFace = stateOffset.get(FACING);
            return !(rightFace == face);
        }
        return true;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        stateIn = stateIn.with(ACTIVE_LEFT, leftConnected(stateIn, worldIn, currentPos)).with(ACTIVE_RIGHT, rightConnected(stateIn, worldIn, currentPos));
        return stateIn;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE_LEFT, ACTIVE_RIGHT);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

/*
    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        BlockState actualState = getActualState(state, worldIn, pos);

        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);

        Direction face = actualState.get(FACING);
        Boolean left = actualState.get(ACTIVE_LEFT);
        Boolean right = actualState.get(ACTIVE_RIGHT);
        if (face == Direction.NORTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }

        }
        if (face == Direction.SOUTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
        }
        if (face == Direction.WEST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
        }
        if (face == Direction.EAST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
        }
    }
*/

    @Nullable
    @Override
    public TileEntityCatWalkStair createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCatWalkStair();
    }
}
