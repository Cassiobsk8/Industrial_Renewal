package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrace extends BlockBase {

    public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create("facing", BlockBrace.EnumOrientation.class);
    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);


    public BlockBrace(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setHardness(3f);
        setResistance(5f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public  BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, BlockBrace.EnumOrientation.byMetadata(meta & 7));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getMetadata();

        return i;
    }

    @SuppressWarnings("deprecation")
    @Override
    public  BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, BlockBrace.EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    //==================================================================================================================
    public static enum EnumOrientation implements IStringSerializable {
        DOWN_EAST(0, "down_east", EnumFacing.DOWN),
        EAST(1, "east", EnumFacing.EAST),
        WEST(2, "west", EnumFacing.WEST),
        SOUTH(3, "south", EnumFacing.SOUTH),
        NORTH(4, "north", EnumFacing.NORTH),
        DOWN_WEST(5, "down_west", EnumFacing.DOWN),
        DOWN_SOUTH(6, "down_south", EnumFacing.DOWN),
        DOWN_NORTH(7, "down_north", EnumFacing.DOWN);

        private static final BlockBrace.EnumOrientation[] META_LOOKUP = new BlockBrace.EnumOrientation[values().length];

        static {
            for (BlockBrace.EnumOrientation blockbrace$enumorientation : values()) {
                META_LOOKUP[blockbrace$enumorientation.getMetadata()] = blockbrace$enumorientation;
            }
        }

        private final int meta;
        private final String name;
        private final EnumFacing facing;

        private EnumOrientation(int meta, String name, EnumFacing facing) {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }

        public static BlockBrace.EnumOrientation byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public static BlockBrace.EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
            switch (clickedSide) {
                case DOWN:
                case UP:
                    switch (entityFacing) {
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

        public int getMetadata() {
            return this.meta;
        }

        public EnumFacing getFacing() {
            return this.facing;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
