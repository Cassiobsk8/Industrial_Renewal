package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.init.SoundsRegistration;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRailGate extends AbstractRailBlock
{
    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class, dir ->
            dir == RailShape.NORTH_SOUTH || dir == RailShape.EAST_WEST
    );
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    protected static final VoxelShape CNORTH_AABB = Block.makeCuboidShape(-4, 0, 6, 20, 32, 10);
    protected static final VoxelShape CWEST_AABB = Block.makeCuboidShape(6, 0, -4, 10, 32, 20);
    protected String name;

    public BlockRailGate()
    {
        super(true, Block.Properties.create(Material.IRON));
        setDefaultState(getDefaultState().with(OPEN, false));
    }

    public IProperty<RailShape> getShapeProperty()
    {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        RailShape direction = state.get(SHAPE);
        VoxelShape FINAL_SHAPE = FLAT_AABB;
        if (!state.get(OPEN))
        {
            switch (direction)
            {
                case NORTH_SOUTH:
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, CNORTH_AABB);
                    break;
                case EAST_WEST:
                    FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, CWEST_AABB);
                    break;
            }
        }
        return FINAL_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        RailShape direction = state.get(SHAPE);
        switch (direction)
        {
            default:
            case NORTH_SOUTH:
                return CNORTH_AABB;
            case EAST_WEST:
                return CWEST_AABB;
        }
    }

    @Override
    protected void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        boolean flag1 = state.get(OPEN);
        boolean flag2 = worldIn.isBlockPowered(pos);
        if (flag1 != flag2)
        {
            worldIn.setBlockState(pos, state.with(OPEN, flag2), 3);
            //Sound
            Random r = new Random();
            float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
            if (flag2)
            {
                worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_OPEN.get(), SoundCategory.NEUTRAL, 1.0F, pitch);
            } else
            {
                worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_CLOSE.get(), SoundCategory.NEUTRAL, 1.0F, pitch);
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(OPEN, SHAPE);
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }
}