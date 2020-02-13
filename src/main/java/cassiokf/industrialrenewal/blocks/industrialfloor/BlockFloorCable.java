package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.enums.EnumEnergyCableType;
import cassiokf.industrialrenewal.init.ModBlocks;
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

public class BlockFloorCable extends BlockEnergyCable
{

    public BlockFloorCable(EnumEnergyCableType type, Block.Properties properties)
    {
        super(type, properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        return ActionResultType.PASS;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        //IProperty[] listedProperties = new IProperty[]{}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN, WSOUTH, WNORTH, WEAST, WWEST, WUP, WDOWN};
        //builder.add(listedProperties, unlistedProperties);
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
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        Block block;
        switch (type)
        {
            default:
            case LV:
                block = ModBlocks.energyCableLV;
                break;
            case MV:
                block = ModBlocks.energyCableMV;
                break;
            case HV:
                block = ModBlocks.energyCableHV;
                break;
        }
        ItemStack itemst = new ItemStack(BlockItem.getItemFromBlock(block));
        if (!worldIn.isRemote) spawnAsEntity(worldIn, pos, itemst);
        super.onPlayerDestroy(worldIn, pos, state);
    }

    /*
        @Override
        public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
        {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        }

        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader worldIn, BlockPos pos)
        {
            return new AxisAlignedBB(0,0,0,1,1,1);
        }
    */
    @Deprecated
    public boolean isTopSolid(BlockState state)
    {
        return true;
    }
}