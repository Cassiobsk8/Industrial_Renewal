package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.blocks.BlockBase;
import cassiokf.industrialrenewal.blocks.BlockCatwalkHatch;
import cassiokf.industrialrenewal.blocks.BlockCatwalkLadder;
import cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;


public class BlockIndustrialFloor extends BlockBase
{

    //public static final ImmutableList<IUnlistedProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
    //        Stream.of(Direction.values())
    //                .map(facing -> new property.PropertyAdapter<>(BooleanProperty.create(facing.getName())))
    //                .collect(Collectors.toList())
    //);
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.875D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB NONE_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB C_UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    protected static final AxisAlignedBB C_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB C_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockIndustrialFloor(Block.Properties property)
    {
        super(property);
    }

    private static boolean isValidConnection(final BlockState neighbourState, final IBlockReader world, final BlockPos ownPos, final Direction neighbourDirection)
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
                && nb instanceof BlockCatwalkLadder && !neighbourState.get(BlockCatwalkLadder.ACTIVE))
                //end
                ;
    }

    public static boolean canConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);

        return !isValidConnection(neighbourState, worldIn, ownPos, neighbourDirection);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader worldIn, BlockPos pos)
        {
            if (isConnected(worldIn, pos, state, Direction.UP) && !isConnected(worldIn, pos, state, Direction.DOWN))
            {
                return UP_AABB;
            }
            if (!isConnected(worldIn, pos, state, Direction.UP) && isConnected(worldIn, pos, state, Direction.DOWN))
            {
                return DOWN_AABB;
            }
            if (!isConnected(worldIn, pos, state, Direction.UP) && !isConnected(worldIn, pos, state, Direction.DOWN))
            {
                return NONE_AABB;
            }
            return FULL_BLOCK_AABB;
        }

        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
        {
            if (isConnected(worldIn, pos, state, Direction.UP))
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, C_UP_AABB);
            }
            if (isConnected(worldIn, pos, state, Direction.DOWN))
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, C_DOWN_AABB);
            }
            if (isConnected(worldIn, pos, state, Direction.NORTH))
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, C_NORTH_AABB);
            }
            if (isConnected(worldIn, pos, state, Direction.SOUTH))
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, C_SOUTH_AABB);
            }
            if (isConnected(worldIn, pos, state, Direction.WEST))
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, C_WEST_AABB);
            }
            if (isConnected(worldIn, pos, state, Direction.EAST))
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, C_EAST_AABB);
            }
        }
    */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        IProperty[] listedProperties = new IProperty[]{}; // listed properties
        //IUnlistedProperty[] unlistedProperties = CONNECTED_PROPERTIES.toArray(new IUnlistedProperty[CONNECTED_PROPERTIES.size()]);
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    for (final Direction facing : Direction.VALUES)
        //    {
        //        eState = eState.with(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(world, pos, facing));
        //    }
        //    return eState;
        //}
        return state;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
        return false;
    }

    public final boolean isConnected(IBlockReader world, BlockPos pos, BlockState state, final Direction facing)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    state = getExtendedState(state, world, pos);
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    return eState.get(CONNECTED_PROPERTIES.get(facing.getIndex()));
        //}
        return false;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        ItemStack playerStack = player.getHeldItem(Hand.MAIN_HAND);
        Item playerItem = playerStack.getItem();

        if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.fluidPipe)))
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlockState(pos, ModBlocks.floorPipe.getDefaultState(), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableMV))
                || playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableLV))
                || playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableHV)))
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            Block block;
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableMV))) block = ModBlocks.floorCableMV;
            else if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableLV)))
                block = ModBlocks.floorCableLV;
            else block = ModBlocks.floorCableHV;
            worldIn.setBlockState(pos, block.getDefaultState(), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.fluorescent)))
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            worldIn.setBlockState(pos, ModBlocks.floorLamp.getDefaultState(), 3);
            if (player.getHorizontalFacing() == Direction.EAST || player.getHorizontalFacing() == Direction.WEST)
            {
                worldIn.setBlockState(pos.up(), ModBlocks.dummy.getDefaultState(), 3);
            }
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}