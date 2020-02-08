package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.References;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDiscBase extends MusicDiscItem
{
    public ItemDiscBase(Item.Properties properties, SoundEvent sound)
    {
        super(8, sound, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ITextComponent getRecordDescription()
    {
        return new StringTextComponent(I18n.format("item." + References.MODID + "." + getRegistryName().getPath() + ".des0"));
    }
}
