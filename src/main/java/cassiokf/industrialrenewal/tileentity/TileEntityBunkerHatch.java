package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBunkerHatch;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityBunkerHatch extends TileEntityMultiBlockBase<TileEntityBunkerHatch>
{
    public TileEntityBunkerHatch(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x1x3Centered(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityBunkerHatch;
    }

    public void changeOpen()
    {
        if (world.isRemote) return;
        if (!isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().changeOpen();
            }
            return;
        }
        BlockState state = world.getBlockState(pos);
        boolean value = !((boolean)state.get(BlockBunkerHatch.OPEN));
        changeOpenFromMaster(value);
        if (value)
        {
            world.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_CLOSE.get(), SoundCategory.NEUTRAL, IRConfig.Main.masterVolumeMult.get().floatValue(), 1.0F);

        } else
        {
            world.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_OPEN.get(), SoundCategory.NEUTRAL, IRConfig.Main.masterVolumeMult.get().floatValue(), 1.0F);
        }
    }

    public void changeOpenFromMaster(boolean value)
    {
        List<BlockPos> list = MachinesUtils.getBlocksIn3x1x3Centered(this.pos);
        for (BlockPos currentPos : list)
        {
            BlockState state = world.getBlockState(currentPos);
            if (state.getBlock() instanceof BlockBunkerHatch)
            {
                world.setBlockState(currentPos, state.with(BlockBunkerHatch.OPEN, value));
            }
        }
    }
}
