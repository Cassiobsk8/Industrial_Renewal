package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTEHorizontalFacingMultiblocks;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.TileEntityDamAxis;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDamAxis extends BlockTEHorizontalFacingMultiblocks<TileEntityDamAxis>
{
    public BlockDamAxis()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        Item playerItem = player.inventory.getCurrentItem().getItem();
        if (playerItem.equals(BlocksRegistration.DAMAXIS_ITEM.get()))
        {
            int n = 1;
            while (worldIn.getBlockState(pos.up(n)).getBlock() instanceof BlockDamAxis)
            {
                n++;
            }
            if (isReplaceable(worldIn, pos.up(n)))
            {
                worldIn.setBlockState(pos.up(n), getBlockFromItem(playerItem).getDefaultState(), 3);
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
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState();
    }

    @Nullable
    @Override
    public TileEntityDamAxis createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityDamAxis();
    }
}
