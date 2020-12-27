package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBunkerHatch;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityBunkerHatch extends TileEntityMultiBlockBase<TileEntityBunkerHatch>
{
    public void changeOpen()
    {
        if (world.isRemote) return;
        if (!isMaster())
        {
            getMaster().changeOpen();
            return;
        }
        IBlockState state = world.getBlockState(pos);
        boolean value = !state.getValue(BlockBunkerHatch.OPEN);
        changeOpenFromMaster(value);
        if (value)
            world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, IRConfig.MainConfig.Sounds.masterVolumeMult, 1.0F);
        else
            world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, IRConfig.MainConfig.Sounds.masterVolumeMult, 1.0F);
    }

    public void changeOpenFromMaster(boolean value)
    {
        List<BlockPos> list = MachinesUtils.getBlocksIn3x1x3Centered(pos);
        for (BlockPos currentPos : list)
        {
            IBlockState state = world.getBlockState(currentPos);
            if (state.getBlock() instanceof BlockBunkerHatch)
            {
                world.setBlockState(currentPos, state.withProperty(BlockBunkerHatch.OPEN, value));
            }
        }
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
}
