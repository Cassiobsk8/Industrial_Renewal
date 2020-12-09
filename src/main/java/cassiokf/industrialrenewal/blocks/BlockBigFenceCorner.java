package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBigFenceCorner extends BlockBigFenceColumn
{
    public BlockBigFenceCorner()
    {
        super();
    }

    @Override
    public boolean isBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockBigFenceCorner;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TEBigFenceCorner();
    }
}
