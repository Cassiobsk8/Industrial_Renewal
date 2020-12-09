package cassiokf.industrialrenewal.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class ItemDiscBase extends MusicDiscItem
{
    protected ItemDiscBase(int comparatorValueIn, SoundEvent soundIn, Properties builder)
    {
        super(comparatorValueIn, soundIn, builder);
    }

    public ItemDiscBase(int comparatorValue, Supplier<SoundEvent> soundSupplier, Properties builder)
    {
        super(comparatorValue, soundSupplier, builder);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }
}
