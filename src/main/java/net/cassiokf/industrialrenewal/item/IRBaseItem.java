package net.cassiokf.industrialrenewal.item;

import net.minecraft.world.item.Item;

public class IRBaseItem extends Item {
    protected String name;
    
    public IRBaseItem(Properties properties) {
        super(properties);
    }
    
    public IRBaseItem() {
        super(new Properties());
    }
}
