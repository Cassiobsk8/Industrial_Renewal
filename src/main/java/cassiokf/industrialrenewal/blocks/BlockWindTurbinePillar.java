package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityWindTurbinePillar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class BlockWindTurbinePillar extends BlockTileEntityConnectedMultiblocks<TileEntityWindTurbinePillar>
{

    public BlockWindTurbinePillar()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        Item playerItem = player.inventory.getCurrentItem().getItem();
        Block clickedBlock = state.getBlock();
        if (playerItem.equals(BlocksRegistration.TURBINEPILLAR_ITEM.get()) && clickedBlock.equals(BlocksRegistration.TURBINEPILLAR.get()))
        {
            int n = 1;
            while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockWindTurbinePillar)
            {
                n++;
            }
            if (worldIn.getBlockState(pos.up(n)).getMaterial().isReplaceable())
            {
                worldIn.setBlockState(pos.up(n), getBlockFromItem(playerItem).getDefaultState().with(FACING, state.get(FACING)), 3);
                if (!player.isCreative())
                {
                    player.inventory.getCurrentItem().shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    private boolean canConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighborDirection)
    {
        final BlockPos neighborPos = ownPos.offset(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        if (neighborDirection == Direction.DOWN)
        {
            return !(neighborState.getBlock() instanceof BlockWindTurbinePillar);
        }
        TileEntity te = worldIn.getTileEntity(ownPos.offset(neighborDirection));
        return te != null && te.getCapability(CapabilityEnergy.ENERGY, neighborDirection.getOpposite()).isPresent();
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    Direction facing = state.get(FACING);
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    boolean down = canConnectTo(world, pos, Direction.DOWN);
        //    eState = eState.with(DOWN, down).with(UP, false);
        //    if (down)
        //        eState = eState.with(SOUTH, canConnectTo(world, pos, facing.getOpposite()))
        //                .with(NORTH, canConnectTo(world, pos, facing))
        //                .with(EAST, canConnectTo(world, pos, facing.rotateY()))
        //                .with(WEST, canConnectTo(world, pos, facing.rotateYCCW()));
        //    else
        //        eState = eState.with(SOUTH, false)
        //                .with(NORTH, false)
        //                .with(EAST, false)
        //                .with(WEST, false);
        //    return eState;
        //}
        return state;
    }

    @Nullable
    @Override
    public TileEntityWindTurbinePillar createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityWindTurbinePillar();
    }
}
