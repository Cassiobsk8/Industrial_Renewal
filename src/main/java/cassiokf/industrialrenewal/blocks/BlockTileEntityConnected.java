package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public abstract class BlockTileEntityConnected<TE extends TileEntity> extends BlockTileEntity<TE>
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    //public static final IUnlistedProperty<Boolean> SOUTH = new Properties.PropertyAdapter<>(BooleanProperty.create("south"));
    //public static final IUnlistedProperty<Boolean> NORTH = new Properties.PropertyAdapter<>(BooleanProperty.create("north"));
    //public static final IUnlistedProperty<Boolean> EAST = new Properties.PropertyAdapter<>(BooleanProperty.create("east"));
    //public static final IUnlistedProperty<Boolean> WEST = new Properties.PropertyAdapter<>(BooleanProperty.create("west"));
    //public static final IUnlistedProperty<Boolean> UP = new Properties.PropertyAdapter<>(BooleanProperty.create("up"));
    //public static final IUnlistedProperty<Boolean> DOWN = new Properties.PropertyAdapter<>(BooleanProperty.create("down"));

    public BlockTileEntityConnected(Block.Properties properties)
    {
        super(properties);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{SOUTH, NORTH, EAST, WEST, UP, DOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }
}
