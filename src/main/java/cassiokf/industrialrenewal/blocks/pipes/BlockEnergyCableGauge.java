package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.enums.EnumEnergyCableType;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCable;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableHVGauge;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableLVGauge;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableMVGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEnergyCableGauge extends BlockEnergyCable
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public BlockEnergyCableGauge(EnumEnergyCableType type)
    {
        super(type);
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        //IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
        //IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN};
        //return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    Direction facing = state.get(FACING);
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    return eState.with(MASTER, isMaster(world, pos))
        //            .with(SOUTH, canConnectToPipe(world, pos, facing.getOpposite())).with(NORTH, canConnectToPipe(world, pos, facing))
        //            .with(EAST, canConnectToPipe(world, pos, facing.rotateY())).with(WEST, canConnectToPipe(world, pos, facing.rotateYCCW()))
        //            .with(UP, canConnectToPipe(world, pos, Direction.UP)).with(DOWN, canConnectToPipe(world, pos, Direction.DOWN))
        //            .with(CSOUTH, canConnectToCapability(world, pos, facing.getOpposite())).with(CNORTH, canConnectToCapability(world, pos, facing))
        //            .with(CEAST, canConnectToCapability(world, pos, facing.rotateY())).with(CWEST, canConnectToCapability(world, pos, facing.rotateYCCW()))
        //            .with(CUP, canConnectToCapability(world, pos, Direction.UP)).with(CDOWN, canConnectToCapability(world, pos, Direction.DOWN));
        //}
        return state;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (player.getHeldItemMainhand().getItem() instanceof ItemPowerScrewDrive)
        {
            if (!worldIn.isRemote)
            {
                Block block = getBlockFromType();
                worldIn.setBlockState(pos, block.getDefaultState(), 3);
                if (!player.isCreative())
                    player.addItemStackToInventory(new ItemStack(BlocksRegistration.ENERGYLEVEL_ITEM.get()));
                ItemPowerScrewDrive.playDrillSound(worldIn, pos);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() == newState.getBlock()) return;
        ItemStack itemst = new ItemStack(BlocksRegistration.ENERGYLEVEL_ITEM.get());
        if (!worldIn.isRemote) Utils.spawnItemStack(worldIn, pos, itemst);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public TileEntityEnergyCable createTileEntity(BlockState state, IBlockReader world)
    {
        switch (type)
        {
            default:
            case LV:
                return new TileEntityEnergyCableLVGauge();
            case MV:
                return new TileEntityEnergyCableMVGauge();
            case HV:
                return new TileEntityEnergyCableHVGauge();
        }
    }
}
