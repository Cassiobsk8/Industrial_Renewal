package cassiokf.industrialrenewal.enums.enumproperty;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public enum EnumBaseDirection implements IStringSerializable
{
    DOWN(0, "down"),
    UP(1, "up"),
    NORTH(2, "north"),
    SOUTH(3, "south"),
    WEST(4, "west"),
    EAST(5, "east"),
    NONE(6, "none");

    private static final EnumBaseDirection[] VALUES = values();

    private final int index;
    private final String name;

    EnumBaseDirection(final int index, final String name)
    {
        this.index = index;
        this.name = name;
    }

    public static EnumBaseDirection byIndex(int index)
    {
        return VALUES[MathHelper.abs(index % VALUES.length)];
    }

    public int getIndex()
    {
        return this.index;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public EnumBaseDirection rotateClockwise()
    {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }

    public EnumBaseDirection rotateCounterClockwise()
    {
        return VALUES[(ordinal() - 1) % VALUES.length];
    }
}
