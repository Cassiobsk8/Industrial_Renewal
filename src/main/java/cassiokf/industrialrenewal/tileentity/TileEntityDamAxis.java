package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.interfaces.IMecanicalEnergy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileEntityDamAxis extends TileEntityMultiBlocksTube<TileEntityDamAxis> implements IMecanicalEnergy
{
    private static final Direction[] faces = new Direction[]{Direction.UP, Direction.DOWN};

    public TileEntityDamAxis(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public boolean canAcceptRotation(BlockPos pos, Direction side)
    {
        return side == Direction.DOWN || side == Direction.UP;
    }

    @Override
    public int passRotation(int amount)
    {
        if (!isMaster() && !isMasterInvalid()) return getMaster().passRotation(amount);

        if (getMachineContainers() != null && !getMachineContainers().isEmpty())
        {
            IMecanicalEnergy te = null;
            for (TileEntity tile : getMachineContainers().keySet())
            {
                if (tile instanceof IMecanicalEnergy && !(tile instanceof TileEntityDamAxis))
                {
                    te = (IMecanicalEnergy) tile;
                }
            }
            if (te != null) return te.passRotation(amount);
        }
        return 0;
    }

    @Override
    public Direction[] getFacesToCheck()
    {
        return faces;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityDamAxis;
    }

    @Override
    public void checkForOutPuts()
    {
        TileEntity te = world.getTileEntity(pos.up());
        if (!(te instanceof TileEntityDamAxis) && te instanceof IMecanicalEnergy && ((IMecanicalEnergy) te).canAcceptRotation(pos.up(), Direction.DOWN))
        {
            addMachine(te, Direction.UP);
        } else removeMachine(te);
    }
}
