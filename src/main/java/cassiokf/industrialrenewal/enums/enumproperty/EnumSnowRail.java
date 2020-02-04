package cassiokf.industrialrenewal.enums.enumproperty;

import net.minecraft.util.IStringSerializable;

public enum EnumSnowRail implements IStringSerializable {
    FALSE("false"),
    LAYER1("layer1"),
    LAYER2("layer2");

    private final String name;

    EnumSnowRail(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
