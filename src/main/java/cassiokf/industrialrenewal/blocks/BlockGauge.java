package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractFacingWithBase;
import cassiokf.industrialrenewal.tileentity.TileEntityGauge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGauge extends BlockAbstractFacingWithBase
{
    public BlockGauge()
    {
        super(Block.Properties.create(Material.IRON), 8, 10);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
    {
        super.onNeighborChange(state, world, pos, neighbor);
        TileEntityGauge te = (TileEntityGauge) world.getTileEntity(pos);
        if (te != null) te.forceCheck();
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        TileEntityGauge te = (TileEntityGauge) world.getTileEntity(pos);
        if (te != null && world.isRemote()) te.forceIndicatorCheck();
        return super.rotate(state, world, pos, direction);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntityGauge te = (TileEntityGauge) worldIn.getTileEntity(pos);
        Direction facing = state.get(BASE);
        te.setBaseFacing(facing);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityGauge createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityGauge();
    }
}