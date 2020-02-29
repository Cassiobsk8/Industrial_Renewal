package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntitySensorRain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSensorRain extends BlockTileEntity<TileEntitySensorRain>
{

    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

    protected static final VoxelShape BLOCK_AABB = Block.makeCuboidShape(0, 0, 0, 16, 2, 16);

    public BlockSensorRain()
    {
        super(Block.Properties.create(Material.IRON));
        setDefaultState(getDefaultState().with(POWER, 0));
    }

    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return state.get(POWER);
    }

    public void updatePower(World worldIn, BlockPos pos, int currentStrength)
    {
        if (worldIn.isRainingAt(pos.up()) && worldIn.canSeeSky(pos))
        {
            BlockState state = worldIn.getBlockState(pos);
            int value = (int) (worldIn.rainingStrength * 15);
            if (value != currentStrength)
            {
                worldIn.setBlockState(pos, state.with(POWER, value));
            }
        } else if (currentStrength != 0)
        {
            BlockState state = worldIn.getBlockState(pos);
            worldIn.setBlockState(pos, state.with(POWER, 0));
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(POWER);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        //int i = pos.getX();
        //int j = pos.getY();
        //int k = pos.getZ();
        //worldIn.scheduleUpdate(new BlockPos(i, j, k), this, this.tickRate(world));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return BLOCK_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return BLOCK_AABB;
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntitySensorRain createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySensorRain();
    }
}
