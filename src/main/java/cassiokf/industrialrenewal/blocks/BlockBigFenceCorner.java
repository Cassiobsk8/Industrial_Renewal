package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockBasicElectricFence;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBigFenceCorner extends BlockBigFenceColumn
{
    public BlockBigFenceCorner(String name, CreativeTabs tab)
    {
        super(name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public boolean IsBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockBigFenceCorner;
    }

    @Override
    public boolean ActiveSide(IBlockAccess world, BlockPos pos, IBlockState state, boolean left, boolean top, boolean down)
    {
        int index = state.getValue(INDEX);
        if (!top && index == 2) return false;
        if (top && index != 2) return false;
        if (!down && index == 0) return false;
        if (down && index != 0) return false;
        EnumFacing facing = state.getValue(FACING);
        for (final EnumFacing face : EnumFacing.HORIZONTALS)
        {
            if ((left && face == facing) || (!left && face == facing.rotateY()))
            {
                IBlockState sideState = world.getBlockState(pos.offset(face));
                Block block = sideState.getBlock();
                return sideState.isFullBlock() || block instanceof BlockElectricGate || block instanceof BlockBasicElectricFence;
            }
        }
        return false;
    }
}
