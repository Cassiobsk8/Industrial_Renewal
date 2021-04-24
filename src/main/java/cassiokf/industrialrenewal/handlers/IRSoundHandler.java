package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.util.TileTickableSound;
import cassiokf.industrialrenewal.util.interfaces.IDynamicSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@OnlyIn(Dist.CLIENT)
public class IRSoundHandler
{
    private static Minecraft mc = Minecraft.getInstance();

    private static Map<Long, ISound> soundMap = new HashMap<>();

    public static boolean isSoundPlaying(BlockPos pos)
    {
        return soundMap.containsKey(pos.toLong());
    }

    /* ------------------------------- Sound handling from Mekanism devs ------------------------------- */
    public static ISound playRepeatableSound(TileEntity tile, SoundEvent soundIn, float volume, float pitch)
    {
        // First, check to see if there's already a sound playing at the desired location
        ISound s = soundMap.get(tile.getPos().toLong());
        if (s == null || !mc.getSoundHandler().isPlaying(s))
        {
            // No sound playing, start one up - we assume that tile sounds will play until explicitly stopped
            s = new TileTickableSound(tile, soundIn, SoundCategory.BLOCKS, volume, pitch, true, 0,
                    ISound.AttenuationType.LINEAR);

            // Start the sound
            playSound(s);

            // N.B. By the time playSound returns, our expectation is that our wrapping-detector handler has fired
            // and dealt with any muting interceptions and, CRITICALLY, updated the soundMap with the final ISound.
            s = soundMap.get(tile.getPos().toLong());
        }

        return s;
    }

    public static void stopTileSound(BlockPos pos)
    {
        long posKey = pos.toLong();
        ISound s = soundMap.get(posKey);
        if (s != null)
        {
            mc.getSoundHandler().stop(s);
            soundMap.remove(posKey);
        }
    }

    public static void playSound(World world, SoundEvent soundEvent, float volume, float pitch, BlockPos pos)
    {
        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, volume, pitch);
    }

    public static void playSound(ISound sound)
    {
        mc.getSoundHandler().play(sound);
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
        if (SoundsRegistration.TICKABLE_SOUNDS.contains(soundLoc.toString()))
        {
            resultSound = new TileSound(event.getSound(), resultSound.getVolume(), 1.0F, true);
            event.setResultSound(resultSound);
        } else if (SoundsRegistration.REPEATABLE_SOUNDS.contains(soundLoc.toString()))
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
        BlockPos pos = new BlockPos(resultSound.getX() - 0.5f, resultSound.getY() - 0.5f, resultSound.getZ() - 0.5);
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
        private Minecraft mc = Minecraft.getInstance();
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
        public void tick()
        {
            // Every configured interval, see if we need to adjust Pitch
            if (isDynamic && mc.world != null && mc.world.getGameTime() % checkInterval == 0)
            {
                TileEntity te = mc.world.getTileEntity(new BlockPos(sound.getX(), sound.getY(), sound.getZ()));
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
        public boolean isGlobal()
        {
            return false;
        }

        @Override
        public int getRepeatDelay()
        {
            return sound.getRepeatDelay();
        }

        @Override
        public float getX()
        {
            return sound.getX();
        }

        @Override
        public float getY()
        {
            return sound.getY();
        }

        @Override
        public float getZ()
        {
            return sound.getZ();
        }

        @Override
        public @Nonnull
        ISound.AttenuationType getAttenuationType()
        {
            return sound.getAttenuationType();
        }
    }
}
