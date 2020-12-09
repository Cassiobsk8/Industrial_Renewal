package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.blocks.BlockBase;
import cassiokf.industrialrenewal.blocks.BlockCatwalkHatch;
import cassiokf.industrialrenewal.blocks.BlockCatwalkLadder;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;


public class BlockIndustrialFloor extends BlockBase
{
    protected static final VoxelShape NONE_AABB = Block.makeCuboidShape(6, 6, 6, 10, 10, 10);
    protected static final VoxelShape C_UP_AABB = Block.makeCuboidShape(0, 15, 0, 16, 16, 16);
    protected static final VoxelShape C_DOWN_AABB = Block.makeCuboidShape(0, 0, 0, 16, 1, 16);
    protected static final VoxelShape C_NORTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape C_SOUTH_AABB = Block.makeCuboidShape(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape C_WEST_AABB = Block.makeCuboidShape(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape C_EAST_AABB = Block.makeCuboidShape(15, 0, 0, 16, 16, 16);

    public BlockIndustrialFloor(Block.Properties properties)
    {
        super(properties);
    }

    private static boolean isValidConnection(final BlockState neighbourState, final Direction neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockIndustrialFloor
                || nb instanceof BlockFloorPipe
                || nb instanceof BlockFloorCable
                || (nb instanceof DoorBlock && neighbourState.get(DoorBlock.FACING).equals(neighbourDirection))
                || (neighbourDirection.equals(Direction.DOWN) && nb instanceof BlockCatwalkLadder)
                || (neighbourDirection.equals(Direction.UP) && nb instanceof BlockCatwalkHatch)
                //start check for horizontal Iladder
                || ((neighbourDirection != Direction.UP && neighbourDirection != Direction.DOWN)
                && nb instanceof BlockCatwalkLadder && !(boolean) neighbourState.get(BlockCatwalkLadder.ACTIVE))
                //end
                ;
    }

    public static boolean canConnectTo(final IWorld worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);

        return !isValidConnection(neighbourState, neighbourDirection);
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        VoxelShape FINAL_SHAPE = NONE_AABB;
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack playerStack = player.getHeldItem(handIn);
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

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
        return false;
    }
}