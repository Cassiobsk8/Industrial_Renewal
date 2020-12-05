package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TEHorizontalDirection;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileEntityDamOutFlow extends TEHorizontalDirection implements ICompressedFluidCapability, ITickableTileEntity
{
    private boolean hasFlow = false;
    private boolean removed = false;

    public TileEntityDamOutFlow(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (hasWorld() && !world.isRemote)
        {
            Block block = world.getBlockState(pos.offset(getBlockFacing())).getBlock();
            if (hasFlow)
            {
                if (!block.equals(Blocks.FLOWING_WATER) && !block.equals(Blocks.WATER))
                {
                    BlockState waterState = Blocks.FLOWING_WATER.getDefaultState();
                    world.setBlockState(pos.offset(getBlockFacing()), waterState, 3);
                    removed = false;
                }
            } else
            {
                if (!removed && (block.equals(Blocks.FLOWING_WATER) || block.equals(Blocks.WATER)))
                {
                    world.removeBlock(pos.offset(getBlockFacing()), false);
                    removed = true;
                }
            }
            hasFlow = false;
        }
    }

    @Override
    public boolean canAccept(Direction face, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean canPipeConnect(Direction face, BlockPos pos)
    {
        return face.equals(getBlockFacing().getOpposite());
    }

    @Override
    public int passCompressedFluid(int amount, int y, boolean simulate)
    {
        int height = y - pos.getY();
        if (!simulate && amount >= 0) hasFlow = true;
        return height >= 0 ? amount : 0;
    }
}
