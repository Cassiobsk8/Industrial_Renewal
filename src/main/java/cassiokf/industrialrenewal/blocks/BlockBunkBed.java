package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.tileentity.TileEntityBunkBed;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BedPart.FOOT;
import static net.minecraft.state.properties.BedPart.HEAD;

public class BlockBunkBed extends BedBlock
{
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public BlockBunkBed(Block.Properties properties)
    {
        super(DyeColor.RED, properties);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(PART) == FOOT)
        {
            worldIn.setBlockState(pos.offset(state.get(HORIZONTAL_FACING)), state.with(PART, HEAD));
        }
    }

    @Override
    public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, @Nullable Entity player)
    {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        switch (state.get(PART))
        {
            case FOOT:
                if (isBunkBed(worldIn, pos.offset(state.get(HORIZONTAL_FACING))))
                    worldIn.removeBlock(pos.offset(state.get(HORIZONTAL_FACING)), false);
                break;
            case HEAD:
                if (isBunkBed(worldIn, pos.offset(state.get(HORIZONTAL_FACING).getOpposite())))
                    worldIn.removeBlock(pos.offset(state.get(HORIZONTAL_FACING).getOpposite()), false);
                break;
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack)
    {
        ItemStack itemstack = this.getItem(worldIn, pos, state);
        spawnAsEntity(worldIn, pos, itemstack);
    }

    private boolean isBunkBed(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockBunkBed;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        PlayerEntity player = worldIn.getDimension().getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        return worldIn.getBlockState(pos).getMaterial().isReplaceable()
                && worldIn.getBlockState(pos.offset(player.getHorizontalFacing())).getMaterial().isReplaceable();
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        boolean top = stateIn.get(PART) == FOOT && worldIn.getBlockState(currentPos.down()).getBlock() instanceof BlockBunkBed;
        return stateIn.with(TOP, top);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(HORIZONTAL_FACING, PART, OCCUPIED, TOP);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(HORIZONTAL_FACING, context.getPlayer().getHorizontalFacing()).with(PART, FOOT);
    }

    @Nullable
    @Override
    public TileEntityBunkBed createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBunkBed();
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new TileEntityBunkBed();
    }
}
