package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BlockRailFacing extends AbstractRailBlock
{

    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class, dir ->
            dir == RailShape.NORTH_SOUTH || dir == RailShape.EAST_WEST
    );

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BlockRailFacing(Block.Properties properties)
    {
        super(false, properties.notSolid());
    }

    public static void propelMinecart(BlockState state, MinecartEntity minecart)
    {
        RailShape dir = state.get(BlockRailFacing.SHAPE);
        Direction facing = state.get(FACING);
        if (dir == RailShape.EAST_WEST)
        {
            if (facing == Direction.EAST)
            {
                minecart.setMotion(0.2d, minecart.getMotion().y, minecart.getMotion().z);
            }
            else
            {
                minecart.setMotion(-0.2d, minecart.getMotion().y, minecart.getMotion().z);
            }
        }
        else if (dir == RailShape.NORTH_SOUTH)
        {
            if (facing == Direction.SOUTH)
            {
                minecart.setMotion(minecart.getMotion().x, minecart.getMotion().y, 0.2d);
            }
            else
            {
                minecart.setMotion(minecart.getMotion().x, minecart.getMotion().y, -0.2d);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction facing = context.getPlayer().getHorizontalFacing();
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
