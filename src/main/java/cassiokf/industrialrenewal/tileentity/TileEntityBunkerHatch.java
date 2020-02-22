package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.blocks.BlockBunkerHatch;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import static cassiokf.industrialrenewal.init.TileRegistration.BUNKERHATCH_TILE;

public class TileEntityBunkerHatch extends TileEntity
{
    private boolean master;
    private boolean breaking;
    private TileEntityBunkerHatch masterTE;
    private boolean masterChecked = false;

    public TileEntityBunkerHatch()
    {
        super(BUNKERHATCH_TILE.get());
    }

    public TileEntityBunkerHatch getMaster()
    {
        List<BlockPos> list = Utils.getBlocksIn3x1x3Centered(this.pos);
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
        BlockState state = getBlockState();
        boolean value = !state.get(BlockBunkerHatch.OPEN);
        changeOpenFromMaster(value);
        if (value)
        {
            world.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_CLOSE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);

        } else
        {
            world.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_OPEN.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    public void breakMultiBlocks()
    {
        if (!this.isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().breakMultiBlocks();
            }
            return;
        }
        if (!breaking)
        {
            breaking = true;
            List<BlockPos> list = Utils.getBlocksIn3x1x3Centered(this.pos);
            for (BlockPos currentPos : list)
            {
                Block block = world.getBlockState(currentPos).getBlock();
                if (block instanceof BlockBunkerHatch) world.removeBlock(currentPos, false);
            }
        }
    }

    public void changeOpenFromMaster(boolean value)
    {
        List<BlockPos> list = Utils.getBlocksIn3x1x3Centered(this.pos);
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

        BlockState state = getBlockState();
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
