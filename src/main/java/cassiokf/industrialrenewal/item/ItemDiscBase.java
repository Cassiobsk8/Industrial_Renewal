package cassiokf.industrialrenewal.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class ItemDiscBase extends MusicDiscItem
{
    protected ItemDiscBase(Properties builder, SoundEvent soundIn)
    {
        super(0, soundIn, builder);
    }

    public ItemDiscBase(Properties builder, Supplier<SoundEvent> soundSupplier)
    {
        super(0, soundSupplier, builder);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }
}
