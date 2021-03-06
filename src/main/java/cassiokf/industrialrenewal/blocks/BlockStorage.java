package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiTankBase;
import cassiokf.industrialrenewal.tileentity.TEStorage;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockStorage extends BlockMultiTankBase<TEStorage>
{
    public BlockStorage(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing == EnumFacing.UP) return false;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TEStorage)
            return ((TEStorage) te).getMaster().getBottomTE().onActivated(playerIn, hand, facing);
        return false;
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x3x2Centered(masterPos, facing);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.up(), state.withProperty(MASTER, true));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER, DOWN);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        boolean isMaster = state.getValue(MASTER);
        return state.withProperty(DOWN, (isMaster ? isBot(worldIn, pos) : 0));
    }

    @Override
    public boolean instanceOf(Block block)
    {
        return block instanceof BlockStorage;
    }

    @Override
    public TEStorage createTileEntity(World world, IBlockState state)
    {
        return new TEStorage();
    }
}
