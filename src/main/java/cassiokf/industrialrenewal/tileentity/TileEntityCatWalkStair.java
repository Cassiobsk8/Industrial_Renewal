package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityCatWalkStair extends TileEntitySync
{
    private final Set<EnumFacing> blackListedFaces = EnumSet.noneOf(EnumFacing.class);

    public boolean toggleFacing(final EnumFacing facing)
    {
        if (blackListedFaces.contains(facing))
        {
            blackListedFaces.remove(facing);
            return false;
        } else
        {
            blackListedFaces.add(facing);
            return true;
        }
    }

    public boolean disableFacing(final EnumFacing facing)
    {
        if (blackListedFaces.contains(facing))
        {
            blackListedFaces.remove(facing);
            return false;
        } else
        {
            return true;
        }
    }

    public boolean activeFacing(final EnumFacing facing)
    {
        if (blackListedFaces.contains(facing))
        {
            return false;
        } else
        {
            blackListedFaces.add(facing);
            return true;
        }
    }

    public boolean isFacingBlackListed(final EnumFacing facing)
    {
        return blackListedFaces.contains(facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        final int[] enabledFacingIndices = blackListedFaces.stream()
                .mapToInt(EnumFacing::getIndex)
                .toArray();
        compound.setIntArray("EnabledFacings", enabledFacingIndices);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        blackListedFaces.clear();
        final int[] enabledFacingIndices = compound.getIntArray("EnabledFacings");
        for (final int index : enabledFacingIndices)
        {
            blackListedFaces.add(EnumFacing.byIndex(index));
        }
        super.readFromNBT(compound);
    }
}
