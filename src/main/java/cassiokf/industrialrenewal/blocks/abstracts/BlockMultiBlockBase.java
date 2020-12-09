package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockMultiBlockBase<TE extends TileEntityMultiBlockBase> extends BlockHorizontalFacing
{
    public static final PropertyBool MASTER = PropertyBool.create("master");

    public BlockMultiBlockBase(Material material, String name, CreativeTabs tab)
    {
        super(name, tab, material);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.offset(state.getValue(FACING)).up(), state.withProperty(MASTER, true));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(MASTER))
        {
            EnumFacing facing = state.getValue(FACING);
            List<BlockPos> posList = getMachineBlockPosList(pos, facing);
            for (BlockPos currentPos : posList)
            {
                if (!currentPos.equals(pos))
                    worldIn.setBlockState(currentPos, state.withProperty(MASTER, false));
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMultiBlockBase)
        {
            ((TileEntityMultiBlockBase) te).breakMultiBlocks();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        EntityPlayer player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        EnumFacing facing = player.getHorizontalFacing();
        List<BlockPos> posList = getMachineBlockPosList(getMasterPosBasedOnPlace(pos, facing), facing);
        for (BlockPos currentPos : posList)
        {
            if (!isReplaceable(worldIn, currentPos)) return false;
        }
        return true;
    }

    protected BlockPos getMasterPosBasedOnPlace(BlockPos pos, EnumFacing facing)
    {
        return pos.offset(facing).up();
    }

    public abstract List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing);

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(MASTER, false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(MASTER, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();

        if (state.getValue(MASTER))
        {
            i |= 4;
        }
        return i;
    }

    public TE getTileEntity(IBlockAccess world, BlockPos pos)
    {
        return (TE) world.getTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);
}
