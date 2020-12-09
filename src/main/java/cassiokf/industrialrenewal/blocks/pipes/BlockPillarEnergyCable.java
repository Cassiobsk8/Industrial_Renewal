package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockPillar;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;

public class BlockPillarEnergyCable extends BlockEnergyCable
{
    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static final float DOWNY1 = 0.0f;
    private static final float UPY2 = 1.0f;

    public BlockPillarEnergyCable(EnumEnergyCableType type)
    {
        super(type);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state)
    {
        Block block = getBlockFromType();
        ItemStack itemst = new ItemStack(block);
        Utils.spawnItemStack(worldIn.getWorld(), pos, itemst);
        super.onPlayerDestroy(worldIn, pos, state);
    }

    public final boolean isConnected(IBlockReader world, BlockPos pos, final Direction facing)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te != null)
        {
            IModelData data = te.getModelData();
            switch (facing)
            {
                case DOWN:
                    return data.hasProperty(TETubeBase.WDOWN) && data.getData(TETubeBase.WDOWN);
                case UP:
                    return data.hasProperty(TETubeBase.WUP) && data.getData(TETubeBase.WUP);
                case NORTH:
                    return data.hasProperty(TETubeBase.WNORTH) && data.getData(TETubeBase.WNORTH);
                case SOUTH:
                    return data.hasProperty(TETubeBase.WSOUTH) && data.getData(TETubeBase.WSOUTH);
                case WEST:
                    return data.hasProperty(TETubeBase.WWEST) && data.getData(TETubeBase.WWEST);
                case EAST:
                    return data.hasProperty(TETubeBase.WEAST) && data.getData(TETubeBase.WEAST);
            }
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(BlocksRegistration.PILLAR_ITEM.get());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack playerStack = player.getHeldItem(handIn);
        Item playerItem = playerStack.getItem();
        if (playerItem instanceof ItemPowerScrewDrive)
        {
            if (!worldIn.isRemote)
            {
                worldIn.setBlockState(pos, BlocksRegistration.PILLAR.get().getDefaultState(), 3);
                if (!player.isCreative())
                {
                    Block block = getBlockFromType();
                    player.addItemStackToInventory(new ItemStack(block));
                }
                ItemPowerScrewDrive.playDrillSound(worldIn, pos);
            }
            return ActionResultType.SUCCESS;
        }
        if (playerItem.equals(BlocksRegistration.PILLAR_ITEM.get()))
        {
            int n = 1;
            while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarEnergyCable
                    || worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarFluidPipe
                    || worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockPillar)
            {
                n++;
            }
            if (isReplaceable(worldIn, pos.up(n)))
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
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        if (isConnected(worldIn, pos, Direction.NORTH))
        {
            NORTHZ1 = 0;
        }
        else
        {
            NORTHZ1 = 4;
        }
        if (isConnected(worldIn, pos, Direction.SOUTH))
        {
            SOUTHZ2 = 16;
        }
        else
        {
            SOUTHZ2 = 12;
        }
        if (isConnected(worldIn, pos, Direction.WEST))
        {
            WESTX1 = 0;
        }
        else
        {
            WESTX1 = 4;
        }
        if (isConnected(worldIn, pos, Direction.EAST))
        {
            EASTX2 = 16;
        }
        else
        {
            EASTX2 = 12;
        }
        return Block.makeCuboidShape(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }
}
