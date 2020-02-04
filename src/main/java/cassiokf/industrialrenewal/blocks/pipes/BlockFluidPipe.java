package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityCableTray;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidPipe extends BlockPipeBase<TileEntityFluidPipe>
{
    public BlockFluidPipe(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public boolean canConnectToPipe(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(neighbourPos);
        Block block = state.getBlock();

        return block instanceof BlockFluidPipe || (block instanceof BlockCableTray && trayHasPipe(worldIn, neighbourPos));
    }

    private boolean trayHasPipe(IBlockAccess world, BlockPos pos)
    {
        TileEntityCableTray te = (TileEntityCableTray) world.getTileEntity(pos);
        if (te != null)
        {
            return te.hasPipe();
        }
        return false;
    }

    @Override
    public boolean canConnectToCapability(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        return !(state.getBlock() instanceof BlockFluidPipe) && !(state.getBlock() instanceof BlockFluidPipeGauge)
                && te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (entity.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor))
        {
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(pos, ModBlocks.floorPipe.getDefaultState(), 3);
                if (!entity.isCreative())
                {
                    entity.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                }
            }
            return true;
        }
        if (entity.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemBlock.getItemFromBlock(ModBlocks.gauge))
        {
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(pos, ModBlocks.fluidPipeGauge.getDefaultState().withProperty(BlockFluidPipeGauge.FACING, entity.getHorizontalFacing()), 3);
                if (!entity.isCreative())
                {
                    entity.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(IRConfig.MainConfig.Main.maxFluidPipeTransferAmount + " mB/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public Class<TileEntityFluidPipe> getTileEntityClass()
    {
        return TileEntityFluidPipe.class;
    }

    @Nullable
    @Override
    public TileEntityFluidPipe createTileEntity(World world, IBlockState state) {
        return new TileEntityFluidPipe();
    }
}
