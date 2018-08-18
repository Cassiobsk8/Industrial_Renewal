package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.tileentity.firstaidkit.ContainerFirstAidKit;
import cassiokf.industrialrenewal.tileentity.firstaidkit.GUIFirstAidKit;
import cassiokf.industrialrenewal.tileentity.firstaidkit.TileEntityFirstAidKit;
import cassiokf.industrialrenewal.tileentity.recordplayer.ContainerRecordPlayer;
import cassiokf.industrialrenewal.tileentity.recordplayer.GUIRecordPlayer;
import cassiokf.industrialrenewal.tileentity.recordplayer.TileEntityRecordPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.Objects;

public class GUIHandler implements IGuiHandler {

    public static final int FIRSTAIDKIT = 0;
    public static final int RECORDPLAYER = 1;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FIRSTAIDKIT) {
            return new ContainerFirstAidKit(player.inventory, (TileEntityFirstAidKit) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
        }
        if (ID == RECORDPLAYER) {
            return new ContainerRecordPlayer(player.inventory, (TileEntityRecordPlayer) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FIRSTAIDKIT) {
            return new GUIFirstAidKit(player.inventory, (TileEntityFirstAidKit) world.getTileEntity(new BlockPos(x, y, z)));
        }
        if (ID == RECORDPLAYER) {
            return new GUIRecordPlayer(player.inventory, (TileEntityRecordPlayer) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }
}
