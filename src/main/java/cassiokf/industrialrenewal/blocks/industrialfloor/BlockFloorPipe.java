package cassiokf.industrialrenewal.blocks.industrialfloor;


import cassiokf.industrialrenewal.blocks.pipes.BlockFluidPipe;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockFloorPipe extends BlockFluidPipe
{
    public BlockFloorPipe(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        Utils.spawnItemStack(worldIn, pos, new ItemStack(BlockItem.getItemFromBlock(ModBlocks.fluidPipe), 1));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        //IProperty[] listedProperties = new IProperty[]{}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN, WSOUTH, WNORTH, WEAST, WWEST, WUP, WDOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    IExtendedBlockState eState = (IExtendedBlockState) super.getExtendedState(state, world, pos);
        //    return eState.with(WSOUTH, BlockIndustrialFloor.canConnectTo(world, pos, Direction.SOUTH)).with(WNORTH, BlockIndustrialFloor.canConnectTo(world, pos, Direction.NORTH))
        //            .with(WEAST, BlockIndustrialFloor.canConnectTo(world, pos, Direction.EAST)).with(WWEST, BlockIndustrialFloor.canConnectTo(world, pos, Direction.WEST))
        //            .with(WUP, BlockIndustrialFloor.canConnectTo(world, pos, Direction.UP)).with(WDOWN, BlockIndustrialFloor.canConnectTo(world, pos, Direction.DOWN));
        //}
        return state;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        return ActionResultType.PASS;
    }

    /*
        @Override
        public void addCollisionBoxToList(BlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
        }

        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
        {
            return FULL_BLOCK_AABB;
        }
    */
    @Deprecated
    public boolean isTopSolid(BlockState state)
    {
        return true;
    }
}