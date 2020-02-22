package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.IRSoundHandler.EnumSoundType;
import cassiokf.industrialrenewal.References;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static cassiokf.industrialrenewal.References.MODID;

public class SoundsRegistration
{
    public static final ObjectSet<String> TICKABLE_SOUNDS = new ObjectOpenHashSet<>();
    public static final ObjectSet<String> REPEATABLE_SOUNDS = new ObjectOpenHashSet<>();

    private static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, MODID);
    public static final RegistryObject<SoundEvent> TILEENTITY_TRAINHORN = createSoundEvent("railroad.train_horn", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> TILEENTITY_VALVE_CHANGE = createSoundEvent("valve", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> TILEENTITY_ALARM = createSoundEvent("modern_alarm", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> ITEM_DRILL = createSoundEvent("drill", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> BLOCK_CATWALKGATE_OPEN = createSoundEvent("gate_opening", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> BLOCK_CATWALKGATE_CLOSE = createSoundEvent("gate_closing", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> EFFECT_SHOCK = createSoundEvent("spark", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> DISC_1 = createSoundEvent("music.visager_royal_entrance", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> BOOK_FLIP = createSoundEvent("book_flip", EnumSoundType.NORMAL);
    public static final RegistryObject<SoundEvent> MOTOR_ROTATION = createSoundEvent("motor_rotation", EnumSoundType.DYNAMIC);
    public static final RegistryObject<SoundEvent> PUMP_ROTATION = createSoundEvent("pump_rotation", EnumSoundType.REPEATABLE_ONLY);
    public static final RegistryObject<SoundEvent> PUMP_START = createSoundEvent("pump_start", EnumSoundType.NORMAL);

    public static void init()
    {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static RegistryObject<SoundEvent> createSoundEvent(String name, EnumSoundType type)
    {
        ResourceLocation location = new ResourceLocation(References.MODID, name);
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
        return SOUNDS.register(name, () -> new SoundEvent(location));
    }

}
