package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import cassiokf.industrialrenewal.util.interfaces.IMecanicalEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;

import java.util.List;

public class TileEntityDamTurbine extends TileEntity3x3MachineBase<TileEntityDamTurbine> implements ICompressedFluidCapability, IDynamicSound
{
    private float volume = IRConfig.MainConfig.Sounds.TurbineVolume;
    private BlockPos outPos = null;
    private int passedFluid = 0;
    private boolean hasFlow = false;
    private float oldRotation;
    private float rotation;

    public TileEntityDamTurbine()
    {
    }

    @Override
    public void tick()
    {
        if (isMaster())
        {
            if (!world.isRemote)
            {
                doRotation();
                passRotationUp();
            } else
            {
                updateSound(getPitch());
            }
        }
    }

    private void updateSound(float pitch)
    {
        if (!world.isRemote) return;
        if (this.rotation > 0)
        {
            IRSoundHandler.playRepeatableSound(IRSoundRegister.MOTOR_ROTATION_RESOURCEL, volume, pitch, pos);
        } else
        {
            IRSoundHandler.stopTileSound(pos);
        }
    }

    @Override
    public void onMasterBreak()
    {
        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
    }

    private void passRotationUp()
    {
        if (rotation <= 0) return;
        TileEntity te = world.getTileEntity(pos.up(2));
        if (te instanceof IMecanicalEnergy)
        {
            ((IMecanicalEnergy) te).passRotation((int) rotation);
        }
    }

    private void doRotation()
    {
        float norm = Utils.normalize(passedFluid, 0, Fluid.BUCKET_VOLUME);
        if (hasFlow)
        {
            float max = (6000 * norm);
            if (rotation < max)
            {
                rotation += Math.min(norm * 10, max - rotation);
            } else if (rotation > max)
            {
                rotation -= 1;
            }
        } else if (rotation > 0)
        {
            rotation -= 10;
        }
        rotation = MathHelper.clamp(rotation, 0, 6000);
        if (rotation != oldRotation)
        {
            oldRotation = rotation;
            Sync();
        }
        hasFlow = false;
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x2x3CenteredPlus1OnTop(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityDamTurbine;
    }

    private BlockPos getOutPutPosition()
    {
        if (outPos != null) return outPos;
        EnumFacing facing = getMasterFacing();
        return outPos = getMaster().getPos().down().offset(facing.getOpposite()).offset(facing.rotateYCCW());
    }

    @Override
    public boolean canAccept(EnumFacing face, BlockPos pos)
    {
        if (getMaster() == null) return false;
        EnumFacing facing = getMasterFacing();
        return face.equals(facing) && pos.equals(getMaster().getPos().offset(facing).offset(facing.rotateY()));
    }

    @Override
    public boolean canPipeConnect(EnumFacing face, BlockPos pos)
    {
        EnumFacing facing = getMasterFacing();
        if (face.equals(facing)) return canAccept(face, pos);
        return face.equals(facing.rotateYCCW()) && pos.equals(getOutPutPosition());
    }

    @Override
    public int passCompressedFluid(int amount, int y, boolean simulate)
    {
        if (!isMaster()) return getMaster().passCompressedFluid(amount, y, simulate);
        int height = y - pos.getY();
        if (height < 0) return 0;
        int realAmount = 0;
        EnumFacing outPutFace = getMasterFacing().rotateYCCW();
        BlockPos pos = getOutPutPosition().offset(outPutFace);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof ICompressedFluidCapability
                && ((ICompressedFluidCapability) te).canAccept(outPutFace.getOpposite(), pos))
        {
            realAmount = ((ICompressedFluidCapability) te).passCompressedFluid(amount, pos.getY() - 1, simulate);
        }
        passedFluid = realAmount;
        if (!simulate && realAmount > 0) hasFlow = true;
        return realAmount;
    }

    public String getRotationText()
    {
        return "Rot: " + (int) rotation + " rpm";
    }

    public float getRotationFill()
    {
        return getNormalizedRotation() * 180;
    }

    private float getNormalizedRotation()
    {
        return Utils.normalize(rotation, 0, 6000);
    }

    @Override
    public float getPitch()
    {
        return getNormalizedRotation() * 0.7f;
    }

    @Override
    public float getVolume()
    {
        return volume;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setFloat("rotation", rotation);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        rotation = compound.getFloat("rotation");
        super.readFromNBT(compound);
    }
}
