package cassiokf.industrialrenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public abstract class BlockAbstractHorizontalFacingWhitBase extends BlockAbstractHorizontalFacing
{
    public static final DirectionProperty BASE = DirectionProperty.create("base", Direction.values());

    public static final VoxelShape UP_SHAPE = Block.makeCuboidShape(6, 8, 6, 10, 16, 10);
    public static final VoxelShape DOWN_SHAPE = Block.makeCuboidShape(6, 0, 6, 10, 8, 10);
    public static final VoxelShape NORTH_SHAPE = Block.makeCuboidShape(6, 6, 0, 10, 10, 8);
    public static final VoxelShape SOUTH_SHAPE = Block.makeCuboidShape(6, 6, 8, 10, 10, 16);
    public static final VoxelShape EAST_SHAPE = Block.makeCuboidShape(8, 6, 6, 16, 10, 10);
    public static final VoxelShape WEST_SHAPE = Block.makeCuboidShape(0, 6, 6, 8, 10, 10);

    private float height = 0;
    private float width = 0;

    public BlockAbstractHorizontalFacingWhitBase(Properties properties, float width, float height)
    {
        super(properties.notSolid());
        this.width = width;
        this.height = height;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        VoxelShape FINAL_SHAPE = Block.makeCuboidShape(8 - (width / 2), 8 - (height / 2), 8 - (width / 2), 8 + (width / 2), 8 + (height / 2), 8 + (width / 2));
        switch (state.get(BASE))
        {
            case DOWN:
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, DOWN_SHAPE);
                break;
            case UP:
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, UP_SHAPE);
                break;
            case NORTH:
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_SHAPE);
                break;
            case SOUTH:
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_SHAPE);
                break;
            case WEST:
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_SHAPE);
                break;
            case EAST:
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_SHAPE);
                break;
        }
        return FINAL_SHAPE;
    }

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
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state.with(FACING, state.get(FACING).rotateY());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }
}
