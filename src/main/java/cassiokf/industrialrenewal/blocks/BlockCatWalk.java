package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityCatWalk;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockCatWalk extends BlockTileEntity<TileEntityCatWalk>
{

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(Direction.values()).map(facing -> BooleanProperty.create(facing.getName())).collect(Collectors.toList()));
    protected static final AxisAlignedBB RENDER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1876D, 1.0D);
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.03125D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.5D, 0.03125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 1.5D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 1.5D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D);

    public BlockCatWalk(Block.Properties property)
    {
        super(property);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (handIn == Hand.MAIN_HAND)
        {
            Item playerItem = player.getHeldItem(Hand.MAIN_HAND).getItem();
            if (playerItem.equals(ModItems.screwDrive))
            {
                TileEntityCatWalk te = (TileEntityCatWalk) worldIn.getTileEntity(pos);
                if (te != null)
                {
                    te.toggleFacing(p_225533_6_.getFace());
                    if (!worldIn.isRemote) ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    worldIn.notifyBlockUpdate(pos, state, state, 2);
                }
            }
            BlockPos posOffset = pos.offset(player.getHorizontalFacing());
            BlockState stateOffset = worldIn.getBlockState(posOffset);
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catWalk))
                    || playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catWalkSteel)))
            {
                if (p_225533_6_.getFace() == Direction.UP)
                {
                    if (stateOffset.getMaterial().isReplaceable())
                    {
                        worldIn.setBlockState(pos.offset(player.getHorizontalFacing()), getBlockFromItem(playerItem).getDefaultState(), 3);
                        if (!player.isCreative())
                        {
                            player.getHeldItem(Hand.MAIN_HAND).shrink(1);
                        }
                        return ActionResultType.SUCCESS;
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catwalkStair)) || playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.catwalkStairSteel)))
            {
                if (stateOffset.getBlock().isAir(stateOffset, worldIn, posOffset))
                {
                    worldIn.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().with(BlockCatwalkStair.FACING, player.getHorizontalFacing()), 3);
                    if (!player.isCreative())
                    {
                        player.getHeldItem(Hand.MAIN_HAND).shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    protected boolean isValidConnection(final BlockState neighbourState, final IBlockReader world, final BlockPos ownPos, final Direction neighbourDirection)
    {
        TileEntityCatWalk te = (TileEntityCatWalk) world.getTileEntity(ownPos);
        if (te != null && te.isFacingBlackListed(neighbourDirection)) return true;

        BlockState downstate = world.getBlockState(ownPos.offset(neighbourDirection).down());
        Block nb = neighbourState.getBlock();

        if (neighbourDirection != Direction.UP && neighbourDirection != Direction.DOWN)
        {
            return nb instanceof BlockCatWalk
                    || nb instanceof DoorBlock
                    || nb instanceof BlockElectricGate
                    || (nb instanceof StairsBlock && (neighbourState.get(StairsBlock.FACING) == neighbourDirection || neighbourState.get(StairsBlock.FACING) == neighbourDirection.getOpposite()))
                    || (downstate.getBlock() instanceof StairsBlock && downstate.get(StairsBlock.FACING) == neighbourDirection.getOpposite())
                    || (nb instanceof BlockCatwalkHatch && neighbourState.get(BlockCatwalkHatch.FACING) == neighbourDirection)
                    || (nb instanceof BlockCatwalkGate && neighbourState.get(BlockCatwalkGate.FACING) == neighbourDirection.getOpposite())
                    || (nb instanceof BlockCatwalkStair && neighbourState.get(BlockCatwalkStair.FACING) == neighbourDirection)
                    || (downstate.getBlock() instanceof BlockCatwalkStair && downstate.get(BlockCatwalkStair.FACING) == neighbourDirection.getOpposite())
                    || (downstate.getBlock() instanceof BlockCatwalkLadder && downstate.get(BlockCatwalkLadder.FACING) == neighbourDirection.getOpposite())
                    || (nb instanceof BlockCatwalkLadder && neighbourState.get(BlockCatwalkLadder.FACING) == neighbourDirection && !neighbourState.get(BlockCatwalkLadder.ACTIVE));
        }
        if (neighbourDirection == Direction.DOWN)
        {
            return nb instanceof BlockCatwalkLadder
                    || nb instanceof LadderBlock
                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorCable || nb instanceof BlockFloorPipe
                    || nb instanceof BlockCatWalk;
        }
        return !(neighbourState.getBlock() instanceof BlockEnergyCable);
    }

    private boolean canConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);

        return !isValidConnection(neighbourState, worldIn, ownPos, neighbourDirection);
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        for (final Direction direction : Direction.values())
        {
            stateIn = stateIn.with(CONNECTED_PROPERTIES.get(direction.getIndex()),
                    canConnectTo(worldIn, currentPos, direction));
        }
        return stateIn;
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        return state.get(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

    /*
        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

            if (!isActualState) {
                state = state.getActualState(worldIn, pos);
            }
            if (isConnected(state, Direction.DOWN)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
            }
            if (isConnected(state, Direction.NORTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            }
            if (isConnected(state, Direction.SOUTH)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            }
            if (isConnected(state, Direction.WEST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
            if (isConnected(state, Direction.EAST)) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }

        }

        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            return RENDER_AABB;
        }
    */
    @Nullable
    @Override
    public TileEntityCatWalk createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCatWalk();
    }
}
