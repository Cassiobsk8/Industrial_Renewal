package cassiokf.industrialrenewal;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class IRSoundHandler {

    public static void init() {
        ResourceLocation sound0 = new ResourceLocation("industrialrenewal", "train_horn");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound0).setRegistryName(sound0));

        ResourceLocation sound1 = new ResourceLocation("industrialrenewal", "valve");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound1).setRegistryName(sound1));

        ResourceLocation sound2 = new ResourceLocation("industrialrenewal", "modern_alarm");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound2).setRegistryName(sound2));
    }
}