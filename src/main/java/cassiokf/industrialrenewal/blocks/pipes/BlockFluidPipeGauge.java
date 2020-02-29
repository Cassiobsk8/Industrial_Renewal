package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeBaseGauge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
