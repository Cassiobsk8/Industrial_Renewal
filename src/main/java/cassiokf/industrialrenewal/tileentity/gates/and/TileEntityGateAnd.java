package cassiokf.industrialrenewal.tileentity.gates.and;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cassiokf.industrialrenewal.tileentity.gates.and.BlockGateAnd.*;

public class TileEntityGateAnd extends TileEntity {


    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }
    public void exchangeOut(World world, BlockPos pos, IBlockState state , boolean active) {
        world.setBlockState(pos, state.withProperty(ACTIVE, active).withProperty(ACTIVE10, active).withProperty(ACTIVE01, active));
    }
    public void exchange10(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(ACTIVE10, true).withProperty(ACTIVE, false).withProperty(ACTIVE01, false));
    }
    public void exchange01(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(ACTIVE01, true).withProperty(ACTIVE, false).withProperty(ACTIVE10, false));
    }
    public void dorotateBlock(World world, BlockPos pos, IBlockState state) {
        EnumFacing newFace = state.getValue(FACING).rotateY();
        world.setBlockState(pos, state.withProperty(FACING, newFace));
    }
}
