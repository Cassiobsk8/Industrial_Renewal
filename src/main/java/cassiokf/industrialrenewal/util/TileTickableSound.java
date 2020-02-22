package cassiokf.industrialrenewal.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileTickableSound extends TickableSound
{
    private TileEntity tile;

    public TileTickableSound(TileEntity tile, SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, boolean repeatIn, int repeatDelayIn, ISound.AttenuationType attenuationTypeIn)
    {
        super(soundIn, categoryIn);
        this.tile = tile;
        this.volume = volumeIn;
        this.pitch = pitchIn;
        this.repeat = repeatIn;
        this.repeatDelay = repeatDelayIn;
        this.attenuationType = attenuationTypeIn;
        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.global = true;
    }

    @Override
    public boolean isDonePlaying()
    {
        return this.donePlaying;
    }

    public boolean canBeSilent()
    {
        return true;
    }

    public void tick()
    {
        if (!tile.isRemoved())
        {
            this.pitch = getPitch();
            this.volume = getVolume();

        } else
        {
            this.donePlaying = true;
        }
    }

    @Override
    public float getVolume()
    {
        if (this.sound == null)
        {
            this.createAccessor(Minecraft.getInstance().getSoundHandler());
        }
        return super.getVolume();
    }
}
