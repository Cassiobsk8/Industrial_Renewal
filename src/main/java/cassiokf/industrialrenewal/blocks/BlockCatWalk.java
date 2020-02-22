package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityCatWalk;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatWalk extends BlockAbstractSixWayConnections
{
    protected static final VoxelShape BASE_AABB = Block.makeCuboidShape(0, 0, 0, 16, 0.5, 16);

    protected static final VoxelShape RNORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape RSOUTH_AABB = Block.makeCuboidShape(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.makeCuboidShape(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.makeCuboidShape(15.5, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 24, 0.5);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0, 0, 15.5, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 0, 0, 0.5, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(15.5, 0, 0, 16, 24, 16);

    public BlockCatWalk()
    {
        super(Block.Properties.create(Material.IRON).speedFactor(1.2F), 16, 2);
        BlockState defaultState = getDefaultState();
        for (Direction dir : Direction.values())
        {
            defaultState = defaultState.with(getPropertyBasedOnDirection(dir), true);
        }
        setDefaultState(defaultState.with(UP, false));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (handIn == Hand.MAIN_HAND)
        {
            Item playerItem = player.getHeldItem(Hand.MAIN_HAND).getItem();
            if (playerItem.equals(ItemsRegistration.SCREWDRIVE.get()))
            {
                TileEntityCatWalk te = (TileEntityCatWalk) worldIn.getTileEntity(pos);
                if (te != null)
                {
                    te.toggleFacing(hit.getFace());
                    if (!worldIn.isRemote) ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    worldIn.setBlockState(pos, updatePostPlacement(state, hit.getFace(), null, worldIn, pos, null));
                    return ActionResultType.SUCCESS;
                }
            }
            BlockPos posOffset = pos.offset(player.getHorizontalFacing());
            BlockState stateOffset = worldIn.getBlockState(posOffset);
            if (playerItem.equals(BlocksRegistration.CATWALK_ITEM.get())
                    || playerItem.equals(BlocksRegistration.CATWALKSTEEL_ITEM.get()))
            {
                if (hit.getFace() == Direction.UP)
                {
                    if (stateOffset.getMaterial().isReplaceable())
                    {
                        worldIn.setBlockState(pos.offset(player.getHorizontalFacing()), getBlockFromItem(playerItem).getDefaultState(), 3);
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        if (!player.isCreative())
                        {
                            player.getHeldItemMainhand().shrink(1);
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            if (playerItem.equals(BlocksRegistration.CATWALKSTAIR_ITEM.get()) || playerItem.equals(BlocksRegistration.CATWALKSTAIRSTEEL_ITEM.get()))
            {
                if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset))
                {
                    worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().with(BlockCatwalkStair.FACING, player.getHorizontalFacing()), 3);
                    if (!player.isCreative())
                    {
                        player.getHeldItemMainhand().shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    protected boolean isValidConnection(final BlockState neighborState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection)
    {
        TileEntityCatWalk te = (TileEntityCatWalk) world.getTileEntity(ownPos);
        if (te != null && te.isFacingBlackListed(neighborDirection)) return true;

        BlockState downstate = world.getBlockState(ownPos.offset(neighborDirection).down());
        Block nb = neighborState.getBlock();

        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            return nb instanceof BlockCatWalk
                    || nb instanceof DoorBlock
                    || nb instanceof BlockElectricGate
                    || (nb instanceof StairsBlock && (neighborState.get(StairsBlock.FACING) == neighborDirection || neighborState.get(StairsBlock.FACING) == neighborDirection.getOpposite()))
                    || (downstate.getBlock() instanceof StairsBlock && downstate.get(StairsBlock.FACING) == neighborDirection.getOpposite())
                    || (nb instanceof BlockCatwalkHatch && neighborState.get(BlockCatwalkHatch.FACING) == neighborDirection)
                    || (nb instanceof BlockCatwalkGate && neighborState.get(BlockCatwalkGate.FACING) == neighborDirection.getOpposite())
                    || (nb instanceof BlockCatwalkStair && neighborState.get(BlockCatwalkStair.FACING) == neighborDirection)
                    || (downstate.getBlock() instanceof BlockCatwalkStair && downstate.get(BlockCatwalkStair.FACING) == neighborDirection.getOpposite())
                    || (downstate.getBlock() instanceof BlockCatwalkLadder && downstate.get(BlockCatwalkLadder.FACING) == neighborDirection.getOpposite())
                    || (nb instanceof BlockCatwalkLadder && neighborState.get(BlockCatwalkLadder.FACING) == neighborDirection && !neighborState.get(BlockCatwalkLadder.ACTIVE));
        }
        if (neighborDirection == Direction.DOWN)
        {
            return nb instanceof BlockCatwalkLadder
                    || nb instanceof LadderBlock
                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorCable || nb instanceof BlockFloorPipe
                    || nb instanceof BlockCatWalk;
        }
        return !(neighborState.getBlock() instanceof BlockEnergyCable);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = getDefaultState();
        for (Direction direction : Direction.values())
        {
            state = state.with(getPropertyBasedOnDirection(direction), canConnectTo(context.getWorld(), context.getPos(), direction));
        }
        return state;
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        return !isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(getPropertyBasedOnDirection(facing), canConnectTo(worldIn, currentPos, facing));
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        return state.get(getPropertyBasedOnDirection(facing));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        VoxelShape SHAPE = NULL_SHAPE;
        if (isConnected(state, Direction.DOWN))
        {
            SHAPE = VoxelShapes.or(SHAPE, BASE_AABB);
        }
        if (isConnected(state, Direction.NORTH))
        {
            SHAPE = VoxelShapes.or(SHAPE, RNORTH_AABB);
        }
        if (isConnected(state, Direction.SOUTH))
        {
            SHAPE = VoxelShapes.or(SHAPE, RSOUTH_AABB);
        }
        if (isConnected(state, Direction.WEST))
        {
            SHAPE = VoxelShapes.or(SHAPE, RWEST_AABB);
        }
        if (isConnected(state, Direction.EAST))
        {
            SHAPE = VoxelShapes.or(SHAPE, REAST_AABB);
        }
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        VoxelShape SHAPE = NULL_SHAPE;
        if (isConnected(state, Direction.DOWN))
        {
            SHAPE = VoxelShapes.or(SHAPE, BASE_AABB);
        }
        if (isConnected(state, Direction.NORTH))
        {
            SHAPE = VoxelShapes.or(SHAPE, NORTH_AABB);
        }
        if (isConnected(state, Direction.SOUTH))
        {
            SHAPE = VoxelShapes.or(SHAPE, SOUTH_AABB);
        }
        if (isConnected(state, Direction.WEST))
        {
            SHAPE = VoxelShapes.or(SHAPE, WEST_AABB);
        }
        if (isConnected(state, Direction.EAST))
        {
            SHAPE = VoxelShapes.or(SHAPE, EAST_AABB);
        }
        return SHAPE;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityCatWalk createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCatWalk();
    }
}
