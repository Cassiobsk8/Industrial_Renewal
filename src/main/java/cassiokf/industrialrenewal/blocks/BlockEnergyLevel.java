package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityEnergyLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEnergyLevel extends BlockTileEntity<TileEntityEnergyLevel>
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final DirectionProperty BASE = DirectionProperty.create("base", Direction.values());

    public BlockEnergyLevel(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));

    }

    @Nonnull
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntityEnergyLevel te = (TileEntityEnergyLevel) worldIn.getTileEntity(currentPos);
        if (te != null) return stateIn.with(BASE, te.getBaseFacing());
        return stateIn;
    }

    @Nonnull
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, BASE);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(BASE, context.getFace().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntityEnergyLevel te = (TileEntityEnergyLevel) worldIn.getTileEntity(pos);
        Direction facing = state.get(BASE);
        assert te != null;
        te.setBaseFacing(facing);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
    {
        super.onNeighborChange(state, world, pos, neighbor);
        TileEntityEnergyLevel te = (TileEntityEnergyLevel) world.getTileEntity(pos);
        if (te != null) te.forceCheck();
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        TileEntityEnergyLevel te = (TileEntityEnergyLevel) world.getTileEntity(pos);
        if (te != null && world.isRemote()) te.forceIndicatorCheck();
        return state.with(FACING, state.get(FACING).rotateY());
    }

    @Nullable
    @Override
    public TileEntityEnergyLevel createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityEnergyLevel();
    }
}