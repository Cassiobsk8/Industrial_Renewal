package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.TileEntityElectricPump;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockElectricPump extends BlockTileEntity<TileEntityElectricPump>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 1);

    public BlockElectricPump()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(INDEX) == 0)
        {
            worldIn.setBlockState(pos.offset(state.get(FACING)), state.with(INDEX, 1));
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        switch (state.get(INDEX))
        {
            case 0:
                if (IsPump(worldIn, pos.offset(state.get(FACING))))
                    worldIn.removeBlock(pos.offset(state.get(FACING)), false);
                break;
            case 1:
                if (IsPump(worldIn, pos.offset(state.get(FACING).getOpposite())))
                    worldIn.removeBlock(pos.offset(state.get(FACING).getOpposite()), false);
                break;
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);

    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    private boolean IsPump(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricPump;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        PlayerEntity player = worldIn.getDimension().getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        return worldIn.getBlockState(pos).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.offset(player.getHorizontalFacing())).getMaterial().isReplaceable();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, INDEX);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(INDEX, 0);
    }

    @Nullable
    @Override
    public TileEntityElectricPump createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityElectricPump();
    }
}
