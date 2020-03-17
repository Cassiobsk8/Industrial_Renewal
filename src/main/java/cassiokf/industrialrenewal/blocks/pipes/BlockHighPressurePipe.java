package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityHighPressurePipe;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHighPressurePipe extends BlockPipeBase<TileEntityHighPressurePipe>
{
    public BlockHighPressurePipe(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public boolean canConnectToPipe(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(neighbourPos);
        Block block = state.getBlock();

        return block instanceof BlockHighPressurePipe;
    }

    @Override
    public boolean canConnectToCapability(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        return !(state.getBlock() instanceof BlockHighPressurePipe)
                && te instanceof ICompressedFluidCapability
                && ((ICompressedFluidCapability) te).canPipeConnect(neighbourDirection.getOpposite(), pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        //tooltip.add(IRConfig.MainConfig.Main.maxFluidPipeTransferAmount + " mB/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public Class<TileEntityHighPressurePipe> getTileEntityClass()
    {
        return TileEntityHighPressurePipe.class;
    }

    @Nullable
    @Override
    public TileEntityHighPressurePipe createTileEntity(World world, IBlockState state)
    {
        return new TileEntityHighPressurePipe();
    }
}
