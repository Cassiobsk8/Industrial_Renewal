package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockAbstractFacingWithBase;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFlameDetector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockFlameDetector extends BlockAbstractFacingWithBase
{
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockFlameDetector()
    {
        super(Block.Properties.create(Material.IRON), 16, 10);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
    {
        return side != state.get(FACING).getOpposite();
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }


    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }


    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        boolean active = state.get(ACTIVE);
        if (!active)
        {
            return 0;
        }
        return 15;
    }

    @Nonnull
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ACTIVE, BASE);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntityFlameDetector te = (TileEntityFlameDetector) worldIn.getTileEntity(pos);
        Direction facing = state.get(BASE);
        te.setBlockFacing(facing);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityFlameDetector createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFlameDetector();
    }
}