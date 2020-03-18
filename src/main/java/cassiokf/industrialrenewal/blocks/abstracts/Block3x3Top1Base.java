package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Block3x3Top1Base<TE extends TileEntity3x3MachineBase> extends Block3x3x3Base<TE>
{
    public Block3x3Top1Base(Material material, String name, CreativeTabs tab)
    {
        super(material, name, tab);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(MASTER))
        {
            for (int y = -1; y < 1; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    for (int x = -1; x < 2; x++)
                    {
                        BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        if (y != 0 || z != 0 || x != 0)
                            worldIn.setBlockState(currentPos, state.withProperty(MASTER, false));
                    }
                }
            }
            worldIn.setBlockState(pos.up(), state.withProperty(MASTER, false));
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        EntityPlayer player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        EnumFacing facing = player.getHorizontalFacing();
        for (int y = 0; y < 2; y++)
        {
            for (int z = 0; z < 3; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    BlockPos currentPos = new BlockPos(pos.offset(facing, z).offset(facing.rotateY(), x).offset(EnumFacing.UP, y));
                    if (!isReplaceable(worldIn, currentPos)) return false;
                }
            }
        }
        return isReplaceable(worldIn, pos.offset(facing).up(2));
    }
}
