package cassiokf.industrialrenewal.blocks.railroad;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public abstract class BlockRailFacing extends AbstractRailBlock
{
    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class, dir ->
            dir == RailShape.NORTH_SOUTH || dir == RailShape.EAST_WEST
    );

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BlockRailFacing(Block.Properties properties)
    {
        super(false, properties);
    }

    public static void propelMinecart(BlockState state, AbstractMinecartEntity minecart)
    {
        RailShape dir = state.get(BlockRailFacing.SHAPE);
        Direction facing = state.get(FACING);
        if (dir == RailShape.EAST_WEST)
        {
            if (facing == Direction.EAST)
            {
                minecart.setVelocity(0.2d, 0, 0);
                //minecart.motionX = 0.2d;
            } else
            {
                minecart.setVelocity(-0.2d, 0, 0);
                //minecart.motionX = -0.2d;
            }
        } else if (dir == RailShape.NORTH_SOUTH)
        {
            if (facing == Direction.SOUTH)
            {
                minecart.setVelocity(0, 0, 0.2d);
                //minecart.motionZ = 0.2d;
            } else
            {
                minecart.setVelocity(0, 0, -0.2d);
                //minecart.motionZ = -0.2d;
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction facing = Direction.fromAngle(context.getPlayer().rotationYawHead);
        RailShape shape = facing == Direction.NORTH || facing == Direction.SOUTH ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST;
        return getDefaultState().with(FACING, facing).with(SHAPE, shape);
    }

    @Nonnull
    @Override
    public IProperty<RailShape> getShapeProperty()
    {
        return SHAPE;
    }

    @Override
    protected abstract void fillStateContainer(StateContainer.Builder<Block, BlockState> builder);

    @Override
    public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }
}
