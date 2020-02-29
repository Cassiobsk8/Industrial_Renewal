package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockConnectedMultiblocks;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityWindTurbinePillar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWindTurbinePillar extends BlockConnectedMultiblocks<TileEntityWindTurbinePillar>
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

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return Block.makeCuboidShape(1, 1, 1, 15, 15, 15);
    }

    @Nullable
    @Override
    public TileEntityWindTurbinePillar createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityWindTurbinePillar();
    }
}
