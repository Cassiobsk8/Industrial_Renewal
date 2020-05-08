package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBunkerHatch;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityBunkerHatch extends TEBase
{
    private boolean master;
    private boolean breaking;
    private TileEntityBunkerHatch masterTE;
    private boolean masterChecked = false;

    public TileEntityBunkerHatch getMaster()
    {
        List<BlockPos> list = MachinesUtils.getBlocksIn3x1x3Centered(this.pos);
        for (BlockPos currentPos : list)
        {
            if (world.getTileEntity(currentPos) instanceof TileEntityBunkerHatch)
            {
                TileEntityBunkerHatch te = (TileEntityBunkerHatch) world.getTileEntity(currentPos);
                if (te != null && te.isMaster())
                {
                    return te;
                }
            }
        }
        world.setBlockToAir(pos);
        world.removeTileEntity(pos);
        return null;
    }

    public void changeOpen()
    {
        if (!isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().changeOpen();
            }
            return;
        }
        IBlockState state = world.getBlockState(pos);
        boolean value = !state.getValue(BlockBunkerHatch.OPEN);
        changeOpenFromMaster(value);
        if (value)
        {
            world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1.0F);

        } else
        {
            world.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1.0F);
        }
    }

    @Override
    public void onBlockBreak()
    {
        if (!this.isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().onBlockBreak();
            }
            return;
        }
        if (!breaking)
        {
            breaking = true;
            List<BlockPos> list = MachinesUtils.getBlocksIn3x1x3Centered(this.pos);
            for (BlockPos currentPos : list)
            {
                Block block = world.getBlockState(currentPos).getBlock();
                if (block instanceof BlockBunkerHatch) world.setBlockToAir(currentPos);
            }
        }
    }

    public void changeOpenFromMaster(boolean value)
    {
        List<BlockPos> list = MachinesUtils.getBlocksIn3x1x3Centered(this.pos);
        for (BlockPos currentPos : list)
        {
            IBlockState state = world.getBlockState(currentPos);
            if (state.getBlock() instanceof BlockBunkerHatch)
            {
                world.setBlockState(currentPos, state.withProperty(BlockBunkerHatch.OPEN, value));
            }
        }
    }

    public boolean isMaster()//tesr uses this
    {
        if (masterChecked) return this.master;

        IBlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof BlockBunkerHatch)) this.master = false;
        else this.master = state.getValue(BlockBunkerHatch.MASTER);
        return this.master;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("master", this.isMaster());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.master = compound.getBoolean("master");
        super.readFromNBT(compound);
    }
}
