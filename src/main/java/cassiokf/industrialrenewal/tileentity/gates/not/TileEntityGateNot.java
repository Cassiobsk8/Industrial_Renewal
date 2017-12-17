package cassiokf.industrialrenewal.tileentity.gates.not;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cassiokf.industrialrenewal.tileentity.gates.and.BlockGateAnd.*;

public class TileEntityGateNot extends TileEntity {


    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }
    public void exchangeOut(World world, BlockPos pos, IBlockState state , boolean active) {
        world.setBlockState(pos, state.withProperty(ACTIVE, active));
    }
    public void dorotateBlock(World world, BlockPos pos, IBlockState state) {
        EnumFacing newFace = state.getValue(FACING).rotateY();
        world.setBlockState(pos, state.withProperty(FACING, newFace));
    }
}
