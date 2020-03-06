package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractBigFence;
import cassiokf.industrialrenewal.tileentity.TEBigFenceColumn;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBigFenceBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockElectricBigFenceColumn extends BlockAbstractBigFence
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 2);

    public BlockElectricBigFenceColumn()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public boolean isBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceColumn;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TEBigFenceBase te = (TEBigFenceBase) worldIn.getTileEntity(currentPos);
        if (te != null) te.requestAllModelsUpdate();
        return stateIn;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        return false;
    }

    @Override
    public VoxelShape getVoxelShape(BlockState state, boolean collision)
    {
        return FULL_SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TEBigFenceColumn();
    }
}
