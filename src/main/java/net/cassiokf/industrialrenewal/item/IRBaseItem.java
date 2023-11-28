package net.cassiokf.industrialrenewal.item;

import net.minecraft.world.item.Item;

public class IRBaseItem extends Item {
    public IRBaseItem(Properties properties) {
        super(properties);
    }

    protected String name;

    public IRBaseItem(){
        super(new Properties());
    }
}
