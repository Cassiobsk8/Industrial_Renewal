package net.cassiokf.industrialrenewal.item;


public class ItemWindBlade extends IRBaseItem {
    
    public static final int MAX_DAMAGE = 48 * 60;
    
    public ItemWindBlade() {
        super(new Properties().durability(MAX_DAMAGE));
    }
    
    public ItemWindBlade(Properties props) {
        super(props);
    }
    
}
