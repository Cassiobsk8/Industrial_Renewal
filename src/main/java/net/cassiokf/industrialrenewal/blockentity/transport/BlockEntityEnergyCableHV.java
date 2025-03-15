package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEnergyCableHV extends BlockEntityEnergyCable {

    public static int MAX_ENERGY = 10240;
    
    public BlockEntityEnergyCableHV(BlockPos pos, BlockState state){
        super(ModBlockEntity.ENERGYCABLE_HV_TILE.get(), pos, state);
    }
    
    public BlockEntityEnergyCableHV(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state){
        super(tileEntityType, pos, state);
    }

    @Override
    public int getMaxEnergyToTransport() {
        return MAX_ENERGY;
    }

    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityEnergyCableHV;
    }
}
