package cassiokf.industrialrenewal;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class IRSoundHandler {

    public static SoundEvent TILEENTITY_TRAINHORN, TILEENTITY_VALVE_CHANGE, TILEENTITY_ALARM, ITEM_DRILL, BLOCK_CATWALKGATE_OPEN, BLOCK_CATWALKGATE_CLOSE, EFECT_SHOCK;

    public static void registerSounds() {
        TILEENTITY_TRAINHORN = registerSound("railroad.train_horn");
        TILEENTITY_VALVE_CHANGE = registerSound("valve");
        TILEENTITY_ALARM = registerSound("modern_alarm");
        ITEM_DRILL = registerSound("drill");
        BLOCK_CATWALKGATE_OPEN = registerSound("gate_opening");
        BLOCK_CATWALKGATE_CLOSE = registerSound("gate_closing");
        EFECT_SHOCK = registerSound("spark");
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(References.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}