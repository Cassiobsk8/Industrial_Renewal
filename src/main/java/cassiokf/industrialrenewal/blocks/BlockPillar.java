package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.BlockCableTray;
import cassiokf.industrialrenewal.blocks.pipes.BlockHVIsolator;
import cassiokf.industrialrenewal.blocks.pipes.BlockPillarEnergyCable;
import cassiokf.industrialrenewal.blocks.pipes.BlockPillarFluidPipe;
import cassiokf.industrialrenewal.blocks.redstone.BlockAlarm;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Objects;

public class BlockPillar extends BlockAbstractSixWayConnections
{
    public BlockPillar()
    {
        super(Block.Properties.create(Material.IRON), 8, 16);
    }

    private static boolean isValidConnection(final BlockState neighborState, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();
        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            return nb instanceof LeverBlock
                    || (nb instanceof BlockHVIsolator && neighborState.get(BlockHVIsolator.FACING) == neighborDirection.getOpposite())
                    || nb instanceof RedstoneTorchBlock
                    || nb instanceof TripWireHookBlock
                    || nb instanceof BlockColumn
                    || (nb instanceof BlockCableTray && neighborState.get(BlockCableTray.BASE).equals(EnumBaseDirection.byIndex(neighborDirection.getOpposite().getIndex())))
                    || nb instanceof LadderBlock
                    || (nb instanceof BlockLight && neighborState.get(BlockLight.FACING) == neighborDirection.getOpposite())
                    || nb instanceof BlockRoof
                    || (nb instanceof BlockBrace && Objects.equals(neighborState.get(BlockBrace.FACING).getName(), neighborDirection.getOpposite().getName()))
                    || (nb instanceof BlockBrace && Objects.equals(neighborState.get(BlockBrace.FACING).getName(), "down_" + neighborDirection.getName()))
                    || (nb instanceof BlockAlarm && neighborState.get(BlockAlarm.FACING) == neighborDirection)
                    || (nb instanceof BlockSignBase && neighborState.get(BlockSignBase.ONWALL) && Objects.equals(neighborState.get(BlockSignBase.FACING).getName(), neighborDirection.getOpposite().getName()))
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:connector")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_decoration2")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:wooden_device1")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_device1")
                    //start Industrial floor side connection
                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
                    || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable;
            //end
        }
        if (neighborDirection == Direction.DOWN)
        {
            return neighborState.isSolid();
        }
        return neighborState.isSolid() || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
                || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable || nb instanceof BlockCatWalk;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (handIn.equals(Hand.MAIN_HAND))
        {
            ItemStack playerStack = player.getHeldItemMainhand();
            Item playerItem = playerStack.getItem();
            Block clickedBlock = state.getBlock();
            if (this.getRegistryName().toString().equals("catwalk_pillar"))
            {
                if (playerItem.equals(BlocksRegistration.ENERGYCABLEMV_ITEM.get()))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos, BlocksRegistration.PILLARENERGYCABLEMV.get().getDefaultState(), 3);
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        if (!player.isCreative()) playerStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (playerItem.equals(BlocksRegistration.ENERGYCABLELV_ITEM.get()))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos, BlocksRegistration.PILLARENERGYCABLELV.get().getDefaultState(), 3);
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        if (!player.isCreative()) playerStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (playerItem.equals(BlocksRegistration.ENERGYCABLEHV_ITEM.get()))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos, BlocksRegistration.PILLARENERGYCABLEHV.get().getDefaultState(), 3);
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        if (!player.isCreative()) playerStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (playerItem.equals(BlocksRegistration.FLUIDPIPE_ITEM.get()))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos, BlocksRegistration.PILLARFLUIDPIPE.get().getDefaultState(), 3);
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        if (!player.isCreative()) playerStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
            if ((playerItem.equals(BlocksRegistration.PILLAR_ITEM.get()) && clickedBlock.equals(BlocksRegistration.PILLAR.get()))
                    || (playerItem.equals(BlocksRegistration.STEEL_PILLAR_ITEM.get())) && clickedBlock.equals(BlocksRegistration.STEEL_PILLAR.get()))
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
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        return isValidConnection(neighborState, neighborDirection);
    }
}
