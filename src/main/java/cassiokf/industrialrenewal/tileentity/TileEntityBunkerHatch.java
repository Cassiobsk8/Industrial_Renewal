package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBunkerHatch;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityBunkerHatch extends TEBase
{
    private boolean master;
    private boolean breaking;
    private TileEntityBunkerHatch masterTE;
    private boolean masterChecked = false;

    public TileEntityBunkerHatch(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

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
        world.removeBlock(pos, false);
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
        BlockState state = world.getBlockState(pos);
        boolean value = !((boolean)state.get(BlockBunkerHatch.OPEN));
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
                if (block instanceof BlockBunkerHatch) world.removeBlock(currentPos, false);
            }
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

    public boolean isMaster()//tesr uses this
    {
        if (masterChecked) return this.master;

        BlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof BlockBunkerHatch)) this.master = false;
        else this.master = state.get(BlockBunkerHatch.MASTER);
        return this.master;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putBoolean("master", this.isMaster());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        this.master = compound.getBoolean("master");
        super.read(compound);
    }
}
