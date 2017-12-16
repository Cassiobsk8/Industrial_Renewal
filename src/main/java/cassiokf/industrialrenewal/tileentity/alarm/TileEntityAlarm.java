package cassiokf.industrialrenewal.tileentity.alarm;

import com.google.common.collect.Lists;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cassiokf.industrialrenewal.tileentity.gates.and.BlockGateAnd.*;

public class TileEntityAlarm extends TileEntity implements ITickable {

    private EnumFacing facing = EnumFacing.SOUTH;
    private final long PERIOD = 1000L; // Adjust to suit timing
    private long lastTime = System.currentTimeMillis() - PERIOD;

    public TileEntityAlarm() {

    }


    @Override
    public void update() {
        playThis();
    }

    private static boolean isPoweredWire(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.REDSTONE_WIRE && Blocks.REDSTONE_WIRE.getStrongPower(world.getBlockState(pos), world, pos, EnumFacing.DOWN) > 0;
    }

    public boolean checkPowered() {
        boolean powered = world.isBlockPowered(this.getPos())
                || isPoweredWire(this.getWorld(), this.getPos().add(1, 0, 0))
                || isPoweredWire(this.getWorld(), this.getPos().add(-1, 0, 0))
                || isPoweredWire(this.getWorld(), this.getPos().add(0, 0, 1))
                || isPoweredWire(this.getWorld(), this.getPos().add(0, 0, -1));
        return powered;
    }
    public void playThis() {
        if (this.checkPowered()) {
            long thisTime = System.currentTimeMillis();
            if ((thisTime - lastTime) >= PERIOD) {
                lastTime = thisTime;
                this.getWorld().playSound((EntityPlayer) null, this.getPos().add(0.5, 0.5, 0.5), net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:modern_alarm")), SoundCategory.BLOCKS, 2.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }
    public EnumFacing getFacing() {
        return facing;
    }
    public void setFacing(final EnumFacing facing) {
        this.facing = facing;
        markDirty();
    }
}
