package cassiokf.industrialrenewal.tileentity.tubes;

import cassiokf.industrialrenewal.blocks.BlockPillar;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;

import static cassiokf.industrialrenewal.init.TileRegistration.PILLAR_PIPE;

public class TEPillarPipe extends TileEntityFluidPipeBase
{
    public TEPillarPipe()
    {
        super(PILLAR_PIPE.get());
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(MASTER, isMaster())
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
                .withInitial(WSOUTH, BlockPillar.canConnect(world, pos, Direction.SOUTH))
                .withInitial(WNORTH, BlockPillar.canConnect(world, pos, Direction.NORTH))
                .withInitial(WEAST, BlockPillar.canConnect(world, pos, Direction.EAST))
                .withInitial(WWEST, BlockPillar.canConnect(world, pos, Direction.WEST))
                .withInitial(WUP, BlockPillar.canConnect(world, pos, Direction.UP))
                .withInitial(WDOWN, BlockPillar.canConnect(world, pos, Direction.DOWN))
                .build();
    }
}
