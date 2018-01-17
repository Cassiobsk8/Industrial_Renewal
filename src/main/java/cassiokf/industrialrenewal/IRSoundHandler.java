package cassiokf.industrialrenewal;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class IRSoundHandler {

    public static void init() {
        //TODO Renomear com o nome certo
        ResourceLocation sound0 = new ResourceLocation("industrialrenewal", "train_horn");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound0).setRegistryName(sound0));

        ResourceLocation sound1 = new ResourceLocation("industrialrenewal", "valve");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound1).setRegistryName(sound1));

        ResourceLocation sound2 = new ResourceLocation("industrialrenewal", "modern_alarm");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound2).setRegistryName(sound2));

        ResourceLocation sound3 = new ResourceLocation("industrialrenewal", "drill");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound3).setRegistryName(sound3));

        ResourceLocation sound4 = new ResourceLocation("industrialrenewal", "gate_opening");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound4).setRegistryName(sound4));

        ResourceLocation sound5 = new ResourceLocation("industrialrenewal", "gate_closing");
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound5).setRegistryName(sound5));
    }
}