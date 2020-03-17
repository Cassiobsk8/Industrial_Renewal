package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockDamOutFlow;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityDamOutFlow extends TileEntity implements ICompressedFluidCapability, ITickable
{
    private EnumFacing facing = null;
    private boolean hasFlow = false;
    private boolean removed = false;

    @Override
    public void update()
    {
        if (hasWorld() && !world.isRemote)
        {
            Block block = world.getBlockState(pos.offset(getBlockFacing())).getBlock();
            if (hasFlow)
            {
                if (!block.equals(Blocks.FLOWING_WATER) && !block.equals(Blocks.WATER))
                {
                    IBlockState waterState = Blocks.FLOWING_WATER.getDefaultState();
                    world.setBlockState(pos.offset(getBlockFacing()), waterState, 3);
                    removed = false;
                }
            } else
            {
                if (!removed && (block.equals(Blocks.FLOWING_WATER) || block.equals(Blocks.WATER)))
                {
                    world.setBlockToAir(pos.offset(getBlockFacing()));
                    removed = true;
                }
            }
            hasFlow = false;
        }
    }

    @Override
    public boolean canAccept(EnumFacing face, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean canPipeConnect(EnumFacing face, BlockPos pos)
    {
        return face.equals(getBlockFacing().getOpposite());
    }

    @Override
    public int passCompressedFluid(int amount, int y, boolean simulate)
    {
        int height = y - pos.getY();
        if (!simulate && amount > 0) hasFlow = true;
        return height >= 0 ? amount : 0;
    }

    private EnumFacing getBlockFacing()
    {
        if (facing != null) return facing;
        IBlockState state = world.getBlockState(pos);
        return facing = state.getBlock() instanceof BlockDamOutFlow ? state.getValue(BlockDamOutFlow.FACING) : EnumFacing.NORTH;
    }
}
