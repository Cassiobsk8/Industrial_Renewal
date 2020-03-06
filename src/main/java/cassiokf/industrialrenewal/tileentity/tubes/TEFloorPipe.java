package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.config.IRConfig;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;

import static cassiokf.industrialrenewal.init.TileRegistration.FLOOR_PIPE;

public class TEFloorPipe extends TileEntityFluidPipeBase
{

    public TEFloorPipe()
    {
        super(FLOOR_PIPE.get());
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(MASTER, IRConfig.Main.showMaster.get() && isMaster())
                .withInitial(SOUTH, canConnectToPipe(Direction.SOUTH))
                .withInitial(NORTH, canConnectToPipe(Direction.NORTH))
                .withInitial(EAST, canConnectToPipe(Direction.EAST))
                .withInitial(WEST, canConnectToPipe(Direction.WEST))
                .withInitial(UP, canConnectToPipe(Direction.UP))
                .withInitial(DOWN, canConnectToPipe(Direction.DOWN))
                .withInitial(CSOUTH, canConnectToCapability(Direction.SOUTH))
                .withInitial(CNORTH, canConnectToCapability(Direction.NORTH))
                .withInitial(CEAST, canConnectToCapability(Direction.EAST))
                .withInitial(CWEST, canConnectToCapability(Direction.WEST))
                .withInitial(CUP, canConnectToCapability(Direction.UP))
                .withInitial(CDOWN, canConnectToCapability(Direction.DOWN))
                .withInitial(WSOUTH, BlockIndustrialFloor.canConnect(world, pos, Direction.SOUTH))
                .withInitial(WNORTH, BlockIndustrialFloor.canConnect(world, pos, Direction.NORTH))
                .withInitial(WEAST, BlockIndustrialFloor.canConnect(world, pos, Direction.EAST))
                .withInitial(WWEST, BlockIndustrialFloor.canConnect(world, pos, Direction.WEST))
                .withInitial(WUP, BlockIndustrialFloor.canConnect(world, pos, Direction.UP))
                .withInitial(WDOWN, BlockIndustrialFloor.canConnect(world, pos, Direction.DOWN))
                .build();
    }
}
