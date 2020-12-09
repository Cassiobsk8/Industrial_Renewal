package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTEHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityDamOutFlow;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockDamOutFlow extends BlockTEHorizontalFacing<TileEntityDamOutFlow>
{
    public BlockDamOutFlow()
    {
        super(Block.Properties.create(Material.ROCK));
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Nullable
    @Override
    public TileEntityDamOutFlow createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityDamOutFlow();
    }
}
