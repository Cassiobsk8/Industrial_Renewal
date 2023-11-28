package net.cassiokf.industrialrenewal.util.enums;

import net.minecraft.util.StringRepresentable;

public enum EnumConveyorTier implements StringRepresentable {
    BASIC("basic"),
    FAST("fast"),
    EXPRESS("express");

    public final String label;

    private EnumConveyorTier(String label) {
        this.label = label;
    }

    @Override
    public String getSerializedName() {
        return label;
    }
}
