package cassiokf.industrialrenewal.tileentity.tubes;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityCableTray extends TileEntityMultiBlocksTube<TileEntityCableTray>
{
    @Override
    public void onLoad()
    {
        refreshConnections();
    }

    @Override
    public void update()
    {
    }

    @Override
    public boolean isMaster()
    {
        return false;
    }

    @Override
    public void setMaster(TileEntityCableTray master)
    {
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityCableTray;
    }

    @Override
    public boolean isTray()
    {
        return true;
    }

    public void refreshConnections()
    {
        List<TileEntityMultiBlocksTube> connectedCables = new ArrayList<>();
        List<TileEntityCableTray> cableTrayList = new ArrayList<>();
        Stack<TileEntityCableTray> traversingCables = new Stack<>();
        traversingCables.add(this);
        while (!traversingCables.isEmpty())
        {
            TileEntityCableTray storage = traversingCables.pop();
            cableTrayList.add(storage);
            for (EnumFacing d : getFacesToCheck())
            {
                TileEntity te = world.getTileEntity(storage.getPos().offset(d));
                if (instanceOf(te) && !cableTrayList.contains(te))
                {
                    traversingCables.add((TileEntityCableTray) te);
                } else if (!instanceOf(te) && te instanceof TileEntityMultiBlocksTube && !connectedCables.contains(te))
                {
                    connectedCables.add((TileEntityMultiBlocksTube) te);
                }
            }
        }
        for (TileEntityMultiBlocksTube cables : connectedCables)
        {
            cables.setMaster(null);
            cables.getMaster();
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        refreshConnections();
    }
}
