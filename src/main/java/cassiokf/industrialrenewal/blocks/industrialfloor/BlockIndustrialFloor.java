package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.blocks.BlockAbstractSixWayConnections;
import cassiokf.industrialrenewal.blocks.BlockCatwalkHatch;
import cassiokf.industrialrenewal.blocks.BlockCatwalkLadder;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;


public class BlockIndustrialFloor extends BlockAbstractSixWayConnections
{
    protected static final VoxelShape NONE_AABB = Block.makeCuboidShape(6, 6, 6, 10, 10, 10);

    protected static final VoxelShape C_UP_AABB = Block.makeCuboidShape(0, 15, 0, 16, 16, 16);
    protected static final VoxelShape C_DOWN_AABB = Block.makeCuboidShape(0, 0, 0, 16, 1, 16);
    protected static final VoxelShape C_NORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape C_SOUTH_AABB = Block.makeCuboidShape(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape C_WEST_AABB = Block.makeCuboidShape(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape C_EAST_AABB = Block.makeCuboidShape(15, 0, 0, 16, 16, 16);

    public BlockIndustrialFloor()
    {
        super(Block.Properties.create(Material.IRON), 16, 16);
    }

    private static boolean isValidConnection(final BlockState neighborState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();
        return nb instanceof BlockIndustrialFloor
                || nb instanceof BlockFloorPipe
                || nb instanceof BlockFloorCable
                || (nb instanceof DoorBlock && neighborState.get(DoorBlock.FACING).equals(neighborDirection))
                || (neighborDirection.equals(Direction.DOWN) && nb instanceof BlockCatwalkLadder)
                || (neighborDirection.equals(Direction.UP) && nb instanceof BlockCatwalkHatch)
                //start check for horizontal Iladder
                || ((neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
                && nb instanceof BlockCatwalkLadder && !neighborState.get(BlockCatwalkLadder.ACTIVE))
                //end
                ;
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        return !isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return VoxelShapes.or(getVoxelShape(state), NONE_AABB);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        VoxelShape FINAL_SHAPE = NULL_SHAPE;
        if (isConnected(state, UP))
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_UP_AABB);
        }
        if (isConnected(state, DOWN))
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_DOWN_AABB);
        }
        if (isConnected(state, NORTH))
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_NORTH_AABB);
        }
        if (isConnected(state, SOUTH))
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_SOUTH_AABB);
        }
        if (isConnected(state, WEST))
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_WEST_AABB);
        }
        if (isConnected(state, EAST))
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_EAST_AABB);
        }
        return FINAL_SHAPE;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
        return false;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        ItemStack playerStack = player.getHeldItem(Hand.MAIN_HAND);
        Item playerItem = playerStack.getItem();

        if (playerItem.equals(BlocksRegistration.FLUIDPIPE_ITEM.get()))
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlockState(pos, BlocksRegistration.FLOORPIPE.get().getDefaultState(), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if (playerItem.equals(BlocksRegistration.ENERGYCABLEMV_ITEM.get())
                || playerItem.equals(BlocksRegistration.ENERGYCABLELV_ITEM.get())
                || playerItem.equals(BlocksRegistration.ENERGYCABLEHV_ITEM.get()))
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            Block block;
            if (playerItem.equals(BlocksRegistration.ENERGYCABLEMV_ITEM.get()))
                block = BlocksRegistration.FLOORCABLEMV.get();
            else if (playerItem.equals(BlocksRegistration.ENERGYCABLELV_ITEM.get()))
                block = BlocksRegistration.FLOORCABLELV.get();
            else block = BlocksRegistration.FLOORCABLEHV.get();
            worldIn.setBlockState(pos, block.getDefaultState(), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if (playerItem.equals(BlocksRegistration.FLUORESCENT_ITEM.get()))
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            worldIn.setBlockState(pos, BlocksRegistration.FLOORLAMP.get().getDefaultState(), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}