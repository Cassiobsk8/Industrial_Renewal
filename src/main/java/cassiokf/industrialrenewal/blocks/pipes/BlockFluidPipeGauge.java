package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeBaseGauge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidPipeGauge extends BlockFluidPipe
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public BlockFluidPipeGauge()
    {
        super();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
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
        if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemPowerScrewDrive)
        {
            if (!worldIn.isRemote)
            {
                worldIn.setBlockState(pos, BlocksRegistration.FLUIDPIPE.get().getDefaultState(), 3);
                if (!player.isCreative())
                    player.addItemStackToInventory(new ItemStack(BlocksRegistration.GAUGE_ITEM.get()));
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
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent("600" + " mB/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public TileEntityFluidPipeBaseGauge createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFluidPipeBaseGauge();
    }
}
