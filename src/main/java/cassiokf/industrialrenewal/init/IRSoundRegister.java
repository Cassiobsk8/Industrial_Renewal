package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.IRSoundHandler.EnumSoundType;
import cassiokf.industrialrenewal.References;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class IRSoundRegister
{
    public static final ObjectSet<String> TICKABLE_SOUNDS = new ObjectOpenHashSet<>();
    public static final ObjectSet<String> REPEATABLE_SOUNDS = new ObjectOpenHashSet<>();

    public static SoundEvent TILEENTITY_TRAINHORN, TILEENTITY_VALVE_CHANGE, TILEENTITY_ALARM, ITEM_DRILL,
            BLOCK_CATWALKGATE_OPEN, BLOCK_CATWALKGATE_CLOSE, EFFECT_SHOCK, DISC_1, BOOK_FLIP, MOTOR_ROTATION, PUMP_ROTATION,
            PUMP_START;
    public static ResourceLocation PUMP_ROTATION_RESOURCEL = new ResourceLocation(References.MODID, "pump_rotation");

    public static ResourceLocation TILEENTITY_TRAINHORN_RESOURCEL = new ResourceLocation(References.MODID, "railroad.train_horn");
    public static ResourceLocation TILEENTITY_VALVE_CHANGE_RESOURCEL = new ResourceLocation(References.MODID, "valve");
    public static ResourceLocation TILEENTITY_ALARM_RESOURCEL = new ResourceLocation(References.MODID, "modern_alarm");
    public static ResourceLocation ITEM_DRILL_RESOURCEL = new ResourceLocation(References.MODID, "drill");
    public static ResourceLocation BLOCK_CATWALKGATE_OPEN_RESOURCEL = new ResourceLocation(References.MODID, "gate_opening");
    public static ResourceLocation BLOCK_CATWALKGATE_CLOSE_RESOURCEL = new ResourceLocation(References.MODID, "gate_closing");
    public static ResourceLocation EFFECT_SHOCK_RESOURCEL = new ResourceLocation(References.MODID, "spark");
    public static ResourceLocation DISC_1_RESOURCEL = new ResourceLocation(References.MODID, "music.visager_royal_entrance");
    public static ResourceLocation BOOK_FLIP_RESOURCEL = new ResourceLocation(References.MODID, "book_flip");
    public static ResourceLocation MOTOR_ROTATION_RESOURCEL = new ResourceLocation(References.MODID, "motor_rotation");
    public static ResourceLocation PUMP_START_RESOURCEL = new ResourceLocation(References.MODID, "pump_start");


    public static void registerSounds()
    {
        TILEENTITY_TRAINHORN = registerSound("railroad.train_horn", TILEENTITY_TRAINHORN_RESOURCEL, EnumSoundType.NORMAL);
        TILEENTITY_VALVE_CHANGE = registerSound("valve", TILEENTITY_VALVE_CHANGE_RESOURCEL, EnumSoundType.NORMAL);
        TILEENTITY_ALARM = registerSound("modern_alarm", TILEENTITY_ALARM_RESOURCEL, EnumSoundType.NORMAL);
        ITEM_DRILL = registerSound("drill", ITEM_DRILL_RESOURCEL, EnumSoundType.NORMAL);
        BLOCK_CATWALKGATE_OPEN = registerSound("gate_opening", BLOCK_CATWALKGATE_OPEN_RESOURCEL, EnumSoundType.NORMAL);
        BLOCK_CATWALKGATE_CLOSE = registerSound("gate_closing", BLOCK_CATWALKGATE_CLOSE_RESOURCEL, EnumSoundType.NORMAL);
        EFFECT_SHOCK = registerSound("spark", EFFECT_SHOCK_RESOURCEL, EnumSoundType.NORMAL);
        DISC_1 = registerSound("music.visager_royal_entrance", DISC_1_RESOURCEL, EnumSoundType.NORMAL);
        BOOK_FLIP = registerSound("book_flip", BOOK_FLIP_RESOURCEL, EnumSoundType.NORMAL);
        MOTOR_ROTATION = registerSound("motor_rotation", MOTOR_ROTATION_RESOURCEL, EnumSoundType.DYNAMIC);
        PUMP_ROTATION = registerSound("pump_rotation", PUMP_ROTATION_RESOURCEL, EnumSoundType.REPEATABLE_ONLY);
        PUMP_START = registerSound("pump_start", PUMP_START_RESOURCEL, EnumSoundType.NORMAL);
    }

    private static SoundEvent registerSound(String name, ResourceLocation location, EnumSoundType type)
    {
        SoundEvent event = new SoundEvent(location);
        switch (type)
        {
            default:
            case NORMAL:
                break;
            case REPEATABLE_ONLY:
                REPEATABLE_SOUNDS.add(location.toString());
                break;
            case DYNAMIC:
                TICKABLE_SOUNDS.add(location.toString());
                break;
        }
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}