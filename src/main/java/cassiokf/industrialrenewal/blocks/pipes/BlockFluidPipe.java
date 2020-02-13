package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityCableTray;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipe;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidPipe extends BlockPipeBase<TileEntityFluidPipeBase>
{
    public BlockFluidPipe(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean canConnectToPipe(IBlockReader worldIn, BlockPos ownPos, Direction neighbourDirection)
    {
        BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        BlockState state = worldIn.getBlockState(neighbourPos);
        Block block = state.getBlock();

        return block instanceof BlockFluidPipe || (block instanceof BlockCableTray && trayHasPipe(worldIn, neighbourPos));
    }

    private boolean trayHasPipe(IBlockReader world, BlockPos pos)
    {
        TileEntityCableTray te = (TileEntityCableTray) world.getTileEntity(pos);
        if (te != null)
        {
            return te.hasPipe();
        }
        return false;
    }

    @Override
    public boolean canConnectToCapability(IBlockReader worldIn, BlockPos ownPos, Direction neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        BlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        return !(state.getBlock() instanceof BlockFluidPipe) && !(state.getBlock() instanceof BlockFluidPipeGauge)
                && te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite()).isPresent();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == BlockItem.getItemFromBlock(ModBlocks.blockIndFloor))
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(pos, ModBlocks.floorPipe.getDefaultState(), 3);
                if (!player.isCreative())
                {
                    player.getHeldItem(Hand.MAIN_HAND).shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == BlockItem.getItemFromBlock(ModBlocks.gauge))
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(pos, ModBlocks.fluidPipeGauge.getDefaultState().with(BlockFluidPipeGauge.FACING, player.getHorizontalFacing()), 3);
                if (!player.isCreative())
                {
                    player.getHeldItem(Hand.MAIN_HAND).shrink(1);
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
