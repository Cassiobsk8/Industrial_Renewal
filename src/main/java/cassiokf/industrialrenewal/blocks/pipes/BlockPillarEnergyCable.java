package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockPillar;
import cassiokf.industrialrenewal.enums.EnumEnergyCableType;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockPillarEnergyCable extends BlockEnergyCable
{
    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

    public BlockPillarEnergyCable(EnumEnergyCableType type, Block.Properties properties)
    {
        super(type, properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        //IProperty[] listedProperties = new IProperty[]{}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN, WSOUTH, WNORTH, WEAST, WWEST, WUP, WDOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Block block;
        switch (type)
        {
            default:
            case LV:
                block = ModBlocks.energyCableLV;
                break;
            case MV:
                block = ModBlocks.energyCableMV;
                break;
            case HV:
                block = ModBlocks.energyCableHV;
                break;
        }
        ItemStack itemst = new ItemStack(BlockItem.getItemFromBlock(block));
        Utils.spawnItemStack(worldIn.getWorld(), pos, itemst);
        super.onPlayerDestroy(worldIn, pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        ItemStack playerStack = player.getHeldItem(Hand.MAIN_HAND);
        Item playerItem = playerStack.getItem();
        if (playerItem instanceof ItemPowerScrewDrive)
        {
            if (!worldIn.isRemote)
            {
                worldIn.setBlockState(pos, ModBlocks.pillar.getDefaultState(), 3);
                if (!player.isCreative())
                {
                    Block block;
                    switch (type)
                    {
                        default:
                        case LV:
                            block = ModBlocks.energyCableLV;
                            break;
                        case MV:
                            block = ModBlocks.energyCableMV;
                            break;
                        case HV:
                            block = ModBlocks.energyCableHV;
                            break;
                    }
                    player.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(block)));
                }
                ItemPowerScrewDrive.playDrillSound(worldIn, pos);
            }
            return ActionResultType.SUCCESS;
        }
        if (playerItem.equals(ModBlocks.pillar))
        {
            int n = 1;
            while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarEnergyCable
                    || worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarFluidPipe
                    || worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillar)
            {
                n++;
            }
            if (worldIn.getBlockState(pos.up(n)).getMaterial().isReplaceable())
            {
                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos.up(n), Block.getBlockFromItem(playerItem).getDefaultState(), 3);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    return eState.with(MASTER, isMaster(world, pos))
        //            .with(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH)).with(NORTH, canConnectToPipe(world, pos, Direction.NORTH))
        //            .with(EAST, canConnectToPipe(world, pos, Direction.EAST)).with(WEST, canConnectToPipe(world, pos, Direction.WEST))
        //            .with(UP, canConnectToPipe(world, pos, Direction.UP)).with(DOWN, canConnectToPipe(world, pos, Direction.DOWN))
        //            .with(CSOUTH, canConnectToCapability(world, pos, Direction.SOUTH)).with(CNORTH, canConnectToCapability(world, pos, Direction.NORTH))
        //            .with(CEAST, canConnectToCapability(world, pos, Direction.EAST)).with(CWEST, canConnectToCapability(world, pos, Direction.WEST))
        //            .with(CUP, canConnectToCapability(world, pos, Direction.UP)).with(CDOWN, canConnectToCapability(world, pos, Direction.DOWN))
        //            .with(WSOUTH, BlockPillar.canConnectTo(world, pos, Direction.SOUTH)).with(WNORTH, BlockPillar.canConnectTo(world, pos, Direction.NORTH))
        //            .with(WEAST, BlockPillar.canConnectTo(world, pos, Direction.EAST)).with(WWEST, BlockPillar.canConnectTo(world, pos, Direction.WEST))
        //            .with(WUP, BlockPillar.canConnectTo(world, pos, Direction.UP)).with(WDOWN, BlockPillar.canConnectTo(world, pos, Direction.DOWN));
        //}
        return state;
    }

    public final boolean isConnected(IBlockReader world, BlockPos pos, BlockState state, final Direction facing)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    state = getExtendedState(state, world, pos);
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    switch (facing)
        //    {
        //        case DOWN:
        //            return eState.get(WDOWN);
        //        case UP:
        //            return eState.get(WUP);
        //        case NORTH:
        //            return eState.get(WNORTH);
        //        case SOUTH:
        //            return eState.get(WSOUTH);
        //        case WEST:
        //            return eState.get(WWEST);
        //        case EAST:
        //            return eState.get(WEAST);
        //    }
        //}
        return false;
    }

/*
    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        if (isConnected(worldIn, pos, state, Direction.NORTH))
        {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.NORTH))
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.SOUTH))
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, Direction.WEST))
        {
            WESTX1 = 0.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.WEST))
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.EAST))
        {
            EASTX2 = 1.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.EAST))
        {
            EASTX2 = 0.750f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        if (isConnected(worldIn, pos, state, Direction.NORTH))
        {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.NORTH))
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.SOUTH))
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, Direction.WEST))
        {
            WESTX1 = 0.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.WEST))
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.EAST))
        {
            EASTX2 = 1.0f;
        } else if (!isConnected(worldIn, pos, state, Direction.EAST))
        {
            EASTX2 = 0.750f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }
    */
}
