package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.block.transport.BlockConveyor;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityConveyorBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityConveyor extends BlockEntityConveyorBase {
    public BlockEntityConveyor(BlockPos pos, BlockState state)
    {
        super(ModBlockEntity.CONVEYOR_TILE.get(), pos, state);
    }

    public void tick(){
        int tickspeed = 4;
        int countperop = 8;
        switch (getBlockState().getValue(BlockConveyor.TIER)){
            case FAST -> {
                tickspeed = 2;
                countperop = 16;
            }
            case EXPRESS -> {
                tickspeed = 1;
                countperop = 32;
            }
        }


        switch (getBlockState().getValue(BlockConveyor.TYPE)) {
            case INSERTER -> {
                super.tickConveyor(tickspeed);
                super.tickInserter();
            }
            case HOPPER -> {
                super.tickConveyor(tickspeed);
                super.tickHopper(countperop);
            }
            default -> super.tickConveyor(tickspeed);
        }
    }
}
