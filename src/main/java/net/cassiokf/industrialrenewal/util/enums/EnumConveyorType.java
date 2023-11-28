package net.cassiokf.industrialrenewal.util.enums;

import net.minecraft.util.StringRepresentable;

public enum EnumConveyorType implements StringRepresentable {
    NORMAL("normal"),
    HOPPER("hopper"),
    INSERTER("inserter");
    private String name;

    private EnumConveyorType(String name){
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
