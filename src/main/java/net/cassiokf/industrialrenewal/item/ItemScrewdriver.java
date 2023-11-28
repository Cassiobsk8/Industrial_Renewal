package net.cassiokf.industrialrenewal.item;

public class ItemScrewdriver extends IRBaseItem{

    public ItemScrewdriver() {
        super(new Properties().stacksTo(1));
    }

    public ItemScrewdriver(Properties props) {
        super(props);
    }
}
