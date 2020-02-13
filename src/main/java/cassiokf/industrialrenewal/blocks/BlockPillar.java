package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.BlockCableTray;
import cassiokf.industrialrenewal.blocks.pipes.BlockPillarEnergyCable;
import cassiokf.industrialrenewal.blocks.pipes.BlockPillarFluidPipe;
import cassiokf.industrialrenewal.blocks.pipes.BlockWireBase;
import cassiokf.industrialrenewal.blocks.redstone.BlockAlarm;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import cassiokf.industrialrenewal.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockPillar extends BlockBase
{

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(Direction.values()).map(facing -> BooleanProperty.create(facing.getName())).collect(Collectors.toList()));
    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

    public BlockPillar(Block.Properties property)
    {
        super(property);
    }

    private static boolean isValidConnection(IBlockReader worldIn, BlockPos neightbourPos, final BlockState neighbourState, final Direction neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        if (neighbourDirection != Direction.UP && neighbourDirection != Direction.DOWN)
        {
            return nb instanceof LeverBlock
                    || (nb instanceof BlockWireBase && neighbourState.get(BlockWireBase.FACING) == neighbourDirection.getOpposite())
                    || nb instanceof RedstoneTorchBlock
                    || nb instanceof TripWireHookBlock
                    || nb instanceof BlockColumn
                    || (nb instanceof BlockCableTray && neighbourState.get(BlockCableTray.BASE).equals(EnumBaseDirection.byIndex(neighbourDirection.getOpposite().getIndex())))
                    || nb instanceof LadderBlock
                    || (nb instanceof BlockLight && neighbourState.get(BlockLight.FACING) == neighbourDirection.getOpposite())
                    || nb instanceof BlockRoof
                    || (nb instanceof BlockBrace && Objects.equals(neighbourState.get(BlockBrace.FACING).getName(), neighbourDirection.getOpposite().getName()))
                    || (nb instanceof BlockBrace && Objects.equals(neighbourState.get(BlockBrace.FACING).getName(), "down_" + neighbourDirection.getName()))
                    || (nb instanceof BlockAlarm && neighbourState.get(BlockAlarm.FACING) == neighbourDirection)
                    || (nb instanceof BlockSignBase && neighbourState.get(BlockSignBase.ONWALL) && Objects.equals(neighbourState.get(BlockSignBase.FACING).getName(), neighbourDirection.getOpposite().getName()))
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:connector")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_decoration2")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:wooden_device1")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_device1")
                    //start Industrial floor side connection
                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
                    || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable;
            //end
        }
        if (neighbourDirection == Direction.DOWN)
        {
            return neighbourState.isSolid();
        }
        return neighbourState.isSolid() || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
                || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable || nb instanceof BlockCatWalk;
    }

    public static boolean canConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);

        return isValidConnection(worldIn, neighbourPos, neighbourState, neighbourDirection);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (this.getRegistryName().toString().equals("catwalk_pillar") && handIn.equals(Hand.MAIN_HAND))
        {
            ItemStack playerStack = player.getHeldItem(Hand.MAIN_HAND);
            Item playerItem = playerStack.getItem();
            Block clickedBlock = state.getBlock();
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableMV)))
            {
                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos, ModBlocks.pillarEnergyCableMV.getDefaultState(), 3);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableLV)))
            {
                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos, ModBlocks.pillarEnergyCableLV.getDefaultState(), 3);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.energyCableHV)))
            {
                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos, ModBlocks.pillarEnergyCableHV.getDefaultState(), 3);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.fluidPipe)))
            {
                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos, ModBlocks.pillarFluidPipe.getDefaultState(), 3);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if ((playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.pillar)) && clickedBlock.equals(ModBlocks.pillar))
                    || (playerItem.equals(BlockItem.getItemFromBlock(ModBlocks.steel_pillar))) && clickedBlock.equals(ModBlocks.steel_pillar))
            {
                int n = 1;
                while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillar
                        || worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarEnergyCable
                        || worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarFluidPipe)
                {
                    n++;
                }
                if (worldIn.getBlockState(pos.up(n)).getMaterial().isReplaceable())
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos.up(n), getBlockFromItem(playerItem).getDefaultState(), 3);
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        if (!player.isCreative()) playerStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
        }
        return ActionResultType.PASS;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        for (final Direction face : Direction.values())
        {
            stateIn = stateIn.with(CONNECTED_PROPERTIES.get(face.getIndex()),
                    canConnectTo(worldIn, currentPos, face));
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
        if (isConnected(state, Direction.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(state, Direction.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(state, Direction.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(state, Direction.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(state, Direction.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(state, Direction.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(state, Direction.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(state, Direction.EAST)) {
            EASTX2 = 0.750f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        BlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, Direction.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(actualState, Direction.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(actualState, Direction.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(actualState, Direction.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(actualState, Direction.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(actualState, Direction.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(actualState, Direction.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(actualState, Direction.EAST)) {
            EASTX2 = 0.750f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }
*/
}
