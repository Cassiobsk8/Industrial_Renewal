package cassiokf.industrialrenewal.tileentity.fusebox;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityFuseBox extends TileEntity {

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    public void changeActivate() {
        IBlockState state = this.world.getBlockState(this.pos);
        boolean active = state.getValue(BlockFuseBox.ACTIVE);
        this.world.setBlockState(this.pos, state.withProperty(BlockFuseBox.ACTIVE, !active));
        changeConnector(!active);
    }

    private void changeConnector(boolean value) {
        int i = 1;
        while (i < 64) {
            Block block = this.world.getBlockState(this.pos.down(i)).getBlock();
            if (block instanceof BlockFuseBoxConnector) {
                setOutput(this.pos.down(i), value);
                return;
            }
            i++;
        }
    }

    private void setOutput(BlockPos pos, boolean value) {
        TileEntityBoxConnector te = (TileEntityBoxConnector) this.world.getTileEntity(pos);
        te.setActive(value);
    }
}
