package cassiokf.industrialrenewal;

import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class IRSoundHandler
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
    private static Minecraft mc = Minecraft.getMinecraft();

    private static Map<Long, ISound> soundMap = new HashMap<>();

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

    public static boolean isSoundPlaying(BlockPos pos)
    {
        return soundMap.containsKey(pos.toLong());
    }

    /* ------------------------------- Sound handling from Mekanism devs ------------------------------- */

    public static ISound playRepeatableSound(ResourceLocation soundLoc, float volume, float pitch, BlockPos pos)
    {
        // First, check to see if there's already a sound playing at the desired location
        ISound s = soundMap.get(pos.toLong());
        if (s == null || !mc.getSoundHandler().isSoundPlaying(s))
        {
            // No sound playing, start one up - we assume that tile sounds will play until explicitly stopped
            s = new PositionedSoundRecord(soundLoc, SoundCategory.BLOCKS, volume, pitch, true, 0,
                    ISound.AttenuationType.LINEAR, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f)
            {
                @Override
                public float getVolume()
                {
                    if (this.sound == null)
                    {
                        this.createAccessor(mc.getSoundHandler());
                    }
                    return super.getVolume();
                }
            };

            // Start the sound
            playSound(s);

            // N.B. By the time playSound returns, our expectation is that our wrapping-detector handler has fired
            // and dealt with any muting interceptions and, CRITICALLY, updated the soundMap with the final ISound.
            s = soundMap.get(pos.toLong());
        }

        return s;
    }

    public static void stopTileSound(BlockPos pos)
    {
        long posKey = pos.toLong();
        ISound s = soundMap.get(posKey);
        if (s != null)
        {
            mc.getSoundHandler().stopSound(s);
            soundMap.remove(posKey);
        }
    }

    public static void playSound(ResourceLocation soundLoc, float volume, float pitch, BlockPos pos)
    {
        ISound s = new PositionedSoundRecord(soundLoc, SoundCategory.BLOCKS, volume, pitch,
                false, 0, ISound.AttenuationType.LINEAR, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f)
        {
            @Override
            public float getVolume()
            {
                if (this.sound == null)
                {
                    this.createAccessor(mc.getSoundHandler());
                }
                return super.getVolume();
            }
        };
        playSound(s);
    }

    public static void playSound(ISound sound)
    {
        mc.getSoundHandler().playSound(sound);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTilePlaySound(PlaySoundEvent event)
    {
        // Ignore any sound event which is null
        ISound resultSound = event.getResultSound();
        if (resultSound == null)
        {
            return;
        }

        // Ignore any sound event outside this mod namespace
        ResourceLocation soundLoc = event.getSound().getSoundLocation();
        if (TICKABLE_SOUNDS.contains(soundLoc.toString()))
        {
            resultSound = new TileSound(event.getSound(), resultSound.getVolume(), 1.0F, true);
            event.setResultSound(resultSound);
        } else if (REPEATABLE_SOUNDS.contains(soundLoc.toString()))
        {
            resultSound = new TileSound(event.getSound(), resultSound.getVolume(), 1.0F, false);
            event.setResultSound(resultSound);
        } else
        {
            return;
        }

        // Finally, update our soundMap so that we can actually have a shot at stopping this sound; note that we also
        // need to "unoffset" the sound position so that we build the correct key for the sound map
        // Aside: I really, really, wish Forge returned the final result sound as part of playSound :/
        BlockPos pos = new BlockPos(resultSound.getXPosF() - 0.5f, resultSound.getYPosF() - 0.5f, resultSound.getZPosF() - 0.5);
        soundMap.put(pos.toLong(), resultSound);
    }

    public enum EnumSoundType
    {
        NORMAL,
        REPEATABLE_ONLY,
        DYNAMIC
    }

    private static class TileSound implements ITickableSound
    {
        private Minecraft mc = Minecraft.getMinecraft();
        // Choose an interval between 60-80 ticks (3-4 seconds) to check for muffling changes. We do this
        // to ensure that not every tile sound tries to run on the same tick and thus create
        // uneven spikes of CPU usage
        private int checkInterval = 1 + ThreadLocalRandom.current().nextInt(8);

        private ISound sound;
        private float volume;
        private float pitch;
        private boolean donePlaying = false;
        private boolean isDynamic;

        TileSound(ISound sound, float volume, float pitch, boolean isDynamic)
        {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
            this.isDynamic = isDynamic;
        }

        @Override
        public void update()
        {
            // Every configured interval, see if we need to adjust Pitch
            if (isDynamic && mc.world != null && mc.world.getTotalWorldTime() % checkInterval == 0)
            {
                TileEntity te = mc.world.getTileEntity(new BlockPos(sound.getXPosF(), sound.getYPosF(), sound.getZPosF()));
                if (te != null)
                {
                    if (te instanceof IDynamicSound)
                    {
                        pitch = ((IDynamicSound) te).getPitch();
                        volume = ((IDynamicSound) te).getVolume();
                    }
                } else
                {
                    donePlaying = true;
                }
            }
        }

        @Override
        public boolean isDonePlaying()
        {
            return donePlaying;
        }

        @Override
        public float getVolume()
        {
            return volume;
        }

        @Override
        public float getPitch()
        {
            return pitch;
        }

        @Override
        public @Nonnull
        ResourceLocation getSoundLocation()
        {
            return sound.getSoundLocation();
        }

        @Override
        public @Nullable
        SoundEventAccessor createAccessor(net.minecraft.client.audio.SoundHandler handler)
        {
            return sound.createAccessor(handler);
        }

        @Override
        public @Nonnull
        Sound getSound()
        {
            return sound.getSound();
        }

        @Override
        public @Nonnull
        SoundCategory getCategory()
        {
            return sound.getCategory();
        }

        @Override
        public boolean canRepeat()
        {
            return sound.canRepeat();
        }

        @Override
        public int getRepeatDelay()
        {
            return sound.getRepeatDelay();
        }

        @Override
        public float getXPosF()
        {
            return sound.getXPosF();
        }

        @Override
        public float getYPosF()
        {
            return sound.getYPosF();
        }

        @Override
        public float getZPosF()
        {
            return sound.getZPosF();
        }

        @Override
        public @Nonnull
        ISound.AttenuationType getAttenuationType()
        {
            return sound.getAttenuationType();
        }
    }
}