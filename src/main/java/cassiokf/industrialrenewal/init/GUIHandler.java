package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.container.*;
import cassiokf.industrialrenewal.entity.EntityLogCart;
import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.gui.*;
import cassiokf.industrialrenewal.tileentity.TileEntityFirstAidKit;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFuseBox;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityFluidLoader;
import cassiokf.industrialrenewal.tileentity.TileEntityRecordPlayer;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityEntityDetector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.Objects;

public class GUIHandler implements IGuiHandler {

    public static final int FIRSTAIDKIT = 0;
    public static final int RECORDPLAYER = 1;
    public static final int CARGOLOADER = 2;
    public static final int STEAMLOCOMOTIVE = 3;
    public static final int ENTITYDETECTOR = 4;
    public static final int FUSEBOX = 5;
    public static final int MANUAL = 6;
    public static final int LOGCART = 7;
    public static final int FLUIDLOADER = 9;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FIRSTAIDKIT) {
            return new ContainerFirstAidKit(player.inventory, (TileEntityFirstAidKit) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
        }
        if (ID == RECORDPLAYER) {
            return new ContainerRecordPlayer(player.inventory, (TileEntityRecordPlayer) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
        }
        if (ID == CARGOLOADER) {
            return new ContainerCargoLoader(player.inventory, (TileEntityCargoLoader) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
        }
        if (ID == STEAMLOCOMOTIVE) {
            return new ContainerSteamLocomotive(player.inventory, (EntitySteamLocomotive) world.getEntityByID(x));
        }
        if (ID == ENTITYDETECTOR) {
            return new ContainerEntityDetector(player.inventory, (TileEntityEntityDetector) world.getTileEntity(new BlockPos(x, y, z)));
        }
        if (ID == FUSEBOX) {
            return new ContainerFuseBox(player.inventory, (TileEntityFuseBox) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
        }
        if (ID == LOGCART) {
            return new ContainerLogCart(player.inventory, (EntityLogCart) world.getEntityByID(x));
        }
        if (ID == FLUIDLOADER) {
            return new ContainerFluidLoader(player.inventory, (TileEntityFluidLoader) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
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
        if (ID == CARGOLOADER) {
            return new GUICargoLoader(player.inventory, (TileEntityCargoLoader) world.getTileEntity(new BlockPos(x, y, z)));
        }
        if (ID == STEAMLOCOMOTIVE) {
            return new GUISteamLocomotive(player.inventory, (EntitySteamLocomotive) world.getEntityByID(x));
        }
        if (ID == ENTITYDETECTOR) {
            return new GUIEntityDetector(player.inventory, (TileEntityEntityDetector) world.getTileEntity(new BlockPos(x, y, z)));
        }
        if (ID == FUSEBOX) {
            return new GUIFuseBox(player, player.inventory, (TileEntityFuseBox) world.getTileEntity(new BlockPos(x, y, z)));
        }
        if (ID == MANUAL) {
            return new GUIManual(world, player);
        }
        if (ID == LOGCART) {
            return new GUILogCart(player.inventory, (EntityLogCart) world.getEntityByID(x));
        }
        if (ID == FLUIDLOADER) {
            return new GUIFluidLoader(player.inventory, (TileEntityFluidLoader) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }
}
