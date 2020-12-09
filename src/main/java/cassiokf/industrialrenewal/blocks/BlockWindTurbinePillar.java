package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTEHorizontalFacingMultiblocks;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityWindTurbinePillar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWindTurbinePillar extends BlockTEHorizontalFacingMultiblocks<TileEntityWindTurbinePillar>
{

    public BlockWindTurbinePillar()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack playerStack = player.getHeldItem(handIn);
        if (playerStack.getItem().equals(BlocksRegistration.TURBINEPILLAR_ITEM.get()))
        {
            int n = 1;
            while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockWindTurbinePillar)
            {
                n++;
            }
            if (isReplaceable(worldIn, pos.up(n)))
            {
                worldIn.setBlockState(pos.up(n), getDefaultState().with(FACING, state.get(FACING)), 3);
                if (!player.isCreative())
                {
                    playerStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityWindTurbinePillar)
        {
            BlockState newState = super.rotate(state, world, pos, direction);
            Direction facing = newState.get(FACING);
            ((TileEntityWindTurbinePillar) te).setFacing(facing);
            return newState;
        }
        return state;
    }

    @Nullable
    @Override
    public TileEntityWindTurbinePillar createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityWindTurbinePillar();
    }
}
