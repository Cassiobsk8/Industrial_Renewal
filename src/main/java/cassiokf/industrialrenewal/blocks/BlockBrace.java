package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

public class BlockBrace extends BlockAbstractNotFullCube
{

    public static final EnumProperty<EnumOrientation> FACING = EnumProperty.create("facing", BlockBrace.EnumOrientation.class);

    public BlockBrace()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, BlockBrace.EnumOrientation.forFacings(context.getFace(), context.getPlayer().getHorizontalFacing()));
    }

    //==================================================================================================================
    public enum EnumOrientation implements IStringSerializable
    {
        DOWN_EAST(0, "down_east", Direction.DOWN),
        EAST(1, "east", Direction.EAST),
        WEST(2, "west", Direction.WEST),
        SOUTH(3, "south", Direction.SOUTH),
        NORTH(4, "north", Direction.NORTH),
        DOWN_WEST(5, "down_west", Direction.DOWN),
        DOWN_SOUTH(6, "down_south", Direction.DOWN),
        DOWN_NORTH(7, "down_north", Direction.DOWN);

        private static final BlockBrace.EnumOrientation[] META_LOOKUP = new BlockBrace.EnumOrientation[values().length];

        static
        {
            for (BlockBrace.EnumOrientation blockbrace$enumorientation : values())
            {
                META_LOOKUP[blockbrace$enumorientation.getMetadata()] = blockbrace$enumorientation;
            }
        }

        private final int meta;
        private final String name;
        private final Direction facing;

        private EnumOrientation(int meta, String name, Direction facing)
        {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }

        public static BlockBrace.EnumOrientation byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public static BlockBrace.EnumOrientation forFacings(Direction clickedSide, Direction entityFacing)
        {
            switch (clickedSide)
            {
                case DOWN:
                case UP:
                    switch (entityFacing)
                    {
                        case EAST:
                            return DOWN_EAST;
                        case NORTH:
                            return DOWN_NORTH;
                        case SOUTH:
                            return DOWN_SOUTH;
                        case WEST:
                            return DOWN_WEST;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }

                case NORTH:
                    return SOUTH;
                case SOUTH:
                    return NORTH;
                case WEST:
                    return EAST;
                case EAST:
                    return WEST;
                default:
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
            }
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public Direction getFacing()
        {
            return this.facing;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
