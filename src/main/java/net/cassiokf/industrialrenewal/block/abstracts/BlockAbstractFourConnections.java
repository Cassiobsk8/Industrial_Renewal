package net.cassiokf.industrialrenewal.block.abstracts;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockAbstractFourConnections extends BlockAbstractNotFullCube {
    
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    
    public static double NORTHZ1 = 4;
    public static double SOUTHZ2 = 12;
    public static double WESTX1 = 4;
    public static double EASTX2 = 12;
    
    public float nodeWidth;
    public float nodeHeight;
    public float collisionY;
    
    public BlockAbstractFourConnections(BlockBehaviour.Properties properties, float nodeWidth, float nodeHeight, float collisionY) {
        super(properties.noOcclusion());
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.collisionY = collisionY;
        //setDefaultState(getDefaultBlockState());
    }
    
    public BlockAbstractFourConnections(Properties props) {
        super(props.noOcclusion());
        this.nodeWidth = 16;
        this.nodeHeight = 16;
        this.collisionY = 16;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return getVoxelShape(state, false);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return getVoxelShape(state, true);
        
    }
    
    public VoxelShape getVoxelShape(BlockState state, boolean collision) {
        if (isConnected(state, NORTH)) {
            NORTHZ1 = 0;
        } else {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, SOUTH)) {
            SOUTHZ2 = 16;
        } else {
            SOUTHZ2 = nodeWidth / 2 + 8;
        }
        if (isConnected(state, WEST)) {
            WESTX1 = 0;
        } else {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, EAST)) {
            EASTX2 = 16;
        } else {
            EASTX2 = nodeWidth / 2 + 8;
        }
        return Block.box(WESTX1, 8 - (nodeHeight / 2), NORTHZ1, EASTX2, (collision && fenceCollision()) ? 24 : (8 + (nodeHeight / 2)), SOUTHZ2);
    }
    
    public boolean fenceCollision() {
        return false;
    }
    
    public final boolean isConnected(final BlockState state, final BooleanProperty property) {
        return state.getValue(property);
    }
    
    public BooleanProperty getPropertyBasedOnDirection(Direction direction) {
        switch (direction) {
            default:
            case DOWN:
            case UP:
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
    }
    
    public abstract boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection);
    
    
    @Override
    protected BlockState getInitDefaultState() {
        return super.getInitDefaultState().setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false);
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = getInitDefaultState();
        for (Direction face : Direction.Plane.HORIZONTAL) {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(context.getLevel(), context.getClickedPos(), face));
        }
        return state;
    }
    
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, EAST, WEST);
        
    }
}
