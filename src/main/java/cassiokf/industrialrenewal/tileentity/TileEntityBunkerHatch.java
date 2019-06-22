package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Objects;

public class TileEntityBunkerHatch extends TileEntity
{
    private boolean master;
    private boolean breaking;

    @Override
    public void onLoad()
    {
        this.getIsMaster();
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
        world.setBlockToAir(pos);
        world.removeTileEntity(pos);
        return null;
    }

    public void changeOpen()
    {
        if (!getIsMaster())
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
            world.playSound(null, pos, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:gate_closing"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);

        } else
        {
            world.playSound(null, pos, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:gate_opening"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    public void breakMultiBlocks()
    {
        if (!this.master)
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
                if (block instanceof BlockBunkerHatch) world.setBlockToAir(currentPos);
            }
        }
    }

    public void changeOpenFromMaster(boolean value)
    {
        List<BlockPos> list = Utils.getBlocksIn3x1x3Centered(this.pos);
        for (BlockPos currentPos : list)
        {
            IBlockState state = world.getBlockState(currentPos);
            if (state.getBlock() instanceof BlockBunkerHatch)
            {
                world.setBlockState(currentPos, state.withProperty(BlockBunkerHatch.OPEN, value));
            }
        }
    }

    public boolean isMaster()//TESR uses this
    {
        return this.master;
    }

    private boolean getIsMaster()
    {
        IBlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof BlockBunkerHatch)) this.master = false;
        else this.master = state.getValue(BlockBunkerHatch.MASTER);
        return this.master;
    }

    public EnumFacing getBlockFacing()
    {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockBunkerHatch.FACING);
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("master", this.getIsMaster());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.master = compound.getBoolean("master");
        super.readFromNBT(compound);
    }
}
