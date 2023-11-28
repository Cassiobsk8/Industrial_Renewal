package net.cassiokf.industrialrenewal.util.enums;

import net.minecraft.util.Mth;

public enum EnumCableIn {
    NONE(0),
    LV(1),
    MV(2),
    HV(3);

    public static final EnumCableIn[] VALUES = values();
    private final int index;

    EnumCableIn(int i)
    {
        index = i;
    }

    public static EnumCableIn byIndex(int i)
    {
        return VALUES[Mth.abs(i % VALUES.length)];
    }

    public int getIndex()
    {
        return ordinal();
    }
}
