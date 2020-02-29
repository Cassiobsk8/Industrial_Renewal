package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public abstract class Block3x3Top1Base<TE extends TileEntity3x3MachineBase> extends Block3x3x3Base<TE>
{
    public Block3x3Top1Base(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (state.get(MASTER))
        {
            for (int y = -1; y < 1; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    for (int x = -1; x < 2; x++)
                    {
                        BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        if (y != 0 || z != 0 || x != 0)
                            worldIn.setBlockState(currentPos, state.with(MASTER, false));
                    }
                }
            }
            worldIn.setBlockState(pos.up(), state.with(MASTER, false));
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        PlayerEntity player = worldIn.getDimension().getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        Direction facing = player.getHorizontalFacing();
        for (int y = 0; y < 2; y++)
        {
            for (int z = 0; z < 3; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    BlockPos currentPos = new BlockPos(pos.offset(facing, z).offset(facing.rotateY(), x).offset(Direction.UP, y));
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.getMaterial().isReplaceable()) return false;
                }
            }
        }
        BlockState upState = worldIn.getBlockState(pos.offset(facing).up(2));
        return upState.getMaterial().isReplaceable();
    }
}
