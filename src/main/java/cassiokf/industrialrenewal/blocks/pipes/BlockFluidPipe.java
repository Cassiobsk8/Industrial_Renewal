package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipe;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidPipe extends BlockPipeBase<TileEntityFluidPipeBase>
{
    public BlockFluidPipe()
    {
        super(Block.Properties.create(Material.IRON), 4, 4);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        ItemStack heldStack = player.getHeldItemMainhand();
        if (heldStack.getItem() == BlocksRegistration.INDFLOOR_ITEM.get())
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(pos, BlocksRegistration.FLOORPIPE.get().getDefaultState(), 3);
                if (!player.isCreative())
                {
                    heldStack.shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
        if (heldStack.getItem() == BlocksRegistration.GAUGE_ITEM.get())
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(pos, BlocksRegistration.FLUIDPIPEGAUGE.get().getDefaultState().with(BlockFluidPipeGauge.FACING, player.getHorizontalFacing()));
                if (!player.isCreative())
                {
                    heldStack.shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(IRConfig.Main.maxFluidPipeTransferAmount.get() + " mB/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public TileEntityFluidPipeBase createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFluidPipe();
    }
}
