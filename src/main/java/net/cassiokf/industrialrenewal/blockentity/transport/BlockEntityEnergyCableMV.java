package net.cassiokf.industrialrenewal.blockentity.transport;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEnergyCableMV extends BlockEntityEnergyCable {

    public static final int MAX_ENERGY = 1024;
    
    public BlockEntityEnergyCableMV(BlockPos pos, BlockState state){
        super(ModBlockEntity.ENERGYCABLE_MV_TILE.get(), pos, state);
    }
    
    public BlockEntityEnergyCableMV(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state){
        super(tileEntityType, pos, state);
    }

    @Override
    public int getMaxEnergyToTransport() {
        return MAX_ENERGY;
    }

    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityEnergyCableMV;
    }
}
