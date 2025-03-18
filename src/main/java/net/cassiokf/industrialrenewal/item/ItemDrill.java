package net.cassiokf.industrialrenewal.item;

import net.minecraft.world.item.ItemStack;

public class ItemDrill extends IRBaseItem {
    
    
    public ItemDrill(Properties props) {
        super(props);
    }
    
    public ItemDrill(final int maxDamage) {
        super(new Properties().stacksTo(1).defaultDurability(maxDamage).setNoRepair());
    }
    
    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }
    
}
