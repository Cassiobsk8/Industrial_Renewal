package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityCableTray;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCableTray extends BlockPipeBase<TileEntityCableTray> implements ITileEntityProvider
{

    public BlockCableTray(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("Is a energy Cable, Fluid Pipe and Data cable in one");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean canConnectToPipe(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        IBlockState state = worldIn.getBlockState(ownPos.offset(neighbourDirection));
        return state.getBlock() instanceof BlockCableTray;
    }

    @Override
    public boolean canConnectToCapability(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        Block block = state.getBlock();
        return !(block instanceof BlockCableTray)
                && block instanceof BlockPipeBase;
    }


    @Override
    public Class<? extends TileEntity> getTileEntityClass()
    {
        return TileEntityCableTray.class;
    }

    @Nullable
    @Override
    public TileEntityCableTray createTileEntity(World world, IBlockState state)
    {
        return new TileEntityCableTray();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCableTray();
    }
}
