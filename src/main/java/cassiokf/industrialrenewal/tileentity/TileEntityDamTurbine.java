package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.MachinesUtils;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.ICompressedFluidCapability;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import cassiokf.industrialrenewal.util.interfaces.IMecanicalEnergy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class TileEntityDamTurbine extends TileEntityMultiBlockBase<TileEntityDamTurbine> implements ICompressedFluidCapability, IDynamicSound
{
    private final float volume = IRConfig.Sounds.turbineVolume.get() * IRConfig.Sounds.masterVolumeMult.get();
    private BlockPos outPos = null;
    private int passedFluid = 0;
    private boolean hasFlow = false;
    private float oldRotation;
    private float rotation;

    public TileEntityDamTurbine(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
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
            IRSoundHandler.playRepeatableSound(SoundsRegistration.MOTOR_ROTATION_RESOURCEL, volume, pitch, pos);
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
        BlockPos outPos = pos.up(2);
        TileEntity te = world.getTileEntity(outPos);
        if (te instanceof IMecanicalEnergy && ((IMecanicalEnergy) te).canAcceptRotation(outPos, Direction.DOWN))
        {
            ((IMecanicalEnergy) te).passRotation((int) rotation);
        }
    }

    private void doRotation()
    {
        float norm = Utils.normalizeClamped(passedFluid, 0, References.BUCKET_VOLUME);
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
            sync();
        }
        hasFlow = false;
    }

    @Override
    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return MachinesUtils.getBlocksIn3x2x3CenteredPlus1OnTop(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityDamTurbine;
    }

    private BlockPos getOutPutPosition()
    {
        if (outPos != null) return outPos;
        Direction facing = getMasterFacing();
        return outPos = getMaster().getPos().down().offset(facing.getOpposite()).offset(facing.rotateYCCW());
    }

    @Override
    public boolean canAccept(Direction face, BlockPos pos)
    {
        if (getMaster() == null) return false;
        Direction facing = getMasterFacing();
        return face.equals(facing) && pos.equals(getMaster().getPos().offset(facing).offset(facing.rotateY()));
    }

    @Override
    public boolean canPipeConnect(Direction face, BlockPos pos)
    {
        Direction facing = getMasterFacing();
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
        Direction outPutFace = getMasterFacing().rotateYCCW();
        BlockPos outPutPos = getOutPutPosition().offset(outPutFace);
        TileEntity te = world.getTileEntity(outPutPos);
        if (te instanceof ICompressedFluidCapability
                && ((ICompressedFluidCapability) te).canAccept(outPutFace.getOpposite(), outPutPos))
        {
            realAmount = ((ICompressedFluidCapability) te).passCompressedFluid(amount, this.pos.getY() - 1, simulate);
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
        return Utils.normalizeClamped(rotation, 0, 6000);
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
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putFloat("rotation", rotation);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        rotation = compound.getFloat("rotation");
        super.read(compound);
    }
}
