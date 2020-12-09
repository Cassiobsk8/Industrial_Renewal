package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipe;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeGauge;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
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
        builder.add(FACING);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (player.getHeldItem(handIn).getItem() instanceof ItemPowerScrewDrive)
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

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock()) return;
        if (!worldIn.isRemote)
        {
            ItemStack itemst = new ItemStack(BlocksRegistration.GAUGE_ITEM.get());
            Utils.spawnItemStack(worldIn, pos, itemst);
            Utils.spawnItemStack(worldIn, pos, getItem(worldIn, pos, state));
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
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
    public TileEntityFluidPipe createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFluidPipeGauge();
    }
}
