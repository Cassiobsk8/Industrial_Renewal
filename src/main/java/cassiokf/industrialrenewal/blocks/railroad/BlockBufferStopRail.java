package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockBufferStopRail extends BlockHorizontalFacing
{
    protected static final VoxelShape FLAT_AABB = Block.makeCuboidShape(0, 0, 0, 16, 10, 16);

    public BlockBufferStopRail()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn instanceof MinecartEntity)
        {
            entityIn.setMotion(0, 0, 0);
        }
    }

    @Override
    protected VoxelShape getVoxelShape(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return FLAT_AABB;
    }
}