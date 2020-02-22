package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityCatWalkStair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalkStair extends BlockAbstractHorizontalFacing
{
    public static final BooleanProperty ACTIVE_LEFT = BooleanProperty.create("active_left");
    public static final BooleanProperty ACTIVE_RIGHT = BooleanProperty.create("active_right");

    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(0, 0, 0, 16, 8, 16);

    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0, 8, 0, 16, 16, 8);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0, 8, 8, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 8, 0, 8, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(8, 8, 0, 16, 16, 16);

    protected static final VoxelShape NC_AABB = Block.makeCuboidShape(0, 0, 0, 16, 32, 0.5);
    protected static final VoxelShape SC_AABB = Block.makeCuboidShape(0, 0, 15.5, 16, 32, 16);
    protected static final VoxelShape WC_AABB = Block.makeCuboidShape(0, 0, 0, 0.5, 32, 16);
    protected static final VoxelShape EC_AABB = Block.makeCuboidShape(15.5, 0, 0, 16, 32, 16);

    protected static final VoxelShape RNC_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape RSC_AABB = Block.makeCuboidShape(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape RWC_AABB = Block.makeCuboidShape(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape REC_AABB = Block.makeCuboidShape(15.5, 0, 0, 16, 16, 16);

    public BlockCatwalkStair()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (handIn.equals(Hand.MAIN_HAND))
        {
            Item playerItem = player.getHeldItemMainhand().getItem();
            if (playerItem.equals(ItemsRegistration.SCREWDRIVE.get()))
            {
                Direction face = state.get(FACING);
                TileEntityCatWalkStair te = (TileEntityCatWalkStair) worldIn.getTileEntity(pos);
                if (te != null && (hit.getFace().equals(face.rotateY()) || hit.getFace().equals(face.rotateYCCW())))
                {
                    te.toggleFacing(hit.getFace());
                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    worldIn.setBlockState(pos, updatePostPlacement(state, hit.getFace(), null, worldIn, pos, null));
                    return ActionResultType.SUCCESS;
                }
            }
            if (playerItem.equals(BlocksRegistration.CATWALKSTAIR_ITEM.get()) || playerItem.equals(BlocksRegistration.CATWALKSTAIRSTEEL_ITEM.get()))
            {
                BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
                BlockState stateOffset = worldIn.getBlockState(posOffset);
                if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset) || stateOffset.getMaterial().isReplaceable())
                {
                    worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().with(BlockCatwalkStair.FACING, player.getHorizontalFacing()));
                    if (!player.isCreative())
                    {
                        player.getHeldItemMainhand().shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
            if (playerItem.equals(BlocksRegistration.CATWALK_ITEM.get()) || playerItem.equals(BlocksRegistration.CATWALKSTEEL_ITEM.get()))
            {
                BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
                BlockState stateOffset = worldIn.getBlockState(posOffset);
                if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset) || stateOffset.getMaterial().isReplaceable())
                {
                    worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState());
                    if (!player.isCreative())
                    {
                        player.getHeldItemMainhand().shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
        }
        return ActionResultType.PASS;
    }

    private Boolean sideConnected(BlockState state, IBlockReader world, BlockPos pos, Direction direction)
    {
        Direction face = state.get(FACING);
        TileEntityCatWalkStair te = (TileEntityCatWalkStair) world.getTileEntity(pos);
        if (te != null && te.isFacingBlackListed(direction)) return false;

        BlockPos posOffset = pos.offset(direction);
        BlockState stateOffset = world.getBlockState(posOffset);

        if (stateOffset.getBlock() instanceof BlockCatwalkStair)
        {
            Direction sideStairFace = stateOffset.get(FACING);
            return !(sideStairFace == face);
        }
        return true;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        Direction direction = stateIn.get(FACING);
        if (facing.equals(direction.rotateY()))
            stateIn = stateIn.with(ACTIVE_RIGHT, sideConnected(stateIn, worldIn, currentPos, direction.rotateY()));
        else if (facing.equals(direction.rotateYCCW()))
            stateIn = stateIn.with(ACTIVE_LEFT, sideConnected(stateIn, worldIn, currentPos, direction.rotateYCCW()));

        return stateIn;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE_LEFT, ACTIVE_RIGHT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction facing = context.getPlayer().getHorizontalFacing();
        BlockState state = getDefaultState().with(FACING, facing);
        return state.with(ACTIVE_LEFT, sideConnected(state, context.getWorld(), context.getPos(), facing.rotateYCCW()))
                .with(ACTIVE_RIGHT, sideConnected(state, context.getWorld(), context.getPos(), facing.rotateY()));
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, true);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, false);
    }

    private VoxelShape getVoxelShape(BlockState state, boolean isForRender)
    {
        VoxelShape FINAL_SHAPE = BASE_AABB;

        Direction face = state.get(FACING);
        Boolean left = state.get(ACTIVE_LEFT);
        Boolean right = state.get(ACTIVE_RIGHT);
        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
            if (left)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RWC_AABB : WC_AABB);
            }
            if (right)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? REC_AABB : EC_AABB);
            }

        }
        if (face == Direction.SOUTH) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
            if (left) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? REC_AABB : EC_AABB);
            }
            if (right) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RWC_AABB : WC_AABB);
            }
        }
        if (face == Direction.WEST) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            if (left) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RSC_AABB : SC_AABB);
            }
            if (right) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RNC_AABB : NC_AABB);
            }
        }
        if (face == Direction.EAST) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
            if (left)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RNC_AABB : NC_AABB);
            }
            if (right)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RSC_AABB : SC_AABB);
            }
        }
        return FINAL_SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityCatWalkStair createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCatWalkStair();
    }
}
