package net.cassiokf.industrialrenewal.block.decor;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractFourConnections;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockBrace extends BlockAbstractFourConnections {
    
    public static final EnumProperty<EnumOrientation> FACING = EnumProperty.create("facing", EnumOrientation.class);
    
    public BlockBrace() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f));
        registerDefaultState(defaultBlockState().setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
    
    protected boolean isValidConnection(final BlockState neighborState, final BlockGetter world, final BlockPos ownPos, final Direction neighborDirection) {
        Block nb = neighborState.getBlock();
        return nb instanceof BlockBrace;
    }
    
    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        return isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }
    
    @Override
    public VoxelShape getBlockSupportShape(BlockState p_230335_1_, BlockGetter p_230335_2_, BlockPos p_230335_3_) {
        return NULL_SHAPE;
        //return super.getBlockSupportShape(p_230335_1_, p_230335_2_, p_230335_3_);
    }
    
    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        for (Direction face : Direction.Plane.HORIZONTAL) {
            if (EnumOrientation.forFacings(context.getClickedFace(), context.getHorizontalDirection()).getName().contains("down"))
                state = state.setValue(getPropertyBasedOnDirection(face.getOpposite()), canConnectTo(context.getLevel(), context.getClickedPos(), face));
            else
                state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(context.getLevel(), context.getClickedPos(), face));
        }
        return state.setValue(FACING, EnumOrientation.forFacings(context.getClickedFace(), context.getHorizontalDirection()));
    }
    
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        for (Direction face : Direction.Plane.HORIZONTAL) {
            if (state.getValue(FACING).getName().contains("down"))
                state = state.setValue(getPropertyBasedOnDirection(face.getOpposite()), canConnectTo(level, pos, face));
            else state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(level, pos, face));
        }
        level.setBlock(pos, state, 2);
    }
    
    //=================================================================
    public enum EnumOrientation implements StringRepresentable {
        DOWN_EAST(0, "down_east", Direction.DOWN), EAST(1, "east", Direction.EAST), WEST(2, "west", Direction.WEST), SOUTH(3, "south", Direction.SOUTH), NORTH(4, "north", Direction.NORTH), DOWN_WEST(5, "down_west", Direction.DOWN), DOWN_SOUTH(6, "down_south", Direction.DOWN), DOWN_NORTH(7, "down_north", Direction.DOWN);
        
        private static final EnumOrientation[] META_LOOKUP = new EnumOrientation[values().length];
        
        static {
            for (EnumOrientation blockbrace$enumorientation : values()) {
                META_LOOKUP[blockbrace$enumorientation.getMetadata()] = blockbrace$enumorientation;
            }
        }
        
        private final int meta;
        private final String name;
        private final Direction facing;
        
        EnumOrientation(int meta, String name, Direction facing) {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }
        
        public static EnumOrientation byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }
            
            return META_LOOKUP[meta];
        }
        
        public static EnumOrientation forFacings(Direction clickedSide, Direction entityFacing) {
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
        
        public Direction getFacing() {
            return this.facing;
        }
        
        public String toString() {
            return this.name;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
