package net.cassiokf.industrialrenewal.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ItemScrewdriver extends IRBaseItem {
    
    public ItemScrewdriver() {
        super(new Properties().stacksTo(1));
    }
    
    public ItemScrewdriver(Properties props) {
        super(props);
    }
    
    public static void playSound(Level pLevel, BlockPos pPos) {
        //TODO
    }
}
