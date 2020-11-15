package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TEMultiTankBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockMultiTankBase<T extends TEMultiTankBase> extends BlockMultiBlockBase<T>
{
    public static final PropertyInteger TOP = PropertyInteger.create("top", 0, 2);
    public static final PropertyInteger DOWN = PropertyInteger.create("bot", 0, 2);

    public BlockMultiTankBase(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(masterPos);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER, TOP, DOWN);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        boolean isMaster = state.getValue(MASTER);
        return state.withProperty(TOP, (isMaster ? isTop(worldIn, pos) : 0))
                .withProperty(DOWN, (isMaster ? isBot(worldIn, pos) : 0));
    }

    private int isTop(IBlockAccess world, BlockPos pos)
    {
        return instanceOf(world.getBlockState(pos.offset(EnumFacing.UP, 2)).getBlock()) ? 2 : 1;
    }

    private int isBot(IBlockAccess world, BlockPos pos)
    {
        return instanceOf(world.getBlockState(pos.offset(EnumFacing.DOWN, 2)).getBlock()) ? 2 : 1;
    }

    public abstract boolean instanceOf(Block block);

    @Nullable
    @Override
    public abstract T createTileEntity(World world, IBlockState state);
}
