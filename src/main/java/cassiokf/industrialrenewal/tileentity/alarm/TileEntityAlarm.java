package cassiokf.industrialrenewal.tileentity.alarm;

import com.google.common.collect.Lists;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
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

public class TileEntityAlarm extends TileEntity {

    private EnumFacing facing = EnumFacing.SOUTH;

    public TileEntityAlarm() {

    }

    private static boolean isPoweredWire(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.REDSTONE_WIRE && Blocks.REDSTONE_WIRE.getStrongPower(world.getBlockState(pos), world, pos, EnumFacing.DOWN) > 0;
    }
    public boolean checkPowered(World world, TileEntity tileentity) {
        if (world != null && tileentity != null) {
            boolean powered = world.isBlockPowered(tileentity.getPos())
                    || isPoweredWire(world, tileentity.getPos().add(1, 0, 0))
                    || isPoweredWire(world, tileentity.getPos().add(-1, 0, 0))
                    || isPoweredWire(world, tileentity.getPos().add(0, 0, 1))
                    || isPoweredWire(world, tileentity.getPos().add(0, 0, -1));
            return powered;
        }
        return false;
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
